/**
 * 
 */
package com.google.code.apis.rest.client.CodeGeneration;

import java.util.Iterator;
import java.util.Vector;

import com.google.code.apis.rest.client.GUI.RestCompilePanel;
import com.google.code.apis.rest.client.GUI.SettingsDialog;
import com.google.code.apis.rest.client.Util.Uri;
import com.google.code.apis.rest.client.Wadl.Analyzer;
import com.google.code.apis.rest.client.Wadl.ApplicationNode;
import com.google.code.apis.rest.client.Wadl.FaultNode;
import com.google.code.apis.rest.client.Wadl.MethodNode;
import com.google.code.apis.rest.client.Wadl.ParamNode;
import com.google.code.apis.rest.client.Wadl.RepresentationNode;
import com.google.code.apis.rest.client.Wadl.RequestNode;
import com.google.code.apis.rest.client.Wadl.ResourceNode;
import com.google.code.apis.rest.client.Wadl.ResourceTypeNode;
import com.google.code.apis.rest.client.Wadl.ResourcesNode;
import com.google.code.apis.rest.client.Wadl.ResponseNode;

/**
 * @author tsteiner
 *
 */
public class CodeGenerator {
  // available languages
  public static final String php5 = "PHP 5";
  public static final String java = "Java";
  public static final String python = "Python";
  public static final String ruby = "Ruby";
  public static final String csharp = "C#";    
  public static final String dogfood_php5 = "Dog Food PHP 5";
  
  private Vector faults = new Vector();  
  public static Vector usedClassNames = new Vector();  
  public static String language = "";
  private ApplicationNode application;
  private Object generator;
  private Vector variables = new Vector();
  
  /**
   * constructor
   * 
   * @param applicationNode
   */
  public CodeGenerator(ApplicationNode applicationNode) {   
    application = applicationNode;
  }
  
  public String generate(String lang) {
    usedClassNames.clear();
    language = lang;    
    
    if (language.equals(php5)) {
      PHP5Generator phpGenerator = new PHP5Generator();
      generator = phpGenerator;
      runThroughSyntaxTree();
      return decodeEntities(phpGenerator.generate(faults));
    }    
    else if (language.equals(ruby)) {
      RubyGenerator rubyGenerator = new RubyGenerator();
      generator = rubyGenerator;
      runThroughSyntaxTree();
      return decodeEntities(rubyGenerator.generate(faults));
    }
    else if (language.equals(python)) {
      PythonGenerator pythonGenerator = new PythonGenerator();
      generator = pythonGenerator;
      runThroughSyntaxTree();
      return decodeEntities(pythonGenerator.generate(faults));
    }
    else if (language.equals(java)) {
      JavaGenerator javaGenerator = new JavaGenerator(getPackageName());
      generator = javaGenerator;
      runThroughSyntaxTree();
      return decodeEntities(javaGenerator.generate(faults));
    }
    else if (language.equals(dogfood_php5)) {
      DogFoodPHP5Generator dogFoodPhpGenerator = new DogFoodPHP5Generator();
      generator = dogFoodPhpGenerator;
      runThroughSyntaxTree();      
      return decodeEntities(dogFoodPhpGenerator.generate(faults));      
    }    
    return null;
  }
  
  private void createRequestClass(RequestMessager requestMessager) {
    if (language.equals(php5)) {
      ((PHP5Generator) generator).createRequestClass(requestMessager);
    }    
    else if (language.equals(ruby)) {
      ((RubyGenerator) generator).createRequestClass(requestMessager);
    }
    else if (language.equals(python)) {
      ((PythonGenerator) generator).createRequestClass(requestMessager);
    }
    else if (language.equals(java)) {
      ((JavaGenerator) generator).createRequestClass(requestMessager);
    }
    else if (language.equals(dogfood_php5)) {
      ((DogFoodPHP5Generator) generator).createRequestClass(requestMessager);
    }
  }
  
