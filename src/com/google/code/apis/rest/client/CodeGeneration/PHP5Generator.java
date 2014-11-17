/**
 * 
 */
package com.google.code.apis.rest.client.CodeGeneration;

import java.util.Iterator;
import java.util.Vector;

import com.google.code.apis.rest.client.GUI.SettingsDialog;
import com.google.code.apis.rest.client.Tree.MethodItem;
import com.google.code.apis.rest.client.Wadl.Analyzer;
import com.google.gwt.core.client.GWT;

/**
 * @author tsteiner
 *
 */
public class PHP5Generator {
  protected String code = "";
  
  private static Templates_PHP templates_PHP;
      
  public PHP5Generator() {
    templates_PHP = (Templates_PHP) GWT.create(Templates_PHP.class);    
  }
  
  protected String generate(Vector faults) {    
    // the fault handling string
    String errorHandlingString = prepareFaults(faults);
    
    // the REST request superclass
    String restRequestClass = templates_PHP.restRequestClass(errorHandlingString);
    
    // add it to the other generated request classes 
    code = restRequestClass + code;
        
    // add the PHP file template around the classes
    code = templates_PHP.defFile(code);
    
    return code;    
  }

  public void createRequestClass(RequestMessager requestMessager) {    
    Iterator variablesIterator = requestMessager.getVariables().iterator();    
    Vector requiredVariables = new Vector();
    Vector optionalVariables = new Vector();
    while(variablesIterator.hasNext()) {
      Parameter parameter = (Parameter) variablesIterator.next();
      if (parameter.isRequired()) {
        requiredVariables.add(parameter);        
      }
      else {
        optionalVariables.add(parameter);
      }
    }
    
    String requestClass = templates_PHP.defClass(
      requestMessager.getName(),
      prepareClassVars(requestMessager.getVariables()),
      prepareClassParams(requiredVariables, optionalVariables),
      prepareParamAssignments(requestMessager.getVariables()),
      prepareRestParameters(requiredVariables, optionalVariables),
      prepareSubmitRequest(
          requestMessager.getHttpMethodName(),
          requestMessager.getAbsoluteAdress())
    );    
    code += requestClass;
  }  
    
  /**
   * @param faults
   * @return
   */
  protected String prepareFaults(Vector faults) {
    String errorHandling = "";
    if (faults != null) {
      Iterator faultsIterator = faults.iterator();    
      while(faultsIterator.hasNext()) {
        HTTPError fault = (HTTPError) faultsIterator.next();
        errorHandling += "\t\t\tcase " + fault.getStatus() + ":\n\t\t\t\t\ttrigger_error(\"" +
          fault.getMessage() + "\", E_USER_ERROR);\n\t\t\t\t\tbreak; // exit with error code " + fault.getStatus() + "\n";
      }      
    }
    return errorHandling;
  }

