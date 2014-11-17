package com.google.code.apis.rest.client.GUI;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.WindowResizeListener;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * This class displays the fullscreen dialog for fullscreen wadl editing
 *
 */
public class FullscreenDialog {  
  public static Button leaveFullscreenButton;
  
  public static void show(final ScrollPanel wadlArea) {
    final DialogBox dialogBox = new DialogBox();   
    final HorizontalPanel fullscreenPanel = new HorizontalPanel(); 
    
    Window.addWindowResizeListener(new WindowResizeListener() {
      public void onWindowResized(int width, int height) {
        dialogBox.setHeight(height + "px");
        dialogBox.setWidth(width + "px");
        fullscreenPanel.setWidth("100%");
        fullscreenPanel.setHeight("100%");        
        wadlArea.setHeight(Window.getClientHeight() - 40 + "px");
        wadlArea.setWidth(Window.getClientWidth() - 120 + "px");
      }
    });
    
    dialogBox.setText(GuiFactory.strings.fullscreenTitle());    
        
    fullscreenPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
    
    VerticalPanel buttonPanel = new VerticalPanel();
    fullscreenPanel.setWidth("100%");
    fullscreenPanel.setHeight("100%");
    fullscreenPanel.add(wadlArea);    
    wadlArea.setHeight(Window.getClientHeight() - 40 + "px");
    wadlArea.setWidth(Window.getClientWidth() - 120 + "px");
    
    String buttonWidth = "8em";
    Button wadlSaveButton = new Button(GuiFactory.strings.dumpXml());
    wadlSaveButton.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        WadlPanel.save();        
      }      
    });
    wadlSaveButton.setWidth(buttonWidth);
    buttonPanel.add(wadlSaveButton);
    
    Button wadlPreviewButton = new Button(GuiFactory.strings.prettyPrintXml());
    wadlPreviewButton.setWidth(buttonWidth);
    wadlPreviewButton.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        WadlPanel.preview();        
      }      
    });
    buttonPanel.add(wadlPreviewButton);   
    
    // leave fullscreen button    
    leaveFullscreenButton = new Button(GuiFactory.strings.leaveFullscreen());
    leaveFullscreenButton.setWidth(buttonWidth);
    buttonPanel.add(leaveFullscreenButton);
    leaveFullscreenButton.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        int width = Window.getClientWidth();
        int height = Window.getClientHeight();
        int panelHeight = height - RequestUriPanel.requestSamplePanel.getOffsetHeight() - MainMenuPanel.menuPanel.getOffsetHeight() - WadlPanel.wadlHeader.getOffsetHeight() - 80;
        WadlPanel.wadlArea.setHeight(panelHeight + "px");
        WadlPanel.wadlArea.setWidth(Math.floor(0.58 * width) + "px");        
        
        WadlPanel.containerPanel.insert(wadlArea, 0);
        WadlPanel.wadlPanel.setHeight("100%");
        WadlPanel.wadlPanel.setWidth("95%");        
        
        ParameterPanel.parameterPanel.setHeight("100%");
        ParameterPanel.parameterPanel.setWidth("100%");
        ParameterPanel.requestArea.setHeight(panelHeight + "px");
        ParameterPanel.requestArea.setWidth(Math.floor(0.28 * width) + "px");        
                
        RequestUriPanel.requestSampleArea.setHeight(Math.floor(0.1 * height) + "px");    
        RequestUriPanel.containerPanel.setWidth(Math.floor(0.97 * width) + "px");
        
        GuiFactory.blockScreen(false);
        dialogBox.hide();
      }
    });        
    fullscreenPanel.add(buttonPanel);
    
    Button generateCodeButton = new Button(GuiFactory.strings.generateCode());
    generateCodeButton.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        WadlPanel.generateCode();        
      }      
    });
    generateCodeButton.setWidth(buttonWidth);    
    buttonPanel.add(generateCodeButton);
    
    Button resetButton = new Button(GuiFactory.strings.resetWadl());
    resetButton.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        WadlPanel.reset();        
      }      
    });
    resetButton.setWidth(buttonWidth);    
    buttonPanel.add(resetButton);
            
    
    dialogBox.setHeight(Window.getClientHeight() + "px");
    dialogBox.setWidth(Window.getClientWidth() + "px");
    dialogBox.setPopupPosition(0, 0);
    dialogBox.setWidget(fullscreenPanel);
    
    GuiFactory.blockScreen(true);    
    dialogBox.show();  
  }
}
