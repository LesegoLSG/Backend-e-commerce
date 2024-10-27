package com.shopping.e_commerce.security.config;

import com.shopping.e_commerce.security.jwt.JwtAuthEntryPoint;
import com.shopping.e_commerce.security.jwt.JwtAuthenticationFilter;
import com.shopping.e_commerce.security.user.ShoppingUserDetailsService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

/**
 * // Class responsible for configuring security settings for the shopping application
 */
@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class shopConfig {

    private final ShoppingUserDetailsService userDetailsService;
    private final JwtAuthEntryPoint authEntryPoint;

    private static final List<String> SECURED_URL = List.of("/api/v1/cart/**","/api/v1/cartItems/**");

    /**
     * Bean for ModelMapper to convert between different object types.
     *
     * @return a ModelMapper instance.
     */
    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

    /**
     * Bean for password encoding using BCrypt.
     *
     * @return a PasswordEncoder instance configured with BCrypt.
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * Bean for the JWT authentication filter to process JWT tokens.
     *
     * @return an instance of JwtAuthenticationFilter.
     */
    @Bean
    public JwtAuthenticationFilter authenticationFilter(){
        return new JwtAuthenticationFilter();
    }

    /**
     * Bean for managing authentication, retrieves the AuthenticationManager.
     *
     * @param authConfig configuration for authentication management.
     * @return an AuthenticationManager instance.
     * @throws Exception if there is an issue retrieving the authentication manager.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception{
        return authConfig.getAuthenticationManager();
    }

    /**
     * Bean for configuring the DAO authentication provider.
     *
     * @return a DaoAuthenticationProvider configured with userDetailsService and password encoder.
     */
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        var authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService); // Setting the user details service
        authProvider.setPasswordEncoder(passwordEncoder()); // Setting the password encoder
        return authProvider;
    }


    /**
     * Configures the security filter chain to manage web security.
     *
     * @param http the HttpSecurity object to configure.
     * @return a SecurityFilterChain configured for the application.
     * @throws Exception if there is an issue configuring the security filter chain.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Configuring the HTTP security settings
        http.csrf(AbstractHttpConfigurer::disable) // Disabling CSRF protection
                .exceptionHandling(exception -> exception.authenticationEntryPoint(authEntryPoint)) // Setting the authentication entry point
                .sessionManagement(session ->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // Configuring stateless session management
                .authorizeHttpRequests(auth -> auth.requestMatchers(SECURED_URL.toArray(String[]::new)).authenticated().anyRequest().permitAll()); // Securing defined URLs
        http.authenticationProvider(daoAuthenticationProvider()); // Setting the authentication provider
        http.addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
