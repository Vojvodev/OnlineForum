package etf.sni.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import etf.sni.data.model.Categories;
import etf.sni.data.repository.CategoriesRepository;


@Service
public class CategoriesService {
	@Autowired
	private CategoriesRepository categoryRep;
	
	
	public List<Categories> getAllCategories() {
		return categoryRep.findAll();
	}
	
	
	public Categories getCategoryById(int id) {
		if(id > 0) {
			Optional<Categories> category = categoryRep.findById(id);
			if(category.isPresent()) {
				return category.get();
			}
		}
		return null;
	}
	

	public ResponseEntity<Categories> createCategory(Categories category) {
		if(category != null) {
			return categoryRep.save(category) != null ? ResponseEntity.ok().build() : ResponseEntity.noContent().build();
		}
		return ResponseEntity.noContent().build();
	}
	
	
	public ResponseEntity<Categories> deleteCategory(Integer id) {
        if(id > 0 && getCategoryById(id) != null) {
        	categoryRep.deleteById(id);
        	return ResponseEntity.ok().build();
        }
		return ResponseEntity.notFound().build();       
    }


	public ResponseEntity<Categories> updateCategory(int id, Categories newCategory) {
		if(id > 0) {
			Categories oldCategory = getCategoryById(id);
			
			if(oldCategory != null) {
				newCategory.setId(id);
				return categoryRep.save(newCategory) != null ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
			}
		}
		return ResponseEntity.notFound().build();
	}
	
	
	
	
}









