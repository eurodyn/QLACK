package com.eurodyn.qlack.fuse.fileupload.response;

import com.eurodyn.qlack.fuse.fileupload.dto.QFFIDBFileDTO;

public class QFFIFileGetResponse {

  private QFFIDBFileDTO file;

  public QFFIFileGetResponse() {

  }

  public QFFIFileGetResponse(QFFIDBFileDTO file) {
    this.file = file;
  }

  public QFFIDBFileDTO getFile() {
    return file;
  }

  public void setFile(QFFIDBFileDTO file) {
    this.file = file;
  }

}
