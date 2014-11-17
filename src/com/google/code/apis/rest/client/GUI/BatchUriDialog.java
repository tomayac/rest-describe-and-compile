package com.google.code.apis.rest.client.GUI;

import com.google.code.apis.rest.client.Tree.MethodItem;
import com.google.code.apis.rest.client.Tree.WadlTreeRoot;
import com.google.code.apis.rest.client.Wadl.Analyzer;
import com.google.code.apis.rest.client.Wadl.ApplicationNode;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * This class creates the batch uri dialog
 *
 */
public class BatchUriDialog {
  private final static String demoStrings = "GET http://search.yahooapis.com/NewsSearchService/V1/newsSearch?appid=YahooDemo&query=google+yahoo&type=all&results=10&start=1&sort=date&language=en&site=www.cnn.com&output=xml&callback=\n" +
      "GET http://search.yahooapis.com/SiteExplorerService/V1/inlinkData?appid=YahooDemo&query=http://search.yahoo.com&results=2\n" +
      "GET http://search.yahooapis.com/SiteExplorerService/V1/pageData?appid=YahooDemo&query=http://search.yahoo.com&results=2\n" +
      "GET http://search.yahooapis.com/SiteExplorerService/V1/ping?sitemap=http://www.yahoo.com\n" +
      "GET http://search.yahooapis.com/SiteExplorerService/V1/updateNotification?appid=YahooDemo&url=http://www.yahoo.com\n" +
      "GET http://search.yahooapis.com/VideoSearchService/V1/videoSearch?appid=YahooDemo&query=madonna&results=2\n" +
      "GET http://search.yahooapis.com/WebSearchService/V1/contextSearch?appid=YahooDemo&query=madonna&results=2&context=Italian+sculptors+and+painters+of+the+renaissance+favored+the+Virgin+Mary+for+inspiration\n" +
      "GET http://search.yahooapis.com/WebSearchService/V1/relatedSuggestion?appid=YahooDemo&query=Madonna\n" +
      "GET http://search.yahooapis.com/WebSearchService/V1/spellingSuggestion?appid=YahooDemo&query=madnna\n" +
      "GET http://search.yahooapis.com/WebSearchService/V1/webSearch?appid=YahooDemo&query=madonna&results=2\n" +
      "GET http://search.yahooapis.com/AudioSearchService/V1/artistSearch?appid=YahooDemo&artist=Madonna&results=2\n" +
      "GET http://search.yahooapis.com/AudioSearchService/V1/albumSearch?appid=YahooDemo&artistid=XXXXXXP000064565&album=Like&results=2\n" +
      "GET http://search.yahooapis.com/AudioSearchService/V1/songSearch?appid=YahooDemo&albumid=XXXXXXR000012208&results=2\n" +
      "GET http://search.yahooapis.com/AudioSearchService/V1/songDownloadLocation?appid=YahooDemo&songid=XXXXXXT002734752&results=2\n";
  
  public static void show() {
    final DialogBox dialogBox = new DialogBox();    
    dialogBox.setText(GuiFactory.strings.batchUriTitle());    
    
    final VerticalPanel batchUriPanel = new VerticalPanel(); 
    
    // label
    HorizontalPanel textPanel = new HorizontalPanel();
    textPanel.add(new HTML(GuiFactory.strings.pasteUrisHere()));
    
    final TextArea uriTextArea = new TextArea();
    HTML demoLink = new HTML("<a href=\"#\"><span style=\"font-size:0.8em; padding-left:4em;\">" + GuiFactory.strings.loadDemos() + "</span></a>");
    demoLink.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        uriTextArea.setText(demoStrings);
      }
    });
    textPanel.add(demoLink);
    batchUriPanel.add(new HTML("&nbsp;"));
    batchUriPanel.add(textPanel);
    
    // text area        
    uriTextArea.setWidth(Math.floor(Window.getClientWidth() * 0.85) + "px");
    uriTextArea.setHeight("15em");
    batchUriPanel.add(uriTextArea);
        
    // ok button
    HorizontalPanel buttonPanel = new HorizontalPanel();
    Button okButton = new Button(GuiFactory.strings.ok());
    buttonPanel.add(okButton);
    okButton.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        // start analysis        
        dialogBox.hide();
        if (uriTextArea.getText().length() > 0) {
          HTML indicator = Indicator.getIndicator(GuiFactory.strings.analyzing());
          RootPanel.get().add(indicator);
          if (Window.confirm(GuiFactory.strings.reallyStartBatchAnalysis())) {
            processRequestStrings(uriTextArea.getText().trim());                   
          }
          RootPanel.get().remove(indicator);
        }
        GuiFactory.blockScreen(false);        
      }
    });
    
    // cancel button
    Button closeButton = new Button(GuiFactory.strings.cancel());
    buttonPanel.add(closeButton);
    closeButton.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {        
        GuiFactory.blockScreen(false);
        dialogBox.hide();
      }
    });
    batchUriPanel.add(buttonPanel);
    batchUriPanel.setCellHorizontalAlignment(buttonPanel, HasHorizontalAlignment.ALIGN_CENTER);    
    
    dialogBox.setPopupPosition((int) Math.floor(Window.getClientWidth() * 0.075), (int) Math.floor(Window.getClientHeight() / 2.6));
    dialogBox.setWidget(batchUriPanel);
    
    GuiFactory.blockScreen(true);    
    dialogBox.show();  
  }

  /**
   * Separates the line break separated request strings and starts the analysis
   * 
   * @param requestStrings The line break separated uri strings
   */
  private static void processRequestStrings(String requestStrings) {
    GuiFactory.resetApplication();   
    String[] separatedRequestStrings = requestStrings.split("\n");
    for (int i = 0; i < separatedRequestStrings.length; i++) { 
      separatedRequestStrings[i] = separatedRequestStrings[i].trim();      
      if (separatedRequestStrings[i].length() > 0) {
        separatedRequestStrings[i] = separatedRequestStrings[i].replaceAll("\\s\\s*", " ");
        // make sure the uri starts with a http method name
        if (!separatedRequestStrings[i].startsWith(MethodItem.delete) &&
            !separatedRequestStrings[i].startsWith(MethodItem.get) &&
            !separatedRequestStrings[i].startsWith(MethodItem.head) &&
            !separatedRequestStrings[i].startsWith(MethodItem.post) &&
            !separatedRequestStrings[i].startsWith(MethodItem.put)) {
          separatedRequestStrings[i] = MethodItem.get + " " + separatedRequestStrings[i];
        }       
      }
    }   
    
    ApplicationNode application = null;
    for(int i = 0; i < separatedRequestStrings.length; i++) {           
      RequestUriTree requestUriTree = new RequestUriTree(separatedRequestStrings[i]);
      RequestUriPanel.uriTree.addItem(requestUriTree);     
      
      Analyzer analyzer = new Analyzer(separatedRequestStrings[i]);      
      application = analyzer.analyze();
    }    
    WadlTreeRoot wadlTreeRoot = new WadlTreeRoot();
    Tree wadlTree = wadlTreeRoot.buildTree(application);
    
    WadlPanel.wadlArea.setWidget(wadlTree);
    GuiFactory.toggleButtonsEnabled(true);
  }
}
