package com.shopping.e_commerce.Services.User;

import com.shopping.e_commerce.Entity.Role;
import com.shopping.e_commerce.Entity.User;
import com.shopping.e_commerce.Enums.RoleEnum;
import com.shopping.e_commerce.Repository.RoleRepository;
import com.shopping.e_commerce.Repository.UserRepository;
import com.shopping.e_commerce.dto.UserDTO.CreateUserRequest;
import com.shopping.e_commerce.dto.UserDTO.UserDto;
import com.shopping.e_commerce.dto.UserDTO.updateUserRequest;
import com.shopping.e_commerce.exceptions.AlreadyExistsException;
import com.shopping.e_commerce.exceptions.ResourceNotFoundException;
import com.shopping.e_commerce.security.user.ShoppingUserDetails;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
/**
 * Service class for managing users.
 */
@Service
@RequiredArgsConstructor
public class UserService implements IUserService{

    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final ModelMapper modelMapper;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private final RoleRepository roleRepository;

    /**
     * Retrieves a user by their ID.
     *
     * @param userId the ID of the user to retrieve
     * @return the User object
     * @throws ResourceNotFoundException if the user is not found
     */
    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    /**
     * Creates a new user based on the provided request.
     *
     * @param request the CreateUserRequest containing user details
     * @return the saved User object
     * @throws AlreadyExistsException if a user with the same email already exists
     */
    @Override
    public User createUser(CreateUserRequest request) {
        // Check if user already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AlreadyExistsException(request.getEmail() + " already exists.");
        }

        // Create a new user object
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setContactNo(request.getContactNo());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Assign roles to the user using the separate method
        Set<Role> userRoles = assignRoles(new HashSet<>(request.getRoles()));
        user.setRoles(userRoles);

        // Save and return the user
        return userRepository.save(user);
    }
    /**
     * Assigns roles to a user, checking if they exist in the database.
     *
     * @param roles the set of roles to assign
     * @return a set of assigned Role objects
     */
    private Set<Role> assignRoles(Set<Role> roles) {
        Set<Role> assignedRoles = new HashSet<>();

        for(Role role : roles){
            //Checking if the role does exist in the database
            Optional<Role> existingRole = roleRepository.findByName(role.getName());
            if(existingRole.isPresent()){
                assignedRoles.add(existingRole.get());
            }else{
                Role newRole = new Role(role.getName());
                roleRepository.save(newRole);
                assignedRoles.add(newRole);
            }
        }
        return assignedRoles;
    }

    /**
     * Updates an existing user's details.
     *
     * @param request the updateUserRequest containing updated user details
     * @param userId  the ID of the user to update
     * @return the updated User object
     * @throws ResourceNotFoundException if the user is not found
     */
    @Override
    public User updateUser(updateUserRequest request, Long userId) {
        return userRepository.findById(userId).map(existingUser -> {
            existingUser.setFirstName(request.getFirstName());
            existingUser.setLastName(request.getLastName());
            existingUser.setContactNo(request.getContactNo());
            return userRepository.save(existingUser);
        }).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    /**
     * Deletes a user by their ID.
     *
     * @param userId the ID of the user to delete
     * @throws ResourceNotFoundException if the user is not found
     */
    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId).ifPresentOrElse(userRepository :: delete, () -> {
            throw new ResourceNotFoundException("User not found");
        });
    }

    /**
     * Converts a User object to a UserDto.
     *
     * @param user the User object to convert
     * @return the converted UserDto object
     */
    @Override
    public UserDto convertUserToDto(User user){
        UserDto userDto = modelMapper.map(user, UserDto.class);

        // If orders are null, set it to an empty list (optional, based on your preference)
        if (userDto.getOrders() == null || userDto.getOrders().isEmpty()) {
            userDto.setOrders(null);
        }

        // If cart is null, set it to null (this should already be happening by default)
        if (userDto.getCart() == null) {
            userDto.setCart(null);
        }

        if (userDto.getReviews() == null || userDto.getReviews().isEmpty()) {
            userDto.setReviews(null);
        }

        if(userDto.getBilling() == null){
            userDto.setBilling(null);
        }

        // If shippingInformations is empty or null, set it to null explicitly
        if (userDto.getShippingInformations() == null || userDto.getShippingInformations().isEmpty()) {
            userDto.setShippingInformations(null);
        }

        return userDto;

    }

    /**
     * Retrieves the currently authenticated user.
     *
     * @return the authenticated User object
     * @throws SecurityException if no authenticated user is found
     */
    @Override
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Check if authentication is null or not authenticated
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            throw new SecurityException("No authenticated user found");
        }

        // Check if the principal is an instance of UserDetails (ShoppingUserDetails)
        if (authentication.getPrincipal() instanceof ShoppingUserDetails) {
            ShoppingUserDetails userDetails = (ShoppingUserDetails) authentication.getPrincipal();
            String email = userDetails.getEmail();

            // Retrieve the user from the database using the email
            return userRepository.findByEmail(email);
        }

        throw new SecurityException("User not found in context");

    }

}
