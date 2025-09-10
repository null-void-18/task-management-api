package com.kiran.taskmanager.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kiran.taskmanager.dto.RoleResponseDto;
import com.kiran.taskmanager.dto.UserRequestDto;
import com.kiran.taskmanager.dto.UserResponseDto;
import com.kiran.taskmanager.exception.RoleNotFoundException;
import com.kiran.taskmanager.exception.UserNotFoundException;
import com.kiran.taskmanager.model.Role;
import com.kiran.taskmanager.model.User;
import com.kiran.taskmanager.repository.RoleRepository;
import com.kiran.taskmanager.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    private RoleRepository roleRepository;

    private UserRepository userRepository;

    public UserService(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }    


    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        User user = mapToEntity(userRequestDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser =userRepository.save(user);

        return mapToDto(savedUser);
    }

    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        return mapToDto(user);
    }


    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll()
            .stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
    }


    public UserResponseDto updateUser(Long id, UserRequestDto userRequestDto) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    
        user.setName(userRequestDto.getName());
        user.setEmail(userRequestDto.getEmail());
    
        if (userRequestDto.getPassword() != null && !userRequestDto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        }
    
        Set<Role> roles = userRequestDto.getRoleIds()
            .stream()
            .map(roleId -> roleRepository.findById(roleId)
                .orElseThrow(() -> new RoleNotFoundException("Role not found with id: " + roleId)))
            .collect(Collectors.toSet());
    
        user.setRoles(roles);
    
        User savedUser = userRepository.save(user);
    
        return mapToDto(savedUser);
    }


    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        userRepository.delete(user);
    }
    
    

    public User mapToEntity(UserRequestDto userRequestDto) {
        Set<Role> roles = userRequestDto.getRoleIds()
            .stream()
            .map(roleId -> roleRepository.findById(roleId)
                .orElseThrow(() -> new RoleNotFoundException("Role not found with id: " + roleId)))
            .collect(Collectors.toSet());

        return User.builder()
            .name(userRequestDto.getName())
            .email(userRequestDto.getEmail())
            .password(userRequestDto.getPassword())
            .roles(roles)
            .build();
    }

    public UserResponseDto mapToDto(User user) {
        Set<RoleResponseDto> roleResponseDtos = user.getRoles()
            .stream()
            .map(role -> RoleResponseDto.builder()
                .id(role.getId())
                .name(role.getName())
                .build())
            .collect(Collectors.toSet());

        return UserResponseDto.builder()
            .id(user.getId())
            .name(user.getName())
            .email(user.getEmail())
            .roles(roleResponseDtos)
            .createdAt(user.getCreatedAt())
            .updatedAt(user.getUpdatedAt())
            .build();
    }
}
