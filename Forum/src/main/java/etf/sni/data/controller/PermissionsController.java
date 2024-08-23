package etf.sni.data.controller;

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
import etf.sni.service.PermissionsService;



@RestController
@RequestMapping("/api/v1/")
@CrossOrigin("https://localhost:4200")
public class PermissionsController {
	@Autowired
	private PermissionsService permissionServ;
	
	
	
	@GetMapping("/allpermissions")					// https://localhost:8080/api/v1/permissions?userId=1&categoryId=1
	public Permissions getUserCategoryPermissions(@RequestParam int userId, @RequestParam int categoryId){
		return permissionServ.getUserCategoryPermissions(userId, categoryId);
	}
	
	@GetMapping("/permissions/id")					// https://localhost:8080/api/v1/permissions/id?id=1
	public Permissions getPermissionById(@RequestParam String id){
		return permissionServ.getPermissionById(Integer.parseInt(id));
	}
	
	
	
	
	@PostMapping("/permissions/add")				// https://localhost:8080/api/v1/permissions/add...
    public ResponseEntity<Permissions> createPermission(@RequestBody Permissions permission) {
        return permissionServ.createPermission(permission);
    }
	
	
	@DeleteMapping("/permissions/delete")			// https://localhost:8080/api/v1/permissions/delete?id=1
	public ResponseEntity<Permissions> deletePermission(@RequestParam int id){
		return permissionServ.deletePermission(id);
	}
	
	
	@PutMapping("/permissions/update")				// https://localhost:8080/api/v1/permissions/update?id=1...
	public ResponseEntity<Permissions> updatePermission(@RequestParam String id, @RequestBody Permissions permission){
		return permissionServ.updatePermission(Integer.parseInt(id), permission);
	}
	
}
