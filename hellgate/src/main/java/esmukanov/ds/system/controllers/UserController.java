package esmukanov.ds.system.controllers;

import esmukanov.ds.system.dtos.request.RegisterRequest;
import esmukanov.ds.system.dtos.response.RegisterResponse;
import esmukanov.ds.system.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/exists/username/{username}")
    public boolean checkUsernameExists(@PathVariable String username) {
        return userService.isExistsUserByUsername(username);
    }

    @PostMapping(value = "/register",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public RegisterResponse registerUser(@RequestBody RegisterRequest registerRequest) {
        return userService.registerUser(registerRequest);
    }

    @PostMapping("/login/username/{username}/password/{password}")
    public boolean loginUser(@PathVariable String username, @PathVariable String password) {
        return userService.loginUser(username, password);
    }
}
