package com.google.code.apis.rest.client.Wadl;

import java.util.Iterator;
import java.util.Vector;

import com.google.code.apis.rest.client.Wadl.ParamNode;

public class ResourceNode extends GenericNode {
  private Vector allParams = new Vector();
  private Vector allMethods = new Vector();
  // resource nodes can contain resources
  private Vector allResources = new Vector();
  private String path;
  private String id;
  private String href;
  private String absolutePath = "";
    
  public ResourceNode(String resourcePath, GenericNode parentNode, ApplicationNode application) {
    path = resourcePath;
    super.parent = parentNode;
    super.application = application;
    
    // here we need to take care whether the parent node is a <resource> or <resources> element
    ResourceNode copyOfMyself = this;
    while (copyOfMyself.getParent() instanceof ResourceNode) {      
      absolutePath = ((ResourceNode) copyOfMyself.getParent()).getPath() + "/" + absolutePath;
      copyOfMyself = (ResourceNode) copyOfMyself.getParent();
    }    
    if (copyOfMyself.getParent() instanceof ResourcesNode) {      
      absolutePath =  ((ResourcesNode) copyOfMyself.getParent()).getBase() + absolutePath;
    }
  }
  
  /**
   * @param nodeValue
   * @param isReferenced
   * @param node
   * @param application
   */
  public ResourceNode(String resourceId, boolean isReferenced, GenericNode parentNode, ApplicationNode application) {
    id = null;
    href = "#" + resourceId;
    super.parent = parentNode;
    super.application = application;
  }

  public String getPath() {
    return path;
  }
  
  public void addParam(ParamNode param) {
    Iterator allParamsIterator = allParams.iterator();
    while(allParamsIterator.hasNext()) {
      ParamNode parameter = (ParamNode) allParamsIterator.next();
      if (param.getName().equals(parameter.getName())) {
        // the param to be inserted is already contained
        return;
      }
    }    
    allParams.add(param);
  }
  
  public void updatePath(String oldPath, String newPath) {
    if (getPath().equals(oldPath)) {
      path = newPath;
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

  public Vector getAllMethods() {
    return allMethods;
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
  
  public Vector getAllResources() {
    return allResources;
  }
  
  public boolean alreadyContainesResource(String path) {       
    Iterator allResourcesIterator = allResources.iterator();
    while (allResourcesIterator.hasNext()) {
      ResourceNode resource = (ResourceNode) allResourcesIterator.next();      
      if (resource.getPath().equals(path)) {
        return true;
      }
    }    
    return false;
  }
  
  public boolean addResource(ResourceNode resource) {    
    if (!alreadyContainesResource(resource.getPath())) {      
      allResources.add(resource);      
      return true;
    }
    else {
      return false;
    }
  }
  
  public ResourceNode getResourceByPath(String path) {
    Vector allResources = getAllResources();
    Iterator allResourcesIterator = allResources.iterator();
    while (allResourcesIterator.hasNext()) {
      ResourceNode resource = (ResourceNode) allResourcesIterator.next();      
      if (resource.getPath().equals(path)) {
        return resource;
      }
    }    
    return null;
  }
  
  public void removeResource(ResourceNode resource) {
    if (allResources.contains(resource)) {
      allResources.remove(resource);
    }
  }
  
  public void updateResources(ResourceNode oldResource, ResourceNode newResource) {
    if (allResources.contains(oldResource)) {
      allResources.remove(oldResource);
      allResources.add(newResource);
    }    
  }

  public String getAbsolutePath() {
    return absolutePath;    
  }  

  /**
   * @return
   */
  public String getId() {
    return id;
  }

  /**
   * @return
   */
  public String getHref() {    
    return href;
  }

  /**
   * @param newId
   */
  public void setId(String newId) {
    id = newId;    
  }

  public MethodNode getMethodByInternalId(String internalMethodId) {
    Iterator allMethodsIterator = allMethods.iterator();
    while (allMethodsIterator.hasNext()) {
      MethodNode method = (MethodNode) allMethodsIterator.next();
      if (method.getInternalMethodId().equals(internalMethodId)) {
        return method;
      }
    }
    return null;
  }
}
