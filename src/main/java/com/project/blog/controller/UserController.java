package com.project.blog.controller;

import com.project.blog.payloads.ApiResponse;
import com.project.blog.payloads.UserDTO;
import com.project.blog.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/")
    public ResponseEntity<?> createUser(@RequestBody @Valid UserDTO userDTO) {
        if (this.userService.isUserExistByEmail(userDTO.getEmail())) {
            return new ResponseEntity<>(new ApiResponse("User already exists"), HttpStatus.CONFLICT);
        }

        UserDTO createdUser = userService.createUser(userDTO);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable("userId") Integer id, @RequestBody @Valid UserDTO userDTO) {
        UserDTO updatedUser = this.userService.updateUser(userDTO, id);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable("userId") Integer userId) { //We can use ? in the place of ApiResponse if we don't know the type of response entity
        this.userService.deleteUser(userId);
        //return new ResponseEntity<>(Map.of("message", "User Deleted Successfully"), HttpStatus.OK);
        return new ResponseEntity<>(new ApiResponse("User Deleted Successfully"), HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(this.userService.getAllUsers());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(this.userService.getUserById(userId));
    }

}
