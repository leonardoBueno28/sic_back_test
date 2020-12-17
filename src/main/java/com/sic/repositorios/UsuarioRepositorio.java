package com.sic.repositorios;

import java.util.List;

import com.sic.core.RepositorioBase;
import com.sic.modelos.Usuario;

public interface UsuarioRepositorio extends RepositorioBase<Usuario>  {

	public List< Usuario >findByUsername(String usuario);

}
