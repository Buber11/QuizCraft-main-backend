package main.QuizCraft.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            if (isPublicEndpoint(request)) {
                filterChain.doFilter(request, response);
                return;
            }

            String token = jwtService.extractToken(request, "jwt_token");
            if (token != null && isAuthenticationRequired()) {
                authenticateRequest(request, token);
                maybeAttachRefreshToken(request);
            }

        } catch (Exception ex) {
            handlerExceptionResolver.resolveException(request, response, null, ex);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean isPublicEndpoint(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return Arrays.stream(SecurityConfiguration.PUBLIC_ENDPOINTS).anyMatch(uri::startsWith);
    }

    private boolean isAuthenticationRequired() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth == null || !auth.isAuthenticated();
    }

    private void authenticateRequest(HttpServletRequest request, String token) {
        String username = jwtService.extractUsername(token);
        if (username == null) return;

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (!jwtService.isTokenValid(token, userDetails)) return;

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);

        request.setAttribute("user_id", jwtService.extractUserId(token));
    }

    private void maybeAttachRefreshToken(HttpServletRequest request) {
        String uri = request.getRequestURI();
        if (Arrays.stream(SecurityConfiguration.REFRESH_TOKEN_ENDPOINT).anyMatch(uri::startsWith)) {
            String refreshToken = jwtService.extractToken(request, "jwt_refresh_token");
            request.setAttribute("refresh_token", refreshToken);
        }
    }
}
