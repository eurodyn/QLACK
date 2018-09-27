package com.eurodyn.qlack.fuse.aaa.repository;

import com.eurodyn.qlack.fuse.aaa.model.UserAttribute;

public interface UserAttributeRepository extends AAARepository<UserAttribute, String> {

  UserAttribute findByUserIdAndName(String userId, String name);
}
