package gr.hua.dit.rentEstate.controllers;

import gr.hua.dit.rentEstate.entities.Estate;
import gr.hua.dit.rentEstate.entities.User;
import gr.hua.dit.rentEstate.service.EstateService;
import gr.hua.dit.rentEstate.service.UserService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;


@Controller
@RequestMapping("/admin")
public class AdminController {
    private UserService userService;
    private EstateService estateService;



    public AdminController(UserService userService,EstateService estateService) {
        this.userService = userService;
        this.estateService = estateService;
    }

    //Home admin layout
    @Secured("ROLE_ADMIN")
    @GetMapping("/admin")
    public String adminDashboard() {
        return "admin/admin";
    }

    //Admin verifies a user
    @Secured("ROLE_ADMIN")
    @GetMapping("/user/verify/{id}")
    public String verifyUser(@PathVariable("id") Long userId, Model model) {
        User user = userService.findById(userId); // Find user by id

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); // Get credentials of logged-in
        String role = authentication.getAuthorities().toString(); // get role of user
        if (role.contains("ROLE_ADMIN")) { // Check if logged-in is admin
            if (user != null && !user.isVerified()) { // Check if user is verified
                String userPassword = user.getPassword(); // Get user password
                user.setVerified(true);  //  Set user to be verified
                user.setPassword(userPassword); // Set password to avoid encryption bug
                userService.saveUser(user); //Save user
            }

            model.addAttribute("users", userService.getUsers());
            return "redirect:/users";
        }
        else if (role.contains("ROLE_USER")) {
            return "error/error-403";
        }
        return "error/error-403";
        // if connected user is not admin return  error-403 no permission
    }

    //Admin Approves an estate , same with verifyUser
    @Secured("ROLE_ADMIN")
    @GetMapping("/estate/approve/{id}")
    public String ApproveEstate(@PathVariable("id") Integer EstateId, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String role = authentication.getAuthorities().toString();
        if (role.contains("ROLE_ADMIN")) {
            Estate estate = estateService.getEstate(EstateId);



            if (estate != null && !estate.isApproved()) {
                estate.setApproved(true);  // Update isApproved to be true
                estateService.saveEstate(estate);
            }

            model.addAttribute("estates", estateService.getEstates());
            return "redirect:/estate/estates";
        }
        else if (role.contains("ROLE_USER")) {
            return "error/error-403";
        }
        return "error/error-403";

    }

    //Delete Estate by Admin
    @Secured("ROLE_ADMIN")
    @PostMapping("/estates/{id}/delete")
    public String deleteEstateByAdmin(@PathVariable Integer id, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String role = authentication.getAuthorities().toString();
        if (role.contains("ROLE_ADMIN")) {
            Estate estate = estateService.getEstate(id);


            if (estate == null) {
                model.addAttribute("errorMessage", "Estate not found.");
                return "redirect:/estate";
            }

            // delete Estate
            estateService.deleteEstate(id);
            return "redirect:/estate/estates";



        } else if (role.contains("ROLE_USER")) {
            return "error/error-403";
        }
        return "error/error-403";

    }

    @Secured("ROLE_ADMIN")
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Long id, Model model) {
        User user = userService.findById(id); // Find user by id

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); // Get credentials of logged-in
        String role = authentication.getAuthorities().toString(); // get role of user
        if (role.contains("ROLE_ADMIN")) { // Check if logged-in is admin
            if (user != null ) { // Check if user is verified
                userService.deleteUser(id); //Save user
            }

            model.addAttribute("users", userService.getUsers());
            return "redirect:/users";
        } else if (role.contains("ROLE_USER")) {
            return "error/error-403";
        }
        return "error/error-403";
    }

}
