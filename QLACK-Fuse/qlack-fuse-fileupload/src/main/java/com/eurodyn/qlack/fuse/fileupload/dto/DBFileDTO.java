package com.eurodyn.qlack.fuse.fileupload.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DBFileDTO {

  private String id;
  private String uploadedBy;
  private String filename;
  private long uploadedAt;
  private byte[] fileData;
  private long totalChunks;
  private long receivedChunks;
  private long reassemblyTime;
  private long totalSize;

}
