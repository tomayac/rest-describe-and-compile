/**
 * 
 */
package com.google.code.apis.rest.client.Wadl;

import java.util.Vector;

/**
 * @author tsteiner
 *
 */
public class ResourceTypeNode extends GenericNode {
  private String id;
  private String href;
  private Vector allParams = new Vector();
  private Vector allMethods = new Vector();
  
  /**
   * @param string
   * @param parentNode   
   */
  public ResourceTypeNode(String resourceTypeId, ApplicationNode parentNode) {
    id = resourceTypeId;
    super.parent = parentNode;    
    super.application = parentNode;
  }

  public String getId() {
    return id;
  }
  
  public String getHref() {
    return href;
  }
      
  public boolean addMethod(MethodNode method) {
    if (!ApplicationNode.alreadyContainesMethod(allMethods, method.getInternalMethodId())) {
      allMethods.add(method);
      return true;
    }
    else {
      return false;
    }
  }

  public void directlyAddMethod(MethodNode method) {    
    allMethods.add(method);    
  }
  
  public void removeMethod(MethodNode method) {    
    if (allMethods.contains(method)) {
      allMethods.remove(method);
    }    
  }

  /**
   * @return
   */
  public Vector getAllMethods() {
    return allMethods;
  }

  public void addParam(ParamNode param) {    
    if (!allParams.contains(param)) {
      allParams.add(param);
    }
  }
  
  public Vector getAllParams() {    
    return allParams;
  }

  public void removeParam(ParamNode param) {
    if (allParams.contains(param)) {
      allParams.remove(param);
    }    
  }

  public void updateId(String oldId, String newId) {
    if (oldId.equals(id)) {
      id = newId;
    }
  }
}
