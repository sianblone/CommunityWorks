package com.sif.util;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public class SpSec {
	
	public static boolean isLoggedIn() {
		return !SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ANONYMOUS"));
	}
	
	public static boolean isAdmin() {
		return SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
	}
	
	public static boolean isUser() {
		return SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER"));
	}
	
	public static String username() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}

}
