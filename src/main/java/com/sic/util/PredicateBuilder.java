package com.sic.util;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

public class PredicateBuilder<T> {
	
	public Predicate addPredicate(int filterType, CriteriaBuilder criteriaBuilder, Path pathToEvaluate, T value) {
		Predicate predicate = criteriaBuilder.and();
		Comparable comparable=(Comparable) value;
		
		if(value == null) {
			predicate = criteriaBuilder.and(predicate, criteriaBuilder.isNull(pathToEvaluate));
			return predicate;
		}
		
		switch (filterType) {
			case CustomSpecification.TYPE_EQUALS:
				predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(pathToEvaluate, comparable));
				break;
			case CustomSpecification.TYPE_LIKE:
				predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(pathToEvaluate.as(String.class), "%" + comparable + "%"));
				break;
			case CustomSpecification.TYPE_GREATER_THAN:
				predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThan(pathToEvaluate, comparable));
				break;
			case CustomSpecification.TYPE_GREATER_THAN_OR_EQUALS_TO:
				predicate = criteriaBuilder.and(predicate,
						criteriaBuilder.greaterThanOrEqualTo(pathToEvaluate,comparable));
				break;
			case CustomSpecification.TYPE_LESS_THAN:
				predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThan(pathToEvaluate, comparable));
				break;
			case CustomSpecification.TYPE_LESS_THAN_OR_EQUALS_TO:
				predicate = criteriaBuilder.and(predicate,
						criteriaBuilder.lessThanOrEqualTo(pathToEvaluate, comparable));
				break;
			case CustomSpecification.TYPE_DIFFERENT_THAN:
				predicate = criteriaBuilder.and(predicate, criteriaBuilder.notEqual(pathToEvaluate, comparable));
				break;
			case CustomSpecification.TYPE_STARTS_WITH:
				predicate = criteriaBuilder.and(predicate,
						criteriaBuilder.like(pathToEvaluate.as(String.class), comparable + "%"));
				break;
			case CustomSpecification.TYPE_ENDS_WITH:
				predicate = criteriaBuilder.and(predicate,
						criteriaBuilder.like(pathToEvaluate.as(String.class), "%" + comparable));
				break;
			case CustomSpecification.TYPE_NOT_LIKE:
				predicate = criteriaBuilder.and(predicate,
						criteriaBuilder.notLike(pathToEvaluate.as(String.class), "%" + comparable + "%"));
				break;
			case CustomSpecification.TYPE_NOT_STARTS_WITH:
				predicate = criteriaBuilder.and(predicate,
						criteriaBuilder.notLike(pathToEvaluate.as(String.class), comparable + "%"));
				break;
			case CustomSpecification.TYPE_NOT_ENDS_WITH:
				predicate = criteriaBuilder.and(predicate,
						criteriaBuilder.notLike(pathToEvaluate.as(String.class), "%" + comparable));
				break;
		}
	
		return predicate;
	}
}
