package etf.sni.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import etf.sni.service.UsersService;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Autowired
	private JwtRequestFilter jwtFilter;
	
	@Autowired
	private UsersService userServ;
	
	@Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
	
	@Bean
    DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userServ);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }
	
    @Bean
    public WAFFilter wafFilter() {
        return new WAFFilter();
    }
	
	
	@Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())	// Should be enabled to check the token sent from frontend (Add the token to every request)
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers("/error/**").permitAll()
                        // TODO: IZBRISATI HEHE
                        .requestMatchers(HttpMethod.GET, "/api/v1/comments/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/users/searchByUsername").permitAll()
                        .requestMatchers("/api/v1/email/send").permitAll()
                        
                        .requestMatchers("/api/v1/users/**").hasAuthority("ADMIN")
                        .requestMatchers("/api/v1/allusers/**").hasAuthority("ADMIN") // hasAuthority DOESNT ASSUME ROLES IN THE FORM: ROLE_ADMIN - hasRole() DOES ASSUME
                        .requestMatchers("/api/v1/permissions/**").hasAuthority("ADMIN")
                        .requestMatchers("/api/v1/allpermissions").hasAuthority("ADMIN")
                        
                        .requestMatchers("/api/v1/comments/add").hasAnyAuthority("USER","MODERATOR","ADMIN")
                        
                        .requestMatchers("/api/v1/comments/delete").hasAnyAuthority("ADMIN","MODERATOR")
                        .requestMatchers("/api/v1/comments/update/**").hasAnyAuthority("ADMIN","MODERATOR")
                        
                        .anyRequest().authenticated()
                )
                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(daoAuthenticationProvider())
                .addFilterBefore(wafFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
	
	
}
