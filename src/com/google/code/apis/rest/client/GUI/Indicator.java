/**
 * 
 */
package com.google.code.apis.rest.client.GUI;

import com.google.gwt.user.client.ui.HTML;

/**
 * Displays a simple activity indicator
 *
 */
public class Indicator {
  public static HTML getIndicator(String message) {    
    return new HTML("<span style=\"position:absolute; top:5; right:5; color:white; background-color:maroon; padding: 2px; font-size: 9pt; font-family: arial;\">" + message + "</span>"); 
  }
}
