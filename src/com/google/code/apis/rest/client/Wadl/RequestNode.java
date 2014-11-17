package com.google.code.apis.rest.client.Wadl;

import java.util.Vector;

public class RequestNode extends GenericNode {
  private Vector allParams = new Vector();
  private Vector allRepresentations = new Vector();
  
  public RequestNode(MethodNode method, ApplicationNode application) {
    super.parent = method;
    super.application = application;
  }

  public void addParam(ParamNode param) {    
    if (!allParams.contains(param)) {
      allParams.add(param);
    }
  }
  
  public void insertParamAt(ParamNode param, int index) {
    allParams.insertElementAt(param, index);
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
}
