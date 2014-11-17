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
public class JavaGenerator {
  private static Templates_Java templates_Java;  
  private String code = "";  
  private String packageName = "";
  
  public JavaGenerator(String packageName) {
    this.packageName = packageName;
    templates_Java = (Templates_Java) GWT.create(Templates_Java.class);    
  }
  
  protected String generate(Vector faults) {
    
    
    // the fault handling string
    String errorHandlingString = prepareFaults(faults);
            
    // the REST request superclass
    String restRequestClass = templates_Java.restRequestClass(        
        errorHandlingString);    
    
    // add it to the other generated request classes 
    code = restRequestClass + code;
    
    // add the Java file template around the classes
    code = templates_Java.defFile(code, packageName);
    
    return code;
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
        errorHandling += "\t\t\tcase " + fault.getStatus() + ":\n\t\t\t\t\tSystem.err.println(\"" +
          fault.getMessage() + "\");\n\t\t\t\t\tbreak; // exit with error code " + fault.getStatus() + "\n";
      }      
    }
    return errorHandling;
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
    
    String requestClass = templates_Java.defClass(
      requestMessager.getName(),      
      prepareRestParameters(requiredVariables, optionalVariables),
      prepareClassVars(requestMessager.getVariables()),
      prepareSubmitRequest(
          requestMessager.getHttpMethodName(),
          requestMessager.getAbsoluteAdress()),      
      prepareConstructors(
          requestMessager.getName(),          
          requiredVariables,
          optionalVariables),
      prepareGettersAndSetters(requestMessager.getVariables(), requestMessager.getName()));
    code += templates_Java.defFile(requestClass, this.packageName);
  }

  /**
   * @param variables
   * @param className
   * @return
   */
  private String prepareGettersAndSetters(Vector variables, String className) {
    String gettersAndSetters = "";
    
    Iterator variableIterator = variables.iterator();
    while(variableIterator.hasNext()) {
      Parameter parameter = (Parameter) variableIterator.next();
      String suffix =
        parameter.getName().substring(0, 1).toUpperCase() +
        parameter.getName().substring(1);
      gettersAndSetters += "\n" +
        "\t/**\n" +
        "\t * @return the String representation of the " + parameter.getName() + "\n" +
        "\t */" + "\n" +
        "\tpublic " + TypeMapper.javaString + " get" + suffix + "() {" + "\n" +
        "\t\tif ((" + parameter.getName() + " + \"\").equals(\"null\")) return \"\";" + "\n" +
        "\t\treturn ((Object) " + parameter.getName() + ").toString();" + "\n" +
        "\t}" + "\n\n" +  
        
        "\t/**" + "\n" +
        "\t * @param " + parameter.getName() + " the " + parameter.getName() + " to set" + "\n" +
        "\t * @return the instance of the class (this) \n" +
        "\t */" + "\n" +
        "\tpublic " + className + " set" + suffix + "(" + parameter.getType() + " " + parameter.getName() + ") {" + "\n" +
        "\t\tthis." + parameter.getName() + " = " + parameter.getName() + ";" + "\n" +
        "\t\treturn this;\n" +
        "\t}\n";
    }
    return gettersAndSetters;
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
  
  /**
   * creates the string (sorted by visibility)
   * public varname // required
   * private _varname // optional
   * 
   */
  private String prepareClassVars(Vector variables) {   
    variables = sortByVisibility(variables);
    String classVarsString = "";
    
    Iterator variablesIterator = variables.iterator();    
    while(variablesIterator.hasNext()) {
      Parameter parameter = (Parameter) variablesIterator.next();     
      classVarsString += "\t" + parameter.getAccess() + " " + parameter.getType() + " " + parameter.getName() + ";";
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
   * @param className
   * @param requiredVariables
   * @param optionalVariables
   * @return
   */
  private String prepareConstructors(String className, Vector requiredVariables, Vector optionalVariables) {
    String constructors = "";
    
    for (int i = 0; i <= optionalVariables.size(); i++) {
      Vector constructorVariables = new Vector();
      
      Iterator requiredVariablesIterator = requiredVariables.iterator();
      while(requiredVariablesIterator.hasNext()) {
        constructorVariables.add(requiredVariablesIterator.next());
      }      
      
      for(int j = 0; j < i; j++) {
        constructorVariables.add(optionalVariables.elementAt(j));
      }
      //com.google.gwt.user.client.Window.alert(constructorVariables.size() + "");
       
      String constructor = templates_Java.defConstructor(
          className,
          prepareClassParams(constructorVariables),
          prepareParamAssignments(constructorVariables),
          prepareParamJavaDoc(constructorVariables),
          constructorVariables.size() + "");  
      constructors += constructor;
    }
    return constructors;
  }

  /**
   * @param constructorVariables
   * @return
   */
  private String prepareParamJavaDoc(Vector variables) {
    String paramJavaDocString = "";
    
    Iterator variablesIterator = variables.iterator();
    while(variablesIterator.hasNext()) {
      Parameter parameter = (Parameter) variablesIterator.next();
      String varName = "";      
      varName = parameter.getName();      
      paramJavaDocString += "\t&nbsp;* @param " + varName + "\n";
    }
    
    return paramJavaDocString;
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
        absoluteAddress = absoluteAddressLeft + "\" + " + templateParamName + " + \"" + absoluteAddressRight;       
        submitRequest += "\t\tString " + templateParamName + " = this.get" + templateParamName.substring(0, 1).toUpperCase() + templateParamName.substring(1) + SettingsDialog.templateSuffix + "();\n"; 
        
        indexOfOpen = absoluteAddress.indexOf(Analyzer.templateParamOpenChar);
        indexOfClose = absoluteAddress.indexOf(Analyzer.templateParamCloseChar);
      }            
      if (absoluteAddress.endsWith(" + \"")) {
        absoluteAddress = absoluteAddress.replaceAll("\\s\\+\\s\"$", "");        
      }
      else {
        absoluteAddress += "\"";
      }
      absoluteAddress = absoluteAddress.replaceAll("\\+\\s\"\"\\s\\+", "\\s\\+\\s");     
    }
    else {
      absoluteAddress += "\"";
    }    
    
    submitRequest += "\t\tString requestUri = \"" + absoluteAddress + ";\n";
    
    if (httpMethodName.equals(MethodItem.get)) {
      submitRequest += "\t\tString response = this.doGetCall(requestUri + \"?\" + this.prepareParams());";
    }
    else if (httpMethodName.equals("POST")) {
      submitRequest += "\t\tString response = this.doPostCall(requestUri, this.prepareParams());";
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
    if (CodeGenerator.containsNoOrOnlyTemplateParams(requiredVariables, optionalVariables)) {
      restParamsString = "\t\t\tURLEncoder.encode(\"\", \"UTF-8\");";
    }
    else {
      // required variables
      Iterator variablesIterator = requiredVariables.iterator();    
      while(variablesIterator.hasNext()) {
        Parameter parameter = (Parameter) variablesIterator.next();
        String varName = parameter.getName();
        if (!parameter.isTemplateParam()) {  
          String varName1stLetterUpperCase = varName.substring(0, 1).toUpperCase() + varName.substring(1);
          restParamsString += "\t\t\tif (!this.get" + varName1stLetterUpperCase + "().equals(\"\")) \n\t\t\t\tparamString += \"&" + varName + "=\" + " + "URLEncoder.encode(this.get" + varName1stLetterUpperCase + "(), \"UTF-8\"); \n\t\t\telse \n\t\t\t\tSystem.err.println(\"The required parameter '" + varName + "' is missing\");";
          if (variablesIterator.hasNext()) {
            restParamsString += "\n";
          }
        }
      }
      
      if (restParamsString.length() > 0) {
        restParamsString = "\n\t\t\t// required parameters\n" + restParamsString;
      }
      if (optionalVariables.size() > 0) {
        restParamsString += "\n\t\t\t// optional parameters\n";
      }
      // optional variables
      variablesIterator = optionalVariables.iterator();    
      while(variablesIterator.hasNext()) {
        Parameter parameter = (Parameter) variablesIterator.next();
        if (!parameter.isTemplateParam()) {
          String varName = parameter.getName();
          String varName1stLetterUpperCase = varName.substring(0, 1).toUpperCase() + varName.substring(1);
          restParamsString += "\t\t\tparamString += (!this.get" + varName1stLetterUpperCase + "().equals(\"\"))?\n\t\t\t\t\"&" + varName + "=\" + " + "URLEncoder.encode(this.get" + varName1stLetterUpperCase + "(), \"UTF-8\") : \"\";";
          if (variablesIterator.hasNext()) {
            restParamsString += "\n";
          }
        }
      }    
    }
    return restParamsString;
  }

  /**
   * @param variables
   * @return
   * 
   * creates the string
   * this.param1 = param1
   * this.param2 = param2
   */
  private String prepareParamAssignments(Vector variables) {
    String classParamAssignmentString = "";
    
    Iterator variablesIterator = variables.iterator();
    while(variablesIterator.hasNext()) {
      Parameter parameter = (Parameter) variablesIterator.next();
      String varName = parameter.getName();      
      classParamAssignmentString += "\t\tthis." + varName + " = " + varName + ";\n";
    }
    if (variables.size() > 0) {
      classParamAssignmentString = "\t\t// assign class variables \n" + classParamAssignmentString;
    }
    else classParamAssignmentString = "\t\t// empty constructor\n";
    return classParamAssignmentString;
  }

  /**
   * creates the string
   * String param1, int param2, float param3 
   */
  private String prepareClassParams(Vector variables) {
    String classParamsString = "";
    
    Iterator variablesIterator = variables.iterator();    
    while(variablesIterator.hasNext()) {
      Parameter parameter = (Parameter) variablesIterator.next();      
      classParamsString += parameter.getType() + " " + parameter.getName();      
      if (variablesIterator.hasNext()) {
        classParamsString += ", ";
      }
    }   
    
    if (classParamsString.length() >= 70) {
      classParamsString = "\n\t\t\t" + classParamsString.replaceAll(",\\s", ",\n\t\t\t");
    }    
    
    return classParamsString;
  }  
}