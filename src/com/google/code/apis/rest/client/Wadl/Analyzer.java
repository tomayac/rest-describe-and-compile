package com.google.code.apis.rest.client.Wadl;

import java.util.Iterator;
import java.util.Vector;

import com.google.code.apis.rest.client.Tree.ParamItem;
import com.google.code.apis.rest.client.Tree.ReferenceManager;
import com.google.code.apis.rest.client.Util.TypeEstimator;
import com.google.code.apis.rest.client.Util.Uri;
import com.google.code.apis.rest.client.Wadl.ParamNode;
import com.google.gwt.user.client.ui.ListBox;

public class Analyzer {
  public static ApplicationNode application;
  private String requestString = "";
  private String referenceParamSuffix = "Param";  
  public static final String sampleValueHeader = " (Sample Value: ";
  public static final String sampleValueFooter = ")";
  public static final String templateParamOpenChar = "{";
  public static final String templateParamCloseChar = "}";
  public static final String templateParamRegExp = "^.*?(\\" + templateParamOpenChar + "(.+?)\\" + templateParamCloseChar + ")+.*?$";  
    
  /**
   * 
   */
  public Analyzer(String request) {
    this.requestString = request;
    return;
  }
  
  public String getRequestString() {
    return this.requestString; 
  } 

  public void setApplication(ApplicationNode applicationNode) {
    application = applicationNode;    
  }
    
  public ApplicationNode analyze() {
    Uri uri = new Uri(requestString.substring(requestString.indexOf(" ") + 1));
    
    String[] queryParameters = uri.getQueryParameters();
    String[] queryValues = uri.getQueryValues();
    String[] pathComponents = uri.getPathComponents();
        
    // <application>
    setApplication(new ApplicationNode(this));    
    
    // <resources>
    ResourcesNode resources = null;
    String base = uri.getScheme() + "://" + uri.getAuthority();
    if (application.getResources() == null) {
      resources = new ResourcesNode(base, application);
      application.addResources(resources);
    }
    else {
      resources = application.getResources();
      resources.updateBase(resources.getBase(), base);
    }    
    if ((pathComponents == null) || (pathComponents.length == 0)) return application; 
    
    // <resource> child of <resources>
    ResourceNode resource = new ResourceNode(pathComponents[0], resources, application);    
    if (!resources.addResource(resource)) {
      // share the already existing resource 
      resource = resources.getResourceByPath(pathComponents[0]);
    } 
    // check for potential necessary template parameters
    checkForTemplateParams(resource);
    
    // <resource> of other <resource>
    for (int i = 1; i < pathComponents.length; i++) {           
      ResourceNode newResource = new ResourceNode(pathComponents[i], resource, application);     
      if (!resource.addResource(newResource)) {      
        // share the already existing resource
        resource = resource.getResourceByPath(pathComponents[i]);        
      }
      else {
        // the new resource is not yet contained
        resource = newResource;
      }
      // check for potential necessary template parameters
      checkForTemplateParams(resource);
    }
    
    // <method>   
    String internalMethodId = "";
    for(int i = 0; i < queryParameters.length; i++) {
      internalMethodId += queryParameters[i];
    }
    ListBox httpMethodListBox = ReferenceManager.getHttpMethodListBox(requestString);
    String httpMethod = httpMethodListBox.getItemText(httpMethodListBox.getSelectedIndex());    
    MethodNode method = new MethodNode(httpMethod, resource, application);    
    if (!resource.addMethod(method)) {
      // share the already existing method, however reanalyze the request params      
      method = resource.getMethodByInternalId(internalMethodId); 
      method.getRequest().getAllParams().clear();
    }
    // make sure that even when recycling an old method the http method is correct
    method.updateName(method.getName(), httpMethod); 
    
    // <request>
    RequestNode request = new RequestNode(method, application);
    method.addRequest(request);
    
    // <param>
    for(int i = 0; i < queryParameters.length; i++) {
      String name = queryParameters[i];      
      String style = ParamItem.query;
      String[] typeEstimate = TypeEstimator.estimate(queryValues[i], queryParameters[i]);
      String type = typeEstimate[0];
      String estimateQuality = typeEstimate[1];
      boolean isRequired = false;
      boolean isRepeating = false; 
      String fixedValue = "";
      String path = "";
      String defaultValue = "";
      String value = queryValues[i];
      
      ParamNode param = new ParamNode(name, type, estimateQuality, style, isRequired, isRepeating, defaultValue, fixedValue, path, value, request, application);
      ApplicationNode.addPotentiallyToBeSharedParam(param);
      DocNode docNode;
      if (queryValues[i].length() > 0) {
        docNode = new DocNode("en", name + sampleValueHeader + queryValues[i] + sampleValueFooter, "", param);
      }
      else {
        docNode = new DocNode("en", name, "", param);
      }
      param.addDoc(docNode);
      request.addParam(param);      
    }
    if (application.getParamsToBeShared().size() > 0) {
      optimizeByOutsourcing();
    }
    return application;
  }

