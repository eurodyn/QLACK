package com.eurodyn.qlack.fuse.fileupload.response;

import com.eurodyn.qlack.fuse.fileupload.dto.DBFileDTO;

import java.util.List;

public class FileListResponse {

  private List<DBFileDTO> files;

  public FileListResponse() {

  }

  public FileListResponse(List<DBFileDTO> files) {
    this.files = files;
  }

  public List<DBFileDTO> getFiles() {
    return files;
  }

  public void setFiles(List<DBFileDTO> files) {
    this.files = files;
  }

}
