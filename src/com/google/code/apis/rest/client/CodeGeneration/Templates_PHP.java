/**
 * 
 */
package com.google.code.apis.rest.client.CodeGeneration;

import com.google.gwt.i18n.client.Messages;

/**
 * @author tsteiner
 *
 */
public interface Templates_PHP extends Messages {
  String restRequestClass(String errorHandling);  
  String defClass(String className, String classVars, String classParams, String paramAssignments, String restParameters, String submitRequest);
  String defFile(String fileContents);
}
