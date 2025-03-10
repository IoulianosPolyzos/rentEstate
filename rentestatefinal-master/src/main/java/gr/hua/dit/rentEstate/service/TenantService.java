package gr.hua.dit.rentEstate.service;

import gr.hua.dit.rentEstate.entities.Tenant;
import gr.hua.dit.rentEstate.repositories.TenantRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TenantService {

    private final TenantRepository tenantRepository; // Repository for Tenant entity

    //Constructor for dependency
    public TenantService(TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }

    //Retrieve all tenants from the database.
    @Transactional
    public List<Tenant> getTenants() {
        return tenantRepository.findAll();
    }

    //Retrieve a tenant by their ID.
    @Transactional
    public Tenant getTenant(Integer tenantId) {
        return tenantRepository.findById(tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Tenant not found with ID: " + tenantId));
    }

    //Find a tenant by their username.
    @Transactional
    public Tenant findByUsername(String username) {
        return tenantRepository.findByTenantUsername(username);
    }

    //Save or update a Tenant entity in the database.
    @Transactional
    public void saveTenant(Tenant tenant) {
        tenantRepository.save(tenant); // Save the tenant to the database
    }
}
