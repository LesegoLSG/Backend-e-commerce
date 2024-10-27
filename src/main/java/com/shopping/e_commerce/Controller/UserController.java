package com.shopping.e_commerce.Controller;

import com.shopping.e_commerce.Entity.User;
import com.shopping.e_commerce.Services.User.IUserService;
import com.shopping.e_commerce.dto.UserDTO.CreateUserRequest;
import com.shopping.e_commerce.dto.UserDTO.UserDto;
import com.shopping.e_commerce.dto.UserDTO.updateUserRequest;
import com.shopping.e_commerce.exceptions.AlreadyExistsException;
import com.shopping.e_commerce.exceptions.ResourceNotFoundException;
import com.shopping.e_commerce.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * The UserController class handles HTTP requests related to user operations
 * within the e-commerce application. It provides endpoints for creating,
 * retrieving, updating, and deleting user accounts.
 *
 * The controller interacts with the IUserService to manage user-related logic
 * and responses are structured using a standardized ApiResponse format for
 * consistency across the API. Error handling is implemented for common
 * exceptions such as user already exists and resource not found.
 */

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/users")
public class UserController {

    @Autowired
    private final IUserService userService;

    /**
     * Retrieves a user by their ID.
     *
     * @param userId The ID of the user to be retrieved.
     * @return A ResponseEntity containing the ApiResponse with a success message and the user DTO.
     *         If the user is not found, a not found response is returned.
     */
    @GetMapping("/{userId}/user")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long userId){
        try {
            User user = userService.getUserById(userId);
            UserDto userDto = userService.convertUserToDto(user);
            return ResponseEntity.ok(new ApiResponse("success", userDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse( e.getMessage(), null));
        }
    }

    /**
     * Creates a new user account.
     *
     * @param request The request object containing user details for creation.
     * @return A ResponseEntity containing the ApiResponse with a success message and the created user DTO.
     *         If the user already exists, a conflict response is returned.
     */
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> createUser(@RequestBody CreateUserRequest request){
        try{
            User user = userService.createUser(request);
            UserDto userDto = userService.convertUserToDto(user);
            return ResponseEntity.ok(new ApiResponse("User created successfully", userDto));
        }catch(AlreadyExistsException e){
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(),null));
        }
    }

    /**
     * Updates an existing user account.
     *
     * @param request The request object containing updated user details.
     * @param userId The ID of the user to be updated.
     * @return A ResponseEntity containing the ApiResponse with a success message and the updated user DTO.
     *         If the user is not found, a not found response is returned.
     */
    @PutMapping("/{userId}/update")
    public ResponseEntity<ApiResponse> updateUser(@RequestBody updateUserRequest request,@PathVariable Long userId){
        try{
            User user = userService.updateUser(request,userId);
            UserDto userDto = userService.convertUserToDto(user);
            return ResponseEntity.ok(new ApiResponse("User updated successfully", userDto));
        }catch(ResourceNotFoundException e){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

    /**
     * Deletes a user account by their ID.
     *
     * @param userId The ID of the user to be deleted.
     * @return A ResponseEntity containing the ApiResponse with a success message.
     *         If the user is not found, a not found response is returned.
     */
    @DeleteMapping("/{userId}/delete")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long userId){
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok(new ApiResponse("User deleted successfully", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }
}
