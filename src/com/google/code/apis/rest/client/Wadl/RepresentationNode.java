package com.google.code.apis.rest.client.Wadl;

public class RepresentationNode extends FaultRepSuperNode {
  
  public RepresentationNode(String representationMediaType, String representationElement, String representationProfile, String representationStatus, GenericNode parent, ApplicationNode application) {
    super(parent, application);
    super.mediaType = representationMediaType;
    super.element = representationElement;
    super.profile = representationProfile;
    super.status = representationStatus;    
  }
  
  public RepresentationNode(String representationElement, String mediaType, GenericNode parentNode, ApplicationNode application) {
    super(parentNode, application);
    super.mediaType = mediaType;
    super.element = representationElement;
    super.profile = "";   
    super.status = "";
  }

  public RepresentationNode(String id, boolean isReferenced, GenericNode parentNode, ApplicationNode application) {
    super(parentNode, application, id);
    super.mediaType = "";
    super.element = "";
    super.profile = "";        
  } 
}
