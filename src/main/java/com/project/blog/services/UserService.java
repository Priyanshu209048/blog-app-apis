package com.project.blog.services;

import com.project.blog.payloads.UserDTO;

import java.util.List;

public interface UserService {

    UserDTO createUser(UserDTO userDTO);
    UserDTO updateUser(UserDTO userDTO, Integer id);
    UserDTO getUserById(Integer id);
    List<UserDTO> getAllUsers();
    void deleteUser(Integer id);
    Boolean isUserExistById(Integer id);
    Boolean isUserExistByEmail(String email);

}
