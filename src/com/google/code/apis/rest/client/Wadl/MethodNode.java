package com.google.code.apis.rest.client.Wadl;

import java.util.Iterator;
import java.util.Vector;

public class MethodNode extends GenericNode {
  private Vector allParams = new Vector();  
  private String name;
  private String id;
  private String href;
  private RequestNode request;
  private ResponseNode response; 
  
  public MethodNode(String methodName, GenericNode node, ApplicationNode application) {
    name = methodName;    
    super.parent = node;
    super.application = application;
  }
  
  public MethodNode(String methodId, boolean isReferenced, GenericNode node, ApplicationNode application) {
    name = null;
    id = null;
    href = "#" + methodId;    
    super.parent = node;
    super.application = application;
  }
  
  public String getName() {
    return name;
  } 
  
  public String getInternalMethodId() {
    String internalMethodId = "";
    if (this.getRequest() != null && this.getRequest().getAllParams() != null) {
      Vector allParams = this.getRequest().getAllParams();
      for (Iterator allParamsIterator = allParams.iterator(); allParamsIterator.hasNext(); /* empty */) {
        ParamNode param = (ParamNode) allParamsIterator.next();
        internalMethodId += param.getName();
      }
    }
    return internalMethodId;
  }
  
  public String getId() {
    return id;
  }  
  
  public void updateId(String oldId, String newId) {
    if (oldId.equals(getId())) {
      id = newId;      
    }
  }
  
  public void updateName(String oldName, String newName) {
    if (getName().equals(oldName)) {
      name = newName;
    }
  }

  public void addParam(ParamNode param) {    
    allParams.add(param);
  }
  
  public Vector getAllParams() {    
    return allParams;
  }

  public void addRequest(RequestNode requestNode) {
    request = requestNode;
  }
  
  public RequestNode getRequest() {
    return request;
  }

  public void addResponse(ResponseNode responseNode) {
    response = responseNode;
  }
  
  public ResponseNode getResponse() {
    return response;
  }

  /**
   * @return
   */
  public String getHref() {    
    return href;
  }

  /**
   * @param methodId
   */
  public void setId(String methodId) {
    id = methodId;    
  }

  /**
   * @param requestNode
   */
  public void removeRequest(RequestNode requestNode) {
    request = null;    
  }
  
  /**
   * @param responseNode
   */
  public void removeResponse(ResponseNode responseNode) {
    response = null;    
  }
}
