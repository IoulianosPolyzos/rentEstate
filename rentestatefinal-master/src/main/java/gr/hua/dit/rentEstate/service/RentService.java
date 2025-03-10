package gr.hua.dit.rentEstate.service;

import gr.hua.dit.rentEstate.entities.Rent;
import gr.hua.dit.rentEstate.repositories.RentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RentService {

    private final RentRepository rentRepository;

    // Constructor for dependency
    public RentService(RentRepository rentRepository) {
        this.rentRepository = rentRepository;
    }

    // Create a new rent request with a default status of "PENDING"
    public void createRent(Rent rent) {
        rent.setStatus("PENDING"); // Default status for a new request
        rentRepository.save(rent); // Save the rent object to the database
    }

    // Approve a rent request by setting status to "APPROVED"
    public void approveRent(Long rentId) {
        Rent rent = rentRepository.findById(rentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid rent ID"));

        rent.setStatus("APPROVED"); // Update the status
        rentRepository.save(rent);  // Save changes to the database
    }

    // Reject a rent request by setting status to "REJECTED"
    public void rejectRent(Long rentId) {
        Rent rent = rentRepository.findById(rentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid rent ID"));

        rent.setStatus("REJECTED"); // Update the status
        rentRepository.save(rent);  // Save changes to the database
    }

    // Get a list of rents for a specific owner and estate
    @Transactional
    public List<Rent> getRentsByOwnerId(Long ownerId, Long estateId) {
        return rentRepository.findRentsByOwnerIdAndEstateId(ownerId, estateId); // Custom query
    }

    public Rent getRentById(Long rentId) {
        return rentRepository.findById(rentId)
                .orElseThrow(() -> new IllegalArgumentException("Rent not found with ID: " + rentId));
    }

    // Get all rent requests made by a specific tenant
    public List<Rent> getRentsByTenantUsername(String tenantUsername) {
        return rentRepository.findByTenantUsername(tenantUsername);
    }

    // Get all rent records in the database
    @Transactional
    public List<Rent> getUsers() {
        return rentRepository.findAll();
    }

    @Transactional
    public void updateRentTenantUsername(String oldUsername, String newUsername) {

        List<Rent> rents = rentRepository.findByTenantUsername(oldUsername);
        for (Rent rent : rents) {
            rent.setTenantUsername(newUsername);
            rentRepository.save(rent);
        }
    }
}
