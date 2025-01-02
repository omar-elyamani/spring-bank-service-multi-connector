package ma.formations.multiconnector.dao;

import ma.formations.multiconnector.service.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByAuthority(String authority);
    List<Role> findAll();
}