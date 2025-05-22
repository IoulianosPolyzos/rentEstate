package gr.hua.dit.rentEstate.service;

import gr.hua.dit.rentEstate.entities.Role;
import gr.hua.dit.rentEstate.entities.User;
import gr.hua.dit.rentEstate.repositories.RoleRepository;
import gr.hua.dit.rentEstate.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository; // Repository for User data
    private final RoleRepository roleRepository; // Repository for Role data
    private final BCryptPasswordEncoder passwordEncoder; // Encoder for securely hashing passwords

    //Constructor for dependency
    public UserService(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //Save or update a User entity.
    @Transactional
    public Integer saveUser(User user) {
        Optional<User> existingUser = userRepository.findById(user.getId()); // Check if the user exists

        if (existingUser.isPresent()) {
            if (!passwordEncoder.matches(user.getPassword(), existingUser.get().getPassword())) {
                user.setPassword(existingUser.get().getPassword());
            }
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        // Set default role if none is provided
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            Role role = roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found.")); // Default to 'ROLE_USER' if no role exists
            Set<Role> roles = new HashSet<>();
            roles.add(role);
            user.setRoles(roles);
        }

        user = userRepository.save(user); // Save user to the repository
        return user.getId();
    }

    //Update a User entity.
    @Transactional
    public Integer updateUser(User user) {
        user = userRepository.save(user); // Save the updated user
        return user.getId();
    }

    //Load a user by username
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> opt = userRepository.findByUsername(username); // Search for the user by username

        if (opt.isEmpty()) {
            throw new UsernameNotFoundException("User with email: " + username + " not found!");
        } else {
            User user = opt.get(); // Get the user
            // Convert the User entity to Spring Security's UserDetails
            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    user.getPassword(),
                    user.getRoles()
                            .stream()
                            .map(role -> new SimpleGrantedAuthority(role.toString())) // Convert roles to authorities
                            .collect(Collectors.toSet())
            );
        }
    }

    //Retrieve all users from the database.
    @Transactional
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    //Retrieve a user by their ID.
    public Object getUser(Long userId) {
        return userRepository.findById(userId).get();
    }

    //Update or insert a Role entity in the database.
    @Transactional
    public void updateOrInsertRole(Role role) {
        roleRepository.updateOrInsert(role); // Insert or update role
    }

    //Retrieve a user by their username
    @Transactional
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username: " + username + " not found!"));
    }

    //Find a user by their ID.
    public User findById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @Transactional
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

}
