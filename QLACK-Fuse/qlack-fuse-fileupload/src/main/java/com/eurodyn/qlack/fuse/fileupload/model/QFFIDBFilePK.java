package com.eurodyn.qlack.fuse.fileupload.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class QFFIDBFilePK implements Serializable {
	private static final long serialVersionUID = 1L;
	@Column
	private String id;
	@Column(name="chunk_order")
	private long chunkOrder;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public long getChunkOrder() {
		return chunkOrder;
	}
	public void setChunkOrder(long chunkOrder) {
		this.chunkOrder = chunkOrder;
	}

	public QFFIDBFilePK() {

	}
	public QFFIDBFilePK(String id, long chunkOrder) {
		super();
		this.id = id;
		this.chunkOrder = chunkOrder;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (chunkOrder ^ (chunkOrder >>> 32));
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		QFFIDBFilePK other = (QFFIDBFilePK) obj;
		if (chunkOrder != other.chunkOrder)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
