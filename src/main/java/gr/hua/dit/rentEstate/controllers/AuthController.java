package gr.hua.dit.rentEstate.controllers;

import gr.hua.dit.rentEstate.entities.Role;
import gr.hua.dit.rentEstate.entities.User;
import gr.hua.dit.rentEstate.repositories.RoleRepository;
import gr.hua.dit.rentEstate.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AuthController {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthController(RoleRepository roleRepository, UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void setup() {
        //Creating roles
        Role role_user = new Role("ROLE_USER");
        Role role_admin = new Role("ROLE_ADMIN");

        if (roleRepository.findByName("ROLE_USER").isEmpty()) {
            roleRepository.save(role_user);
        }

        if (roleRepository.findByName("ROLE_ADMIN").isEmpty()) {
            roleRepository.save(role_admin);
        }

        //Creating admin and give him credentials
        if (userRepository.findByUsername("admin").isEmpty()) {
            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setPassword(passwordEncoder.encode("admin123"));
            adminUser.setEmail("rent_estate_admin@gmail.com");
            adminUser.setVerified(true);

            //Assign Role_Admin to admin
            Role admin_role = roleRepository.findByName("ROLE_ADMIN").orElseThrow(() -> new RuntimeException("ROLE_ADMIN not found"));
            adminUser.getRoles().add(admin_role);

            //Save admin
            userRepository.save(adminUser);
        }
    }

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @RequestMapping("/postLogin")
    public String postLogin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String role = authentication.getAuthorities().toString();

        //Check if logged-in user is admin
        if (role.contains("ROLE_ADMIN")) {
            return "redirect:/admin/admin"; // Redirect to admin homepage
        } else {
            return "redirect:/"; // Redirect to home page
        }
    }
}