  /**
   * @param httpMethodName
   * @param absoluteAddress
   * @return
   */
  protected String prepareSubmitRequest(String httpMethodName, String absoluteAddress) {
    String submitRequest = "";
    
    if (absoluteAddress.matches(Analyzer.templateParamRegExp)) {
      int indexOfOpen = absoluteAddress.indexOf(Analyzer.templateParamOpenChar);
      int indexOfClose = absoluteAddress.indexOf(Analyzer.templateParamCloseChar);        
      while(indexOfOpen >= 0 &&
          indexOfClose > 0 &&
          indexOfOpen < indexOfClose) {          
        String templateParamName = absoluteAddress.substring(indexOfOpen + 1, indexOfClose);        
        String absoluteAddressLeft = absoluteAddress.substring(0, indexOfOpen);
        String absoluteAddressRight = absoluteAddress.substring(indexOfClose + 1);
        absoluteAddress = absoluteAddressLeft + "'.$" + templateParamName + ".'" + absoluteAddressRight;       
        submitRequest += "\t\t$" + templateParamName + " = $this->get" + templateParamName.substring(0, 1).toUpperCase() + templateParamName.substring(1) + SettingsDialog.templateSuffix + "();\n"; 
        
        indexOfOpen = absoluteAddress.indexOf(Analyzer.templateParamOpenChar);
        indexOfClose = absoluteAddress.indexOf(Analyzer.templateParamCloseChar);
      }            
      if (absoluteAddress.endsWith(".'")) {
        absoluteAddress = absoluteAddress.replaceAll("\\.'$", "");        
      }
      else {
        absoluteAddress += "'";
      }
      absoluteAddress = absoluteAddress.replaceAll("\\.''\\.", "\\.");     
    }
    else {
      absoluteAddress += "'";
    }
    
    submitRequest += "\t\t$requestUri = '" + absoluteAddress + ";\n\n";
    
    if (httpMethodName.equals(MethodItem.get)) {
      submitRequest += "\t\t$response = $this->doGetCall($requestUri . '?' . $this->prepareParams());";
    }
    else if (httpMethodName.equals("POST")) {
      submitRequest += "\t\t$response = $this->doPostCall($requestUri, $this->prepareParams());";
    }
    return submitRequest;
  }

  /**
   * @param variables
   * @param optionalVariables 
   * @return
   * 
   * creates the param1=value1&param2=value2&param3=value3 string
   */
  protected String prepareRestParameters(Vector requiredVariables, Vector optionalVariables) {
    String restParamsString = "";
    if (!CodeGenerator.containsNoOrOnlyTemplateParams(requiredVariables, optionalVariables)) {      
      // required variables
      Iterator variablesIterator = requiredVariables.iterator();    
      while(variablesIterator.hasNext()) {
        Parameter parameter = (Parameter) variablesIterator.next();
        if (!parameter.isTemplateParam()) {
          String varName = getVarName(parameter);
          String varName1stLetterUpperCase = varName.substring(0, 1).toUpperCase() + varName.substring(1);
          restParamsString += "\t\t$paramString .= ($this->get" + varName1stLetterUpperCase + "())?\n\t\t\t'&" + varName + "=' . " + "urlencode($this->get" + varName1stLetterUpperCase + "()) :\n\t\t\ttrigger_error(\"The required parameter '" + varName + "' is missing\", E_USER_ERROR);";
          if (variablesIterator.hasNext()) {
            restParamsString += "\n";
          }
        }
      }
      
      if (restParamsString.length() > 0) {
        restParamsString = "\n\t\t// required parameters\n" + restParamsString;
      }
      if (optionalVariables.size() > 0) {
        restParamsString += "\n\t\t// optional parameters\n";
      }
      // optional variables
      variablesIterator = optionalVariables.iterator();    
      while(variablesIterator.hasNext()) {
        Parameter parameter = (Parameter) variablesIterator.next();
        if (!parameter.isTemplateParam()) {
          String varName = getVarName(parameter);
          String varName1stLetterUpperCase = varName.substring(0, 1).toUpperCase() + varName.substring(1);
          restParamsString += "\t\t$paramString .= ($this->get" + varName1stLetterUpperCase + "())?\n\t\t\t'&" + varName + "=' . " + "urlencode($this->get" + varName1stLetterUpperCase + "()) : '';";
          if (variablesIterator.hasNext()) {
            restParamsString += "\n";
          }
        }
      }
    }    
    return restParamsString;
  }

  protected String getVarName(Parameter parameter) {
    String varName = "";
    // strip off a potential $ sign
    if (parameter.getName().startsWith("$")) {
      varName = parameter.getName().substring(1);
    }
    else {
      varName = parameter.getName();
    }
    
    // strip off potential $_private or $_Tprotected markers
    if (varName.startsWith(Parameter.protectedMarker)) {      
      varName = varName.substring(Parameter.protectedMarker.length());        
    }  
    else if (varName.startsWith(Parameter.privateMarker)) {        
      varName = varName.substring(Parameter.privateMarker.length());
    }
    return varName;
  }
  
