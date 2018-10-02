package com.eurodyn.qlack.fuse.fileupload.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;
import javax.persistence.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "flu_file")
@Getter
@Setter
@NoArgsConstructor
public class DBFile {

  @EmbeddedId
  private DBFilePK id;
  @Column(name = "uploaded_by")
  private String uploadedBy;
  @Column(name = "file_name")
  private String fileName;
  @Column(name = "uploaded_at")
  private long uploadedAt;
  @Column(name = "file_size")
  private long fileSize;
  @Column(name = "expected_chunks")
  private long expectedChunks;
  @Column(name = "chunk_data")
  @Basic(fetch = FetchType.LAZY)
  private byte[] chunkData;
  @Column(name = "chunk_size")
  private long chunkSize;

  @Version
  private long dbversion;

  public DBFile(DBFilePK id) {
    super();
    this.id = id;
  }

  public DBFilePK getId() {
    return id;
  }
}
