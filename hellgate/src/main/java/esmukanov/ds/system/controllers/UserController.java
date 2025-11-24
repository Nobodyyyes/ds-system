package esmukanov.ds.system.controllers;

import esmukanov.ds.system.dtos.request.RegisterRequest;
import esmukanov.ds.system.dtos.response.RegisterResponse;
import esmukanov.ds.system.models.User;
import esmukanov.ds.system.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<User> getAll() {
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable UUID id) {
        return userService.getById(id);
    }

    @PostMapping
    public User save(@RequestBody User user) {
        return userService.save(user);
    }

    @PutMapping("/{id}")
    public User update(@PathVariable UUID id, @RequestBody User user) {
        return userService.update(id, user);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        userService.delete(id);
    }

    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public RegisterResponse registerUser(@RequestBody RegisterRequest registerRequest) {
        return userService.registerUser(registerRequest);
    }

    @PostMapping("/login/username/{username}/password/{password}")
    public boolean loginUser(@PathVariable String username, @PathVariable String password) {
        return userService.loginUser(username, password);
    }

    @GetMapping("/exists/username/{username}")
    public boolean checkUsernameExists(@PathVariable String username) {
        return userService.isExistsUserByUsername(username);
    }
}
