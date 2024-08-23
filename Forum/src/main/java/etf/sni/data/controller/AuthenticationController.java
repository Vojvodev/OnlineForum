package etf.sni.data.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import etf.sni.authentication.AuthRequest;
import etf.sni.authentication.SignupData;
import etf.sni.authentication.TokenDTO;
import etf.sni.service.AuthenticationService;





@RestController
@CrossOrigin(origins = "https://localhost:4200")
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
	@Autowired
	private AuthenticationService authenticationServ;
	
	
	
	@PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
		return authenticationServ.login(request);
	}
	
	
	@PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody TokenDTO tokenDto) {
		String token = tokenDto.getToken();
		authenticationServ.logout(token);
        return ResponseEntity.status(HttpStatus.OK).body("Logged out successfully");
	}
	
	@PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupData user) {
		return authenticationServ.signup(user);
	}
	
	
	@PostMapping("/generate-jwt")
    public ResponseEntity<?> generateJWT(@RequestParam int id) {
		return ResponseEntity.ok(authenticationServ.generateJWT(id));
    }
	
	
    @GetMapping("/github-token-endpoint")
    public ResponseEntity<?> githubLogin(@RequestParam String code) {
        return authenticationServ.githubLogin(code);
    }
	
}







