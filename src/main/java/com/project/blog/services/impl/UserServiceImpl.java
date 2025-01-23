package com.project.blog.services.impl;

import com.project.blog.entities.User;
import com.project.blog.exceptions.ResourceNotFoundException;
import com.project.blog.payloads.UserDTO;
import com.project.blog.repositories.RoleRepository;
import com.project.blog.repositories.UserRepository;
import com.project.blog.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, BCryptPasswordEncoder bCryptPasswordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.roleRepository = roleRepository;
    }

    private User UserDTOToUser(UserDTO userDTO) {
        /*user.setId(userDTO.getId());
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setAbout(userDTO.getAbout());*/
        return this.modelMapper.map(userDTO, User.class);
    }

    private UserDTO UserToUserDTO(User user) {
        /*userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        userDTO.setAbout(user.getAbout());*/
        return this.modelMapper.map(user, UserDTO.class);
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        userDTO.setPassword(this.bCryptPasswordEncoder.encode(userDTO.getPassword()));
        User user = UserDTOToUser(userDTO);
        user.getRoles().add(roleRepository.findById(2).orElseThrow(() -> new ResourceNotFoundException("Role", "id", String.valueOf(2))));
        User save = this.userRepository.save(user);
        return this.UserToUserDTO(save);
    }

    @Override
    public UserDTO updateUser(UserDTO userDTO, Integer id) {
        User user = this.userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", String.valueOf(id)));

        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setAbout(userDTO.getAbout());

        User update = this.userRepository.save(user);
        return this.UserToUserDTO(update);
    }

    @Override
    public UserDTO getUserById(Integer id) {
        User get = this.userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", String.valueOf(id)));
        return this.UserToUserDTO(get);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = this.userRepository.findAll();
        //return users.stream().map(user -> this.UserToUserDTO(user)).collect(Collectors.toList());
        //or we can use method reference way
        return users.stream().map(this::UserToUserDTO).collect(Collectors.toList());
    }

    @Override
    public void deleteUser(Integer id) {
        User user = this.userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", String.valueOf(id)));
        /*this.userRepository.deleteById(id);*/
        user.setRoles(null);
        this.userRepository.delete(user);
        //this.userRepository.delete(user);
    }

    @Override
    public Boolean isUserExistById(Integer id) {
        return this.userRepository.existsById(id);
    }

    @Override
    public Boolean isUserExistByEmail(String email) {
        return this.userRepository.existsByEmail(email);
    }
}
