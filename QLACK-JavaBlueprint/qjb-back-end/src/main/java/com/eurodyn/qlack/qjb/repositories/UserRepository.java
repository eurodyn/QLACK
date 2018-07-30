package com.eurodyn.qlack.qjb.repositories;

import com.eurodyn.qlack.qjb.model.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, String> {

}
