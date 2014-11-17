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
public class RubyGenerator {
  private static Templates_Ruby templates_Ruby;  
  private String code = "";  
  
  public RubyGenerator() {
    templates_Ruby = (Templates_Ruby) GWT.create(Templates_Ruby.class);    
  }
  
  protected String generate(Vector faults) {    
    // the fault handling string
    String errorHandlingString = prepareFaults(faults);

    // the REST request superclass
    String restRequestClass = templates_Ruby.restRequestClass(errorHandlingString);
    
    // add it to the other generated request classes 
    code = restRequestClass + code;
    
    // add the Ruby file template around the classes
    code = templates_Ruby.defFile(code);
    
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
    
    String requestClass = templates_Ruby.defClass(
      requestMessager.getName(),      
      prepareClassParams(requiredVariables, optionalVariables),
      prepareParamAssignments(requestMessager.getVariables()),
      prepareRestParameters(requiredVariables, optionalVariables),
      prepareSubmitRequest(
          requestMessager.getHttpMethodName(),
          requestMessager.getAbsoluteAdress()),
      prepareAttrAccessor(requestMessager.getVariables())    
    );    
    code += requestClass;
  }  
  
  /**
   * @param variables 
   * @return
   */
  private String prepareAttrAccessor(Vector variables) {
   String attrAccessorString = "";
    
    Iterator variablesIterator = variables.iterator();
    while(variablesIterator.hasNext()) {
      Parameter parameter = (Parameter) variablesIterator.next();      
      attrAccessorString += ":" + parameter.getName();
      if (variablesIterator.hasNext()) {
        attrAccessorString += ", ";
      }
    }
    
    return attrAccessorString;
  }

  /**
   * creates the string
   * param1, param2 = 'default', param3, param4 = NULL 
   */
  private String prepareClassParams(Vector requiredVariables, Vector optionalVariables) {
    String classParamsString = "";
    
    Iterator variablesIterator = requiredVariables.iterator();    
    while(variablesIterator.hasNext()) {
      Parameter parameter = (Parameter) variablesIterator.next();      
      classParamsString += parameter.getName();
      if (!parameter.getDefaultValue().equals("")) {
        classParamsString += " = \"" + parameter.getDefaultValue() + "\"";
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
        classParamsString += " = \"" + parameter.getDefaultValue() + "\""; 
      }
      else {
        classParamsString += " = nil";
      }
      if (variablesIterator.hasNext()) {
        classParamsString += ", ";
      }
    }       
    
    return classParamsString;
  }
  
  /**
   * 
   * @param variables
   * @return
   * 
   * creates the string
   * @param1 = param1
   * @param2 = param2
   */
  private String prepareParamAssignments(Vector variables) {
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
      classParamAssignmentString += "\t\t@" + varName + " = " + parameter.getName() + "\n";
    }
    
    return classParamAssignmentString;
  }
  
  /**
   * @param variables
   * @param optionalVariables 
   * @return
   * 
   * creates the param1=value1&param2=value2&param3=value3 string
   */
  private String prepareRestParameters(Vector requiredVariables, Vector optionalVariables) {
    String restParamsString = "";
    if (!CodeGenerator.containsNoOrOnlyTemplateParams(requiredVariables, optionalVariables)) {
      // required variables
      Iterator variablesIterator = requiredVariables.iterator();
      
      while(variablesIterator.hasNext()) {
        Parameter parameter = (Parameter) variablesIterator.next();
        if (!parameter.isTemplateParam()) {
          String varName = parameter.getName();        
          restParamsString += "\t\tparam_string += (self." + varName + ")?\n\t\t\t'&" + varName + "=' + " + "URI.encode(self." + varName + ".to_s) :\n\t\t\traise(\"The required parameter '" + varName + "' is missing.\")";
          if (variablesIterator.hasNext()) {
            restParamsString += "\n";
          }
        }
      }     
      
      if (restParamsString.length() > 0) {
        restParamsString = "\n\t\t# required parameters\n" + restParamsString;
      }
      if (optionalVariables.size() > 0) {
        restParamsString += "\n\t\t# optional parameters\n";
      }
      // optional variables
      variablesIterator = optionalVariables.iterator();    
      while(variablesIterator.hasNext()) {
        Parameter parameter = (Parameter) variablesIterator.next();
        if (!parameter.isTemplateParam()) {
          String varName = parameter.getName();      
          restParamsString += "\t\tparam_string += (self." + varName + ")?\n\t\t\t'&" + varName + "=' + " + "URI.encode(self." + varName + ".to_s) : ''";
          if (variablesIterator.hasNext()) {
            restParamsString += "\n";
          }
        }
      }
    } 
    return restParamsString;
  }
  
  /**
   * @param httpMethodName
   * @param absoluteAddress
   * @return
   */
  private String prepareSubmitRequest(String httpMethodName, String absoluteAddress) {
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
        absoluteAddress = absoluteAddressLeft + "'+" + templateParamName + "+'" + absoluteAddressRight;       
        submitRequest += "\t\t" + templateParamName + " = self." + templateParamName + SettingsDialog.templateSuffix + ".to_s\n"; 
        
        indexOfOpen = absoluteAddress.indexOf(Analyzer.templateParamOpenChar);
        indexOfClose = absoluteAddress.indexOf(Analyzer.templateParamCloseChar);
      }            
      if (absoluteAddress.endsWith("+'")) {
        absoluteAddress = absoluteAddress.replaceAll("\\+'$", "");        
      }
      else {
        absoluteAddress += "'";
      }
      absoluteAddress = absoluteAddress.replaceAll("\\+''\\+", "\\+");     
    }
    else {
      absoluteAddress += "'";
    }
    
    submitRequest += "\t\trequestUri = '" + absoluteAddress + "\n\n";
    
    if (httpMethodName.equals(MethodItem.get)) {
      submitRequest += "\t\tresponse = self.do_get_call(requestUri + '?' + self.prepare_params())";
    }
    else if (httpMethodName.equals("POST")) {
      submitRequest += "\t\tresponse = self.do_post_call(requestUri, self.prepare_params())";
    }
    return submitRequest;
  }
  
  /**
   * @param faults
   * @return
   */
  private String prepareFaults(Vector faults) {
    String errorHandling = "";
    if (faults != null) {
      Iterator faultsIterator = faults.iterator();    
      while(faultsIterator.hasNext()) {
        HTTPError fault = (HTTPError) faultsIterator.next();
        errorHandling += "\t\twhen \"" + fault.getStatus() + "\"\n\t\t\traise (\"" +
          fault.getMessage() + "\")\n\t\t\t# exit with error code " + fault.getStatus() + "\n";
      }      
    }
    return errorHandling;
  }
}
