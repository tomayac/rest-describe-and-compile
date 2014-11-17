package com.google.code.apis.rest.client.Wadl;

import java.util.Iterator;
import java.util.Vector;

public class ApplicationNode extends GenericNode {
  // each new <application> node shares the already existing <resources> node
  private static ResourcesNode resources;
  // each new <application> node shares the already existing <grammars> node
  public static GrammarsNode grammars;
  // each new <application> node shares the already existing @extNs attributes
  private static Vector allNamespaces = new Vector();
  // each new <application> node shares the already existing <fault> nodes
  private Vector allFaults = new Vector();
  // each new <application> node shares the already existing <method> nodes
  private static Vector allMethods = new Vector();
  // each new <application> node shares the already existing <method> nodes
  private static Vector allRepresentations = new Vector();
  // each new <application> node shares the already existing <param> nodes
  private static Vector allParams = new Vector();
  // each new <application> node shares the already existing <resource_type> nodes
  private static Vector allResourceTypes = new Vector();
  private static Vector paramsPotentiallyToBeShared = new Vector();
  private static Vector paramsToBeShared = new Vector();
  private Analyzer analyzer;
    
  public ApplicationNode(Analyzer requestAnalyzer) {
    analyzer = requestAnalyzer;
    return;
  }  
  
  public Vector getParamsToBeShared() {
    return paramsToBeShared;
  }
  
  public Analyzer getAnalyzer() {
    return analyzer;
  }
  
  public GrammarsNode getGrammars() {    
    return grammars;
  }
  
  public ResourcesNode getResources() {
    return resources;     
  }
  
  public void addGrammars(GrammarsNode grammarsNode) {
    grammars = grammarsNode;
  }
  
  public void addResources(ResourcesNode resourcesNode) {    
    resources = resourcesNode;    
  }
  
  public void addNamespace(NamespaceAttribute namespace) {
    if (!namespaceAlreadyContained(namespace)) {
      allNamespaces.add(namespace);
    }
  }
  
  public static void addPotentiallyToBeSharedParam(ParamNode param) {
    for(Iterator i = paramsPotentiallyToBeShared.iterator(); i.hasNext(); /* empty */) {
      ParamNode p = (ParamNode) i.next();
      if (p.getName().equals(param.getName()) &&
          p.getValue().equals(param.getValue())) {
        addToBeSharedParam(param);
      }      
    }
    paramsPotentiallyToBeShared.add(param);
  }
  
  /**
   * @param param
   */
  private static void addToBeSharedParam(ParamNode param) {
    for(Iterator i = paramsToBeShared.iterator(); i.hasNext(); /* empty */) {
      ParamNode p = (ParamNode) i.next();
      if (p.getName().equals(param.getName()) &&
          p.getValue().equals(param.getValue())) {
        return;
      }      
    }
    paramsToBeShared.add(param);    
  }

  private boolean namespaceAlreadyContained(NamespaceAttribute namespaceToCheck) {
    Iterator namespaceIterator = allNamespaces.iterator();
    while(namespaceIterator.hasNext()) {
      NamespaceAttribute namespace = (NamespaceAttribute) namespaceIterator.next();
      if (namespace.getValue().equals(namespaceToCheck.getValue())) {
        return true;
      }
    }
    return false;
  }
  
  public void removeNamespace(String namespaceValue) {
    Iterator allNamespacesIterator = allNamespaces.iterator();
    while(allNamespacesIterator.hasNext()) {
      NamespaceAttribute namespace = (NamespaceAttribute) allNamespacesIterator.next();
      if (namespace.getValue().equals(namespaceValue)) {
        allNamespaces.remove(namespace);
        break;
      }
    }
  }
  
  public Vector getAllNamespaces() {
    return allNamespaces;
  }

  public void updateNamespace(String oldNamespace, String newNamespace) {
    Iterator allNamespacesIterator = allNamespaces.iterator();
    while(allNamespacesIterator.hasNext()) {
      NamespaceAttribute namespace = (NamespaceAttribute) allNamespacesIterator.next();
      if (namespace.getValue().equals(oldNamespace)) {
        namespace.setValue(newNamespace);        
        break;
      }
    }
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
  
  public void removeFault(FaultNode fault) {
    if (allFaults.contains(fault)) {
      allFaults.remove(fault);
    }    
  }
  
  public void addFault(FaultNode fault) {
    if (!allFaults.contains(fault)) {
      allFaults.add(fault);
    }
  }

  public Vector getAllFaults() {    
    return allFaults;
  }
  
  public void addRepresentation(RepresentationNode representation) {    
    if (!allRepresentations.contains(representation)) {
      allRepresentations.add(representation);
    }
   }
  
  public Vector getAllRepresentations() {    
    return allRepresentations;
  }

  public void removeRepresentation(RepresentationNode representation) {
    if (allRepresentations.contains(representation)) {
      allRepresentations.remove(representation);
    }    
  }

  /**
   * Resets the current application node
   */
  public void reset() {    
    if (paramsPotentiallyToBeShared != null) {
      paramsPotentiallyToBeShared.clear();
    }
    if (paramsToBeShared != null) {
      paramsToBeShared.clear();
    }    
    if (allResourceTypes != null) {
      allResourceTypes.clear();
    }
    if (allNamespaces != null) {
      allNamespaces.clear();
    }
    if (allMethods != null) {
      allMethods.clear();
    }
    if (allParams != null) {
      allParams.clear();
    }    
    if (allFaults != null) {
      allFaults.clear();
    }
    if (allRepresentations != null) {
      allRepresentations.clear();
    }
    if (resources != null) {
      resources.getAllResources().clear();
      resources.updateBase(resources.getBase(), "");
    }
    if (grammars != null) {
      grammars.getAllIncludes().clear();
    }
  }

  public Vector getAllMethods() {
    return allMethods;
  }
  
  public boolean addMethod(MethodNode method) {
    if (!alreadyContainesMethod(allMethods, method.getInternalMethodId())) {
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
  
  public static boolean alreadyContainesMethod(Vector allMethods, String internalMethodId) {
    if (internalMethodId == null) {
      return false;
    }
    Iterator allMethodsIterator = allMethods.iterator();
    while (allMethodsIterator.hasNext()) {
      MethodNode method = (MethodNode) allMethodsIterator.next();
      if (method.getInternalMethodId() == null) {
        continue;
      }
      if (method.getInternalMethodId().equals(internalMethodId)) {
        return true;
      }
    }
    return false;
  }

  /**
   * @return
   */
  public Vector getAllResourceTypes() {    
    return allResourceTypes;
  }
  
  public void addResourceType(ResourceTypeNode resourceType) {    
    allResourceTypes.add(resourceType);
  }
  
  public void removeResourceType(ResourceTypeNode resourceType) {
    if (allResourceTypes.contains(resourceType)) {
      allResourceTypes.remove(resourceType);
    }
  }

  /**
   * 
   */
  public void removeResources() {
    resources = null;    
  }

  /**
   * 
   */
  public void removeGrammars() {
    grammars = null;    
  }
}
