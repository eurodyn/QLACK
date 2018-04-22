package com.eurodyn.qlack.fuse.aaa.repository;

import com.eurodyn.qlack.fuse.aaa.model.Session;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface SessionRepository extends CrudRepository<Session, String> {
  @Modifying
  void deleteByCreatedOnBefore(Date expiryDate);
}
