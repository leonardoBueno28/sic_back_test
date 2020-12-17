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
@Table(name="PERSONA")
public class Persona extends ModeloBase{

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="PERSONA_SEQ")
	@SequenceGenerator(name="PERSONA_SEQ", sequenceName="persona_seq")
	private Integer id;

	public Integer getId() {
		return id;
	}

	private String tipoIdentificacion;
	
	private Integer identificacion;
	
	private String nombres;
	
	private String apellidos;
	
	private String telefono;

	@Column(columnDefinition = "TEXT")
	private String direccion;

	private String email;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name = "usuarioId", insertable = false, updatable = false)
	@JsonIdentityReference(alwaysAsId = true)
	private Usuario usuario;
	private Integer usuarioId;

	public String getTipoIdentificacion() {
		return tipoIdentificacion;
	}

	public void setTipoIdentificacion(String tipoIdentificacion) {
		this.tipoIdentificacion = tipoIdentificacion;
	}

	public Integer getIdentificacion() {
		return identificacion;
	}

	public void setIdentificacion(Integer identificacion) {
		this.identificacion = identificacion;
	}

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(Integer usuarioId) {
		this.usuarioId = usuarioId;
	}
	
	public Usuario getUsuario() {
		return usuario;
	}
	
	
}
