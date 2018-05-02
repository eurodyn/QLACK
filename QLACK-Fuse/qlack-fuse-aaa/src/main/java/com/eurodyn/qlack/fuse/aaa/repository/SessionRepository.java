package com.eurodyn.qlack.fuse.aaa.repository;

import com.eurodyn.qlack.fuse.aaa.model.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface SessionRepository extends PagingAndSortingRepository<Session, String> {
  @Modifying
  void deleteByCreatedOnBefore(Date date);
  Page<Session> findByUserId(String userId, Pageable pageable);
  List<Session> findByCreatedOnBeforeAndTerminatedOnNotNull(Date date);
}
