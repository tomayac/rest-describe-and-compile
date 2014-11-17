/**
 * 
 */
package com.google.code.apis.rest.client.CodeGeneration;

import com.google.gwt.i18n.client.Messages;

/**
 * @author tsteiner
 *
 */
public interface Templates_DogFoodPHP extends Messages {
  String dogFood(String location, String requestName);
  String defClass(String className, String classVars, String classParams, String paramAssignments, String restParameters, String submitRequest);
  String xmlHeader(String code);  
  String restRequestClass(String errorHandlingString);
}
