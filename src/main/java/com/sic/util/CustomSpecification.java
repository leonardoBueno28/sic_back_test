package com.sic.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import javax.persistence.metamodel.Attribute.PersistentAttributeType;

import org.hibernate.metamodel.internal.SingularAttributeImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;

import com.sic.core.exceptions.AttributeNotFoundException;

public class CustomSpecification<Modelo> implements Specification<Modelo> {
	public final static int TYPE_LIKE = 1;
	public final static int TYPE_EQUALS = 2;
	public final static int TYPE_GREATER_THAN = 3;
	public final static int TYPE_GREATER_THAN_OR_EQUALS_TO = 4;
	public final static int TYPE_LESS_THAN = 5;
	public final static int TYPE_LESS_THAN_OR_EQUALS_TO = 6;
	public final static int TYPE_DIFFERENT_THAN = 7;
	public final static int TYPE_STARTS_WITH = 8;
	public final static int TYPE_ENDS_WITH = 9;
	public final static int TYPE_NOT_STARTS_WITH = 10;
	public final static int TYPE_NOT_ENDS_WITH = 11;
	public final static int TYPE_NOT_LIKE = 12;

	public List<Order> orders;
	public Selection<Path<?>>[] selects;
	public String[] sorting;
	public List<String[]> fieldsList;
	public int limit;
	public Sort sort;
	public boolean export = false;
	
	private int offset;
	private String search = "";
	private boolean hasJoin = false;
	private String[] fields = {};
	private Map<String, String> parameters;
	private PageRequest pageRequest;

	public CustomSpecification(Map<String, String> parameters) {
		this.parameters = parameters;
		this.offset = Integer.parseInt(parameters.get("offset") == null ? "1" : parameters.get("offset")) - 1;
		this.fields = parameters.get("fields") == null ? this.fields : parameters.get("fields").split(",");
		this.search = parameters.get("search") == null ? "" : parameters.get("search");
		this.export = parameters.get("export") == null ? false : Boolean.parseBoolean(parameters.get("export"));
		this.limit = Integer.parseInt(parameters.get("limit") == null ? "0" : parameters.get("limit"));
		this.hasJoin = parameters.get("fields") == null ? false : parameters.get("fields").contains(".");
		String[] sorts = {};
		sorts = parameters.get("sort") == null ? sorts : parameters.get("sort").split(",");
		sorting = sorts;
		orders = new ArrayList<Order>();
		for (String sort : sorts) {
			String[] par = sort.split("\\.");
			if (par[1].equals("desc")) {
				orders.add(Order.desc(par[0]));
			} else if (par[1].equals("asc")) {
				orders.add(Order.asc(par[0]));
			}
		}

		if (limit == 0 && export) {
			limit = Integer.MAX_VALUE;
		} else if ((limit == 0 || limit > 20) && !export) {
			limit = 20;
		}

		sort = Sort.by(orders);
		fieldsList = new ArrayList<>();
		for (String f : this.fields) {
			fieldsList.add(f.split("\\."));
		}
		this.pageRequest = PageRequest.of(offset, limit, sort);
	}

	public String[] getFields() {
		return fields;
	}
	
	public Map<String, String> getParameters() {
		return parameters;
	}

