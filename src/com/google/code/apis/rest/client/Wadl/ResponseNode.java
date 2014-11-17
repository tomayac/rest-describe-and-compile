package com.google.code.apis.rest.client.Wadl;

import java.util.Vector;

public class ResponseNode extends GenericNode {
  private Vector allParams = new Vector();
  private Vector allRepresentations = new Vector();
  private Vector allFaults = new Vector();  

  public ResponseNode(MethodNode method, ApplicationNode application) {   
    super.parent = method;
    super.application = application;
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
}
