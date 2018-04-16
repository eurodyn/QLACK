package com.eurodyn.qlack.fuse.fileupload.response;

import com.eurodyn.qlack.fuse.fileupload.dto.QFFIDBFileChunkDTO;

public class QFFIChunkGetResponse {

  private QFFIDBFileChunkDTO chunk;

  /**
   * @param chunk
   */
  public QFFIChunkGetResponse(QFFIDBFileChunkDTO chunk) {
    super();
    this.chunk = chunk;
  }

  /**
   * @return the chunk
   */
  public QFFIDBFileChunkDTO getChunk() {
    return chunk;
  }

  /**
   * @param chunk the chunk to set
   */
  public void setChunk(QFFIDBFileChunkDTO chunk) {
    this.chunk = chunk;
  }

  public QFFIChunkGetResponse() {

  }


}
