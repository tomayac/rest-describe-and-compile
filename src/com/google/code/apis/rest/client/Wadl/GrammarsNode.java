package com.google.code.apis.rest.client.Wadl;

import java.util.Vector;

public class GrammarsNode extends GenericNode {
  // each new <grammars> node shares the already existing <include> nodes
  private static Vector allIncludes = new Vector();
  
  public GrammarsNode(ApplicationNode application) {
    allIncludes.clear();
    super.parent = application;   
    super.application = application;
  }
  
  public void addInclude(String href) {
    if (!allIncludes.contains(href)) {
      allIncludes.add(href);
    }
  }  
  
  public void removeInclude(String href) {
    if (allIncludes.contains(href)) {
      allIncludes.remove(href);
    }
  }  
  
  public Vector getAllIncludes() {
    return allIncludes;
  }

  public void updateInclude(String oldInclude, String newInclude) {    
    if (allIncludes.contains(oldInclude)) {
      allIncludes.remove(oldInclude);
      allIncludes.add(newInclude);
    }
  }
}
