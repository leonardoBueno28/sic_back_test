package com.sic.modelos;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.sic.core.ModeloBase;
@Entity
@Table(name="EMPLEADO")
public class Empleado extends ModeloBase{

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="EMPLEADO_SEQ")
	@SequenceGenerator(name="EMPLEADO_SEQ", sequenceName="empleado_seq")
	private Integer id;

	private String dependencia;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm", shape = Shape.STRING, timezone = "America/Bogota")
	private Date fechaIngreso;

	public String getDependencia() {
		return dependencia;
	}

	public void setDependencia(String dependencia) {
		this.dependencia = dependencia;
	}

	public Date getFechaIngreso() {
		return fechaIngreso;
	}

	public void setFechaIngreso(Date fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}
	
	
	
}