  private String decodeEntities(String code) {
    code = code.replaceAll("&lt;", "<");
    code = code.replaceAll("&gt;", ">");
    code = code.replaceAll("&nbsp;", " ");
    code = code.replaceAll("&apos;", "'");
    code = code.replaceAll("&quot;", "\"");
    code = code.replaceAll("&#123;", "{");
    code = code.replaceAll("&#125;", "}");
    if (SettingsDialog.indentWithSpaces) {
      String spaces = "";
      for (int i = 0; i < SettingsDialog.indentWidth; i++) {
        spaces += " ";
      }
      code = code.replaceAll("\\t", spaces);
    }
    return code;
  }

  /**
   * 
   */
  public void runThroughSyntaxTree() {
    // applicationChildren = {resourcesNode, grammarsNode, resourceTypeNode, methodNode, representationNode, faultNode, docNode}
    
    // resources
    if (application.getResources() != null) {
      treatResources(application.getResources());
    }    
  }
  
  /**
   * @param resources
   */
  private void treatResources(ResourcesNode resources) {    
    // resourcesChildren = {resourceNode, docNode}
    
    // resource
    Vector allResources =  resources.getAllResources();
    if (allResources != null) {
      Iterator resourceIterator = allResources.iterator(); 
      while (resourceIterator.hasNext()) {      
        treatResource((ResourceNode) resourceIterator.next());                  
      }
    }
  }

  /**
   * @param resource
   * @param base 
   */
  private void treatResource(ResourceNode resource) {    
    // resourceChildren = {paramNode, methodNode, resourceNode, docNode};
    
    // method
    Vector allMethods =  resource.getAllMethods();
    if (allMethods != null) {
      Iterator methodIterator = allMethods.iterator();
      while (methodIterator.hasNext()) {            
        treatMethod((MethodNode) methodIterator.next());                        
      }
    }
   
    // resource
    Vector allInnerResources =  resource.getAllResources();
    if (allInnerResources != null) {
      Iterator innerResourceIterator = allInnerResources.iterator(); 
      while (innerResourceIterator.hasNext()) {                     
        treatResource((ResourceNode) innerResourceIterator.next());      
      }
    }    
  }
  
  /**
   * @param method
   */
  private void treatMethod(MethodNode method) {    
    // methodChildren = {requestNode, responseNode, docNode}    
    
    // check whether we have a referenced method
    if (method.getHref() != null) {      
      getElementByIdThenTreat(method, method.getHref().substring(1));
      return;
    }
    
    // response
    if (method.getResponse() != null) {
      treatResponse(method.getResponse());      
    }
    
    // request
    if (method.getRequest() != null) {
      treatRequest(method.getRequest());
    }
    else {
      // adding an empty request node is safe
      method.addRequest(new RequestNode(method, application));
      treatRequest(method.getRequest());
    }    
  }

  /**
   * @param response
   */
  private void treatResponse(ResponseNode response) {    
    // responseChildren = {representationNode, faultNode, paramNode, docNode}    
    
    // fault
    Vector allFaults = response.getAllFaults();
    if (allFaults != null) {
      Iterator faultIterator = allFaults.iterator();
      while (faultIterator.hasNext()) {                    
        treatFault((FaultNode) faultIterator.next());      
      }
    }    
  }
  
