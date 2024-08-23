package etf.sni.data.controller;

import java.util.List;

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

import etf.sni.data.model.Categories;
import etf.sni.service.CategoriesService;



@RestController
@RequestMapping("/api/v1/")
@CrossOrigin("https://localhost:4200")
public class CategoriesController {
	@Autowired
	private CategoriesService categoryServ;
	
	
	
	@GetMapping("/allcategories")					// https://localhost:8080/api/v1/allcategories
	public List<Categories> getAllCategories(){
		return categoryServ.getAllCategories();
	}
	
	
	@PostMapping("/categories/add")					// https://localhost:8080/api/v1/categories/add...
    public ResponseEntity<Categories> createCategory(@RequestBody Categories category) {
        return categoryServ.createCategory(category);
    }
	
	
	@DeleteMapping("/categories/delete")			// https://localhost:8080/api/v1/categories/delete?id=1
	public ResponseEntity<Categories> deleteCategory(@RequestParam int id){
		return categoryServ.deleteCategory(id);
	}
	
	
	@PutMapping("/categories/update")				// https://localhost:8080/api/v1/categories/update?id=1...
	public ResponseEntity<Categories> updateCategory(@RequestParam int id, @RequestBody Categories category){
		return categoryServ.updateCategory(id, category);
	}
	
	
}








