package com.eurodyn.qlack.fuse.fileupload.response;

import com.eurodyn.qlack.fuse.fileupload.dto.DBFileDTO;

public class FileGetResponse {

  private DBFileDTO file;

  public FileGetResponse() {

  }

  public FileGetResponse(DBFileDTO file) {
    this.file = file;
  }

  public DBFileDTO getFile() {
    return file;
  }

  public void setFile(DBFileDTO file) {
    this.file = file;
  }

}
