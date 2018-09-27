package com.eurodyn.qlack.fuse.aaa.repository;

import com.eurodyn.qlack.fuse.aaa.model.UserHasOperation;
import java.util.List;

public interface UserHasOperationRepository extends AAARepository<UserHasOperation, String> {

  List<UserHasOperation> findByUserId(String userId);

  UserHasOperation findByUserIdAndOperationName(String userId, String operationName);

  UserHasOperation findByUserIdAndResourceIdAndOperationName(String userId, String resourceId,
      String operationName);

  List<UserHasOperation> findByOperationName(String name);

  List<UserHasOperation> findByResourceIdAndOperationName(String resourceId, String operationName);
}