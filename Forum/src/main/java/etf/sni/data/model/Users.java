package etf.sni.data.model;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;


@Entity
@Table(name="users")
public class Users implements UserDetails{
	private static final long serialVersionUID = 1L;

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name="username", unique=true, nullable=false)
	@Pattern(regexp = "^[a-zA-Z0-9 ]+$")
	private String username;
	
	@Column(name="email", nullable=false)
	@Email
	private String email;
	
	@Column(name="password", nullable=false)
	private String password;
	
	@Column(name="role", nullable=false)
	@Enumerated(EnumType.STRING)		// To view them as strings instead of integers
	private Role role = Role.USER;
	
	@Column(name="status", nullable=false)
	@Enumerated(EnumType.STRING)
	private Status status = Status.BLOCKED;
	
	
	@Column(name="created_at", nullable=false, columnDefinition = "TIMESTAMP DEFAULT NOW()")
	private Timestamp createdAt;
	
	@PrePersist
    protected void onCreate() {
        createdAt = Timestamp.from(Instant.now());
    }
	
	
	// -------- RELATIONSHIPS ---------
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
	@JsonIgnore
    private List<Comments> commentsList;
	
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)												
    private List<Permissions> permissionsList;
	
	//----------------------------------

	
	
	
	public Users(@Pattern(regexp = "^[a-zA-Z0-9 ]+$") String username, @Email String email, String password, Role role,
			Status status, Timestamp createdAt) {
		super();
		this.username = username;
		this.email = email;
		this.password = password;
		this.role = role;
		this.status = status;
		this.createdAt = createdAt;
	}


	public Users() {
		super();
		// TODO Auto-generated constructor stub
	}

	

	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public Role getRole() {
		return role;
	}


	public void setRole(Role role) {
		this.role = role;
	}


	public Status getStatus() {
		return status;
	}


	public void setStatus(Status status) {
		this.status = status;
	}


	public Timestamp getCreatedAt() {
		return createdAt;
	}


	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}


	public List<Comments> getCommentsList() {
		return commentsList;
	}


	public void setCommentsList(List<Comments> commentsList) {
		this.commentsList = commentsList;
	}


	public List<Permissions> getPermissionsList() {
		return permissionsList;
	}


	public void setPermissionsList(List<Permissions> permissionsList) {
		this.permissionsList = permissionsList;
	}


	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority(role.toString()));	
		// IMPORTANT: SPRING BY DEFAULT EXPECTS ROLES TO HAVE A PREFIX ROLE_ OR JUST USE hasAuthority()
	}
	
	
}
