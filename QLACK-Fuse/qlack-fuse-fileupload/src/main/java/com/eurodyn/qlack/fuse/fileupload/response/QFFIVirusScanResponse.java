package com.eurodyn.qlack.fuse.fileupload.response;


public class QFFIVirusScanResponse {
	// The ID of the file that was checked.
	private String id;
	// Indication of whether a virus was found or not
	private boolean virusFree;
	// A description of the result of the virus scan.
	private String virusScanDescription;
	
	public QFFIVirusScanResponse() {

	}

	public QFFIVirusScanResponse(String id, boolean virusFree,
			String virusScanDescription) {
		super();
		this.id = id;
		this.virusFree = virusFree;
		this.virusScanDescription = virusScanDescription;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isVirusFree() {
		return virusFree;
	}

	public void setVirusFree(boolean virusFree) {
		this.virusFree = virusFree;
	}

	public String getVirusScanDescription() {
		return virusScanDescription;
	}

	public void setVirusScanDescription(String virusScanDescription) {
		this.virusScanDescription = virusScanDescription;
	}

}
