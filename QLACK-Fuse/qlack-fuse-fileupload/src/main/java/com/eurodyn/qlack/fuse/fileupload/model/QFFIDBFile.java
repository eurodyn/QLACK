package com.eurodyn.qlack.fuse.fileupload.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "flu_file")
public class QFFIDBFile {
	@EmbeddedId
	private QFFIDBFilePK id;
	@Column(name = "uploaded_by")
	private String uploadedBy;
	@Column(name = "file_name")
	private String fileName;
	@Column(name = "uploaded_at")
	private long uploadedAt;
	@Column(name = "file_size")
	private long fileSize;
	@Column(name = "expected_chunks")
	private long expectedChunks;
	@Column(name = "chunk_data")
	@Basic(fetch = FetchType.LAZY)
	private byte[] chunkData;
	@Column(name = "chunk_size")
	private long chunkSize;
	
	@Version
	private long dbversion;

	public QFFIDBFile() {

	}

	public QFFIDBFile(QFFIDBFilePK id) {
		super();
		this.id = id;
	}

	public QFFIDBFilePK getId() {
		return id;
	}

	public void setId(QFFIDBFilePK id) {
		this.id = id;
	}

	public long getExpectedChunks() {
		return expectedChunks;
	}

	public void setExpectedChunks(long expectedChunks) {
		this.expectedChunks = expectedChunks;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public String getUploadedBy() {
		return uploadedBy;
	}

	public void setUploadedBy(String uploadedBy) {
		this.uploadedBy = uploadedBy;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public long getUploadedAt() {
		return uploadedAt;
	}

	public void setUploadedAt(long uploadedAt) {
		this.uploadedAt = uploadedAt;
	}

	public byte[] getChunkData() {
		return chunkData;
	}

	public void setChunkData(byte[] chunkData) {
		this.chunkData = chunkData;
	}

	public long getChunkSize() {
		return chunkSize;
	}

	public void setChunkSize(long chunkSize) {
		this.chunkSize = chunkSize;
	}

	public static QFFIDBFile getChunk(String id, long chunkOrder, EntityManager em) {
		return em.find(QFFIDBFile.class, new QFFIDBFilePK(id, chunkOrder));
	}

	public static long delete(String id, EntityManager em) {
		Query q = em.createQuery("delete from QFFIDBFile f where f.id.id = :id")
				.setParameter("id", id);

		return q.executeUpdate();
	}

}
