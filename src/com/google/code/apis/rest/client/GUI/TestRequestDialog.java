package com.google.code.apis.rest.client.GUI;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * This class creates the test request dialog
 *
 */
public class TestRequestDialog { 
  
  /**
   * Creates the test request dialog
   * @param requestSampleTextBox The request uri textbox
   * @param requestUriTree The corresponding request uri tree
   */
  public static void show(TextBox requestSampleTextBox, final RequestUriTree requestUriTree) {
    final DialogBox dialogBox = new DialogBox();
    dialogBox.setText(GuiFactory.strings.testRequestDialog());    
    
    final Frame frame = new Frame(requestSampleTextBox.getText());
    frame.setWidth(Math.floor(Window.getClientWidth() * 0.9) + "px");
    frame.setHeight(Math.floor(Window.getClientHeight() * 0.75) + "px");
    
    VerticalPanel iframePanel = new VerticalPanel();
    iframePanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);

    HorizontalPanel iframeSampleBoxPanel = new HorizontalPanel();   
    
    final TextBox iframeSampleTextBox = new TextBox();
    iframeSampleTextBox.setText(requestSampleTextBox.getText());
    iframeSampleTextBox.setWidth(Math.floor(Window.getClientWidth() * 0.6) + "px");
    iframeSampleBoxPanel.add(iframeSampleTextBox);    
    iframeSampleBoxPanel.add(new HTML("&nbsp;"));    
     
    Button iframeReTestButton = new Button(GuiFactory.strings.reTestRequest());
    iframeReTestButton.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        frame.setUrl(iframeSampleTextBox.getText());
      }
    });
    iframeSampleBoxPanel.add(iframeReTestButton);
    
    iframeSampleBoxPanel.add(new HTML("&nbsp;"));    
    
    Button iframeAcceptRequestButton = new Button(GuiFactory.strings.acceptRequest());
    iframeAcceptRequestButton.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {        
        GuiFactory.blockScreen(false);
        requestUriTree.setRequestUri(iframeSampleTextBox.getText());
        dialogBox.hide();            
      }
    });
    iframeSampleBoxPanel.add(iframeAcceptRequestButton);      
    
    iframeSampleBoxPanel.add(new HTML("&nbsp;"));
    
    Button iframeCancelRequestButton = new Button(GuiFactory.strings.cancel());
    iframeCancelRequestButton.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        GuiFactory.blockScreen(false);
        dialogBox.hide();     
      }
    });
    iframeSampleBoxPanel.add(iframeCancelRequestButton);
    
    iframePanel.add(iframeSampleBoxPanel);    
        
    iframePanel.add(frame);    
    
    GuiFactory.blockScreen(true);   
    dialogBox.setWidget(iframePanel);
    dialogBox.setPopupPosition((int) Math.floor(Window.getClientHeight() * 0.05), (int) Math.floor(Window.getClientHeight() * 0.05));    
    dialogBox.show();
  }
}
