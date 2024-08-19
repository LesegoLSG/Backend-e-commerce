package com.shopping.e_commerce.Services.User;

import com.shopping.e_commerce.Entity.User;
import com.shopping.e_commerce.dto.UserDTO.CreateUserRequest;
import com.shopping.e_commerce.dto.UserDTO.UserDto;
import com.shopping.e_commerce.dto.UserDTO.updateUserRequest;

public interface IUserService {
    User getUserById(Long userId);
    User createUser(CreateUserRequest request);
    User updateUser(updateUserRequest request, Long userId);
    void deleteUser(Long userId);

    //Convert user to UserDto
    UserDto convertUserToDto(User user);
}
