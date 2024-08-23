
package etf.sni.config;

import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.regex.Pattern;

import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;




public class WAFFilter extends OncePerRequestFilter{

	// Long reg exps are time consuming
	private static final Pattern SQL_INJECTION_PATTERN  = Pattern.compile("(?i)SELECT|INSERT|UPDATE|DELETE|DROP|UNION", Pattern.CASE_INSENSITIVE);
    private static final Pattern XSS_PATTERN 			= Pattern.compile("<.*?(script|img|svg|iframe|object|embed|link|style|base|form|input|button|a|href|on\\w+).*?>|(?i)(javascript:|vbscript:|data:|expression\\(|alert\\(.*?\\)|eval\\(.*?\\))", Pattern.CASE_INSENSITIVE);
	private static final int 	 BUFFER_OVERFLOW_LIMIT  = 1_000;
	
	// Should also add rate limiting (DOS) 
	
	
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
    		throws ServletException, IOException {
    	
    	// Allows the filter to read the request body and then pass down the untouched request for further filtering (otherwise it would have been corrupted)
    	CachedBodyHttpServletRequest cachedBodyRequest = new CachedBodyHttpServletRequest(request);		
    	
    	
    	// Check parameters
        for (String paramValue : request.getParameterMap().values().stream().flatMap(Arrays::stream).toList()) {       
        	if ( isMalicious(paramValue) ) {    	
            	System.out.println("Malicious input detected in parameters!");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Malicious input detected in parameters!");
                return; // Block the request
            }
        }
        
        
        // Check headers
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            
            if ( isMalicious(headerValue) ) {
            	System.out.println("Malicious input detected in header!");
            	response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Malicious input detected in header!");
                return;
            }
        }
        
        
        // Check request body
        String requestBody = cachedBodyRequest.getRequestBody();
        
        if ( isMalicious(requestBody) ) {
        	System.out.println("Malicious input detected in request body!");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Malicious input detected in request body!");
            return;
        }

        
        // Check cookies
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
            	
            	if ( isMalicious(cookie.getValue()) ) {
                	System.out.println("Malicious input detected in cookies!");
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Malicious input detected in cookies!");
                    return;
                }
            	
            }
        }
        
        
        
        // Proceed with the request if no malicious patterns are found
        filterChain.doFilter(cachedBodyRequest, response);
        
    }
    
    
    
    private boolean isMalicious(String req) {
    	return SQL_INJECTION_PATTERN.matcher(req).find() || 
        		XSS_PATTERN.matcher(req).find() ||
        		BUFFER_OVERFLOW_LIMIT <= req.length(); 
    }
    
}
