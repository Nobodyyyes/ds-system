package esmukanov.ds.system.services;

import esmukanov.ds.system.components.BaseCrudOperation;
import esmukanov.ds.system.users.models.User;

import java.util.UUID;

public interface UserService extends BaseCrudOperation<User, UUID> {

}
