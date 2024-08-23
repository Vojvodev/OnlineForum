package etf.sni.data.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import etf.sni.data.model.Status;
import etf.sni.data.model.Users;


@Repository
public interface UsersRepository extends JpaRepository<Users, Integer>{

	Users findByUsernameAndPassword(String username, String password);
	Optional<Users> findByUsername(String username);
	Optional<Users> findByEmail(String email);
	List<Users> findByStatus(Status status);
}
