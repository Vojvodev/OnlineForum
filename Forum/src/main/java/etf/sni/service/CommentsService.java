package etf.sni.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import etf.sni.data.model.Categories;
import etf.sni.data.model.Comments;
import etf.sni.data.model.Status;
import etf.sni.data.model.SubmittedComment;
import etf.sni.data.model.Users;
import etf.sni.data.repository.CategoriesRepository;
import etf.sni.data.repository.CommentsRepository;
import etf.sni.data.repository.UsersRepository;



@Service
public class CommentsService {
	@Autowired
	private CommentsRepository commentRep;
	
	@Autowired
	private UsersRepository userRep;
	
	@Autowired
	private CategoriesRepository categoryRep;
	
	
	public Page<Comments> getPaginatedCategoryComments(int categoryId, int page, int size){
		if(categoryId > 0) {
			Optional<Categories> category = categoryRep.findById(categoryId);			
            if (category.isPresent()) {
            	return commentRep.findByCategory(category.get(), PageRequest.of(page, size));
            }
		}
		return null;
	}
	
	
	public List<Comments> getAllCategoryComments(int categoryId) {
		if(categoryId > 0) {
            Optional<Categories> category = categoryRep.findById(categoryId);			
            if (category.isPresent()) {
                return commentRep.findByCategory(category.get());
            }
		}
		return new ArrayList<Comments>();
	}


	public List<Comments> getAllUserComments(int userId) {
		if(userId > 0) {
			Optional<Users> user = userRep.findById(userId);
			if(user.isPresent()) {
				return commentRep.findByUser(user.get());
			}
		}
		return new ArrayList<Comments>();
	}
	
	
	public ResponseEntity<String> createComment(SubmittedComment subComment) {
		//TODO: VALIDATE INPUT
		
		Categories cat = categoryRep.findByName(subComment.getCategory());
		Optional<Users> optUsr = userRep.findByUsername(subComment.getCurrentUsername());
		Users usr;
		if(optUsr.isPresent()) {
			usr = optUsr.get();
		}
		else return ResponseEntity.status(HttpStatus.NO_CONTENT).body("You are not signed in!");
		
		Comments comment = new Comments(subComment.getCommentText(), Status.ACTIVE, cat, usr);
		
		return commentRep.save(comment) != null ? ResponseEntity.status(HttpStatus.OK).body("Comment saved.") : 
			ResponseEntity.status(HttpStatus.NO_CONTENT).body("Could not save comment!");
	}
	
	
	public Comments getCommentById(int id) {
		if(id > 0) {
			Optional<Comments> comment = commentRep.findById(id);
			if(comment.isPresent()) {
				return comment.get();
			}
		}
		return null;
	}
	
	
	public ResponseEntity<String> deleteComment(Integer id) {
        if(id > 0 && getCommentById(id) != null) {
        	commentRep.deleteById(id);
        	return ResponseEntity.status(HttpStatus.OK).body("Comment deleted successfully.");
        }
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Could not find comment!");
    }


	public ResponseEntity<Comments> updateComment(int id, Comments newComment) {
		// TODO: VALIDATE DATA
		if(id > 0) {
			Comments oldComment = getCommentById(id);
			
			if(oldComment != null) {
				newComment.setId(id);
				return commentRep.save(newComment) != null ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
			}
		}
		return ResponseEntity.notFound().build();
	}


	public ResponseEntity<String> updateCommentText(int id, String text) {
		// TODO: VALIDATE DATA
		Optional<Comments> optionalComment = commentRep.findById(id);
        if (optionalComment.isPresent()) {
            Comments comment = optionalComment.get();
            comment.setText(text);
            return commentRep.save(comment) != null ? ResponseEntity.status(HttpStatus.OK).body("Comment saved.") : 
    			ResponseEntity.status(HttpStatus.NO_CONTENT).body("Could not save comment!");
        } else {
        	return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comment not foud!");
        }
	}
	
	
}