  private void getMethodById(Object parent, String id, String httpMethodName, RequestNode request) {
    Vector allResources = null;
    if (parent instanceof ResourcesNode) {
      allResources = ((ResourcesNode) parent).getAllResources();
    }
    else if (parent instanceof ResourceNode) {
      allResources = ((ResourceNode) parent).getAllResources();
    } 
    Iterator allResourcesIterator = allResources.iterator();          
    while(allResourcesIterator.hasNext()) {
      ResourceNode resource = (ResourceNode) allResourcesIterator.next();
      if (resource.getAllMethods() != null) {
        Vector allMethods = resource.getAllMethods();
        Iterator allMethodsIterator = allMethods.iterator();
        while(allMethodsIterator.hasNext()) {
          MethodNode method = (MethodNode) allMethodsIterator.next();
          if (id.equals(method.getHref().substring(1))) {
            ResourceNode resourceNode = ((ResourceNode) method.getParent());
            String name = resourceNode.getPath();
            String absoluteAddress = resourceNode.getAbsolutePath() + resourceNode.getPath();                  
            // check if we have a template parameter
            while (resource.getPath().matches(Analyzer.templateParamRegExp)) {
              // don't confuse: param is from the WADL, parameter is for code generation
              String path = resource.getPath();        
              int indexOfOpen = path.indexOf(Analyzer.templateParamOpenChar);
              int indexOfClose = path.indexOf(Analyzer.templateParamCloseChar);        
              while(indexOfOpen >= 0 &&
                  indexOfClose > 0 &&
                  indexOfOpen < indexOfClose) {          
                String templateParamName = path.substring(indexOfOpen + 1, indexOfClose);
                // from all the potential params find the one that matches the template {name}
                Vector templateParams = resource.getAllParams();
                Iterator templateParamsIterator = templateParams.iterator();          
                ParamNode templateParam = null;
                if (templateParamsIterator.hasNext()) {
                  templateParam = (ParamNode) templateParamsIterator.next();
                }
                while(!templateParam.getName().equals(templateParamName)) {
                  templateParam = (ParamNode) templateParamsIterator.next();
                }
                // now generate the code generation parameter       
                Parameter templateParameter = new Parameter(
                    templateParamName + SettingsDialog.templateSuffix,
                    templateParam.getType(),
                    Parameter.accessPrivate, 
                    templateParam.getIsRequired(),               
                    language,
                    true);
                variables.insertElementAt(templateParameter, 0);                
                
                path = path.substring(indexOfClose + 1);
                indexOfOpen = path.indexOf(Analyzer.templateParamOpenChar);
                indexOfClose = path.indexOf(Analyzer.templateParamCloseChar);          
              }             
              if (resource.getParent().getParent() instanceof ResourceNode) {
                resource = (ResourceNode) resource.getParent().getParent();
              }
              else {
                break;
              }
            }            
            // create the class
            name = getRequestName(name, resourceNode, request);            
            RequestMessager requestMessager = new RequestMessager(name, variables, httpMethodName, absoluteAddress);
            createRequestClass(requestMessager);            
          }
        }
      }
      getMethodById(resource, id, httpMethodName, request);
    }    
  }
  
  public static String cleanName(String name, boolean returnPackageName) {    
    name = name.replaceAll("[^a-zA-Z0-9]", SettingsDialog.separationCharacter);
    name = name.replaceAll("(" + SettingsDialog.separationCharacter + ")(" + SettingsDialog.separationCharacter + ")+" , SettingsDialog.separationCharacter);
    name = name.replaceAll("^(" + SettingsDialog.separationCharacter + ")*", "");
    name = name.replaceAll("(" + SettingsDialog.separationCharacter + ")*$", "");
    if (name.length() > 0) {
      name = name.substring(0, 1).toUpperCase() + name.substring(1);
    }
    if (!returnPackageName) {
      name = name + SettingsDialog.separationCharacter + SettingsDialog.classDefaultSuffix;
    }
    int indexOfSeparationCharacter = name.indexOf(SettingsDialog.separationCharacter);
    while (indexOfSeparationCharacter > 0) {      
      if (name.length() > indexOfSeparationCharacter + 1) {        
        name = name.substring(0, indexOfSeparationCharacter) +
            name.substring(indexOfSeparationCharacter + 1, indexOfSeparationCharacter + 2).toUpperCase() + 
            name.substring(indexOfSeparationCharacter + 2);
      }
      else {
        name = name.replaceAll("(" + SettingsDialog.separationCharacter + ")*$", "");
      }
      indexOfSeparationCharacter = name.indexOf(SettingsDialog.separationCharacter, indexOfSeparationCharacter + 1);
    }    
    if (SettingsDialog.useSeparationCharacter) {
      String copyOfName = name.substring(0, 1);
      for (int i = 1; i < name.length(); i++) {        
        if (Character.isUpperCase(name.charAt(i))) {
          copyOfName += SettingsDialog.separationCharacter;
        }        
        copyOfName += name.substring(i, i + 1);
      }
      name = copyOfName;
    }
    return name;
  }
  
