package com.eurodyn.qlack.fuse.fileupload.request;

public class VirusScanRequest {

  // The ID of the file to check (as present in FLU_FILE.ID).
  private String id;
  // A scan request can optionally provide the address of the ClamAV server to
  // use. If this is left empty, the default value from the service's cfg file
  // will be used (i.e. localhost:3310).
  private String clamAVHost;
  private int clamAVPort;

  public String getClamAVHost() {
    return clamAVHost;
  }

  public void setClamAVHost(String clamAVHost) {
    this.clamAVHost = clamAVHost;
  }

  public int getClamAVPort() {
    return clamAVPort;
  }

  public void setClamAVPort(int clamAVPort) {
    this.clamAVPort = clamAVPort;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public VirusScanRequest(String id) {
    super();
    this.id = id;
  }

  public VirusScanRequest() {
    super();
  }

}
