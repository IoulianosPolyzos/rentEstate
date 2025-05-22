package gr.hua.dit.rentEstate.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import gr.hua.dit.rentEstate.entities.Role;
import java.util.Optional;


public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByName(String roleName);

    default Role updateOrInsert(Role role) {
        Role existing_role = findByName(role.getName()).orElse(null);
        if (existing_role != null) {
            return existing_role;
        }
        else {
            return save(role);
        }
    }
}
