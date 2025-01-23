package com.project.blog.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int id;

    @NotBlank(message = "User Name can not be empty !!")
    @Size(min = 2,max = 20,message = "User name must be between 2 - 20 characters")
    private String name;

    @Column(unique = true)
    //We can also use @Pattern
    @Email(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$")
    @NotBlank(message = "E-Mail can not be empty !!")
    private String email;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 5, max = 15, message = "Password must contain minimum 5 characters and maximum of 15 characters ")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotBlank
    @Column(length = 500)
    private String about;

    private Set<RoleDTO> roles = new HashSet<>();

}
