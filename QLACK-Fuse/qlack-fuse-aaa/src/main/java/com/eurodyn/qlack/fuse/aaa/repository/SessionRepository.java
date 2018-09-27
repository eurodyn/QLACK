package com.eurodyn.qlack.fuse.aaa.repository;

import com.eurodyn.qlack.fuse.aaa.model.Session;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

public interface SessionRepository extends AAARepository<Session, String> {

  @Modifying
  void deleteByCreatedOnBefore(long date);

  Page<Session> findByUserId(String userId, Pageable pageable);

  List<Session> findByCreatedOnBeforeAndTerminatedOnNull(long date);
}
