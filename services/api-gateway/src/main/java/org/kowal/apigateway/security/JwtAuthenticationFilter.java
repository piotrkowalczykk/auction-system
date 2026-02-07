package org.kowal.apigateway.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.kowal.apigateway.exception.custom.JwtAuthenticationException;
import org.kowal.security.JwtService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final HandlerExceptionResolver resolver;

    public JwtAuthenticationFilter(JwtService jwtService, @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.jwtService = jwtService;
        this.resolver = resolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if(header == null || !header.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);

        try{
            String userId = jwtService.extractUserId(token);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userId, null, Collections.emptyList());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException ex){
            SecurityContextHolder.clearContext();
            resolver.resolveException(request, response, null, new JwtAuthenticationException("Token expired", ex));
            return;
        } catch (JwtException ex){
            SecurityContextHolder.clearContext();
            resolver.resolveException(request, response, null, new JwtAuthenticationException("Invalid token", ex));
            return;
        }


    }
}