	public PageRequest getPageRequest() {
		return pageRequest;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Predicate toPredicate(Root<Modelo> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		Predicate predicate = criteriaBuilder.and();

		for (Map.Entry<String, String> parameter: parameters.entrySet()) {
			String key = parameter.getKey();
			String value = parameter.getValue();
			
			if (key.equals("limit") || key.equals("offset") || key.equals("fields") || key.equals("sort")
					|| key.equals("export")) {

			} else if (key.equals("search")) {
				Field[] currentFields = root.getJavaType().getDeclaredFields();
				Predicate predicateSearch = criteriaBuilder.or();
				for (Field field : currentFields) {
					Class classModel = field.getType();
					Annotation annotation = classModel.getAnnotation(Entity.class);
					if (annotation == null) {
						predicateSearch = criteriaBuilder.or(predicateSearch,
								criteriaBuilder.like(root.get(field.getName()).as(String.class), "%" + value + "%"));
					}
				}
				predicate = criteriaBuilder.and(predicate, predicateSearch);

			} else if (key.equals("search.fields")) {
				Predicate predicateSearch = criteriaBuilder.or();
				for (String field : fields) {

					Path<?> pathToEvaluate;

					String search[] = field.split("\\.");
					pathToEvaluate = root.get(search[0]);
					
					Path<?> partPath = root.get(search[0]);
					SingularAttributeImpl<?, ?> singularAttribute = (SingularAttributeImpl<?, ?>) partPath.getModel();

					if (singularAttribute.getPersistentAttributeType() != PersistentAttributeType.BASIC) {
						final Join<?, ?> join = ((From<?, ?>) root).join(search[0], JoinType.LEFT);
						pathToEvaluate = join;
					} else {
						pathToEvaluate = partPath;
					}

					for (int i = 1; i < search.length; i++) {
						partPath = pathToEvaluate.get(search[i]);
						 singularAttribute = (SingularAttributeImpl<?, ?>) partPath.getModel();

						if (singularAttribute.getPersistentAttributeType() != PersistentAttributeType.BASIC) {
							final Join<?, ?> join = ((From<?, ?>) pathToEvaluate).join(search[i], JoinType.LEFT);
							pathToEvaluate = join;
						} else {
							pathToEvaluate = partPath;
						}
					}

					// if(root.getJavaType().getDeclaredField(field).getType().getAnnotation(Entity.class)==null)
					// {
					if (((SingularAttributeImpl<?, ?>) pathToEvaluate.getModel())
							.getPersistentAttributeType() == PersistentAttributeType.BASIC) {
						predicateSearch = criteriaBuilder.or(predicateSearch,
								criteriaBuilder.like(pathToEvaluate.as(String.class), "%" + value + "%"));
					}
					// }
				}
				
				predicate = criteriaBuilder.and(predicate, predicateSearch);
				
			} else {

				String entry[] = key.split("\\.");
				int filterType = TYPE_EQUALS;

				if (entry.length > 1) {
					switch (entry[entry.length - 1]) {
						case "gt":
							filterType = TYPE_GREATER_THAN;
							break;
						case "ge":
							filterType = TYPE_GREATER_THAN_OR_EQUALS_TO;
							break;
						case "lt":
							filterType = TYPE_LESS_THAN;
							break;
						case "le":
							filterType = TYPE_LESS_THAN_OR_EQUALS_TO;
							break;
						case "ne":
							filterType = TYPE_DIFFERENT_THAN;
							break;
						case "sw":
							filterType = TYPE_STARTS_WITH;
							break;
						case "ew":
							filterType = TYPE_ENDS_WITH;
							break;
						case "has":
							filterType = TYPE_LIKE;
							break;
						case "!has":
							filterType = TYPE_NOT_LIKE;
							break;
						case "!sw":
							filterType = TYPE_NOT_STARTS_WITH;
							break;
						case "!ew":
							filterType = TYPE_NOT_ENDS_WITH;
							break;
					}
				}

				Path pathToEvaluate;
				int setting = (filterType == TYPE_EQUALS) ? 0 : 1;
				try {
					pathToEvaluate = root.get(entry[0]);

					for (int i = 1; i < entry.length - setting; i++) {
						pathToEvaluate = pathToEvaluate.get(entry[i]);
					}

				} catch (Exception e) {
					throw new AttributeNotFoundException(key);
				}
				
				String className = pathToEvaluate.getJavaType().getSimpleName();
				String[] format = value.split("format=");
				String formatDefault = "";
				
				if(format.length == 2) {
					formatDefault = format[1];
				}
				
				String[] items = format[0].split("\\,");
				Predicate[] predicates = new Predicate[items.length];
				PredicateBuilder<Object> prueba = new PredicateBuilder<>();
				
				for(int i = 0; i < items.length; i++) {
					String[] orItems = items[i].split("\\;");
					Predicate predicateAnd = criteriaBuilder.and();
					Predicate[] predicatesOr = new Predicate[orItems.length];
					for(int j = 0; j < orItems.length; j++) {
						String item = orItems[j];
						Object newValue = getNewValue(item, className, formatDefault);
						predicatesOr[j] = criteriaBuilder.and(prueba.addPredicate(filterType, criteriaBuilder, pathToEvaluate, newValue));
					}
					predicateAnd = criteriaBuilder.and(predicateAnd, criteriaBuilder.or(predicatesOr));
					predicates[i] =  predicateAnd;
				}
				
				if(filterType == TYPE_EQUALS) {
					predicate = criteriaBuilder.and(predicate, criteriaBuilder.or(predicates));
				} else {
					predicate = criteriaBuilder.and(predicate, criteriaBuilder.and(predicates));
				}
			}

		}

		return predicate;
	}
	
	private Object getNewValue(String item, String className, String format) {
		Object newValue = item;
		
		if(item.equals("null")) {
			return null;
		}
		
		if(className.equalsIgnoreCase(Integer.class.getSimpleName())) {
			newValue = ObjectBuilder.getNewIntegerValue(item, format);
			
		} else if(className.equalsIgnoreCase(Double.class.getSimpleName())) {
			newValue = ObjectBuilder.getNewDoubleValue(item, format);
			
		} else if(className.equalsIgnoreCase(Boolean.class.getSimpleName())) {
			newValue = ObjectBuilder.getNewBooleanValue(item, format);
			
		} else if(className.equalsIgnoreCase(Character.class.getSimpleName())) {
			newValue = ObjectBuilder.getNewCharacterValue(item, format);
			
		} else if(className.equalsIgnoreCase(Float.class.getSimpleName())) {
			newValue = ObjectBuilder.getNewFloatValue(item, format);
			
		} else if(className.equalsIgnoreCase(Date.class.getSimpleName())) {
			newValue = ObjectBuilder.getNewDateValue(item, format);
			
		} else if(className.equalsIgnoreCase(LocalDate.class.getSimpleName())) {
			newValue = ObjectBuilder.getNewLocalDateValue(item, format);
			
		} else if(className.equalsIgnoreCase(LocalDateTime.class.getSimpleName())) {
			newValue = ObjectBuilder.getNewLocalDateTimeValue(item, format);
		}
		
		return newValue;
	}
}
