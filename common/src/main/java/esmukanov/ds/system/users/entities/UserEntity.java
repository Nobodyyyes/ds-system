package esmukanov.ds.system.users.entities;

import esmukanov.ds.system.users.enums.Role;
import esmukanov.ds.system.users.enums.UserStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "middleName")
    private String middleName;

    @Column(name = "user_status")
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    @Column(name = "user_role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "registered_date")
    private LocalDateTime registeredDate;

    @Column(name = "last_login_date")
    private LocalDateTime lastLoginDate;
}
