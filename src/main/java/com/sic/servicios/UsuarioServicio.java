package com.sic.servicios;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sic.core.ServicioBase;
import com.sic.modelos.Usuario;
import com.sic.repositorios.UsuarioRepositorio;

@Service
public class UsuarioServicio extends ServicioBase<UsuarioRepositorio,Usuario> implements UserDetailsService {

	@Autowired
	private PasswordEncoder passwordEncoder;

	public Usuario buscarUsuarioPorNombre(String usuario) {
		List<Usuario> usuarios;
		usuarios=repositorio.findByUsername(usuario);
		if(usuarios.size()>0)
			return usuarios.get(0);
		return null;
		
	}
	
	@Override
	public void crear(Usuario usuario) {
		String username = usuario.getUsername();
		usuario.setPassword(usuario.getPassword());
		repositorio.save(usuario);	
	}
	
	@Override 
	public Usuario actualizar(Usuario usuario, Integer id) {
		Usuario oldUser = consultar(id);
		
		String username = usuario.getUsername();
		Usuario userByUsername = buscarUsuarioPorNombre(username);
		
		if(userByUsername != null && userByUsername.getId() != id) {
			throw new UsernameNotFoundException(username);
		}
		
		if(usuario.getPassword() != null && !usuario.getPassword().isEmpty()) {
			usuario.setPassword(usuario.getPassword());
		} else {
			usuario.setPassword(oldUser.getPassword());
		}
		
		return super.actualizar(usuario, id);	
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Usuario usuario=buscarUsuarioPorNombre(username);
		if (usuario!=null) {
			return new User(usuario.getUsername(), usuario.getPassword(),
					new ArrayList<>());
		} else {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
	}
}
