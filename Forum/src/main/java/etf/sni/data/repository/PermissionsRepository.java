package etf.sni.data.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import etf.sni.data.model.Categories;
import etf.sni.data.model.Permissions;
import etf.sni.data.model.Users;


@Repository
public interface PermissionsRepository extends JpaRepository<Permissions, Integer>{

	Permissions findByUserAndCategory(Users users, Categories categories);

	@Query(value = "SELECT * FROM permissions p WHERE p.categories_id = :categoryId AND p.users_id = :userId", nativeQuery = true)
	Optional<Permissions> findByCategoryAndUser(@Param("categoryId") int categoryId, @Param("userId") int userId);

}
