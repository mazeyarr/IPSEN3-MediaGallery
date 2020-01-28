package com.iipsen2.app.modules.Upload.models;

import java.util.Date;

public class ResourceSimple {
  public String resourceUrl;
  public Date createdAt;
  public Date expiresAt;

  public ResourceSimple(String resourceUrl, Date createdAt, Date expiresAt) {
    this.resourceUrl = resourceUrl;
    this.createdAt = createdAt;
    this.expiresAt = expiresAt;
  }

  public ResourceSimple() {
  }

  public String getResourceUrl() {
    return resourceUrl;
  }

  public void setResourceUrl(String resourceUrl) {
    this.resourceUrl = resourceUrl;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public Date getExpiresAt() {
    return expiresAt;
  }

  public void setExpiresAt(Date expiresAt) {
    this.expiresAt = expiresAt;
  }
}
