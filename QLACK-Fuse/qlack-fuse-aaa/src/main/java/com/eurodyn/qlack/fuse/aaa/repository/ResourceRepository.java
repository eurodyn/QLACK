package com.eurodyn.qlack.fuse.aaa.repository;

import com.eurodyn.qlack.fuse.aaa.model.Resource;

public interface ResourceRepository extends AAARepository<Resource, String> {

  Resource findByObjectId(String objectId);
}
