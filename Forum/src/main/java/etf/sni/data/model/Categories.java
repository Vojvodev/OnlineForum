package etf.sni.data.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;


@Entity
@Table(name="categories")
public class Categories {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@Column(name="name", nullable=false)
	@Pattern(regexp = "^[a-zA-Z0-9]+$")
	private String name;

	
	// --------- RELATIONSHIPS --------------
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "category", cascade = CascadeType.ALL)
	@JsonIgnore												// Prevents infinite recursion when casting to JSON
    private List<Comments> commentsList;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "category", cascade = CascadeType.ALL)
	@JsonIgnore												// Prevents infinite recursion when casting to JSON
    private List<Permissions> permissionsList;
	
	// ----------------------------------------
	
	
	public Categories() {
		super();
	}

	public Categories(@Pattern(regexp = "^[a-zA-Z0-9]+$") String name) {
		super();
		this.name = name;
	}

	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
