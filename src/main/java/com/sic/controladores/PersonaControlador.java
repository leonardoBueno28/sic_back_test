package com.sic.controladores;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sic.core.ControladorBase;
import com.sic.modelos.Persona;
import com.sic.servicios.PersonaServicio;


@RestController
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
@RequestMapping(path="/personas")
public class PersonaControlador extends ControladorBase<Persona,PersonaServicio> {

}