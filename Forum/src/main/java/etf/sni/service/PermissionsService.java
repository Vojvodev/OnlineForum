package etf.sni.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import etf.sni.data.model.Categories;
import etf.sni.data.model.Permissions;
import etf.sni.data.model.Users;
import etf.sni.data.repository.CategoriesRepository;
import etf.sni.data.repository.PermissionsRepository;
import etf.sni.data.repository.UsersRepository;



@Service
public class PermissionsService {
	@Autowired
	private PermissionsRepository permissionRep;
	
	@Autowired
	private CategoriesRepository categoryRep;
	
	@Autowired
	private UsersRepository userRep;
	
	
	
	public Permissions getUserCategoryPermissions(int userId, int categoryId) {
		if(userId > 0 && categoryId > 0) {
			Optional<Users> user = userRep.findById(userId);
			Optional<Categories> category = categoryRep.findById(categoryId);
			if(user.isPresent() && category.isPresent()) {
				return permissionRep.findByUserAndCategory(user.get(), category.get());
			}
		}		
		return null;
	}

	
	public Permissions getPermissionById(int id) {
		if(id > 0) {
			Optional<Permissions> permission = permissionRep.findById(id);
			if(permission.isPresent()) {
				return permission.get();
			}
		}
		return null;
	}
	
	
	public ResponseEntity<Permissions> createPermission(Permissions permission) {
		if(permission != null) {
			return permissionRep.save(permission) != null ? ResponseEntity.ok().build() : ResponseEntity.noContent().build();
		}
		return ResponseEntity.noContent().build();
	}
	
	
	public ResponseEntity<Permissions> deletePermission(Integer id) {
        if(id > 0 && getPermissionById(id) != null) {
        	permissionRep.deleteById(id);
        	return ResponseEntity.ok().build();
        }
		return ResponseEntity.notFound().build();
    }


	public ResponseEntity<Permissions> updatePermission(int id, Permissions newPermission) {
		if(id > 0) {
			Permissions oldPermission = getPermissionById(id);
			
			if(oldPermission != null) {
				newPermission.setId(id);
				return permissionRep.save(newPermission) != null ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
			}
		}
		return ResponseEntity.notFound().build();
	}
	
	
}
