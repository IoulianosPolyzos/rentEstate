package gr.hua.dit.rentEstate.controllers;

import gr.hua.dit.rentEstate.entities.Estate;
import gr.hua.dit.rentEstate.entities.Owner;
import gr.hua.dit.rentEstate.entities.User;
import gr.hua.dit.rentEstate.service.EstateService;
import gr.hua.dit.rentEstate.service.OwnerService;
import gr.hua.dit.rentEstate.service.TenantService;
import gr.hua.dit.rentEstate.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import java.security.Principal;

@Controller
@RequestMapping("estate")
public class EstateController {
    EstateService estateService;
    OwnerService ownerService;
    TenantService tenantService;
    UserService userService;
    public EstateController(EstateService estateService, OwnerService ownerService, TenantService tenantService, UserService userService) {
        this.estateService = estateService;
        this.ownerService = ownerService;
        this.tenantService = tenantService;
        this.userService = userService;
    }



    //Show Estates
    @GetMapping("/estates")
    public String showEstates(Model model){
        model.addAttribute("estates", estateService.getEstates());
        return "estate/estates";
    }

    //Show Estate
    @GetMapping("/{id}")
    public String showEstate(@PathVariable Integer id, Model model){
        model.addAttribute("estates", estateService.getEstate(id));
        return "estate/estates";
    }

    //Show Estate Details
    @GetMapping("/{id}/details")
    public String getEstateDetails(@PathVariable Integer id, Model model) {
        Estate estate = estateService.getEstate(id);
        model.addAttribute("estate", estate);
        return "estate/estate_details";
    }


    //Add Estate
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/new")
    public String addEstate(Model model, Principal principal) {
        String loggedInUsername = principal.getName();
        User user = userService.getUserByUsername(loggedInUsername);
        Estate estate = new Estate();
        model.addAttribute("estate", estate);

        if (loggedInUsername == null) {
            //If user is not logged in , redirect to login
            return "redirect:/login";
        }

        //If user is not Verified by admin , user cannot add estate and must wait for admin to verify
        if (!user.isVerified()) {
            model.addAttribute("error", "Your account is not verified. You cannot add an estate.");
            return "error/not_verified_page";
        }

        return "estate/estate";

    }


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/new")
    public String saveEstate(@ModelAttribute Estate estate, Model model, Principal principal) {
        // Get logged-in user username
        String loggedInUsername = principal.getName();

        //Find owner
        Owner owner = ownerService.findByUsername(loggedInUsername);

        //If there is no owner , create one and set logged-in user username and email
        if (owner == null) {
            owner = new Owner();
            owner.setOwnerUsername(loggedInUsername);

            User user = userService.getUserByUsername(loggedInUsername);
            if (user != null) {
                owner.setEmail(user.getEmail());
            }

            estate.setOwner(owner);  //Set estate owner
            ownerService.saveOwner(owner);  //Save Owner
        }

        //Check if renovationYear is earlier than YearBuilt
        if (estate.getRenovationYear() < estate.getYearBuilt()) {
            model.addAttribute("error", "Renovation year cannot be earlier than the year built.");
            model.addAttribute("estate", estate);
            return "estate/estate";
        }

        //Check if renovationYear and YearBuilt are correctly given by user input

        if (estate.getRenovationYear() < 1900 || estate.getRenovationYear() > 2025) {
            model.addAttribute("error", "Renovation year must be between 1900 and 2025.");
            model.addAttribute("estate", estate);
            return "estate/estate";
        }
        if (estate.getYearBuilt() < 1900 || estate.getYearBuilt() > 2025) {
            model.addAttribute("error", "Year built must be between 1900 and 2025.");
            model.addAttribute("estate", estate);
            return "estate/estate";
        }

        //Update Estate
        estate.setOwnerName(principal.getName());
        estate.setOwner(owner);
        estateService.saveEstate(estate);


        model.addAttribute("estates", estateService.getEstates());

        return "estate/add_estate_success";
    }


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/my-estates/{id}/delete")
    public String deleteEstate(@PathVariable Integer id, Model model, Principal principal) {
        // Get Estate
        Estate estate = estateService.getEstate(id);

        // Check if there is this estate
        if (estate == null) {
            model.addAttribute("errorMessage", "Estate not found.");
            return "redirect:/estate";
        }

        // Get logged-in user username
        String loggedInUsername = principal.getName();

        // Check this logged-in user is the owner of the Estate, if not show message
        if (!estate.getOwner().getOwnerUsername().equals(loggedInUsername)) {
            model.addAttribute("errorMessage", "You are not authorized to delete this estate.");
            return "redirect:/estate";
        }

        // Delete Estate
        estateService.deleteEstate(id);
        return "redirect:/estate/my-estates";
    }

