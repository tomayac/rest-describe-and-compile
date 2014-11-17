/**
 * 
 */
package com.google.code.apis.rest.client.GUI;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.RequestTimeoutException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * @author tsteiner
 *
 */
public class XmlHttpRequest {
  
  public XmlHttpRequest(String postData, final String path) {    
    Indicator.getIndicator(GuiFactory.strings.analyzing());
    final HTML indicator = Indicator.getIndicator(GuiFactory.strings.analyzing());
    RootPanel.get().add(indicator);    
    
    RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, path);
    builder.setHeader("Content-Type", "application/x-www-form-urlencoded"); 
    try {
      /*
       * wait #timeout seconds for the request to complete
       */
      builder.setTimeoutMillis(SettingsDialog.requestTimeoutSeconds * 1000);      
      Request response = builder.sendRequest("data=" + URL.encodeComponent(postData), new RequestCallback() {        
        public void onError(Request request, Throwable exception) {
          if (exception instanceof RequestTimeoutException) {
            RootPanel.get().remove(indicator);
            com.google.gwt.user.client.Window.alert("Timeout");            
          }
          else {
            RootPanel.get().remove(indicator);
            com.google.gwt.user.client.Window.alert(exception.toString());            
          }
        }

        public void onResponseReceived(Request request, Response response) {
          RootPanel.get().remove(indicator);        
          new XHRresponseHandler(response.getText(), path); 
        }
      });
      
    } catch (RequestException e) {
      RootPanel.get().remove(indicator);
      com.google.gwt.user.client.Window.alert("Failed to send the request: " + e.getMessage());
    }
  }
}
