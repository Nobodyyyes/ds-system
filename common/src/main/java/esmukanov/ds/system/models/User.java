package esmukanov.ds.system.models;

import esmukanov.ds.system.enums.Role;
import esmukanov.ds.system.enums.UserStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    private UUID id;

    private String username;

    private String email;

    private String password;

    private String firstName;

    private String lastName;

    private String middleName;

    private UserStatus userStatus;

    private Role role;

    private LocalDateTime registeredDate;

    private LocalDateTime lastLoginDate;
}
