package etf.sni.config;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import etf.sni.data.model.Users;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	// @Value("${token.signing.key}")
    // private String jwtSigningKey;
	
	private String jwtSigningKey = "NDEzRjQ0Mjg0NzJCNEI2MjUwNjU1MzY4NTY2RDU5NzAzMzczMzY3NjM5NzkyNDQyMjY0NTI5NDg0MDRENjM1MQ==";
	// private final Key jwtSigningKey = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);
	// private String jwtSigningKey = env.getProperty("token.signing.key");
	
	@Autowired
    private TokenBlacklist tokenBlacklist;
	
	
	public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        return  tokenBlacklist == null || (
	        		!tokenBlacklist.isTokenBlacklisted(token)
	                && extractUsername(token).equals(userDetails.getUsername())
	                && !isTokenExpired(token));
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        if(userDetails instanceof Users){
        	Users user = (Users) userDetails;
            extraClaims.put("role", user.getRole().toString());
            extraClaims.put("id", user.getId());
        }
        return Jwts.builder().setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                //.signWith(jwtSigningKey).compact();
                .signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSigningKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    
    
}
