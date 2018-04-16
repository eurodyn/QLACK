package com.eurodyn.qlack.fuse.fileupload.response;

public class QFFICheckChunkResponse {
	private boolean chunkExists;

	public QFFICheckChunkResponse() {

	}
	public boolean isChunkExists() {
		return chunkExists;
	}

	public void setChunkExists(boolean chunkExists) {
		this.chunkExists = chunkExists;
	}
}