  /**
   * creates the string (sorted by visibility)
   * public varname // required
   * private _varname // optional
   * 
   */
  protected String prepareClassVars(Vector variables) {   
    variables = sortByVisibility(variables);
    String classVarsString = "";
    
    Iterator variablesIterator = variables.iterator();    
    while(variablesIterator.hasNext()) {
      Parameter parameter = (Parameter) variablesIterator.next();
      classVarsString += "\t" + parameter.getAccess() + " " + parameter.getName() + ";";
      if (parameter.isRequired()) {
        classVarsString += " // required";
      }
      else {
        classVarsString += " // optional";
      }
      if (variablesIterator.hasNext()) {
        classVarsString += "\n";
      }
    }   
    
    return classVarsString;
  }
  
  /**
   * creates the string
   * param1, param2 = 'default', param3, param4 = NULL 
   */
  protected String prepareClassParams(Vector requiredVariables, Vector optionalVariables) {
    String classParamsString = "";
    
    Iterator variablesIterator = requiredVariables.iterator();    
    while(variablesIterator.hasNext()) {
      Parameter parameter = (Parameter) variablesIterator.next();      
      classParamsString += parameter.getName();
      if (!parameter.getDefaultValue().equals("")) {
        classParamsString += " = '" + parameter.getDefaultValue() + "'";
      }
      if (variablesIterator.hasNext()) {
        classParamsString += ", ";
      }
    }   
    
    if ((classParamsString.length() > 0) && (optionalVariables.size() > 0)) {
      classParamsString += ", ";
    }
    variablesIterator = optionalVariables.iterator();
    while(variablesIterator.hasNext()) {
      Parameter parameter = (Parameter) variablesIterator.next();
      classParamsString += parameter.getName();
      if (!parameter.getDefaultValue().equals("")) {
        classParamsString += " = '" + parameter.getDefaultValue() + "'"; 
      }
      else {
        classParamsString += " = NULL";
      }
      if (variablesIterator.hasNext()) {
        classParamsString += ", ";
      }
    }
    
    if (classParamsString.length() >= 70) {
      classParamsString = "\n\t\t\t" + classParamsString.replaceAll(",\\s", ",\n\t\t\t");
    }    
    
    return classParamsString;
  }
  
  /**
   * @param variables
   * @return
   * 
   * creates the string
   * $this->param1 = param1
   * $this->param2 = param2
   */
  protected String prepareParamAssignments(Vector variables) {
    String classParamAssignmentString = "";
    
    Iterator variablesIterator = variables.iterator();
    while(variablesIterator.hasNext()) {
      Parameter parameter = (Parameter) variablesIterator.next();
      String varName = "";
      if (parameter.getName().startsWith("$")) {
        varName = parameter.getName().substring(1);
      }
      else {
        varName = parameter.getName();
      }
      classParamAssignmentString += "\t\t$this->" + varName + " = " + parameter.getName() + ";\n";
    }
    
    return classParamAssignmentString;
  }
  
  /** 
   * @param variables
   * @return
   * 
   * sorts the variables vector by visibility (public < protected < private)
   */
  private Vector sortByVisibility(Vector variables) {
    Vector sortedVars = new Vector();
    
    Iterator variablesIterator = variables.iterator();    
    while(variablesIterator.hasNext()) {
      Parameter parameter = (Parameter) variablesIterator.next();
      if (parameter.getAccess().equals(Parameter.accessPublic)) {        
        sortedVars.insertElementAt(parameter, 0);
      }
      else if (parameter.getAccess().equals(Parameter.accessProtected)) {        
        sortedVars.add(parameter);
      }
    }
    
    variablesIterator = variables.iterator();    
    while(variablesIterator.hasNext()) {
      Parameter parameter = (Parameter) variablesIterator.next();
      if (parameter.getAccess().equals(Parameter.accessPrivate)) {        
        sortedVars.add(parameter);
      }      
    }
    
    return sortedVars;
  }  
}
