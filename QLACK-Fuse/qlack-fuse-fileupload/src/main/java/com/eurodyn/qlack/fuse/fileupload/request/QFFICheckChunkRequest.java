package com.eurodyn.qlack.fuse.fileupload.request;

public class QFFICheckChunkRequest {

  private String fileAlias;
  private long chunkNumber;

  public String getFileAlias() {
    return fileAlias;
  }

  public void setFileAlias(String fileAlias) {
    this.fileAlias = fileAlias;
  }

  public long getChunkNumber() {
    return chunkNumber;
  }

  public void setChunkNumber(long chunkNumber) {
    this.chunkNumber = chunkNumber;
  }

  public QFFICheckChunkRequest() {

  }

}
