package com.sic.core.manejoRepositorio.impl;

import java.beans.Expression;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import javax.persistence.metamodel.Attribute.PersistentAttributeType;

import org.hibernate.metamodel.internal.SingularAttributeImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.sic.core.ModeloBase;
import com.sic.core.RepositorioBase;
import com.sic.util.CustomSpecification;

public class RepositorioBaseImpl<Modelo extends ModeloBase> extends SimpleJpaRepository<Modelo, Integer>
		implements RepositorioBase<Modelo> {

	private EntityManager entityManager;

	public RepositorioBaseImpl(JpaEntityInformation<Modelo, ?> entityInformation, EntityManager entityManager) {
		super(entityInformation, entityManager);
		this.entityManager = entityManager;
	}

	@Transactional(readOnly = true)
	public Page<?> consultaAvanzada(CustomSpecification<Modelo> especificacion) {

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Tuple> cQuery = builder.createTupleQuery();
		Root<Modelo> root = cQuery.from(getDomainClass());

		String[] fields = especificacion.getFields();
		int tamanoFields = fields.length;
		Path<?>[] paths = new Path<?>[tamanoFields];

		for (int i = 0; i < tamanoFields; i++) {

			//paths[i]= getPath(root,fields[i]);
			
			String[] rutas = fields[i].split("\\.");
			paths[i] = getPath(root,rutas[0]) ;
			

			for (int j = 1; j < rutas.length; j++) {
				paths[i] = getPath( paths[i],rutas[j]);

			}
	
			
		}
	

		if (tamanoFields > 0) {
			cQuery.multiselect(paths);
		} else {
			cQuery.multiselect(root.alias("modelo"));
		}
		List<Order> orders = new ArrayList<>();

		for (String sort : especificacion.sorting) {
			String[] par = sort.split("\\.");
			if (par[par.length - 1].equals("desc")) {
				Path or = root.get(par[0]);
				for (int j = 1; j < par.length - 1; j++) {
					or = or.get(par[j]);
				}
				orders.add(builder.desc(or));
			} else if (par[par.length - 1].equals("asc")) {
				Path or = root.get(par[0]);
				for (int j = 1; j < par.length - 1; j++) {
					or = or.get(par[j]);
				}
				orders.add(builder.asc(or));
			}
		}

		cQuery.orderBy(orders).where(especificacion.toPredicate(root, cQuery, builder));

		CriteriaQuery<Long> cq = builder.createQuery(Long.class);
		Root<Modelo> rc = cq.from(getDomainClass());
		cq.select(builder.count(rc)).where(especificacion.toPredicate(rc, cq, builder));

		Long count = entityManager.createQuery(cq).getSingleResult();

		Query query = entityManager.createQuery(cQuery);

		int offset = (int) especificacion.getPageRequest().getOffset();
		int pageSize = (int) especificacion.getPageRequest().getPageSize();

		query.setFirstResult(offset);
		query.setMaxResults(pageSize);

		List<Tuple> resultados = query.getResultList();

		List<Object> resultadoLista = new ArrayList<>();
		if (tamanoFields > 0) {
			for (Tuple tuple : resultados) {
				Map<String, Object> convert = new LinkedHashMap<String, Object>();
				int i=0;
			
				for (String[] campos: especificacion.fieldsList) {
					
					int tamC=campos.length;
					
					if(tamC==1) {
						if(tuple.get(i)==null) {
							convert.put(campos[0], null);
						}else if(tuple.get(i).getClass()==java.sql.Timestamp.class) {
							SimpleDateFormat formato=new SimpleDateFormat("yyyy-MM-dd HH:mm");
							convert.put(campos[0],	formato.format( tuple.get(i))); 
						}else {
							convert.put(campos[0], tuple.get(i));
						}
						
						
						
					}else {
						
						Map<String, Object> last=convert;
						
						for(int j=0;j<campos.length-1;j++) {
							
							if(last.get(campos[j])==null) {
								last.put(campos[j],  new LinkedHashMap<String, Object>());
							}
							last=(Map<String, Object>) last.get(campos[j]);
							
						}
						
								
						if(tuple.get(i)==null) {
							last.put(campos[tamC-1], null);
						}else {
							if(tuple.get(i).getClass()==java.sql.Timestamp.class) {
								
								
								SimpleDateFormat formato=new SimpleDateFormat("dd/MM/yy");
								last.put(campos[tamC-1],	formato.format( tuple.get(i))); 
						     
						      
							}else {
								last.put(campos[tamC-1], tuple.get(i));
							}
						}
						
					}
				
					
					i++;
				}
				
			
				resultadoLista.add(convert);

			}
		} else {
			for (Tuple tuple : resultados) {
				resultadoLista.add(tuple.get(0));

			}
		}

		PageImpl<?> resultado = new PageImpl<Object>(resultadoLista, especificacion.getPageRequest(), count);

		return resultado;
	}

	
	
	  public static <T> Path<?> getPath(final Path<?> base, final String naam) {
	   
		  	final Path<?> result;
	           
	        final Path<?> partPath = base.get(naam);
	        SingularAttributeImpl<?, ?> coso= (SingularAttributeImpl<?, ?>) partPath.getModel();
	     
	        if (   coso.getPersistentAttributeType()!=PersistentAttributeType.BASIC) {
	            final Join<?, ?> join = ((From<?, ?>) base).join(naam, JoinType.LEFT);
	          	return join;
	        } else {
	           result = partPath;
	        }
	   
	        return result;
	    }
	
	
	
}
