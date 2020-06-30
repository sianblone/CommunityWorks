package com.sif.util;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public class SpringSecurityUtil {
	
	public static boolean isLoggedIn() {
		return !SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ANONYMOUS"));
	}

}
