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
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

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
        user.setGender(request.getGender());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Assign roles to the user using the separate method
        Set<Role> userRoles = assignRoles(new HashSet<>(request.getRoles()));
        user.setRoles(userRoles);

        // Save and return the user
        return userRepository.save(user);
    }
    /**
     * This method accepts a set of roles, checks if they exist in the database,
     * and returns the existing roles or creates and returns new ones if necessary.
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

    @Override
    public User updateUser(updateUserRequest request, Long userId) {
        return userRepository.findById(userId).map(existingUser -> {
            existingUser.setFirstName(request.getFirstName());
            existingUser.setLastName(request.getLastName());
            existingUser.setContactNo(request.getContactNo());
            existingUser.setGender(request.getGender());
            return userRepository.save(existingUser);
        }).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId).ifPresentOrElse(userRepository :: delete, () -> {
            throw new ResourceNotFoundException("User not found");
        });
    }

    //Convert user to UserDto
    @Override
    public UserDto convertUserToDto(User user){
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        return userRepository.findByEmail(email);

    }

}
