package com.eurodyn.qlack.fuse.fileupload.response;

import com.eurodyn.qlack.fuse.fileupload.dto.QFFIDBFileDTO;

import java.util.List;

public class QFFIFileListResponse {

  private List<QFFIDBFileDTO> files;

  public QFFIFileListResponse() {

  }

  public QFFIFileListResponse(List<QFFIDBFileDTO> files) {
    this.files = files;
  }

  public List<QFFIDBFileDTO> getFiles() {
    return files;
  }

  public void setFiles(List<QFFIDBFileDTO> files) {
    this.files = files;
  }

}