    @GetMapping("/my-estates")
    @PreAuthorize("isAuthenticated()")
    public String getMyEstates(Model model, Principal principal) {
        // Get logged-in user username
        String loggedInUsername = principal.getName();

        // Get owner
        Owner owner = ownerService.findByUsername(loggedInUsername);

        // Get Owner Estates
        List<Estate> myEstates = estateService.findByOwner(owner);

        // Check if there are Owner Estates
        if (myEstates.isEmpty()) {
            model.addAttribute("message", "No estates found for this user.");
        }


        model.addAttribute("estates", myEstates);


        return "estate/my_estates";
    }

    // Return edit_estate html template
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/edit")
    public String editEstate(@PathVariable Integer id, Model model, Principal principal) {
        Estate estate = estateService.getEstate(id);

        //Check if there is this estate
        if (estate == null) {
            model.addAttribute("errorMessage", "Estate not found.");
            return "redirect:/estate/my-estates";
        }

       //Check if logged-in user username is the owner of the Estate
        String loggedInUsername = principal.getName();
        if (!estate.getOwner().getOwnerUsername().equals(loggedInUsername)) {
            model.addAttribute("errorMessage", "You are not authorized to edit this estate.");
            return "redirect:/estate/my-estates";
        }


        model.addAttribute("estate", estate);
        return "estate/edit_estate";
    }

    //
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/update")
    public String updateEstate(@PathVariable Integer id, @ModelAttribute Estate estate, Model model, Principal principal) {
        Estate existingEstate = estateService.getEstate(id);


        if (existingEstate == null) {
            model.addAttribute("errorMessage", "Estate not found.");
            return "redirect:/estate/my-estates";
        }


        String loggedInUsername = principal.getName();
        if (!existingEstate.getOwner().getOwnerUsername().equals(loggedInUsername)) {
            model.addAttribute("errorMessage", "You are not authorized to update this estate.");
            return "redirect:/estate/my-estates";
        }

        //Update the Estate attributes
        existingEstate.setPrice(estate.getPrice());
        existingEstate.setBedrooms(estate.getBedrooms());
        existingEstate.setBathrooms(estate.getBathrooms());
        existingEstate.setRenovationYear(estate.getRenovationYear());


        // Save Estate
        estateService.saveEstate(existingEstate);


        model.addAttribute("message", "Estate updated successfully.");
        return "redirect:/estate/my-estates";
    }

    // Search estate
    @GetMapping("/search")
    public String searchEstates(@RequestParam(required = false) String cityName,
                                @RequestParam(required = false) String areaName,
                                @RequestParam(required = false) Integer bedrooms,
                                @RequestParam(required = false) Integer bathrooms,
                                @RequestParam(required = false) Integer minPrice,
                                @RequestParam(required = false) Integer maxPrice,
                                @RequestParam(required = false) Integer MinSqM,
                                @RequestParam(required = false) Integer MaxSqM,
                                Model model) {
        List<Estate> estates = estateService.searchEstates(cityName,areaName, bedrooms, bathrooms, minPrice, maxPrice,MinSqM,MaxSqM);
        model.addAttribute("estates", estates);
        return "estate/estates";
    }


}
