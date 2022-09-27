package home.sweethome.fussdb.repository;

import home.sweethome.fussdb.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
