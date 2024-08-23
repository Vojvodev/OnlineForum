package etf.sni.data.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;



@Entity
@Table(name="permissions")
public class Permissions {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name="add_permission", nullable=false)
	private boolean add = true;
		
	@Column(name="edit_permission", nullable=false)
	private boolean edit = true;
	
	@Column(name="delete_permission", nullable=false)
	private boolean delete = true;
 
	
	
	// --------- RELATIONSHIPS -----------
	@ManyToOne
	@JoinColumn(name="categories_id")
	private Categories category;
	
	@ManyToOne
	@JoinColumn(name="users_id")
	@JsonIgnore
	private Users user;
	// -----------------------------------
	
	

	public Permissions() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Permissions(boolean add, boolean edit, boolean delete) {
		super();
		this.add = add;
		this.edit = edit;
		this.delete = delete;
	}

	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isAdd() {
		return add;
	}

	public void setAdd(boolean add) {
		this.add = add;
	}

	public boolean isEdit() {
		return edit;
	}

	public void setEdit(boolean edit) {
		this.edit = edit;
	}

	public boolean isDelete() {
		return delete;
	}

	public void setDelete(boolean delete) {
		this.delete = delete;
	}
	
	
	
	
	
}