  /**
   * @return
   */
  private void optimizeByOutsourcing() {
    Vector paramsToBeShared = application.getParamsToBeShared();
    Vector usedIds = new Vector();
    for(Iterator i = paramsToBeShared.iterator(); i.hasNext(); /* empty */) {
      ParamNode param = (ParamNode) i.next();
      String id = param.getName() + referenceParamSuffix;
      if (usedIds.contains(id)) {
        int j = 1;
        while(usedIds.contains(id + j)) {
          // empty
        }
        id = id + j;
      }      
      usedIds.add(id);
      param.setId(id);
      application.addParam(param);
      replaceParamByReferenceParam(application.getResources(), param);
    }
  }

  /**
   * @param param
   */
  private void replaceParamByReferenceParam(GenericNode parent, ParamNode param) {
    Vector allResources = null;
    if (parent instanceof ResourcesNode) {
      allResources = ((ResourcesNode) parent).getAllResources();
    }
    else if (parent instanceof ResourceNode) {
      allResources = ((ResourceNode) parent).getAllResources();
    } 
    for (Iterator i = allResources.iterator(); i.hasNext(); /* empty */) {
      ResourceNode resource = (ResourceNode) i.next();
      if (resource.getAllMethods() != null) {
        Vector allMethods = resource.getAllMethods();
        for (Iterator j = allMethods.iterator(); j.hasNext(); /* empty */) {
          MethodNode method = (MethodNode) j.next();
          if (method.getRequest() != null) {
            RequestNode request = method.getRequest();
            if (request.getAllParams() != null) {
              Vector allParams = request.getAllParams();
              int paramIndex = 0;
              for(Iterator k = ((Vector) allParams.clone()).iterator(); k.hasNext(); /* empty */) {
                ParamNode p = (ParamNode) k.next();
                if (p.getName().equals(param.getName()) &&
                    p.getValue().equals(param.getValue())) {                  
                  request.removeParam(p);                  
                  ParamNode referenceParam = new ParamNode(param.getName(), request, application);
                  referenceParam.setHref("#" + param.getId());
                  request.insertParamAt(referenceParam, paramIndex);                  
                }
                paramIndex++;
              }
            }  
          }        
        }
      }
      replaceParamByReferenceParam(resource, param);
    }
  }
  
  /**
   * @param resource
   */
  private void checkForTemplateParams(ResourceNode resource) {
    // check if we have a template path like /shop/{cartId}/      
    if (resource.getPath().matches(templateParamRegExp)) {     
      String path = resource.getPath();        
      int indexOfOpen = path.indexOf(templateParamOpenChar);
      int indexOfClose = path.indexOf(templateParamCloseChar);        
      while(indexOfOpen >= 0 &&
          indexOfClose > 0 &&
          indexOfOpen < indexOfClose) {          
        String templateParamName = path.substring(indexOfOpen + 1, indexOfClose);          
        ParamNode templateParam = new ParamNode(
            templateParamName,
            TypeEstimator.typeString,            
            ParamItem.template,
            resource,
            application);
        resource.addParam(templateParam);
        path = path.substring(indexOfClose + 1);
        indexOfOpen = path.indexOf(templateParamOpenChar);
        indexOfClose = path.indexOf(templateParamCloseChar);
      }        
    }    
  }

  /**
   * @param request
   */
  public void setRequestString(String request) {
    requestString = request;    
  }
}
