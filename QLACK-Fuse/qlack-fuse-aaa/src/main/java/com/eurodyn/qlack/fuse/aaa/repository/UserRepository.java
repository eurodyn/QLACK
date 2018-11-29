package com.eurodyn.qlack.fuse.aaa.repository;

import com.eurodyn.qlack.fuse.aaa.model.AAAModel;
import com.eurodyn.qlack.fuse.aaa.model.User;
import java.util.Set;
import java.util.stream.Collectors;

public interface UserRepository extends AAARepository<User, String> {

    User findByUsername(String username);

    Set<User> findBySuperadminTrue();

    Set<User> findBySuperadminFalse();

    default Set<String> getUserIds(Boolean superadmin) {
        if (superadmin) {
            return findBySuperadminTrue().stream()
                .map(AAAModel::getId)
                .collect(Collectors.toSet());
        } else {
            return findBySuperadminFalse().stream()
                .map(AAAModel::getId)
                .collect(Collectors.toSet());
        }
    }

}
