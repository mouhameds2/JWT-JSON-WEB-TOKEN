package tech.fall.avis.securite;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import tech.fall.avis.entite.Jwt;
import tech.fall.avis.service.UtilisateurService;

import java.io.IOException;

@Service
public class JwtFilter extends OncePerRequestFilter {

    private static final String BEARER = "Bearer";
    private UtilisateurService utilisateurService;
    private JwtService jwtService;
    //handlerExceptionResolver son role c'est de recuperer l'exception et le transferer à notre servlet
    private HandlerExceptionResolver handlerExceptionResolver;

    public JwtFilter(UtilisateurService utilisateurService, JwtService jwtService,HandlerExceptionResolver handlerExceptionResolver) {
        this.utilisateurService = utilisateurService;
        this.jwtService = jwtService;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = null;
        Jwt tokenDansLaBDD = null;
        String username = null;
        boolean isTokenExpired = true;

        try {
            // Bearer eyJhbGciOiJIUzI1NiJ9.eyJub20iOiJBY2hpbGxlIE1CT1VHVUVORyIsImVtYWlsIjoiYWNoaWxsZS5tYm91Z3VlbmdAY2hpbGxvLnRlY2gifQ.zDuRKmkonHdUez-CLWKIk5Jdq9vFSUgxtgdU1H2216U
            final String authorization = request.getHeader("Authorization");
            if (authorization != null && authorization.startsWith(BEARER + " ")) {

                token = authorization.substring(7);
                tokenDansLaBDD = this.jwtService.tokenByValue(token);
                isTokenExpired = jwtService.isTokenExpired(token);
                username = jwtService.extractUsername(token);
                System.out.println("===Token==== " + token
                        + "===username==== " + username);
            }

            if (!isTokenExpired && username != null &&
                    tokenDansLaBDD.getUtilisateur().getEmail().equals(username) &&
                    SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = utilisateurService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }

            filterChain.doFilter(request, response);

        } catch (Exception exception) {
            handlerExceptionResolver.resolveException(request, response, null, exception);
        }
    }
}
