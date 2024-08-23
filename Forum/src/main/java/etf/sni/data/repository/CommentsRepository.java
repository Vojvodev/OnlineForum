package etf.sni.data.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import etf.sni.data.model.Categories;
import etf.sni.data.model.Comments;
import etf.sni.data.model.Users;


@Repository
public interface CommentsRepository extends JpaRepository<Comments, Integer> {

	List<Comments> findByCategory(Categories categories);

	List<Comments> findByUser(Users users);

	Page<Comments> findByCategory(Categories category, Pageable pageable);
	
}
