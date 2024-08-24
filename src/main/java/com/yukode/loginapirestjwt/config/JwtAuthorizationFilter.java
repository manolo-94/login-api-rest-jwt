package com.yukode.loginapirestjwt.config;

import com.yukode.loginapirestjwt.security.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtAuthorizationFilter extends OncePerRequestFilter {

//    @Autowired
//    private JwtUtils jwtUtils;
    private final JwtUtils jwtUtils;

    public JwtAuthorizationFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String jwt = parseJwt(request);

        String path = request.getRequestURI();

//        if("/api/auth/register".equals(path) || "/api/auth/login".equals(path)){
//            
//            filterChain.doFilter(request, response);
//            return;
//        }

        if (jwt != null && jwtUtils.validateJwtToken(jwt)) {

            String email = jwtUtils.getEmailFromJwToken(jwt);
            String role = jwtUtils.getRoleFromJwtToken(jwt);
            
            List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));

            UsernamePasswordAuthenticationToken authenticazion = new UsernamePasswordAuthenticationToken(email, null, authorities);

            authenticazion.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authenticazion);

        }

        filterChain.doFilter(request, response);

//        throw new UnsupportedOperationException("Not supported yet."); 
    }

    private String parseJwt(HttpServletRequest request) {

        String headerAuth = request.getHeader("Authorization");

        if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }

}
