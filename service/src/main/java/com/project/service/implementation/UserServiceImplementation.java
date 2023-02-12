package com.project.service.implementation;

import com.project.domain.ShoppingCart;
import com.project.domain.dto.RegisterDto;
import com.project.domain.exceptions.*;
import com.project.domain.identity.Role;
import com.project.domain.identity.User;
import com.project.repository.RoleRepository;
import com.project.repository.ShoppingCartRepository;
import com.project.repository.UserRepository;
import com.project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImplementation implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Optional<User> addUser(RegisterDto registerDto) {
        if (registerDto.getUsername() == null || registerDto.getUsername().isEmpty() || registerDto.getPassword() == null || registerDto.getPassword().isEmpty())
            throw new InvalidUsernameOrPasswordException();
        if (!registerDto.getPassword().equals(registerDto.getRepeatPassword()))
            throw new PasswordsDoNotMatchException();
        if (this.userRepository.findByUsername(registerDto.getUsername()).isPresent())
            throw new UsernameAlreadyExistsException(registerDto.getUsername());
        ShoppingCart shoppingCart = new ShoppingCart();
        User user = new User(registerDto.getUsername(), passwordEncoder.encode(registerDto.getPassword()), registerDto.getFirstName(), registerDto.getLastName(), shoppingCart);
        this.userRepository.save(user);
        addRoleToUser(registerDto.getUsername(), registerDto.getRole());
        this.shoppingCartRepository.save(shoppingCart);
        return Optional.of(user);
    }

    @Override
    public Optional<Role> addRole(Role role) {
        this.roleRepository.save(role);
        return Optional.of(role);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        User user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
        Role role = roleRepository.findByName(roleName).orElseThrow(RoleNotFoundException::new);
        user.getRoles().add(role);
        //
        this.userRepository.save(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return this.userRepository.findByUsername(username);
    }

    @Override
    public List<User> findAll() {
        return this.userRepository.findAll();
    }

    @Override
    public List<Role> findAllRoles() {
        return roleRepository.findAll();
    }

    //for springboot to get all the users and check their roles
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(r -> authorities.add(new SimpleGrantedAuthority(r.getName())));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }
}
