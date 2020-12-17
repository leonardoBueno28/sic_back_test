package com.sic.core;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Tuple;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.sic.util.CustomSpecification;

@Service

public abstract class ServicioBase<Repositorio extends RepositorioBase<Modelo>, Modelo extends ModeloBase> {

	
	
	@Autowired
	protected Repositorio repositorio;
	
	public void crear(Modelo modelo) {
		repositorio.save(modelo);
	}

	public Modelo consultar(Integer id) {
		return repositorio.findById(id).orElse(null);
	}
	public Page<Modelo> consultar(CustomSpecification<Modelo> especificacion){
		
		
		
		Page<Modelo> resultado=repositorio.findAll(especificacion,especificacion.getPageRequest());
		return resultado;
	}
	

	
	public Modelo actualizar(Modelo modelo,Integer id) {
		Modelo modeloAnterior=repositorio.findById(id).orElse(null);
		if(modeloAnterior==null) {
			return null;
		}
		try {
			
			Class clase=modelo.getClass();
	
			for(Field campo:clase.getDeclaredFields()) {
				campo.setAccessible(true);
				Object valor=campo.get(modelo);
					if (valor!=null) {
						campo.set(modeloAnterior, valor);
					}
			
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	
		return repositorio.save(modeloAnterior);
	}
	
	public boolean eliminar(Integer id) {
		repositorio.deleteById(id);
		return true;
	}

	public Page<?> consultaAvanzada(CustomSpecification<Modelo> especificacion) {
		// TODO Auto-generated method stub
		 return repositorio.consultaAvanzada(especificacion);
		//	Page<Modelo> resultado=repositorio.findAll(especificacion,especificacion.getPageRequest());
		//	return resultado;
	}
	
	

}
