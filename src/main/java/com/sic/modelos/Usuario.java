package com.sic.modelos;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.sic.core.ModeloBase;

@Entity
@Table(name="USUARIOS")
public class Usuario extends ModeloBase{

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="USUARIO_SEQ")
	@SequenceGenerator(name="USUARIO_SEQ", sequenceName="usuario_seq")
	private Integer id;
	
	private String username;

	private String password;

	private String rol;

	public Integer getId() {
		return id;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}
}
