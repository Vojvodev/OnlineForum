package etf.sni.data.model;

public class SubmittedComment {
	private String commentText;
	private String category;
	private String currentUsername;
	
	
	public SubmittedComment(String commentText, String category, String currentUsername) {
		super();
		this.commentText = commentText;
		this.category = category;
		this.currentUsername = currentUsername;
	}
	public SubmittedComment() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public String getCommentText() {
		return commentText;
	}
	public void setCommentText(String commentText) {
		this.commentText = commentText;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getCurrentUsername() {
		return currentUsername;
	}
	public void setCurrentUsername(String currentUsername) {
		this.currentUsername = currentUsername;
	}
	
	
}
