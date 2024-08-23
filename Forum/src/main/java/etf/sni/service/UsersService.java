package etf.sni.service;

import java.util.List;
import java.util.Optional;

import javax.security.auth.login.AccountNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import etf.sni.data.model.Permissions;
import etf.sni.data.model.Role;
import etf.sni.data.model.Status;
import etf.sni.data.model.Users;
import etf.sni.data.repository.PermissionsRepository;
import etf.sni.data.repository.UsersRepository;



@Service
public class UsersService implements UserDetailsService {
	@Autowired
	private UsersRepository	userRep;
	
	@Autowired
	private PermissionsRepository permissionRep;
	
	
	
	public List<Users> getAllusers() {
		return userRep.findAll();
	}
	
	public Users getUsersById(int id) {
		if(id > 0) {
			Optional<Users> user = userRep.findById(id);
			if(user.isPresent()) {
				return user.get();
			}
		}
		return null;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO VALIDATE INPUT
		return userRep.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Invalid username!"));
	}
	
	public UserDetails loadUserByEmail(String email) throws AccountNotFoundException{
        return userRep.findByEmail(email).orElseThrow(() -> new AccountNotFoundException("Invalid email!"));
    }
	
	public Users getUsersByUsernameAndPassword(String username, String password) {
		// TODO VALIDATE INPUT
		return userRep.findByUsernameAndPassword(username, password);
	}
	
	
	

	public ResponseEntity<Users> createUser(Users user) {
		if(user != null) {
			return userRep.save(user) != null ? ResponseEntity.ok().build() : ResponseEntity.noContent().build();
		}
		return ResponseEntity.noContent().build();
	}
	
	
	
	public ResponseEntity<Users> deleteUser(int id) {
		if(id > 0 && getUsersById(id) != null) {
			userRep.deleteById(id);
			return ResponseEntity.ok().build();
        }
		return ResponseEntity.notFound().build(); 
    }


	
	public ResponseEntity<Users> updateUser(int id, Users newUser) {
		if(id > 0) {
			Users oldUser = getUsersById(id);
			
			if(oldUser != null) {
				newUser.setId(id);
				return userRep.save(newUser) != null ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
			}
		}
		return ResponseEntity.notFound().build();
	}

	
	
	public List<Users> getUsersBlocked() {
		return userRep.findByStatus(Status.BLOCKED);
	}

	
	public Users updateUserRole(int id, String roleStr) {
		Optional<Users> optionalUser = userRep.findById(id);
        if (optionalUser.isPresent()) {
            Users user = optionalUser.get();
            
            switch (roleStr) {
				case "ADMIN": {
					user.setRole(Role.ADMIN);
		            return userRep.save(user);
				}
				case "MODERATOR": {
					user.setRole(Role.MODERATOR);
		            return userRep.save(user);
				}
				case "USER": {
					user.setRole(Role.USER);
		            return userRep.save(user);
				}
            }
        }
        return null;
    }

	
	
	
	
	
	public Permissions updateUserPermission(int categoriesId, int usersId, String type) {
		Optional<Permissions> optionalPermission = permissionRep.findByCategoryAndUser(categoriesId, usersId);
        if (optionalPermission.isPresent()) {
            Permissions permission = optionalPermission.get();
            
            switch (type) {
				case "add": {
					if(permission.isAdd()) { permission.setAdd(false); return permissionRep.save(permission);}
					else { permission.setAdd(true); return permissionRep.save(permission);}
				}
				case "edit": {
					if(permission.isEdit()) { permission.setEdit(false); return permissionRep.save(permission);}
					else { permission.setEdit(true); return permissionRep.save(permission);}
				}
				case "delete": {
					if(permission.isDelete()) { permission.setDelete(false); return permissionRep.save(permission);}
					else { permission.setDelete(true); return permissionRep.save(permission);}
				}
            }
                     
        }
        return null;
	}

	
	public Users updateUserStatus(int userId) {
		Optional<Users> optionalUser = userRep.findById(userId);
        if (optionalUser.isPresent()) {
            Users user = optionalUser.get();
            
            if(user.getStatus().toString().equals(Status.ACTIVE.toString())) {
            	user.setStatus(Status.BLOCKED);
            	return userRep.save(user);
            }
            else if(user.getStatus().toString().equals(Status.BLOCKED.toString())){
            	user.setStatus(Status.ACTIVE);
            	return userRep.save(user);
            }
            
        }
        return null;
    }
	
	
	
}














