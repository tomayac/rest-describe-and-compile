/**
 * 
 */
package com.google.code.apis.rest.client.GUI;

import com.google.gwt.i18n.client.Messages;

/**
 * @author tsteiner
 *
 */
public interface Notifications extends Messages {
  String invalidChildren(String elementName, String children);
  String invalidAttributes(String elementName, String attributes);  
  String mustContainAtLeast(String elementNode, String things);
}
