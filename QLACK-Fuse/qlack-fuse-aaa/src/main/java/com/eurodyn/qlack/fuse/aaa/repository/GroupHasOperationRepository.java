package com.eurodyn.qlack.fuse.aaa.repository;

import com.eurodyn.qlack.fuse.aaa.model.GroupHasOperation;
import java.util.List;

public interface GroupHasOperationRepository extends AAARepository<GroupHasOperation, String> {

  GroupHasOperation findByGroupIdAndOperationName(String groupId, String operationName);

  GroupHasOperation findByGroupIdAndResourceIdAndOperationName(String groupId, String resourceId,
      String operationName);

  List<GroupHasOperation> findByOperationName(String operationName);

  List<GroupHasOperation> findByResourceIdAndOperationName(String resourceId, String operationName);

  List<GroupHasOperation> findByGroupName(String groupName);
}
