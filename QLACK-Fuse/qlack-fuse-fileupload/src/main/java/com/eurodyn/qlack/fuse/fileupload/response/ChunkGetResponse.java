package com.eurodyn.qlack.fuse.fileupload.response;

import com.eurodyn.qlack.fuse.fileupload.dto.DBFileChunkDTO;

public class ChunkGetResponse {

  private DBFileChunkDTO chunk;

  /**
   * @param chunk
   */
  public ChunkGetResponse(DBFileChunkDTO chunk) {
    super();
    this.chunk = chunk;
  }

  /**
   * @return the chunk
   */
  public DBFileChunkDTO getChunk() {
    return chunk;
  }

  /**
   * @param chunk the chunk to set
   */
  public void setChunk(DBFileChunkDTO chunk) {
    this.chunk = chunk;
  }

  public ChunkGetResponse() {

  }


}
