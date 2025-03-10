package gr.hua.dit.rentEstate.service;

import gr.hua.dit.rentEstate.entities.Owner;
import gr.hua.dit.rentEstate.repositories.EstateRepository;
import gr.hua.dit.rentEstate.repositories.OwnerRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class OwnerService {

    private final OwnerRepository ownerRepository; // Repository for Owner entity
    private final EstateRepository estateRepository; // Repository for Estate entity

    // Constructor for dependency
    public OwnerService(OwnerRepository ownerRepository, EstateRepository estateRepository) {
        this.ownerRepository = ownerRepository;
        this.estateRepository = estateRepository;
    }

    //Retrieve a list of all owners.
    @Transactional
    public List<Owner> getOwners() {
        return this.ownerRepository.findAll();
    }

    //Retrieve a specific owner by their ID.
    @Transactional
    public Owner getOwner(Integer ownerId) {
        return this.ownerRepository.findById(ownerId).get();
    }

    //Save or update an Owner entity in the database.
    @Transactional
    public void saveOwner(Owner owner) {
        this.ownerRepository.save(owner); // Save or update the owner in the database
    }

    //Find an owner by their username.
    @Transactional
    public Owner findByUsername(String username) {
        return ownerRepository.findByOwnerUsername(username);
    }
}
