package com.eurodyn.qlack.fuse.fileupload.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FileUploadRequest {
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
}
