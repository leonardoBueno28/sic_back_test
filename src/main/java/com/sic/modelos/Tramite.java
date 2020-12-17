package com.sic.modelos;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.sic.core.ModeloBase;
@Entity
@Table(name="TRAMITE")
public class Tramite extends ModeloBase{
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="TRAMITE_SEQ")
	@SequenceGenerator(name="TRAMITE_SEQ", sequenceName="tramite_seq")
	private Integer id;
	
	public Integer getId() {
		return id;
	}

	private Integer numero;

	private Integer anhoRadicacion;

	private String nombre;

	@Column(columnDefinition = "TEXT")
	private String descripcion;

	@ManyToOne(fetch = FetchType.EAGER)
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name = "personaRadicoIdId", insertable = false, updatable = false)
	@JsonIdentityReference(alwaysAsId = true)
	private Persona personaRadico;
	private Integer personaRadicoIdId;

	@ManyToOne(fetch = FetchType.EAGER)
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name = "funcionarioRecibioId", insertable = false, updatable = false)
	@JsonIdentityReference(alwaysAsId = true)
	private Persona funcionarioRecibio;
	private Integer funcionarioRecibioId;
	
	private String estado;
	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Integer getAnhoRadicacion() {
		return anhoRadicacion;
	}

	public void setAnhoRadicacion(Integer anhoRadicacion) {
		this.anhoRadicacion = anhoRadicacion;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public Integer getPersonaRadicoIdId() {
		return personaRadicoIdId;
	}

	public void setPersonaRadicoIdId(Integer personaRadicoIdId) {
		this.personaRadicoIdId = personaRadicoIdId;
	}

	public Persona getPersonaRadico() {
		return personaRadico;
	}

	public Integer getFuncionarioRecibioId() {
		return funcionarioRecibioId;
	}

	public void setFuncionarioRecibioId(Integer funcionarioRecibioId) {
		this.funcionarioRecibioId = funcionarioRecibioId;
	}

	public Persona getFuncionarioRecibio() {
		return funcionarioRecibio;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	} 

}
