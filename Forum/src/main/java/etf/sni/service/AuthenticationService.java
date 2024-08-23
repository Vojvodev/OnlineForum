package etf.sni.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import etf.sni.authentication.AuthRequest;
import etf.sni.authentication.JwtAuthResponse;
import etf.sni.authentication.SignupData;
import etf.sni.config.JwtService;
import etf.sni.config.TokenBlacklist;
import etf.sni.data.model.Users;
import etf.sni.data.repository.UsersRepository;


@Service
public class AuthenticationService {
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	AuthenticationManager authenticationMan;
	
	@Autowired
	UsersRepository userRep;
	
	@Autowired
	TokenBlacklist tokenBlacklist;
	
	@Autowired
	JwtService jwtService;
	
	
	
	public ResponseEntity<?> login(AuthRequest request) {
		// TODO: VALIDATE DATA
		try {
			authenticationMan.authenticate( new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
			
			Optional<Users> optUser = userRep.findByUsername(request.getUsername());
			if(optUser.isPresent()) {
				Users user = optUser.get();
				
				if (user.getStatus().toString().equals("ACTIVE")) {
					return ResponseEntity.status(HttpStatus.OK).body("Successfully logged in.");
			    } else {
			        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Account not activated!");	
			    }
				
			}
			else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password!");
			}
		} catch (AuthenticationException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password!");
		} catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred!");
        }
		
	}
	
	public JwtAuthResponse generateJWT(int id) {
		Optional<Users> optUser = userRep.findById(id);
		if(optUser.isPresent()) {
			Users user = optUser.get();
			var jwt = jwtService.generateToken(user);
			return new JwtAuthResponse(jwt);
		}
		else {
			return null;
		}
	}
	
	
	

	public ResponseEntity<?> signup(SignupData user) {
		// TODO: VALIDATE DATA
		
		Users newUser = new Users();
        newUser.setEmail(user.getEmail());
        newUser.setUsername(user.getUsername());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        
        if(userRep.save(newUser) != null) {
        	return ResponseEntity.status(HttpStatus.OK).body("Successfully signed in.");
        }
        else return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not sign you in!");
	}


	public void logout(String token) {
        tokenBlacklist.blacklistToken(token);
    }




	public ResponseEntity<?> githubLogin(String code){
		String CLIENT_ID = "Ov23liYFRQ9jiXfEM3a5";
	    String CLIENT_SECRET = "d2c8848aa167a0283c1ec3c889ed45402abdab0a";
	    String REDIRECT_URI = "https://localhost:4200/login/oauth2/code/github";
	    String TOKEN_URL = "https://github.com/login/oauth/access_token";
	     
	    
	    RestTemplate restTemplate = new RestTemplate();

        String url = TOKEN_URL + "?client_id=" + CLIENT_ID + "&client_secret=" + CLIENT_SECRET + "&code=" + code + "&redirect_uri=" + REDIRECT_URI;

        ResponseEntity<String> response = restTemplate.postForEntity(url, null, String.class);
        
        String accessToken = extractAccessToken(response.getBody());
        String email = getUserProfile(accessToken);
        
        Optional<Users> optUser = userRep.findByEmail(email);
        if(optUser.isPresent()) {
        	Users user = optUser.get();
        	if (user.getStatus().toString().equals("ACTIVE")) {
				return ResponseEntity.status(HttpStatus.OK).body(user.getUsername());
		    } else {
		        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Account not activated!");	
		    }
			
		}
		else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Could not log in!");
		}
        
	}

	
	
	private String extractAccessToken(String responseBody) {
        String prefix = "access_token=";
        String suffix = "&scope";
        int startIndex = responseBody.indexOf(prefix);
        int endIndex = responseBody.indexOf(suffix);
        if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
            return responseBody.substring(startIndex + prefix.length(), endIndex);
        }
        return null; 
    }

	private String getUserProfile(String accessToken) {
	    RestTemplate restTemplate = new RestTemplate();
	    HttpHeaders headers = new HttpHeaders();
	    headers.add("Authorization", "Bearer " + accessToken);
	    HttpEntity<String> entity = new HttpEntity<>(headers);

	    // Get the primary email of the user
	    String emailsUrl = "https://api.github.com/user/emails";
	    ResponseEntity<String> emailResponse = restTemplate.exchange(emailsUrl, HttpMethod.GET, entity, String.class);
	    
	    // Extract email from the response
	    return extractPrimaryEmail(emailResponse.getBody());
	}

	
	private String extractPrimaryEmail(String emailsJson) {
	    Gson gson = new Gson();
	    JsonArray emailsArray = gson.fromJson(emailsJson, JsonArray.class);

	    for (JsonElement emailElement : emailsArray) {
	        JsonObject emailObject = emailElement.getAsJsonObject();
	        if (emailObject.get("primary").getAsBoolean()) {
	            return emailObject.get("email").getAsString();
	        }
	    }
	    return null;
	}
    
    
    private String extractUsername(String userDetails) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(userDetails, JsonObject.class);
        if (jsonObject.has("login")) {
            return jsonObject.get("login").getAsString();
        }
        return null; // Returns null if the login field is not present
    }
	
	
	
}









