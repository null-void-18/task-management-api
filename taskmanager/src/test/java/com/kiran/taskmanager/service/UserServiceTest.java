package com.kiran.taskmanager.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.kiran.taskmanager.dto.UserRequestDto;
import com.kiran.taskmanager.dto.UserResponseDto;
import com.kiran.taskmanager.exception.RoleNotFoundException;
import com.kiran.taskmanager.exception.UserNotFoundException;
import com.kiran.taskmanager.model.Role;
import com.kiran.taskmanager.model.User;
import com.kiran.taskmanager.repository.RoleRepository;
import com.kiran.taskmanager.repository.UserRepository;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private UserRequestDto sampleRequest;
    private User sampleUser;
    private Role sampleRole;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    
        // Mock encode method
        when(passwordEncoder.encode(any(CharSequence.class))).thenAnswer(invocation -> invocation.getArgument(0));
    
        sampleRole = Role.builder().id(1L).name("USER").build();
        sampleUser = User.builder()
                .id(1L)
                .name("Kiran Sekhar")
                .email("kiran@example.com")
                .password("hashedpassword")
                .roles(Set.of(sampleRole))
                .build();
    
        sampleRequest = UserRequestDto.builder()
                .name("Kiran Sekhar")
                .email("kiran@example.com")
                .password("password123")
                .roleIds(Set.of(1L))
                .build();
    
        // Manually create UserService with mocks
        userService = new UserService(userRepository, roleRepository, passwordEncoder);
    }
    

    @Test
    void testCreateUser() {
        when(roleRepository.findById(1L)).thenReturn(Optional.of(sampleRole));
        when(userRepository.save(any(User.class))).thenReturn(sampleUser);

        UserResponseDto response = userService.createUser(sampleRequest);

        assertEquals("Kiran Sekhar", response.getName());
        assertEquals(1, response.getRoles().size());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testCreateUserRoleNotFound() {
        when(roleRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RoleNotFoundException.class, () -> userService.createUser(sampleRequest));
    }

    @Test
    void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(sampleUser));

        List<UserResponseDto> users = userService.getAllUsers();
        assertEquals(1, users.size());
        assertEquals("Kiran Sekhar", users.get(0).getName());
    }

    @Test
    void testGetUserById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));

        UserResponseDto user = userService.getUserById(1L);
        assertEquals("Kiran Sekhar", user.getName());
    }

    @Test
    void testGetUserByIdNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(1L));
    }

    @Test
    void testDeleteUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
        doNothing().when(userRepository).delete(sampleUser);

        assertDoesNotThrow(() -> userService.deleteUser(1L));
        verify(userRepository, times(1)).delete(sampleUser);
    }

    @Test
    void testDeleteUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(1L));
    }
}
