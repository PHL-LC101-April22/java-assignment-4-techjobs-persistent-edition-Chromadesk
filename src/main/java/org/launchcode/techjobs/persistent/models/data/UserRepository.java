package org.launchcode.techjobs.persistent.models.data;

import org.launchcode.techjobs.persistent.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {

    User findByName(String name);

}