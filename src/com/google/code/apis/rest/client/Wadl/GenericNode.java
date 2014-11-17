package com.google.code.apis.rest.client.Wadl;

import java.util.Vector;

public class GenericNode {
  private Vector doc = new Vector();
  protected GenericNode parent;
  protected ApplicationNode application;
  
  public void addDoc(DocNode docNode) {
    doc.add(docNode);
  }
  
  public Vector getAllDocs() {
    return doc;
  }
  
  public GenericNode getParent() {
    return parent;
  }
  
  public ApplicationNode getApplication() {
    return application;
  }
}
