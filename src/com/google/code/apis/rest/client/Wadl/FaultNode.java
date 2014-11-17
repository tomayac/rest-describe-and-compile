package com.google.code.apis.rest.client.Wadl;

public class FaultNode extends FaultRepSuperNode {
  
  public FaultNode(String faultMediaType, String faultElement, String faultProfile, String faultStatus, GenericNode parent, ApplicationNode application) {
    super(parent, application);
    super.mediaType = faultMediaType;
    super.element = faultElement;
    super.profile = faultProfile;
    super.status = faultStatus;    
  }
  
  public FaultNode(String faultStatus, GenericNode parent, ApplicationNode application) {
    super(parent, application);
    mediaType = "";
    element = "";
    profile = "";
    status = faultStatus;    
  }
  
  public FaultNode(String id, boolean isReferenced, GenericNode parent, ApplicationNode application) {
    super(parent, application, id);
    mediaType = "";
    element = "";
    profile = "";    
  }
}
