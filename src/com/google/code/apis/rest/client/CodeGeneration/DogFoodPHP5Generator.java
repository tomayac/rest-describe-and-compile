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
public class DogFoodPHP5Generator extends PHP5Generator {  
  private static Templates_PHP templates_PHP;
  private static Templates_DogFoodPHP templates_DogFoodPHP;
      
  public DogFoodPHP5Generator() {
    templates_PHP = (Templates_PHP) GWT.create(Templates_PHP.class);
    templates_DogFoodPHP = (Templates_DogFoodPHP) GWT.create(Templates_DogFoodPHP.class);
  }
  
  protected String generate(Vector faults) {    
    // the fault handling string
    String errorHandlingString = prepareFaults(faults);
    
    // the REST request superclass
    String restRequestClass = templates_DogFoodPHP.restRequestClass(errorHandlingString);
    
    // add it to the other generated request classes 
    code = restRequestClass + code;    
    
    // add the PHP file template around the classes 
    code = templates_PHP.defFile(code);
    
    // add the xml header and the main document opening and closing tags    
    code = templates_DogFoodPHP.xmlHeader(code);
    
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
    
    String requestClass = templates_DogFoodPHP.defClass(
      requestMessager.getName(),
      prepareClassVars(requestMessager.getVariables()),
      prepareClassParams(requestMessager.getVariables()),
      prepareParamAssignments(requestMessager.getVariables()),
      prepareRestParameters(requestMessager.getVariables()),
      prepareSubmitRequest(
          requestMessager.getHttpMethodName(),
          requestMessager.getAbsoluteAdress())
    );    
    code += requestClass;        
    code += createDogFoodCode(requestMessager);    
  }

  /**
   * @param requestMessager
   * @return
   */
  private String createDogFoodCode(RequestMessager requestMessager) {    
    String dogFoodCode = "\n" + prepareInitializations(requestMessager.getVariables());
    
    String variables = prepareParamList(requestMessager.getVariables());
    dogFoodCode += "\n\n// Valid request from the original sample. This should succeed.\n" +
        "$" + requestMessager.getName() + "Valid = new " + requestMessager.getName() + "("+ variables +");\n";
    dogFoodCode += "$responseValid = $" + requestMessager.getName() + "Valid->submit();\n\n";
    
    dogFoodCode += "\n" + prepareBrokenInitializations(requestMessager.getVariables());
    
    dogFoodCode += "\n// Adds 'abc' to each parameter value. Goal: break any key parameters and/or operation parameters.\n" +
        "$" + requestMessager.getName() + "Erroneous1 = new " + requestMessager.getName() + "("+ variables +");\n";
    dogFoodCode += "$responseErroneous1 = $" + requestMessager.getName() + "Erroneous1->submit();\n\n";
    
    dogFoodCode += "// Call without any parameters. Goal: cause 'missing parameter' errors.\n" +
        "$" + requestMessager.getName() + "Erroneous2 = new " + requestMessager.getName() + "();\n";
    dogFoodCode += "$responseErroneous2 = $" + requestMessager.getName() + "Erroneous2->submit();\n\n";
    
    dogFoodCode += "// Adds an extra parameter '&abc = abc'. Goal: cause 'invalid parameter' errors.\n" +
        "$" + requestMessager.getName() + "Erroneous3 = new " + requestMessager.getName() + "("+ variables +");\n";
    dogFoodCode += "$responseErroneous3 = $" + requestMessager.getName() + "Erroneous3->submit('&abc=abc');\n\n";
    
    dogFoodCode += templates_DogFoodPHP.dogFood(requestMessager.getAbsoluteAdress(), requestMessager.getName());
    return dogFoodCode;
  }

  /**
   * @param variables
   * @return
   */
  private String prepareInitializations(Vector variables) {
    String classParamsString = "";
    Iterator variablesIterator = variables.iterator();
    while (variablesIterator.hasNext()) {
      Parameter param = (Parameter) variablesIterator.next();
      if (param.getSampleValue().length() > 0) {
        classParamsString += param.getName() + " = \"" + param.getSampleValue() + "\";\n";
      }
      else {
        classParamsString += param.getName() + " = \"\";\n";
      }
    }    
    return classParamsString;
  }
  
  /**
   * @param variables
   * @return
   */
  private String prepareBrokenInitializations(Vector variables) {
    String classParamsString = "";
    Iterator variablesIterator = variables.iterator();
    while (variablesIterator.hasNext()) {
      Parameter param = (Parameter) variablesIterator.next();
      if (param.getSampleValue().length() > 0) {
        classParamsString += param.getName() + " = \"" + param.getSampleValue() + " \" + \"abc\"" + ";\n";
      }
      else {
        classParamsString += param.getName() + " = \"abc\";\n";
      }
    }    
    return classParamsString;
  }
  
  /**
   * @param variables
   * @return
   */
  private String prepareParamList(Vector variables) {
    String classParamsString = "";
    Iterator variablesIterator = variables.iterator();
    while (variablesIterator.hasNext()) {
      Parameter param = (Parameter) variablesIterator.next();
      classParamsString += param.getName();
      
      if (variablesIterator.hasNext()) {
        classParamsString += ", ";
      }
    }
    if (classParamsString.length() > 70) {
      classParamsString = "\n\t\t" + classParamsString.replaceAll(",\\s", ",\n\t\t");
    }
    return classParamsString;
  }
  
  /**
   * creates the string
   * param1, param2 = 'default', param3, param4 = NULL 
   */
  private String prepareClassParams(Vector variables) {
    String classParamsString = "";    
    
    Iterator variablesIterator = variables.iterator();
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
      submitRequest += "\t\t$response = $this->doGetCall($requestUri . '?' . $this->prepareParams() . $suffix);";
    }
    else if (httpMethodName.equals("POST")) {
      submitRequest += "\t\t$response = $this->doPostCall($requestUri, $this->prepareParams() . $suffix);";
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
  protected String prepareRestParameters(Vector variables) {
    String restParamsString = "";
    
    Iterator variablesIterator = variables.iterator();    
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
    
    return restParamsString;
  }
 
}
