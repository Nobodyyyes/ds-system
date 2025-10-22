package esmukanov.ds.system.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RegisterRequest {

    private String username;

    private String email;

    private String password;

    private String firstName;

    private String lastName;

    private String middleName;
}
