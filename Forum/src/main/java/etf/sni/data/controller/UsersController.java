package etf.sni.data.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import etf.sni.data.model.Permissions;
import etf.sni.data.model.Users;
import etf.sni.service.UsersService;


@RestController
@RequestMapping("/api/v1/")
@CrossOrigin("https://localhost:4200")
public class UsersController {
	@Autowired
	private UsersService userServ;
	
	
	
	
	@GetMapping("/allusers")						// https://localhost:8080/api/v1/allusers
	public List<Users> getAllUsers(){
		return userServ.getAllusers();
	}
	

	@GetMapping("/users/blocked")					// https://localhost:8080/api/v1/users/search?username=DARIJOTEST&password=DARIJOTESTPASS
	public List<Users> getUsersBlocked(){
		return userServ.getUsersBlocked();
	}
	
	
	@GetMapping("/users/search")					// https://localhost:8080/api/v1/users/search?username=DARIJOTEST&password=DARIJOTESTPASS
	public Users getUsersByUsernameAndPassword(@RequestParam String username, @RequestParam String password){
		return userServ.getUsersByUsernameAndPassword(username, password);
	}

	@GetMapping("/users/searchByUsername")				// https://localhost:8080/api/v1/users/searchByUsername?username=...
	public Users getUsersByUsername(@RequestParam String username){
		return (Users) userServ.loadUserByUsername(username);
	}
	
	
	@GetMapping("/users/searchById")				// https://localhost:8080/api/v1/users/searchById?id=1
	public Users getUsersById(@RequestParam int id){
		return userServ.getUsersById(id);
	}
	
	
	
	
	@PostMapping("/users/add")						// https://localhost:8080/api/v1/users/add...
    public ResponseEntity<Users> createUser(@RequestBody Users user) {
        return userServ.createUser(user);
    }
	
	
	@DeleteMapping("/users/delete")					// https://localhost:8080/api/v1/users/delete?id=1
	public ResponseEntity<Users> deleteUser(@RequestParam int id){
		return userServ.deleteUser(id);
	}
	
	
	@PutMapping("/users/update")					// https://localhost:8080/api/v1/users/update?id=1...
	public ResponseEntity<Users> updateUser(@RequestParam int id, @RequestBody Users user){
		return userServ.updateUser(id, user);
	}
	
	@PutMapping("/users/update/role")					// https://localhost:8080/api/v1/users/update/role?id=1&role='admin'...
	public Users updateUserRole(@RequestParam String id, @RequestParam String role){
		return userServ.updateUserRole(Integer.parseInt(id), role);
	}
	
	@PutMapping("/users/update/permission")					// https://localhost:8080/api/v1/users/update/permission?categoryId=1&userId=1...
	public Permissions updateUserPermission(@RequestParam String categoryId, @RequestParam String userId, @RequestParam String type){
		return userServ.updateUserPermission(Integer.parseInt(categoryId), Integer.parseInt(userId), type);
	}
	
	@PutMapping("/users/update/status")					// https://localhost:8080/api/v1/users/update/status?userId=1
	public Users updateUserStatus(@RequestParam String userId){
		return userServ.updateUserStatus(Integer.parseInt(userId));
	}
	
	
	
}









