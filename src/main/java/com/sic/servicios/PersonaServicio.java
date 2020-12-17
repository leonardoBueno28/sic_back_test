package com.sic.servicios;

import org.springframework.stereotype.Service;

import com.sic.core.ServicioBase;
import com.sic.modelos.Persona;
import com.sic.repositorios.PersonaRepositorio;

@Service
public class PersonaServicio extends ServicioBase<PersonaRepositorio,Persona> {

}
