package com.google.code.apis.rest.client.Wadl;

import java.util.Iterator;
import java.util.Vector;

public class ResourcesNode extends GenericNode {
  // each new <resources> node shares the already existing @base attribute
  private static String base;
  // each new <resources> node shares the already existing <resource> nodes
  private static Vector allResources = new Vector();
    
  public ResourcesNode(String resourcesBase, ApplicationNode application) {
    if (!resourcesBase.endsWith("/")) {
      resourcesBase += "/";
    }
    base = resourcesBase;
    allResources.clear();
    super.parent = application;
    super.application = application;
  }
  
  public String getBase() {
    return base;    
  }
   
  public Vector getAllResources() {
    return allResources;
  }
  
  public boolean alreadyContained(String path) {    
    Iterator allResourcesIterator = allResources.iterator();
    while (allResourcesIterator.hasNext()) {
      ResourceNode resource = (ResourceNode) allResourcesIterator.next();      
      if (resource.getPath().equals(path)) {
        return true;
      }
    }    
    return false;
  }
  
  public ResourceNode getResourceByPath(String path) {
    Iterator allResourcesIterator = allResources.iterator();
    while (allResourcesIterator.hasNext()) {
      ResourceNode resource = (ResourceNode) allResourcesIterator.next();      
      if (resource.getPath().equals(path)) {
        return resource;
      }
    }    
    return null;
  }
  
  public boolean addResource(ResourceNode resource) {    
    if (!alreadyContained(resource.getPath())) {      
      allResources.add(resource);      
      return true;
    }
    else {
      return false;
    }
  }
  
  public void removeResource(ResourceNode resource) {
    if (allResources.contains(resource)) {
      allResources.remove(resource);
    }
  }
  
  public void updateBase(String oldBase, String newBase) {
    if (oldBase.equals(base)) {
      base = newBase;
    }
  }
    
  public void updateResources(ResourceNode oldResource, ResourceNode newResource) {
    if (allResources.contains(oldResource)) {
      allResources.remove(oldResource);
      allResources.add(newResource);
    }    
  }  
}
