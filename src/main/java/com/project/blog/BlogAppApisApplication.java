package com.project.blog;

import com.project.blog.entities.Role;
import com.project.blog.repositories.RoleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BlogAppApisApplication implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Autowired
    public BlogAppApisApplication(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public void addRole(){
        roleRepository.save(new Role(1, "ROLE_ADMIN"));
        roleRepository.save(new Role(2, "ROLE_USER"));
    }

    public static void main(String[] args) {
        SpringApplication.run(BlogAppApisApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Override
    public void run(String... args) {
        addRole();
    }
}
