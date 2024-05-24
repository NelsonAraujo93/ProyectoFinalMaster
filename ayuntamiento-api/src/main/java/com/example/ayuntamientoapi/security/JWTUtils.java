package com.example.ayuntamientoapi.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JWTUtils {

    private String CLAVE_SECRETA = "secreto";

    public String extraerNombreUsuario(String token) {
        return extraerReclamacion(token, Claims::getSubject);
    }

    public Date extraerExpiracion(String token) {
        return extraerReclamacion(token, Claims::getExpiration);
    }

    public <T> T extraerReclamacion(String token, Function<Claims, T> reclamacionResolver) {
        final Claims reclamaciones = extraerTodasReclamaciones(token);
        return reclamacionResolver.apply(reclamaciones);
    }

    private Claims extraerTodasReclamaciones(String token) {
        return Jwts.parser().setSigningKey(CLAVE_SECRETA).parseClaimsJws(token).getBody();
    }

    private Boolean esTokenExpirado(String token) {
        return extraerExpiracion(token).before(new Date());
    }

    public String generarToken(String nombreUsuario) {
        Map<String, Object> reclamaciones = new HashMap<>();
        return crearToken(reclamaciones, nombreUsuario);
    }

    private String crearToken(Map<String, Object> reclamaciones, String sujeto) {
        return Jwts.builder().setClaims(reclamaciones).setSubject(sujeto).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, CLAVE_SECRETA).compact();
    }

    public Boolean validarToken(String token, String nombreUsuario) {
        final String nombreUsuarioExtraido = extraerNombreUsuario(token);
        return (nombreUsuarioExtraido.equals(nombreUsuario) && !esTokenExpirado(token));
    }
}
