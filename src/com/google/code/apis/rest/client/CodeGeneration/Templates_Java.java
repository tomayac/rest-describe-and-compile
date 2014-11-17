/**
 * 
 */
package com.google.code.apis.rest.client.CodeGeneration;

import com.google.gwt.i18n.client.Messages;

/**
 * @author tsteiner
 *
 */
public interface Templates_Java extends Messages {
  String restRequestClass(String errorHandling);  
  String defClass(String name, String restParameters, String classVars, String submitRequest, String constructors, String gettersAndSetters);
  String defFile(String fileContents, String packageName);
  String defConstructor(String className, String classParams, String paramAssignments, String paramJavaDoc, String numberOfParams);
}
