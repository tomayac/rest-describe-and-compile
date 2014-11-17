package com.google.code.apis.rest.client.GUI;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * This class creates the request uri panel
 *
 */
public class RequestUriPanel extends Composite {
   
  private static final String yahooNewsSearchDemoRequest = "GET http://search.yahooapis.com/NewsSearchService/V1/newsSearch?" +
      "appid=YahooDemo&" +
      "query=WADL&" +
      "type=all&" +
      "results=10&" +
      "start=1&" +
      "sort=date&" +      
      "language=en&" +
      "site=cnn.com&" +
      "output=xml&" +
      "callback="; 
 
  private HTML requestSampleHeader;  
  public static VerticalPanel requestSamplePanel = new VerticalPanel();
  public static ScrollPanel requestSampleArea = new ScrollPanel(); 
  public static VerticalPanel containerPanel = new VerticalPanel();
  public static Button addRequestUriButton;
  public static Button batchUriButton;  
  public static Tree uriTree = new Tree();
  
  /**
   * Creates the request uri panel
   */
  public RequestUriPanel() {    
    requestSamplePanel.setStyleName("restDescribe-requestSamplePanel");    
    
    requestSampleHeader = new HTML("<h2>" + GuiFactory.strings.requestSample() + "</h2>");
    requestSamplePanel.add(requestSampleHeader);
                
    containerPanel.add(requestSampleArea);    
    containerPanel.setStyleName("restDescribe-requestSampleArea");
        
    requestSampleArea.add(uriTree);
    addRequestUriButton = new Button(GuiFactory.strings.addRequestUri());
    HorizontalPanel buttonPanel = new HorizontalPanel();
    containerPanel.add(buttonPanel);
    buttonPanel.add(new HTML("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"));
    buttonPanel.add(addRequestUriButton);
    addRequestUriButton.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {  
        String requestUri = GuiFactory.strings.newRequestUri();                        
        uriTree.addItem(new RequestUriTree(requestUri));        
      }
    });  
    
    buttonPanel.add(new HTML("&nbsp;"));
    batchUriButton = new Button(GuiFactory.strings.batchUri());
    buttonPanel.add(batchUriButton);
    batchUriButton.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        BatchUriDialog.show();
      }
    });
    
    final HTML loadDemoHyperlink = new HTML("<nobr><a href=\"#\"><span style=\"font-size:0.8em; padding-left:1em;\">" + GuiFactory.strings.loadDemo() + "</span></a></nobr>");
    loadDemoHyperlink.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {        
        GuiFactory.resetApplication(yahooNewsSearchDemoRequest);        
        loadDemoHyperlink.setHTML("<nobr><span style=\"font-size:0.8em; padding-left:1em;\">" + GuiFactory.strings.exploreUri() + "</span><a href=\"#\"><span style=\"font-size:0.8em; padding-left:1em;\">" + GuiFactory.strings.resetDemo() + "</span></a></nobr>");
      }
    });    
    buttonPanel.add(loadDemoHyperlink);    
    
    requestSamplePanel.add(containerPanel);
    initWidget(requestSamplePanel);
	}
}