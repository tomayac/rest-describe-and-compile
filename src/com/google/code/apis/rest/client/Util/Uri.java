package com.google.code.apis.rest.client.Util;

import com.google.code.apis.rest.client.GUI.GuiFactory;

public class Uri {
  private String scheme = "", authority = "", path = "", query = "", fragment = "";
  
  /*
   *   foo://example.com:8042/over/there?name=ferret#nose
   *   \ /   \______________/\_________/ \_________/ \__/
   *    |           |             |           |        |
   *  scheme    authority       path        query   fragment
   * 
   */
  
  public Uri(String uriString) {   
    resolve(uriString);
  }
  
  private void resolve(String uriString) {
    try {
      // INPUT: http://whatever.com/some/path?some=query#someFragment
      // scheme
      int marker = uriString.indexOf("://");    
      if (marker > 0) {
        scheme = uriString.substring(0, marker);
        uriString = uriString.substring(marker + 3);
      }
      else {
        throw new InvalidUriException(GuiFactory.strings.invalidUri());   
      }
      
      // INPUT: whatever.com/some/path?some=query#someFragment
      // authority
      marker = uriString.indexOf("/");     
      if (marker > 0) {
        // whatever.com/...
        authority = uriString.substring(0, marker);      
      }
      // whatever.com 
      else {
        authority = uriString;
        return;
      }        
      uriString = uriString.substring(marker + 1);
      
      // INPUT: some/path?some=query#someFragment
      // path                  
      marker = uriString.indexOf("?");
      // some/path?...
      if (marker > 0) {  
        path = uriString.substring(0, marker);
        uriString = uriString.substring(marker);
      }
      else if (marker == -1) {
        // some/path#...
        marker = uriString.indexOf("#");
        if (marker > 0) {      
          path = uriString.substring(0, marker);
          uriString = uriString.substring(marker);
        }    
        else if (marker == -1) {
          // some/path/ or some/path
          path = uriString;        
          return;
        }
      }
      
      // INPUT: ?some=query#someFragment
      // query    
      marker = uriString.indexOf("#");    
      if (marker > 0) {
        // ?some=query#...
        query = uriString.substring(1, marker);
        uriString = uriString.substring(marker);
      }
      else if (marker == -1) {
        // ?some=query
        query = uriString.substring(1);
        return;
      }   
      
      // INPUT: #someFragment
      // fragment
      fragment = uriString.substring(1);
    } catch (InvalidUriException e) {      
    }
  }

  public String getScheme() {    
    return scheme;
  }

  public String getAuthority() {
    if (!authority.endsWith("/")) {
      return authority + "/";
    }
    return authority;
  }  
  
  public String getPath() {    
    return path;
  }
  
  public String getQuery() {
    if (query.startsWith("&")) return query.substring(1);
    else return query;
  }  
  
  public String getFragment() {    
    return fragment;
  }  
  
  public String[] getPathComponents() {    
    String[] pathComponents = this.getPath().split("/");
    if (pathComponents[0].length() == 0) {
      String[] newPathComponents = new String[pathComponents.length - 1];
      for (int i = 1; i < pathComponents.length; i++) {
        newPathComponents[i] = pathComponents[i];
      }
      pathComponents = newPathComponents;
    }
    return pathComponents;
  }
  
  public String[] getQueryParameters() {
    String[] queryParameters = this.getQuery().split("&");
    // this deals with the http://www.bla.com/bla?&var=param case ('?' followed immediately by '&')
    if (queryParameters[0].length() == 0) {
      String[] newQueryParameters = new String[queryParameters.length - 1];
      for (int i = 1; i < queryParameters.length; i++) {
        newQueryParameters[i] = queryParameters[i];
      }
      queryParameters = newQueryParameters;
    }
    for (int i = 0; i < queryParameters.length; i++) {      
      if (queryParameters[i].indexOf("=") > 0) {
        queryParameters[i] = queryParameters[i].substring(0, queryParameters[i].indexOf("="));
      }
      else {
        queryParameters[i] = "";
      }
    }
    return queryParameters;
  }
  
  public String[] getQueryValues() {
    String[] queryValues = this.getQuery().split("&");
    if (queryValues[0].length() == 0) {
      String[] newQueryValues = new String[queryValues.length - 1];
      for (int i = 1; i < queryValues.length; i++) {
        newQueryValues[i] = queryValues[i];
      }
      queryValues = newQueryValues;
    }
    for (int i = 0; i < queryValues.length; i++) {
      if (queryValues[i].indexOf("=") >= 0) {
        queryValues[i] = queryValues[i].substring(queryValues[i].indexOf("=") + 1);
      }
      else {        
        queryValues[i] = queryValues[i];
      }
    }
    return queryValues;
  }  
  
  public String toString() {
    String uriString = scheme + "://" + authority + "/";
    if (path.length() > 0) uriString += path;
    if (query.length() > 0) uriString += "?" + query;
    if (fragment.length() > 0) uriString += "#" + fragment;    
    return uriString;
  }

  public String getEndpoint() {
    String endpoint = scheme + "://" + authority;
    String[] pathCompontents = this.getPathComponents();
    for (int i = 0; i < pathCompontents.length - 1; i++) {
      endpoint += "/" + pathCompontents[i];
    }
    return endpoint;
  }

  public String getRequest() {
    String[] pathCompontents = this.getPathComponents();
    if (pathCompontents.length > 0) {
      return pathCompontents[pathCompontents.length - 1];
    }
    else {
      return "";
    }
  }  
}
