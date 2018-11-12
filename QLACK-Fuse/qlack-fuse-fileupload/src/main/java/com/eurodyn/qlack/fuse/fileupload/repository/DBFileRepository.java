package com.eurodyn.qlack.fuse.fileupload.repository;

import com.eurodyn.qlack.fuse.fileupload.model.DBFile;
import com.eurodyn.qlack.fuse.fileupload.model.DBFilePK;
import com.eurodyn.qlack.fuse.fileupload.model.QDBFile;
import com.querydsl.core.types.Predicate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.lang.NonNull;

public interface DBFileRepository extends JpaRepository<DBFile, DBFilePK>, QuerydslPredicateExecutor<DBFile> {

  @Override
  @NonNull
  List<DBFile> findAll(@NonNull Predicate predicate);

  @Override
  @NonNull
  List<DBFile> findAll(@NonNull Predicate predicate,@NonNull Sort sort);

  default DBFile getChunk(String id, Long chunkOrder){
    Optional<DBFile> optional = findById(new DBFilePK(id, chunkOrder));

    return optional.orElse(null);
  }

  default long deleteById(String id){
    QDBFile qdbFile = QDBFile.dBFile;
    Predicate predicate = qdbFile.id.id.eq(id);

    return findAll(predicate).stream()
        .peek(this::delete)
        .count();
  }
}