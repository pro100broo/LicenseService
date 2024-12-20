package ru.mtuci.license_service.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.context.SecurityContextHolder;


import ru.mtuci.license_service.models.orm.UserDetailsImpl;
import ru.mtuci.license_service.servicies.UserService;
import ru.mtuci.license_service.utils.LicenseServiceException;

import java.io.IOException;


@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String HEADER_NAME = "Authorization";
    private final JwtService jwtService;
    private final UserService userService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            String authHeader = request.getHeader(HEADER_NAME);

            if (authHeader == null || authHeader.isEmpty() || !authHeader.startsWith(BEARER_PREFIX)) {
                filterChain.doFilter(request, response);
                return;
            }

            String username = jwtService.extractUserName(request);

            if (username.isEmpty() && SecurityContextHolder.getContext().getAuthentication() != null) {
                throw new LicenseServiceException("Invalid token data");
            }

            UserDetailsImpl user = userService.getByUsername(username);

            if (!jwtService.isTokenValid(request, user)) {
                throw new LicenseServiceException("Invalid token data or token expired");
            }

            SecurityContext context = SecurityContextHolder.createEmptyContext();

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    user,
                    null,
                    user.getAuthorities()
            );

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            context.setAuthentication(authToken);
            SecurityContextHolder.setContext(context);
            filterChain.doFilter(request, response);

        } catch (LicenseServiceException ex) {
            int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
            String responseBody = String.format(
                    "{ \"statusCode\": \"%d\", \"description\": \"%s\"}",
                    status,
                    ex.getMessage()
            );
            response.setStatus(status);
            response.getOutputStream().print(responseBody);
        }
    }
}
