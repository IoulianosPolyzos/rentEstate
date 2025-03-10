package gr.hua.dit.rentEstate.repositories;

import gr.hua.dit.rentEstate.entities.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, Integer> {
    Tenant findByTenantUsername(String tenantUsername);
}