  /**
   * @param name
   * @param resourceNode 
   * @return
   */
  private String getRequestName(String name, ResourceNode resourceNode, RequestNode request) {
    // use the endpoint as name
    if (SettingsDialog.useEndpointAsName) {      
      int namingDepthLevel = SettingsDialog.namingDepthLevel;
      for (int i = 1; i < namingDepthLevel; i++) {
        if (resourceNode.getParent() instanceof ResourceNode) {
          // use the parent resource path as a prefix
          name = ((ResourceNode) resourceNode.getParent()).getPath() +
              name.substring(0, 1).toUpperCase() + name.substring(1);
          resourceNode = (ResourceNode) resourceNode.getParent();
        }      
        else if (resourceNode.getParent() instanceof ResourcesNode) {
          // else use the parent resources authority with special characters stripped off
          //name = getPackageBasedName() +
          //    name.substring(0, 1).toUpperCase() + name.substring(1);
          break;
        }     
      }      
      // in case of where the name is already in use, add an integer to it
      if (usedClassNames.contains(name)) {
        int i = 1;
        while(usedClassNames.contains(name + i)) {
          // empty
        }
        name = name + i;
      }
    }
    // use a operation parameter value as name
    else {
      Vector parameters = request.getAllParams();
      Iterator parametersIterator = parameters.iterator();
      while(parametersIterator.hasNext()) {
        ParamNode param = (ParamNode) parametersIterator.next();        
        if (param.getName().equalsIgnoreCase(RestCompilePanel.paramToUseForName)) {
          name = param.getValue();
          break;
        }        
      }            
    }
    name = cleanName(name, false);
    usedClassNames.add(name);
    return name;
  }
  
  /**
   * Returns a name based on the inversed resources base
   * with capitalized letters
   * id est "http://www.service.api.com/" becomes "ServiceApiCom"
   * 
   * @return the package name
   */
  private String getPackageBasedName() {
    String packageName = application.getResources().getBase();
    Uri packageNameUrl = new Uri(packageName);
    packageName = packageNameUrl.getAuthority();
    packageName = packageName.replaceAll("http(s)?://(www)?", "");
    packageName = packageName.replaceAll("[/]", "");
    String[] packageNameComponents = packageName.split("\\.");
    packageName = "";
    for (int i = 0; i < packageNameComponents.length; i++) {
      packageNameComponents[i] = packageNameComponents[i];
      packageName += packageNameComponents[i].substring(0, 1).toUpperCase() + packageNameComponents[i].substring(1);      
    }
    return packageName;      
  }
  
  /**
   * Returns the package name based on the inversed resources base
   * id est "http://www.service.api.com/" becomes "com.api.service"
   * 
   * @return the package name
   */
  private String getPackageName() {
    String packageName = application.getResources().getBase();
    Uri packageNameUrl = new Uri(packageName);
    packageName = packageNameUrl.getAuthority();
    packageName = packageName.replaceAll("http(s)?://(www)?", "");
    packageName = packageName.replaceAll("[/]", "");
    String[] packageNameComponents = packageName.split("\\.");
    packageName = "";
    for (int i = packageNameComponents.length -1 ; i >= 0 ; i--) {
      packageName += cleanName(packageNameComponents[i], true);
      if (i > 0) packageName += ".";
    }
    return packageName.toLowerCase();      
  }

