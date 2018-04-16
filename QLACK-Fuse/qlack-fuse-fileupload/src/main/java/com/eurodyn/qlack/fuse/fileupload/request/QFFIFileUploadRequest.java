package com.eurodyn.qlack.fuse.fileupload.request;

public class QFFIFileUploadRequest {
	private long chunkNumber;
	private long totalChunks;
	private long chunkSize;
	private long totalSize;
	private String alias;
	private String filename;
	private byte[] data;
	private String uploadedBy;
	private boolean autoDelete;
	private boolean virusScan;
	
	public boolean isVirusScan() {
		return virusScan;
	}
	public void setVirusScan(boolean virusScan) {
		this.virusScan = virusScan;
	}
	public long getChunkNumber() {
		return chunkNumber;
	}
	public void setChunkNumber(long chunkNumber) {
		this.chunkNumber = chunkNumber;
	}
	public long getTotalChunks() {
		return totalChunks;
	}
	public void setTotalChunks(long totalChunks) {
		this.totalChunks = totalChunks;
	}
	public long getChunkSize() {
		return chunkSize;
	}
	public void setChunkSize(long chunkSize) {
		this.chunkSize = chunkSize;
	}
	public long getTotalSize() {
		return totalSize;
	}
	public void setTotalSize(long totalSize) {
		this.totalSize = totalSize;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
	public String getUploadedBy() {
		return uploadedBy;
	}
	public void setUploadedBy(String uploadedBy) {
		this.uploadedBy = uploadedBy;
	}
	public boolean isAutoDelete() {
		return autoDelete;
	}
	public void setAutoDelete(boolean autoDelete) {
		this.autoDelete = autoDelete;
	}
}
