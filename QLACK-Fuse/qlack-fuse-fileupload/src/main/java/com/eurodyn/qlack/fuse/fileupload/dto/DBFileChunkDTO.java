package com.eurodyn.qlack.fuse.fileupload.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DBFileChunkDTO {

  private String id;
  private byte[] binContent;
  private boolean hasMoreChunks = false;
  private long chunkIndex = 0;
  private String uploadedBy;
  private String filename;
  private long uploadedAt;
  private long totalChunks;
  private long totalSize;

}