  private void getElementByIdThenTreat(Object referenceNode, String href) {    
    Vector allObjects = null;
    if (referenceNode instanceof MethodNode) {
      allObjects = application.getAllMethods();
    }
    else if (referenceNode instanceof FaultNode) {
      allObjects = application.getAllFaults();
    }
    else if (referenceNode instanceof ParamNode) {
      allObjects = application.getAllParams();
    }
    else if (referenceNode instanceof RepresentationNode) {
      allObjects = application.getAllRepresentations();
    }    
    if (allObjects != null) {
      Iterator allObjectsIterator = allObjects.iterator();
      while (allObjectsIterator.hasNext()) {
        if (referenceNode instanceof MethodNode) {
          MethodNode referencedMethod = (MethodNode) allObjectsIterator.next();
          if (referencedMethod.getId().equals(href)) {
            treatMethod(referencedMethod);
            return;
          }
        }
        else if (referenceNode instanceof FaultNode) {
          FaultNode referencedFault = (FaultNode) allObjectsIterator.next();
          if (referencedFault.getId().equals(href)) {
            treatFault(referencedFault);
            return;
          }
        }
        else if (referenceNode instanceof ParamNode) {
          ParamNode referencedParam = (ParamNode) allObjectsIterator.next();          
          if (referencedParam.getId().equals(href)) {
            treatParam(referencedParam);            
            return;
          }
        }
        else if (referenceNode instanceof RepresentationNode) {
          RepresentationNode referencedRepresentation = (RepresentationNode) allObjectsIterator.next();
          if (referencedRepresentation.getId().equals(href)) {
            treatRepresentation(referencedRepresentation);
            return;
          }
        }        
      }
    }
  }
  
  /**
   * @param referencedRepresentation
   */
  private void treatRepresentation(RepresentationNode referencedRepresentation) {
    // TODO Auto-generated method stub
    
  }

  /**
   * @param fault
   * @return
   */
  private void treatFault(FaultNode fault) {
    // faultChildren = {paramNode, docNode}
  
    if (fault.getHref() != null) {
      getElementByIdThenTreat(fault, fault.getHref().substring(1));
      return;
    }
    
    HTTPError httpError = new HTTPError(fault.getStatus());
    boolean alreadyContained = false;
    Iterator faultsIterator = faults.iterator();
    while(faultsIterator.hasNext()) {
      if (((HTTPError) faultsIterator.next()).getStatus().equals(httpError.getStatus())) {
        alreadyContained = true;
        break;
      }
    }
    if (!alreadyContained) {
      faults.add(httpError);
    }
  }

