package esmukanov.ds.system.services;

import esmukanov.ds.system.components.base.BaseCrudOperation;
import esmukanov.ds.system.dtos.request.RegisterRequest;
import esmukanov.ds.system.dtos.response.RegisterResponse;
import esmukanov.ds.system.models.User;

import java.util.UUID;

public interface UserService extends BaseCrudOperation<User, UUID> {

    RegisterResponse registerUser(RegisterRequest registerRequest);

    boolean loginUser(String username, String password);

    boolean existsUser(UUID userId);
}
