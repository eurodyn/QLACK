package com.eurodyn.qlack.fuse.fileupload.response;

public class CheckChunkResponse {
	private boolean chunkExists;

	public CheckChunkResponse() {

	}
	public boolean isChunkExists() {
		return chunkExists;
	}

	public void setChunkExists(boolean chunkExists) {
		this.chunkExists = chunkExists;
	}
}
