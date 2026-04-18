package com.snowbird.donations.security.filter;

import com.snowbird.donations.security.model.CurrentUser;
import com.snowbird.donations.security.util.JwtTokenService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenService jwtTokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            Claims claims = jwtTokenService.parseToken(token);
            String userId = jwtTokenService.extractPrincipal(claims);
            String name = jwtTokenService.extractName(claims);
            String email = jwtTokenService.extractEmail(claims);
            List<String> roles = jwtTokenService.extractRoles(claims);

            Set<SimpleGrantedAuthority> authorities = roles.stream()
                    .filter(role -> role != null && !role.isBlank())
                    .map(String::trim)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toSet());

            CurrentUser currentUser = CurrentUser.builder()
                    .userId(userId)
                    .name(name)
                    .email(email)
                    .roles(roles.stream().collect(Collectors.toSet()))
                    .authorities(authorities)
                    .build();

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(currentUser, null, authorities);

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}
