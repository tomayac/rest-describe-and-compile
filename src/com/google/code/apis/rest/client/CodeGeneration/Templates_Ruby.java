/**
 * 
 */
package com.google.code.apis.rest.client.CodeGeneration;

import com.google.gwt.i18n.client.Messages;
/**
 * @author tsteiner
 *
 */
public interface Templates_Ruby extends Messages {
  String restRequestClass(String errorHandling);
  String defFile(String code);
  String defClass(String className, String classParams, String paramAssignments, String restParameters, String submitRequest, String attrAccessor);
}
