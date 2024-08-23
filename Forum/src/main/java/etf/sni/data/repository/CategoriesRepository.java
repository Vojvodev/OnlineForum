package etf.sni.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import etf.sni.data.model.Categories;


@Repository
public interface CategoriesRepository extends JpaRepository<Categories, Integer>{

	Categories findByName(String name);

}
