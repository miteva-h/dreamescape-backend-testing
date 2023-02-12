package com.project.service.implementation;

import com.project.domain.ShoppingCart;
import com.project.domain.dto.RegisterDto;
import com.project.domain.exceptions.*;
import com.project.domain.identity.Role;
import com.project.domain.identity.User;
import com.project.repository.RoleRepository;
import com.project.repository.ShoppingCartRepository;
import com.project.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplementationTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImplementation userService;

//    @Test
//    void addUser_ShouldReturnOptionalOfUser() {
//        RegisterDto registerDto = new RegisterDto("user1", "password1", "password1", "firstName1", "lastName1", "role1");
//        Role role = new Role("role1");
//        ShoppingCart shoppingCart = new ShoppingCart();
//        User user = new User(registerDto.getUsername(), passwordEncoder.encode(registerDto.getPassword()), registerDto.getFirstName(), registerDto.getLastName(), shoppingCart);
//        User expectedUser = new User("user1", passwordEncoder.encode("password1"), "firstName1", "lastName1", shoppingCart);
//        expectedUser.setRoles(Arrays.asList(role));
//        when(userRepository.findByUsername("user1")).thenReturn(Optional.empty());
//        when(roleRepository.findByName("role1")).thenReturn(Optional.of(role));
//        when(userRepository.save(expectedUser)).thenReturn(expectedUser);
//        when(shoppingCartRepository.save(new ShoppingCart())).thenReturn(new ShoppingCart());
//
//        Optional<User> actualUser = userService.addUser(registerDto);
//
//        assertTrue(actualUser.isPresent());
//        assertThat(actualUser.get()).isEqualTo(expectedUser);
//        verify(userRepository).save(expectedUser);
//        verify(shoppingCartRepository, times(2)).save(new ShoppingCart());
//        verify(roleRepository).findByName("role1");
//        verify(userRepository).save(expectedUser);
//    }

    @Test
    public void addRole_ShouldReturnOptionalOfRole() {
        Role role = new Role("ROLE_ADMIN");
        when(roleRepository.save(role)).thenReturn(role);
        Optional<Role> result = userService.addRole(role);
        assertEquals(role, result.get());
    }

    @Test
    public void addRoleToUser_ShouldReturnVoid() {
        Role role = new Role("ROLE_ADMIN");
        User user = new User("user1", "password", "firstName", "lastName", null);

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(roleRepository.findByName(role.getName())).thenReturn(Optional.of(role));
        userService.addRoleToUser(user.getUsername(), role.getName());
        assertEquals(1, user.getRoles().size());
        assertEquals(role, user.getRoles().get(0));
    }

    @Test
    public void addRoleToUser_UserNotFound_ShouldThrowException() {
        Role role = new Role("ROLE_ADMIN");
        User user = new User("user1", "password", "firstName", "lastName", null);

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.addRoleToUser(user.getUsername(), role.getName()));
    }

    @Test
    public void addRoleToUser_RoleNotFound_ShouldThrowException() {
        Role role = new Role("ROLE_ADMIN");
        User user = new User("user1", "password", "firstName", "lastName", null);

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(roleRepository.findByName(role.getName())).thenReturn(Optional.empty());
        assertThrows(RoleNotFoundException.class, () -> userService.addRoleToUser(user.getUsername(), role.getName()));
    }

    @Test
    void addUser_PasswordsDoNotMatch_ShouldThrowException() {
        RegisterDto registerDto = new RegisterDto("user1", "password1", "password2", "firstName1", "lastName1", "role1");

        assertThrows(PasswordsDoNotMatchException.class, () -> userService.addUser(registerDto));
        verify(userRepository, never()).save(any(User.class));
        verify(shoppingCartRepository, never()).save(any(ShoppingCart.class));
    }

    @Test
    void addUser_UsernameAlreadyExists_ShouldThrowException() {
        RegisterDto registerDto = new RegisterDto("user1", "password1", "password1", "firstName1", "lastName1", "role1");
        when(userRepository.findByUsername("user1")).thenReturn(Optional.of(new User("user1", "", "", "", new ShoppingCart())));

        assertThrows(UsernameAlreadyExistsException.class, () -> userService.addUser(registerDto));
        verify(userRepository, never()).save(any(User.class));
        verify(shoppingCartRepository, never()).save(any(ShoppingCart.class));
    }

    @Test
    void addUser_InvalidUsernameOrPassword_ShouldThrowException() {
        RegisterDto registerDto1 = new RegisterDto(null, "password1", "password1", "firstName1", "lastName1", "role1");
        assertThrows(InvalidUsernameOrPasswordException.class, () -> userService.addUser(registerDto1));

        RegisterDto registerDto2 = new RegisterDto("", "password1", "password1", "firstName1", "lastName1", "role1");
        assertThrows(InvalidUsernameOrPasswordException.class, () -> userService.addUser(registerDto2));

        RegisterDto registerDto3 = new RegisterDto("user1", null, null, "firstName1", "lastName1", "role1");
        assertThrows(InvalidUsernameOrPasswordException.class, () -> userService.addUser(registerDto3));

        RegisterDto registerDto4 = new RegisterDto("user1", "", "", "firstName1", "lastName1", "role1");
        assertThrows(InvalidUsernameOrPasswordException.class, () -> userService.addUser(registerDto4));

        verify(userRepository, never()).save(any(User.class));
        verify(shoppingCartRepository, never()).save(any(ShoppingCart.class));
    }

    @Test
    void findByUsername_ShouldReturnOptionalOfUser() {
        User expectedUser = new User("user1", "password1", "firstName1", "lastName1", new ShoppingCart());
        when(userRepository.findByUsername("user1")).thenReturn(Optional.of(expectedUser));

        Optional<User> actualUser = userService.findByUsername("user1");

        assertTrue(actualUser.isPresent());
        assertThat(actualUser.get()).isEqualTo(expectedUser);
        verify(userRepository).findByUsername("user1");
    }

    @Test
    void findAll_ShouldReturnListOfUsers() {
        List<User> expectedUsers = Arrays.asList(
                new User("user1", "password1", "firstName1", "lastName1", new ShoppingCart()),
                new User("user2", "password2", "firstName2", "lastName2", new ShoppingCart()),
                new User("user3", "password3", "firstName3", "lastName3", new ShoppingCart())
        );
        when(userRepository.findAll()).thenReturn(expectedUsers);

        List<User> actualUsers = userService.findAll();

        assertThat(actualUsers).isEqualTo(expectedUsers);
        verify(userRepository).findAll();
    }

    @Test
    void findAllRoles_ShouldReturnListOfRoles() {
        List<Role> expectedRoles = Arrays.asList(
                new Role("role1"),
                new Role("role2"),
                new Role("role3")
        );
        when(roleRepository.findAll()).thenReturn(expectedRoles);

        List<Role> actualRoles = userService.findAllRoles();

        assertThat(actualRoles).isEqualTo(expectedRoles);
        verify(roleRepository).findAll();
    }

    @Test
    public void loadUserByUsername_ShouldReturnUserDetails(){
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("testpassword");
        Role role = new Role();
        role.setName("ROLE_USER");
        user.setRoles(Arrays.asList(role));
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        UserDetails result = userService.loadUserByUsername("testuser");
        assertEquals("testuser", result.getUsername());
        assertEquals("testpassword", result.getPassword());
        Collection<? extends GrantedAuthority> authorities = result.getAuthorities();
        assertEquals(1, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_USER")));
    }

    @Test
    public void loadUserByUsername_UsernameNotFound_ShouldThrowException() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("testuser"));
    }

}