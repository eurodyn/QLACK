package com.eurodyn.qlack.fuse.aaa.repository;

import com.eurodyn.qlack.fuse.aaa.model.VerificationToken;
import org.springframework.data.jpa.repository.Modifying;

public interface VerificationTokenRepository extends AAARepository<VerificationToken, String> {

  @Modifying
  void deleteByExpiresOnBefore(long expiryDate);
}
