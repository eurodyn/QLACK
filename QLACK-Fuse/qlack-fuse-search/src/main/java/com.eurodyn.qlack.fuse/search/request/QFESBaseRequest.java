package com.eurodyn.qlack.fuse.search.request;

public abstract class QFESBaseRequest {

  private boolean async = false;

  public boolean isAsync() {
    return async;
  }

  public void setAsync(boolean async) {
    this.async = async;
  }
}
