package etf.sni.data.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

import etf.sni.data.model.Comments;
import etf.sni.data.model.SubmittedComment;
import etf.sni.service.CommentsService;


@RestController
@RequestMapping("/api/v1/")
@CrossOrigin("https://localhost:4200")
public class CommentsController {
	@Autowired
	private CommentsService commentServ;
	
	
	
	@GetMapping("/comments/get")
	public Comments getComentById(@RequestParam int id) {
		return commentServ.getCommentById(id);
	}
	
	
	@GetMapping("/comments/paginated/category")			// https://localhost:8080/api/v1/comments/paginated/category?categoryId=1&page=1&size=10
	public Page<Comments> getPaginatedCategoryComments(@RequestParam int categoryId, 
														@RequestParam(defaultValue = "0") int page, 
														@RequestParam(defaultValue = "10") int size){
		return commentServ.getPaginatedCategoryComments(categoryId, page, size);
	}
	
	
	@GetMapping("/comments/category")			// https://localhost:8080/api/v1/comments/category?categoryId=1
	public List<Comments> getAllCategoryComments(@RequestParam int categoryId){
		return commentServ.getAllCategoryComments(categoryId);
	}
	
	
	@GetMapping("/comments/user")				// https://localhost:8080/api/v1/comments/user?userId=1
	public List<Comments> getAllUserComments(@RequestParam int userId){
		return commentServ.getAllUserComments(userId);
	}
	
	
	
	@PostMapping("/comments/add")				// https://localhost:8080/api/v1/comments/add...
    public ResponseEntity<String> createComment(@RequestBody SubmittedComment subComment) {
        return commentServ.createComment(subComment);
    }
	
	
	@DeleteMapping("/comments/delete")			// https://localhost:8080/api/v1/comments/delete?id=1
	public ResponseEntity<String> deleteComment(@RequestParam String id){
		return commentServ.deleteComment(Integer.parseInt(id));
	}
	
	
	@PutMapping("/comments/update")				// https://localhost:8080/api/v1/comments/update?id=1...
	public ResponseEntity<Comments> updateComment(@RequestParam String id, @RequestBody Comments comment){
		return commentServ.updateComment(Integer.parseInt(id), comment);
	}
	
	@PutMapping("/comments/update/text")				// https://localhost:8080/api/v1/comments/update/text?id=1...
	public ResponseEntity<String> updateCommentText(@RequestParam String id, @RequestBody String text){
		return commentServ.updateCommentText(Integer.parseInt(id), text);
	}
	
	
}








