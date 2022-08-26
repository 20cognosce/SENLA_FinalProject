package com.senla.security.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Objects;

@RequiredArgsConstructor
@Component
public class JwtAuthTokenFilter extends GenericFilterBean {

    private final JwtTokenSupplier jwtTokenSupplier;

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        String token = jwtTokenSupplier.resolveToken((HttpServletRequest) servletRequest);

        try {
            if (Objects.nonNull(token) && jwtTokenSupplier.validateToken(token)) {
                Authentication authentication = jwtTokenSupplier.getAuthentication(token);
                if (Objects.nonNull(authentication)) {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            throw new IllegalArgumentException("Просроченный или некорректный JWT-токен");
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
