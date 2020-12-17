package com.sic.core;



import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import com.sic.util.CustomSpecification;





@NoRepositoryBean
public interface RepositorioBase<Modelo extends ModeloBase > extends JpaRepository<Modelo,Integer>, JpaSpecificationExecutor<Modelo>{
	public  Page<?> consultaAvanzada(CustomSpecification<Modelo> especificacion) ;
	
}
