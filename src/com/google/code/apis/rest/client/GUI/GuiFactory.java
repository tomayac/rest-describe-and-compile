package com.google.code.apis.rest.client.GUI;

import com.google.code.apis.rest.client.Tree.ReferenceManager;
import com.google.code.apis.rest.client.Wadl.Analyzer;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.WindowResizeListener;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * This class creates the GUI and handles panel resizing in case of where the main window is resized
 *
 */
public class GuiFactory implements WindowResizeListener {
  private static DockPanel blockScreen;
  public static Strings strings;
  public static Notifications notifications;  
  private DockPanel dockPanel;
  public static final String restCompile = "restCompile";
  public static final String restDescribe = "restDescribe";
  
	/**
	 * Creates the GUI consisting of various panels
	 */
	public GuiFactory() {
    // I18N strings
    strings = (Strings) GWT.create(Strings.class);
    notifications = (Notifications) GWT.create(Notifications.class);
   
    // disallow scrolling
    Window.enableScrolling(false);
    // resize the panels on main window resize
    Window.addWindowResizeListener(this);        
    
    // main application
    dockPanel = new DockPanel();    
    dockPanel.setStyleName("restDescribe-mainApplicationPanel");
    dockPanel.setWidth("99.7%");
    dockPanel.setHeight("99.7%");    
    dockPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);    
    dockPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);    

    // main menu
    MainMenuPanel mainMenuPanel = new MainMenuPanel();    
    dockPanel.add(mainMenuPanel, DockPanel.NORTH);
    dockPanel.setCellHeight(mainMenuPanel, "10%");

    // rest compile panel
    RestCompilePanel restCompilePanel = new RestCompilePanel();    
    dockPanel.add(restCompilePanel, DockPanel.NORTH);
    
    // request sample panel    
    RequestUriPanel requestUriPanel = new RequestUriPanel();
    dockPanel.add(requestUriPanel, DockPanel.NORTH);
    dockPanel.setCellHeight(requestUriPanel, "10%");    
    
    // navigation panel
    ParameterPanel parameterPanel = new ParameterPanel();    
    dockPanel.add(parameterPanel, DockPanel.WEST);
    dockPanel.setCellHeight(parameterPanel, "80%");
    dockPanel.setCellWidth(parameterPanel, "45%");
    dockPanel.setCellVerticalAlignment(parameterPanel, HasVerticalAlignment.ALIGN_TOP);
    
    // wadl panel
    WadlPanel wadlPanel = new WadlPanel();
    dockPanel.add(wadlPanel, DockPanel.EAST);    
    dockPanel.setCellHeight(wadlPanel, "80%");
    dockPanel.setCellWidth(wadlPanel, "55%");
    dockPanel.setCellVerticalAlignment(wadlPanel, HasVerticalAlignment.ALIGN_TOP);
    
    toggleMode(restDescribe);
    
		RootPanel.get().add(dockPanel);
    
    // semi-transparent screen blocker for dialogs
    blockScreen = new DockPanel();        
    HTML semiTransparentDiv = getBlockedScreenDiv(Window.getClientWidth(), Window.getClientHeight());
    blockScreen.add(semiTransparentDiv, DockPanel.SOUTH);
    blockScreen.setVisible(false);    
    RootPanel.get().add(blockScreen);

    onWindowResized(Window.getClientWidth(), Window.getClientHeight());
    
    // reset the application
    GuiFactory.resetApplication(GuiFactory.strings.newRequestUri());
	}
  
  /* (non-Javadoc)
   * @see com.google.gwt.user.client.WindowResizeListener#onWindowResized(int, int)
   */
  public void onWindowResized(int width, int height) {    
    RequestUriPanel.requestSampleArea.setHeight(Math.floor(0.1 * height) + "px");    
    RequestUriPanel.containerPanel.setWidth(Math.floor(0.97 * width) + "px");
    
    int panelHeight = height - RequestUriPanel.requestSamplePanel.getOffsetHeight() - MainMenuPanel.menuPanel.getOffsetHeight() - WadlPanel.wadlHeader.getOffsetHeight() - 80;    
    ParameterPanel.parameterPanel.setHeight("100%");
    ParameterPanel.parameterPanel.setWidth("100%");
    ParameterPanel.requestArea.setHeight(panelHeight + "px");
    ParameterPanel.requestArea.setWidth(Math.floor(0.28 * width) + "px");
                
    WadlPanel.wadlPanel.setHeight("100%");
    WadlPanel.wadlPanel.setWidth("95%");    
    
    WadlPanel.wadlArea.setHeight(panelHeight + "px");
    WadlPanel.wadlArea.setWidth(Math.floor(0.58 * width) + "px");    
    
    GuiFactory.blockScreen.clear();
    GuiFactory.blockScreen.add(getBlockedScreenDiv(width, height), DockPanel.NORTH);    
  }
  
  /**
   * Blocks the screen by means of a semi transparent blocking layer when dialogs are shown 
   * @param isVisible Determines whether or not to display the blocking layer
   */
  public static void blockScreen(boolean isVisible) {
    blockScreen.setVisible(isVisible);
  }
  
  /**
   * Dynamically creates the semi transparent blocking layer html
   * @param width Current screen width
   * @param height Current screen height
   * @return The blocking layer html
   */
  private static HTML getBlockedScreenDiv(int width, int height) {
    return new HTML("<div style='z-index:0; width:" + width + "px; height:" + height + "px; position:absolute; left:0px; top:0px; background-color:black; opacity:.40; filter: alpha(opacity=40);'>&nbsp;</div>");
  }
  
  /**
   * Resets the application and loads an initial request string
   * @param initialRequestString The initial request string
   */
  public static void resetApplication(String initialRequestString) {
    resetApplication();
    ReferenceManager.allRequestStrings.add(initialRequestString);      
    RequestUriTree.listRequestSamples(ReferenceManager.allRequestStrings);    
  }
  
  public static void toggleButtonsEnabled(boolean isEnabled) {
    WadlPanel.wadlSaveButton.setEnabled(isEnabled);
    WadlPanel.wadlPreviewButton.setEnabled(isEnabled);
    WadlPanel.resetButton.setEnabled(isEnabled);
    WadlPanel.fullscreenButton.setEnabled(isEnabled);
    WadlPanel.generateCodeButton.setEnabled(isEnabled);
  }
  
  public static void toggleMode(String mode) {
    boolean toggleBit = false;
    if (mode.equals(restCompile)) {
      toggleBit = true;
      if (FullscreenDialog.leaveFullscreenButton != null) {
        FullscreenDialog.leaveFullscreenButton.click();
      }
      RestCompilePanel.codeTextArea.setText("");
      RestCompilePanel.buttonPanel.setVisible(false);
      RestCompilePanel.compilePanel.setVisible(false);      
      MainMenuPanel.logo.setHTML(MainMenuPanel.restCompileLogo);
    }
    else if (mode.equals(restDescribe)) {
      toggleBit = false;
      MainMenuPanel.logo.setHTML(MainMenuPanel.restDescribeLogo);
    }    
    
    WadlPanel.wadlPanel.setVisible(!toggleBit);
    RequestUriPanel.requestSamplePanel.setVisible(!toggleBit);
    ParameterPanel.parameterPanel.setVisible(!toggleBit);
    RestCompilePanel.restCompilePanel.setVisible(toggleBit);
    
    int width = Window.getClientWidth();
    int height = Window.getClientHeight();
    RequestUriPanel.requestSampleArea.setHeight(Math.floor(0.1 * height) + "px");    
    RequestUriPanel.containerPanel.setWidth(Math.floor(0.97 * width) + "px");
    
    int panelHeight = height - RequestUriPanel.requestSamplePanel.getOffsetHeight() - MainMenuPanel.menuPanel.getOffsetHeight() - WadlPanel.wadlHeader.getOffsetHeight() - 80;    
    ParameterPanel.parameterPanel.setHeight("100%");
    ParameterPanel.parameterPanel.setWidth("100%");
    ParameterPanel.requestArea.setHeight(panelHeight + "px");
    ParameterPanel.requestArea.setWidth(Math.floor(0.28 * width) + "px");
                
    WadlPanel.wadlPanel.setHeight("100%");
    WadlPanel.wadlPanel.setWidth("95%");    
    
    WadlPanel.wadlArea.setHeight(panelHeight + "px");
    WadlPanel.wadlArea.setWidth(Math.floor(0.58 * width) + "px");  
  }

  /**
   * Resets the application without loading an inital uri
   */
  public static void resetApplication() {
    RestCompilePanel.paramToUseForName = "";    
    RequestUriPanel.uriTree.removeItems();    
    ParameterPanel.requestContainer.clear();
    WadlPanel.wadlArea.clear();
    if (Analyzer.application != null) {      
      Analyzer.application.reset();      
    }
    ReferenceManager.clearReferences();
    toggleButtonsEnabled(false);    
  }
}
