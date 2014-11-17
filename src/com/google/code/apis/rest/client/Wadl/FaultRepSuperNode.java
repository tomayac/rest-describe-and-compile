package com.google.code.apis.rest.client.Wadl;

import java.util.Vector;

public abstract class FaultRepSuperNode extends GenericNode {
  protected String mediaType;
  protected String element;
  protected String profile;
  protected String status;
  protected String id;
  protected String href;
  protected GenericNode parent;
  protected Vector allParams = new Vector();
  
  public FaultRepSuperNode(GenericNode parentNode, ApplicationNode applicationNode) {
    super.parent = parentNode;
    super.application = applicationNode;
  }
  
  public FaultRepSuperNode(GenericNode parentNode, ApplicationNode applicationNode, String faultId) {
    super.parent = parentNode;
    super.application = applicationNode;
    id = null;
    href = "#" + faultId;
    status = "";
  }
  
  public String getMediaType() {
    return mediaType;
  }
  
  /**
   * @return
   */
  public String getHref() {    
    return href;
  }
    
  public String getId() {
    return id;
  }
  
  public void updateId(String oldId, String newId) {
    if (oldId.equals(getId())) {
      id = newId;      
    }
  }
  
  public void setId(String newId) {
    id = newId;
  }
  
  public String getProfile() {
    return profile;
  }
  
  public String getStatus() {
    return status;
  }
  
  public String getElement() {
    return element;
  }
  
  public void updateStatus(String oldStatus, String newStatus) {
    if (getStatus().equals(oldStatus)) {
      status = newStatus;
    }
  }
  
  public void updateElement(String oldElement, String newElement) {
    if (getElement().equals(oldElement)) {
      element = newElement;
    }
  }
  
  public void updateProfile(String oldProfile, String newProfile) {
    if (getProfile().equals(oldProfile)) {
      profile = newProfile;
    }
  }
  
  public void updateMediaType(String oldMediaType, String newMediaType) {
    if (getMediaType().equals(oldMediaType)) {
      mediaType = newMediaType;
    }
  }
  
  public void addParam(ParamNode param) {    
    if (!allParams.contains(param)) {
      allParams.add(param);
    }
   }
     
  public void removeParam(ParamNode param) {
    if (allParams.contains(param)) {
      allParams.remove(param);
    }    
  }
  
  public Vector getAllParams() {    
    return allParams;
  }
}
