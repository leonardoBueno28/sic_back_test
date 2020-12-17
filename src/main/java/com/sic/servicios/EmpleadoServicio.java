package com.sic.servicios;

import org.springframework.stereotype.Service;

import com.sic.core.ServicioBase;
import com.sic.modelos.Empleado;
import com.sic.repositorios.EmpleadoRepositorio;

@Service
public class EmpleadoServicio extends ServicioBase<EmpleadoRepositorio,Empleado> {

}

