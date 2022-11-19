package com.senla.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
@Component
public class JwtAuthTokenFilter extends GenericFilterBean {

    private final JwtTokenSupplier jwtTokenSupplier;

    public AuthenticationFailureHandler authenticationFailureHandler() {
        return (request, response, exception) -> {
            response.setStatus(HttpStatus.BAD_REQUEST.value());

            Map<String, Object> data = new HashMap<>();
            data.put("timestamp", LocalDateTime.now().toString());
            data.put("exception", exception.getMessage());

            response.getWriter().println(new ObjectMapper().writeValueAsString(data));
        };
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
            throws IOException, ServletException {

        String token = jwtTokenSupplier.resolveToken((HttpServletRequest) req);
        try {
            if (Objects.nonNull(token) && jwtTokenSupplier.validateToken(token)) {
                Authentication authentication = jwtTokenSupplier.getAuthentication(token);
                if (Objects.nonNull(authentication)) {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            authenticationFailureHandler().onAuthenticationFailure(
                    (HttpServletRequest) req,
                    (HttpServletResponse) res,
                    new AuthenticationServiceException(e.getMessage()));
            return;
        }
        filterChain.doFilter(req, res);
    }
}
