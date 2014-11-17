/**
 * 
 */
package com.google.code.apis.rest.client.GUI;

/**
 * @author tsteiner
 *
 */
public class XHRresponseHandler {

  /**
   * @param serviceDataXmlString
   */
  public XHRresponseHandler(String responseString, String path) {
    if (path.equals(SettingsDialog.pathToDiscoverer)) {
      DiscoveredItemsDialog.parseServiceDataXml(responseString);
    }
    else if (path.equals(SettingsDialog.pathToJsonConverter)) {
      WadlSaveDialog.show(responseString);
    }
  }
}
