/**
 * 
 */
package com.google.code.apis.rest.client.GUI;

import com.google.gwt.user.client.Window;

/**
 * @author tsteiner
 *
 */
public class HelpDialog {

  /**
   * 
   */
  public static void show() {
    Window.open("./doc/index.html", "help", "left=100,top=50,height=600,width=800,menubar=yes,toolbar=yes,location=yes,directories=yes,status=yes,resizable=yes,scrollbars=yes");    
  }

}
