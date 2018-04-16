package com.eurodyn.qlack.fuse.fileupload.dto;

public class QFFIDBFileDTO {
	private String id;
	private String uploadedBy;
	private String filename;
	private long uploadedAt;
	private byte[] fileData;
	private long totalChunks;
	private long receivedChunks;
	private long reassemblyTime;
	private long totalSize;

	public long getTotalSize() {
		return totalSize;
	}
	public void setTotalSize(long totalSize) {
		this.totalSize = totalSize;
	}
	public long getReassemblyTime() {
		return reassemblyTime;
	}
	public void setReassemblyTime(long reassemblyTime) {
		this.reassemblyTime = reassemblyTime;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUploadedBy() {
		return uploadedBy;
	}
	public void setUploadedBy(String uploadedBy) {
		this.uploadedBy = uploadedBy;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public long getUploadedAt() {
		return uploadedAt;
	}
	public void setUploadedAt(long uploadedAt) {
		this.uploadedAt = uploadedAt;
	}
	public byte[] getFileData() {
		return fileData;
	}
	public void setFileData(byte[] fileData) {
		this.fileData = fileData;
	}
	public long getTotalChunks() {
		return totalChunks;
	}
	public void setTotalChunks(long totalChunks) {
		this.totalChunks = totalChunks;
	}
	public long getReceivedChunks() {
		return receivedChunks;
	}
	public void setReceivedChunks(long receivedChunks) {
		this.receivedChunks = receivedChunks;
	}



}
