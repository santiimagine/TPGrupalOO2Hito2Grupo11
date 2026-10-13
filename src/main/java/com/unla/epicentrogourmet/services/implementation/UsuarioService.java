package com.unla.epicentrogourmet.services.implementation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.unla.epicentrogourmet.entities.UserRol;
import com.unla.epicentrogourmet.entities.Usuario;
import com.unla.epicentrogourmet.repositories.IUsuarioRepository;

@Service("usuarioService")
public class UsuarioService implements UserDetailsService {

	private IUsuarioRepository usuarioRepository;

	public UsuarioService(IUsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
		Usuario usuario = usuarioRepository.findByMailAndFetchRolesEagerly(mail);
		if (usuario == null) {
			throw new UsernameNotFoundException("Usuario no encontrado: " + mail);
		}
		return buildUser(usuario, buildGrantedAuthorities(usuario.getRoles()));
	}

	private User buildUser(Usuario usuario, List<GrantedAuthority> grantedAuthorities) {
		return new User(usuario.getMail(), usuario.getContrasenia(), true,
						true, true, true, //accountNonExpired, credentialsNonExpired, accountNonLocked,
						grantedAuthorities);
	}

	private List<GrantedAuthority> buildGrantedAuthorities(Set<UserRol> roles) {
		Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
		for(UserRol userRol: roles) {
			grantedAuthorities.add(new SimpleGrantedAuthority(userRol.getRol().name()));
		}
		return new ArrayList<>(grantedAuthorities);
	}
}