  /**
   * @param request
   * @param faults 
   */
  private void treatRequest(RequestNode request) {    
    // requestChildren = {representationNode, paramNode, docNode}
    
    // param
    variables.clear();
    if (request.getAllParams() != null) {
      Vector allParams = request.getAllParams();      
      Iterator paramIterator = allParams.iterator();
      while (paramIterator.hasNext()) {
        ParamNode p = (ParamNode) paramIterator.next();      
        treatParam(p);        
      }
    }   
    
    // get the http method name and the request name
    MethodNode thisRequestsMethod = ((MethodNode) request.getParent());
    String httpMethodName = thisRequestsMethod.getName();   
    String absoluteAddress = null;
    String name = null;
    Object thisRequestsMethodsParent = (Object) thisRequestsMethod.getParent();
    if (thisRequestsMethodsParent instanceof ResourceNode) {
      ResourceNode resource = (ResourceNode) thisRequestsMethodsParent;
      name = resource.getPath();
      absoluteAddress = resource.getAbsolutePath() + resource.getPath();      
      // check if we have template parameters
      while (resource.getPath().matches(Analyzer.templateParamRegExp)) {
        // don't confuse: param is from the WADL, parameter is for code generation
        String path = resource.getPath();        
        int indexOfOpen = path.indexOf(Analyzer.templateParamOpenChar);
        int indexOfClose = path.indexOf(Analyzer.templateParamCloseChar);        
        while(indexOfOpen >= 0 &&
            indexOfClose > 0 &&
            indexOfOpen < indexOfClose) {          
          String templateParamName = path.substring(indexOfOpen + 1, indexOfClose);
          // from all the potential params find the one that matches the template {name}
          Vector templateParams = resource.getAllParams();
          Iterator templateParamsIterator = templateParams.iterator();          
          ParamNode templateParam = null;
          if (templateParamsIterator.hasNext()) {
            templateParam = (ParamNode) templateParamsIterator.next();
          }
          while(!templateParam.getName().equals(templateParamName)) {
            templateParam = (ParamNode) templateParamsIterator.next();
          }
          // now generate the code generation parameter       
          Parameter templateParameter = new Parameter(
              templateParamName + SettingsDialog.templateSuffix,
              templateParam.getType(),
              Parameter.accessPrivate, 
              templateParam.getIsRequired(),               
              language,
              true);
          variables.insertElementAt(templateParameter, 0);
          
          path = path.substring(indexOfClose + 1);
          indexOfOpen = path.indexOf(Analyzer.templateParamOpenChar);
          indexOfClose = path.indexOf(Analyzer.templateParamCloseChar);          
        } 
        if (resource.getParent().getParent() instanceof ResourceNode) {
          resource = (ResourceNode) resource.getParent().getParent();
        }
        else {
          break;
        }
      }            
    }
    else if (thisRequestsMethodsParent instanceof ResourceTypeNode) {
      // TODO
    }
    else if (thisRequestsMethodsParent instanceof ApplicationNode) {
      // find the reference method of this referenced method
      getMethodById(
          application.getResources(),
          thisRequestsMethod.getId(),          
          httpMethodName,
          request);
      return;
    }
    
    // create the class
    name = getRequestName(name, ((ResourceNode) thisRequestsMethodsParent), request);    
    RequestMessager requestMessager = new RequestMessager(name, variables, httpMethodName, absoluteAddress);
    createRequestClass(requestMessager);    
  }

  private void treatParam(ParamNode param) {
    if (param.getHref() != null) {      
      getElementByIdThenTreat(param, param.getHref().substring(1));
      return;
    }    
    String paramName = param.getName();    
    String sampleValue = param.getValue();    
    variables.add(new Parameter(
        paramName, 
        param.getType(), 
        Parameter.accessPrivate,
        param.getIsRequired(), 
        param.getDefaultValue(), 
        language, 
        sampleValue));         
  }

  /**
   * @param requiredVariables
   * @param optionalVariables
   * @return
   */
  public static boolean containsNoOrOnlyTemplateParams(Vector requiredVariables, Vector optionalVariables) {
    // both empty
    if (requiredVariables.isEmpty() && optionalVariables.isEmpty()) {
      return true;
    }
    // one or both contain params
    // check if only template params, or also others
    else {      
      int templateParams = 0;
      int otherParams = 0;
      Iterator requiredVariablesIterator = requiredVariables.iterator();
      while(requiredVariablesIterator.hasNext()) {
        Parameter param = (Parameter) requiredVariablesIterator.next();
        if (param.isTemplateParam()) {
          templateParams++;
        }
        else {
          otherParams++;
        }
      }
      Iterator optionalVariablesIterator = optionalVariables.iterator();
      while(optionalVariablesIterator.hasNext()) {
        Parameter param = (Parameter) optionalVariablesIterator.next();
        if (param.isTemplateParam()) {
          templateParams++;
        }
        else {
          otherParams++;
        }
      }
      if (otherParams == 0 && templateParams > 0) {
        return true;
      }
    }    
    return false;
  }
}
