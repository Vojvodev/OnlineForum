package etf.sni.data.model;

import java.sql.Timestamp;
import java.time.Instant;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;


@Entity
@Table(name="comments")
public class Comments {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@Column(name="text", nullable=false)
	@Pattern(regexp = "^[a-zA-Z0-9\\p{Punct} ]+$")
	private String text;
	
	@Column(name="status", nullable=false)
	@Enumerated(EnumType.STRING)		// Interpret as a string instead of integer
	private Status status;
	
	
	@Column(name="created_at", nullable=false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Timestamp createdAt;
	
	@PrePersist
    protected void onCreate() {
        createdAt = Timestamp.from(Instant.now());
    }
	
	
	
	// --------- RELATIONSHIPS -----------
	@ManyToOne
	@JoinColumn(name="categories_id")
	private Categories category;
	
	@ManyToOne
	@JoinColumn(name="users_id")
	private Users user;
	// -----------------------------------

	
	
	
	public Comments() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Comments(@Pattern(regexp = "^[a-zA-Z0-9\\p{Punct} ]+$") String text, Status status,
			Categories category, Users user) {
		super();
		this.text = text;
		this.status = status;
		this.category = category;
		this.user = user;
	}

	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
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

	public Categories getCategory() {
		return category;
	}

	public void setCategory(Categories category) {
		this.category = category;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}
	
	
}
