package etf.sni.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import etf.sni.data.model.Users;
import etf.sni.service.UsersService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;



@Component
public class JwtRequestFilter extends OncePerRequestFilter{
	@Autowired
	private UsersService usersServ;
	
	private final JwtService jwtServ = new JwtService();
	
	private final TokenBlacklist tokenBlacklist = new TokenBlacklist();
    
    
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		final String authorizationHeader = request.getHeader("Authorization");
        String username = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            username = jwtServ.extractUsername(jwt);
        }

        if (username != null 
        	&& SecurityContextHolder.getContext().getAuthentication() == null
        	&& !tokenBlacklist.isTokenBlacklisted(jwt)) {
            
        	Users user = (Users) this.usersServ.loadUserByUsername(username);
            if (jwtServ.isTokenValid(jwt, user)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
	
    
    
	
}
