package com.eurodyn.qlack.fuse.aaa.repository;

import com.eurodyn.qlack.fuse.aaa.model.Operation;

public interface OperationRepository extends AAARepository<Operation, String> {

  Operation findByName(String name);

}
