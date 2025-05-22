package gr.hua.dit.rentEstate.repositories;


import gr.hua.dit.rentEstate.entities.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository

public interface OwnerRepository extends JpaRepository<Owner, Integer> {

    Owner findByOwnerUsername(String ownerUsername);

}
