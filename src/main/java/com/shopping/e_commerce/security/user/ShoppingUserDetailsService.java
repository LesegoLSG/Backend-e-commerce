package com.shopping.e_commerce.security.user;

import com.shopping.e_commerce.Entity.User;
import com.shopping.e_commerce.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShoppingUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = Optional.ofNullable(userRepository.findByEmail(email)).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return ShoppingUserDetails.buildUserDetails(user);
    }
}
