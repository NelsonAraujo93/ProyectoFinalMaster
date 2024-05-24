package com.example.ayuntamientoapi.seguridad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class JWTRequestFilter extends OncePerRequestFilter {

    @Autowired
    private UtilidadJWT utilidadJWT;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest solicitud, @NonNull HttpServletResponse respuesta, @NonNull FilterChain cadena)
            throws ServletException, IOException {

        final String encabezadoAutorizacion = solicitud.getHeader("Authorization");

        String nombreUsuario = null;
        String jwt = null;

        if (encabezadoAutorizacion != null && encabezadoAutorizacion.startsWith("Bearer ")) {
            jwt = encabezadoAutorizacion.substring(7);
            nombreUsuario = utilidadJWT.extraerNombreUsuario(jwt);
        }

        if (nombreUsuario != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(nombreUsuario, null, new ArrayList<>());
            usernamePasswordAuthenticationToken
                    .setDetails(new WebAuthenticationDetailsSource().buildDetails(solicitud));
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }
        cadena.doFilter(solicitud, respuesta);
    }
}
