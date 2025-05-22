package gr.hua.dit.rentEstate.controllers;

import gr.hua.dit.rentEstate.entities.*;
import gr.hua.dit.rentEstate.service.EstateService;
import gr.hua.dit.rentEstate.service.RentService;
import gr.hua.dit.rentEstate.service.TenantService;
import gr.hua.dit.rentEstate.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("rent")
public class RentController {

    //Service classes
    private final EstateService estateService;
    private final RentService rentService;
    private final TenantService tenantService;
    private final UserService userService;

    // Constructor to initialize services
    public RentController(EstateService estateService, RentService rentService, TenantService tenantService, UserService userService) {
        this.estateService = estateService;
        this.rentService = rentService;
        this.tenantService = tenantService;
        this.userService = userService;
    }

    // Rent request form
    @GetMapping("/form")
    public String showRentForm(@RequestParam("estateId") Integer estateId, Model model) {
        Estate estate = estateService.getEstate(estateId);
        if (estate == null) {
            throw new IllegalArgumentException("Invalid estate ID");
        }
        model.addAttribute("estate", estate);
        model.addAttribute("rent", new Rent()); // Create a new Rent object for the form
        return "estate/request";
    }

    // View the list of rent requests for a specific owner and estate
    @GetMapping("/rent")
    public String viewOwnerRents(@RequestParam("ownerId") Long ownerId, Long estateId, Model model) {
        List<Rent> rent = rentService.getRentsByOwnerId(ownerId, estateId);
        model.addAttribute("rent", rent);
        model.addAttribute("ownerId", ownerId);
        model.addAttribute("estateId", estateId);
        return "estate/rentList";
    }

    // Approve a rent request
    @PostMapping("/approve")
    public String approveRent(@RequestParam("rentId") Long rentId, @RequestParam("ownerId") Long ownerId, @RequestParam("estateId") Integer estateId) {
        Rent rent = rentService.getRentById(rentId);
        if (rent == null) {
            throw new IllegalArgumentException("Invalid rent ID");
        }

        Estate estate = estateService.getEstate(estateId);
        if (estate == null) {
            throw new IllegalArgumentException("Invalid estate ID");
        }

        String tenantUsername = rent.getTenantUsername();
        Tenant tenant = tenantService.findByUsername(tenantUsername);
        if (tenant == null) {
            throw new IllegalArgumentException("Tenant not found");
        }

        // Associate the estate with the tenant
        if (tenant.getEstates() == null) {
            tenant.setEstates(new ArrayList<>());
        }
        tenant.getEstates().add(estate);
        estate.setTenant(tenant);

        tenantService.saveTenant(tenant); // Save tenant changes
        rentService.approveRent(rentId);  // Approve the rent request

        return "redirect:/rent/rent?estateId=" + estateId + "&ownerId=" + ownerId; // Redirect to the rent list
    }

    // Reject a rent request
    @PostMapping("/reject")
    public String rejectRent(@RequestParam("rentId") Long rentId, @RequestParam("ownerId") Long ownerId, @RequestParam("estateId") Long estateId) {
        rentService.rejectRent(rentId); // Reject the rent request
        return "redirect:/rent/rent?estateId=" + estateId + "&ownerId=" + ownerId; // Redirect to the rent list
    }

    // Submit a new rent request
    @PostMapping("/submit")
    public String submitRentRequest(@ModelAttribute Rent rent, @RequestParam("estateId") Integer estateId, Model model, Principal principal) {
        String loggedInUsername = principal.getName(); // Get the logged-in user's username

        Estate estate = estateService.getEstate(estateId);
        if (estate == null) {
            throw new IllegalArgumentException("Invalid estate ID");
        }

        Tenant tenant = tenantService.findByUsername(loggedInUsername);
        if (tenant == null) {
            // If the tenant doesn't exist, create a new one
            tenant = new Tenant();
            tenant.setTenantUsername(loggedInUsername);
            rent.setTenantUsername(loggedInUsername);
            User user = userService.getUserByUsername(loggedInUsername);
            if (user != null) {
                tenant.setEmail(user.getEmail()); // Set email from user info
            }
            estate.setTenant(tenant); // Associate tenant with estate
            tenantService.saveTenant(tenant);
        }

        rent.setEstate(estate); // Link the rent request to the estate
        rent.setTenantUsername(loggedInUsername); // Set the tenant's username
        rentService.createRent(rent); // Save the rent request

        model.addAttribute("message", "Your rent request has been submitted successfully.");
        return "estate/success";
    }

    // View the logged-in user's rental requests
    @GetMapping("/my-rentals")
    public String viewMyRentals(Model model, Principal principal) {
        String loggedInUsername = principal.getName(); // Get logged-in username

        Tenant tenant = tenantService.findByUsername(loggedInUsername);
        List<Rent> MyRentRequests = rentService.getRentsByTenantUsername(loggedInUsername);

        if (MyRentRequests.isEmpty()) {
            model.addAttribute("msg", "There are no Rent Requests.");
        }

        model.addAttribute("rentRequests", MyRentRequests); // Add rental requests
        return "estate/myRentals";
    }
}
