package com.sic.controladores;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sic.core.ControladorBase;
import com.sic.modelos.Tramite;
import com.sic.servicios.TramiteServicio;


@RestController
@RequestMapping(path="/tramites")
public class TramiteControlador extends ControladorBase<Tramite,TramiteServicio> {

}
