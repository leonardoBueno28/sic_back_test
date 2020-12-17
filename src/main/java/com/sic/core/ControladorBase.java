package com.sic.core;


import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Tuple;
import javax.validation.Valid;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;


import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sic.core.exceptions.AttributeNotFoundException;
import com.sic.core.util.FieldsErrorController;
import com.sic.util.CustomSpecification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType;

@RestController
@EnableHypermediaSupport(type = HypermediaType.HAL)
public abstract class ControladorBase<Modelo extends ModeloBase,Servicio extends ServicioBase<? extends RepositorioBase<Modelo>,Modelo >> {
	
	@Autowired
	protected Servicio servicio;
	

	protected ModeloBase modelo;
	private final Class<Modelo> persistentClass;
	
	
	public ControladorBase() {
		this.persistentClass=(Class<Modelo>) ((ParameterizedType) this.getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];
	}
	
	@PostMapping() 
	public @ResponseBody ResponseEntity<?>crear(@Valid @RequestBody Modelo modelo, BindingResult result) {
		if(result.hasErrors()) {
            return FieldsErrorController.attachErrors(result.getFieldErrors());
        }
		
		servicio.crear(modelo);
		return new ResponseEntity<ModeloBase>(modelo,HttpStatus.OK);
	}

	@GetMapping(value="/{id}")
	public @ResponseBody ResponseEntity< ModeloBase> consultar(@PathVariable("id") Integer id) {
		Modelo modelo=servicio.consultar(id);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");   
		return new ResponseEntity<ModeloBase>(modelo,HttpStatus.OK);
		
	}

	@GetMapping()
	public @ResponseBody ResponseEntity<Map > todas(@RequestParam Map<String,String> requestParams) {
			
		HttpHeaders responseHeaders = new HttpHeaders();
		try {
			
		
			
			Map<String,Object> response =new LinkedHashMap<String, Object>();
			Map<String,Object> links =new LinkedHashMap<String, Object>();

			CustomSpecification<Modelo> especificacion=new CustomSpecification<Modelo>( requestParams);
			
		Page<?> resultado= servicio.consultaAvanzada(especificacion);
		final int paginaActual=resultado.getNumber()+1;
		final int ultimaPagina=resultado.getTotalPages();
		final int pageSize=resultado.getSize();
		
		Link linkSelf = linkTo(methodOn(this.getClass()).todas(requestParams)).withSelfRel();
		links.put("self", linkSelf.getHref().toString());
		requestParams.put("offset", (1)+"");
		final Link linkFirst= linkTo(methodOn(this.getClass()).todas(requestParams)).withSelfRel();
		links.put("first", linkFirst.getHref().toString());
	
		if(paginaActual<ultimaPagina && paginaActual>=1) {
			requestParams.put("offset", (paginaActual+1)+"");
			final Link linkNext= linkTo(methodOn(this.getClass()).todas(requestParams)).withSelfRel();
			links.put("next", linkNext.getHref().toString());
		}else {
			
			links.put("next","");
		}
		
		
		if(paginaActual>1 && paginaActual<=ultimaPagina) {
			requestParams.put("offset", (paginaActual-1)+"");
			final Link linkPrev	= linkTo(methodOn(this.getClass()).todas(requestParams)).withSelfRel();
			links.put("prev", linkPrev.getHref().toString());
		}else {
			
			links.put("prev","");
		}
		requestParams.put("offset", (ultimaPagina)+"");
		final Link linkLast= linkTo(methodOn(this.getClass()).todas(requestParams)).withSelfRel();
		links.put("last", linkLast.getHref().toString());
	
		long total=resultado.getTotalElements();	
		response.put("pages",ultimaPagina);
		response.put("pageSize", pageSize);
		response.put("actualPage", paginaActual);
		response.put("total", total);
		response.put("links",links);
		
		
		
		response.put("data", resultado.getContent());
		
		
		return new ResponseEntity<Map> (response,responseHeaders,HttpStatus.OK);
			
	/*	Page<?> resultado=servicio.consultar(especificacion);	
			


			if(especificacion.getFields().length>0) {
				List<Map<String,Object>> result=convertToFields(resultado.getContent(),especificacion.getFields());
				response.put("data", result);			
			}else{
				response.put("data", resultado.getContent());
				
			}
			
				*/
			
			
		}catch (AttributeNotFoundException ex) {
			
			Map<String,String> body = new LinkedHashMap<String, String>();;
			body.put("mensaje", ex.getMessage());
			return new ResponseEntity<Map> (body,responseHeaders,HttpStatus.OK);	
			
			
			
		}

		
	}
	
	@PatchMapping(value="/{id}")
	public @ResponseBody ResponseEntity< Modelo >actualizar(@PathVariable("id") Integer id,@RequestBody Modelo modelo) {
		servicio.actualizar(modelo,id);
		return new ResponseEntity<Modelo>(modelo,HttpStatus.OK);
	}
	@DeleteMapping(value="/{id}")
	public @ResponseBody ResponseEntity< ModeloBase >actualizar(@PathVariable("id") Integer id) {
		servicio.eliminar(id);
		
		return new ResponseEntity<ModeloBase>(modelo,HttpStatus.OK);
	}
	
	public List< Map<String,Object>>  convertToFields(List<?> modelos,String[] sFields){
		
		List< Map<String,Object>> result=new ArrayList<>();

		
		ArrayList<Llamado> llamados=new ArrayList<Llamado>();
		for(String field:sFields) {
			try{
				llamados.add(new Llamado(persistentClass.getMethod("get"+field.substring(0, 1).toUpperCase()+field.substring(1)),field));
			}catch(Exception e) {
				
			}
		}
		for(Object modelo: modelos) {
			Map<String,Object> convert= new LinkedHashMap<String, Object>();
			for(Llamado llamado:llamados) {		
				
				try {
					JsonFormat jsonFormat=llamado.method.getDeclaredAnnotation(JsonFormat.class);
			
					if(jsonFormat==null) {
						convert.put(llamado.key,llamado.method.invoke(modelo));
						
					}else {
						if(llamado.method.getReturnType()==Date.class) {
							String patron=jsonFormat.pattern();
		
							DateFormat dateFormat = new SimpleDateFormat(patron);
							
							Object dato=llamado.method.invoke(modelo);
							
							if(dato!=null) {
								convert.put(llamado.key,dateFormat.format(dato ));
							}else {
								convert.put(llamado.key, dato);
							}
							
							
						}							
					}
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
			result.add(convert);
		}
		return result;
		
	}
	class Llamado{
		Method method;
		String key;
		Llamado(Method method,String key){
			this.method=method;
			this.key=key;
		}
	}

}
