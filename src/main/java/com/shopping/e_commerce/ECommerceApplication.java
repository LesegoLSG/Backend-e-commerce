package com.shopping.e_commerce;

import com.shopping.e_commerce.Entity.Role;
import com.shopping.e_commerce.Repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@SpringBootApplication
public class ECommerceApplication implements CommandLineRunner {
	@Autowired
	private final RoleRepository roleRepository;

	Set<String> defaultRoles = Set.of("CUSTOMER","ADMIN","CONTENT_CREATOR");

	public static void main(String[] args) {
		SpringApplication.run(ECommerceApplication.class, args);
		System.out.println("Ran Successfully");
	}

	private void createDefaultRoleIfNotExists(Set<String> roles){
		roles.forEach(roleName ->{
			Optional<Role> existingRole = roleRepository.findByName(roleName);
			if(existingRole.isEmpty()){
				Role newRole = new Role(roleName);
				roleRepository.save(newRole);

			}
		});
	}

	@Override
	public void run(String... args) throws Exception {
		createDefaultRoleIfNotExists(defaultRoles);
	}
}
