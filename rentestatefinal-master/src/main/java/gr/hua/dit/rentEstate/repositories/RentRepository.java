package gr.hua.dit.rentEstate.repositories;

import gr.hua.dit.rentEstate.entities.Rent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;



@Repository
public interface RentRepository extends JpaRepository<Rent, Long> {


    // Custom query to find Rent entities associated with a specific owner and estate
    @Query("SELECT a FROM Rent a WHERE a.estate.owner.id = :ownerId AND a.estate.id = :estateId")
    List<Rent> findRentsByOwnerIdAndEstateId(
            @Param("ownerId") Long ownerId,  // The ID of the owner
            @Param("estateId") Long estateId // The ID of the estate
    );


    List<Rent> findByTenantUsername(String tenantUsername);
}


