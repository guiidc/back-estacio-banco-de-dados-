package com.guilherme.stock.config;

import com.guilherme.stock.services.JwtService;
import jakarta.persistence.Column;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            String accessToken = getJwtFromRequestHeader(request);

            if (accessToken == null || accessToken.isBlank()) {
                filterChain.doFilter(request, response);
                return;
            }

            if (jwtService.isTokenExpired(accessToken)) {
                System.out.println("Token is expired");
                filterChain.doFilter(request, response);
                return;
            }

            String userEmail = jwtService.getSubjectFromToken(accessToken);
            if (userEmail == null) {
                System.out.println("User email is null");
                filterChain.doFilter(request, response);
                return;
            }


            UserDetails user = userDetailsService.loadUserByUsername(userEmail);
            if (user == null) {
                System.out.println("User is null");
                filterChain.doFilter(request, response);
                return;
            }

            if (!jwtService.validateToken(accessToken, userEmail)) {
                System.out.println("Token is invalid");
                filterChain.doFilter(request, response);
                return;
            }

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            authentication.setDetails(user);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            filterChain.doFilter(request, response);
        }
    }

    public String getJwtFromRequestHeader(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return null;
        }

        return authorizationHeader.substring("Bearer ".length());
    }
}
