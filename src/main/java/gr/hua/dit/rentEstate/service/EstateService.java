package gr.hua.dit.rentEstate.service;

import java.util.List;
import gr.hua.dit.rentEstate.entities.Estate;
import gr.hua.dit.rentEstate.entities.Owner;
import gr.hua.dit.rentEstate.repositories.EstateRepository;
import gr.hua.dit.rentEstate.repositories.OwnerRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class EstateService {

    private final EstateRepository estateRepository;
    private final OwnerRepository ownerRepository;

    // Constructor for dependency
    public EstateService(EstateRepository estateRepository,OwnerRepository ownerRepository) {
        this.estateRepository = estateRepository;
        this.ownerRepository = ownerRepository;
    }

    // Retrieve all estates from the database
    @Transactional
    public List<Estate> getEstates() {
        return estateRepository.findAll(); // Returns all estate entities
    }

    // Save or update an estate entity in the database
    @Transactional
    public void saveEstate(Estate estate) {
        estateRepository.save(estate);
    }

    // Retrieve a specific estate by ID
    @Transactional
    public Estate getEstate(Integer estateId) {
        return estateRepository.findById(estateId).get();
    }

    // Delete an estate by ID
    @Transactional
    public void deleteEstate(Integer id) {
        estateRepository.deleteById(id);
    }

    // Find all estates owned by a specific owner
    public List<Estate> findByOwner(Owner owner) {
        return estateRepository.findByOwner(owner);
    }

    // Search estates
    public List<Estate> searchEstates(String cityName, String areaName, Integer bedrooms, Integer bathrooms, Integer minPrice, Integer maxPrice, Integer MinSqM, Integer MaxSqM) {
        Specification<Estate> spec = Specification.where(hasCityName(cityName))
                .and(hasAreaName(areaName))
                .and(hasBedrooms(bedrooms))
                .and(hasBathrooms(bathrooms))
                .and(hasPriceBetween(minPrice, maxPrice))
                .and(hasSqMBetween(MinSqM, MaxSqM));

        return estateRepository.findAll(spec);
    }

    // Specification to filter estates by city name
    private Specification<Estate> hasCityName(String cityName) {
        return (root, query, criteriaBuilder) ->
                (cityName != null && !cityName.trim().isEmpty())
                        ? criteriaBuilder.equal(criteriaBuilder.lower(root.get("cityName")), cityName.toLowerCase())
                        : null;
    }

    // Specification to filter estates by area name
    private Specification<Estate> hasAreaName(String areaName) {
        return (root, query, criteriaBuilder) ->
                (areaName != null && !areaName.trim().isEmpty())
                        ? criteriaBuilder.equal(criteriaBuilder.lower(root.get("areaName")), areaName.toLowerCase())
                        : null;
    }

    // Specification to filter estates by the number of bedrooms
    private Specification<Estate> hasBedrooms(Integer bedrooms) {
        return (root, query, criteriaBuilder) ->
                (bedrooms != null) ? criteriaBuilder.equal(root.get("bedrooms"), bedrooms) : null;
    }

    // Specification to filter estates by the number of bathrooms
    private Specification<Estate> hasBathrooms(Integer bathrooms) {
        return (root, query, criteriaBuilder) ->
                (bathrooms != null) ? criteriaBuilder.equal(root.get("bathrooms"), bathrooms) : null;
    }

    // Specification to filter estates by size range in square meters
    private Specification<Estate> hasSqMBetween(Integer MinSqM, Integer MaxSqM) {
        return (root, query, criteriaBuilder) -> {
            if (MinSqM != null && MaxSqM != null) {
                return criteriaBuilder.between(root.get("sqM"), MinSqM, MaxSqM); // Between range
            }
            if (MinSqM != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("sqM"), MinSqM); // Minimum size
            }
            if (MaxSqM != null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("sqM"), MaxSqM); // Maximum size
            }
            return null;
        };
    }

    // Specification to filter estates by price range
    private Specification<Estate> hasPriceBetween(Integer minPrice, Integer maxPrice) {
        return (root, query, criteriaBuilder) -> {
            if (minPrice != null && maxPrice != null) {
                return criteriaBuilder.between(root.get("price"), minPrice, maxPrice); // Between range
            }
            if (minPrice != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice); // Minimum price
            }
            if (maxPrice != null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice); // Maximum price
            }
            return null;
        };
    }

    @Transactional
    public void updateOwnerUsernameInEstates(String oldUsername, String newUsername) {
        // Βρίσκουμε όλα τα ακίνητα που ανήκουν στον παλιό χρήστη
        List<Estate> estates = estateRepository.findByownerName(oldUsername);
        for (Estate estate : estates) {
            estate.setOwnerName(newUsername);  // Ενημερώνουμε το username του ιδιοκτήτη
            estateRepository.save(estate);  // Αποθηκεύουμε την αλλαγή στο ακίνητο
        }
        Owner owner = ownerRepository.findByOwnerUsername(oldUsername); // Εδώ μπορείς να το προσδιορίσεις με τη μέθοδο findByUsername
        if (owner != null) {
            owner.setOwnerUsername(newUsername);  // Ενημερώνουμε το username του ιδιοκτήτη
            ownerRepository.save(owner);  // Αποθηκεύουμε την αλλαγή στον ιδιοκτήτη
        }
    }
}
