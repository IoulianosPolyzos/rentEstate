package gr.hua.dit.rentEstate.repositories;
import gr.hua.dit.rentEstate.entities.Estate;
import gr.hua.dit.rentEstate.entities.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EstateRepository extends JpaRepository<Estate, Integer>, JpaSpecificationExecutor<Estate> {
    List<Estate> findByOwner(Owner owner);
    List<Estate> findByownerName(String username);
}
