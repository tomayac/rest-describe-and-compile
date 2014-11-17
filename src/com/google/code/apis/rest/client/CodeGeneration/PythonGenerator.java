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
public class PythonGenerator {
  private String code = "";
  
  private static Templates_Python templates_Python;
      
  public PythonGenerator() {
    templates_Python = (Templates_Python) GWT.create(Templates_Python.class);    
  }
  
  protected String generate(Vector faults) {    
    // the fault handling string
    String errorHandlingString = prepareFaults(faults);
    
    // the REST request superclass
    String restRequestClass = templates_Python.restRequestClass(errorHandlingString);
    
    // add it to the other generated request classes 
    code = restRequestClass + code;
        
    // add the PHP file template around the classes
    code = templates_Python.defFile(code);
    
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
    
    String requestClass = templates_Python.defClass(
      requestMessager.getName(),      
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
  private String prepareFaults(Vector faults) {
    String errorHandling = "";
    if (faults != null) {
      Iterator faultsIterator = faults.iterator();
      int i = 0;
      while(faultsIterator.hasNext()) {
        HTTPError fault = (HTTPError) faultsIterator.next();
        String elif = "";
        if(i != 0) elif = "el"; else elif = "";        
        errorHandling += "\t\t\t" + elif + "if e.code == " + fault.getStatus() + ":\n\t\t\t\tsys.exit(\"" + fault.getMessage() + "\")";
        if (faultsIterator.hasNext()) errorHandling += "\n";
        i++;
      }
    }
    return errorHandling;
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
        submitRequest += "\t\t" + templateParamName + " = str(self." + templateParamName + SettingsDialog.templateSuffix + ")\n"; 
        
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
    
    submitRequest += "\t\trequest_uri = '" + absoluteAddress + "\n\n";
    
    if (httpMethodName.equals(MethodItem.get)) {
      submitRequest += "\t\treturn self.do_get_call(request_uri + '?' + self.prepare_params())";
    }
    else if (httpMethodName.equals("POST")) {
      submitRequest += "\t\treturn self.do_post_call(requestUri, self.prepare_params())";
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
  private String prepareRestParameters(Vector requiredVariables, Vector optionalVariables) {
    String restParamsString = "";
    if (!CodeGenerator.containsNoOrOnlyTemplateParams(requiredVariables, optionalVariables)) {
      // required variables
      Iterator variablesIterator = requiredVariables.iterator();    
      while(variablesIterator.hasNext()) {
        Parameter parameter = (Parameter) variablesIterator.next();
        if (!parameter.isTemplateParam()) {
          String varName = parameter.getName();        
          restParamsString += "\t\tif self." + varName + ":\n\t\t\tparams['" + varName + "'] = self." + varName + "\n\t\telse:\n\t\t\tsys.exit(\"The required parameter '" + varName + "' is missing\")";
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
          restParamsString += "\t\tif self." + varName + ":\n\t\t\tparams['" + varName + "'] =  " + "self." + varName;
          if (variablesIterator.hasNext()) {
            restParamsString += "\n";
          }
        }
      }
    }    
    return restParamsString;
  }
  
  /**
   * creates the string
   * param1, param2 = 'default', param3, param4 = None 
   */
  private String prepareClassParams(Vector requiredVariables, Vector optionalVariables) {
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
        classParamsString += " = None";
      }
      if (variablesIterator.hasNext()) {
        classParamsString += ", ";
      }
    }
       
    return classParamsString;
  }
  
  /**
   * @param variables
   * @return
   * 
   * creates the string
   * self.param1 = param1
   * self.param2 = param2
   */
  private String prepareParamAssignments(Vector variables) {
    String classParamAssignmentString = "";
    
    Iterator variablesIterator = variables.iterator();
    while(variablesIterator.hasNext()) {
      Parameter parameter = (Parameter) variablesIterator.next();
      String varName = parameter.getName();      
      classParamAssignmentString += "\t\tself." + varName + " = " + parameter.getName() + "\n";
    }
    
    return classParamAssignmentString;
  }
}