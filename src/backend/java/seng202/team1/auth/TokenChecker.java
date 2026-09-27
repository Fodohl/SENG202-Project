package seng202.team1.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.List;

@Component
public class TokenChecker extends OncePerRequestFilter {

    private final TokenService tokenService;

    public TokenChecker(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        try{

            // Must have a token
            String header = request.getHeader("Authorization");
            if (header == null || !header.startsWith("Bearer "))
                throw new InvalidParameterException("Request must have bearer token");


            // Parse the token
            String token = header.substring(7);
            Long user = tokenService.parseToken(token);

            // Invalid token
            if(user == -1L)
                throw  new InvalidParameterException("Token malformed or not signed");

            // Store the auth so routes can use it
            var auth = new UsernamePasswordAuthenticationToken(user, null, List.of());
            SecurityContextHolder.getContext().setAuthentication(auth);

        } catch (Exception e) {

            logger.info(String.format("Token auth failed: %s", e.getMessage()));
            return;

        } finally {

            // Pass to backend
            chain.doFilter(request, response);

        }
    }
}
