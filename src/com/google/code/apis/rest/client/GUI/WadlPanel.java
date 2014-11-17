package com.google.code.apis.rest.client.GUI;

import com.google.code.apis.rest.client.Util.SyntaxHighlighter;
import com.google.code.apis.rest.client.Wadl.Analyzer;
import com.google.code.apis.rest.client.Wadl.WadlXml;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * This class creates the wadl panel
 *
 */
public class WadlPanel extends Composite implements ClickListener {   
  public static HTML wadlHeader;
  public static VerticalPanel wadlPanel = new VerticalPanel();  
  public static ScrollPanel wadlArea = new ScrollPanel();  
  public static Button wadlPreviewButton;
  public static Button wadlSaveButton;
  public static Button resetButton;
  public static Button fullscreenButton;
  public static Button generateCodeButton;
  public static HorizontalPanel containerPanel = new HorizontalPanel();
  
	/**
	 * Creates the wadl panel
	 */
	public WadlPanel() {    
    wadlPanel.setStyleName("restDescribe-wadlPanel");  
    wadlPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
    
    wadlHeader = new HTML("<h2>" + GuiFactory.strings.wadlFile() + "</h2>");    
    wadlPanel.add(wadlHeader);
    
    VerticalPanel buttonPanel = new VerticalPanel();          
    
    containerPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
    
    wadlArea.setStyleName("restDescribe-wadlArea");    
    containerPanel.add(wadlArea);
       
    wadlSaveButton = new Button(GuiFactory.strings.dumpXml());  
    wadlSaveButton.setWidth("8em");
    wadlSaveButton.addClickListener(this); 
    wadlSaveButton.setEnabled(false);
    buttonPanel.add(wadlSaveButton);
    
    wadlPreviewButton = new Button(GuiFactory.strings.prettyPrintXml());
    wadlPreviewButton.setWidth("8em");
    wadlPreviewButton.addClickListener(this);
    wadlPreviewButton.setEnabled(false);
    buttonPanel.add(wadlPreviewButton);
    
    fullscreenButton = new Button(GuiFactory.strings.fullscreen());
    fullscreenButton.setWidth("8em");
    fullscreenButton.addClickListener(this);
    fullscreenButton.setEnabled(false);
    buttonPanel.add(fullscreenButton);
    
    generateCodeButton = new Button(GuiFactory.strings.generateCode());
    generateCodeButton.setWidth("8em");
    generateCodeButton.addClickListener(this);
    generateCodeButton.setEnabled(false);
    buttonPanel.add(generateCodeButton);
    
    resetButton = new Button(GuiFactory.strings.resetWadl());
    resetButton.setWidth("8em");
    resetButton.addClickListener(this);
    resetButton.setEnabled(false);
    buttonPanel.add(resetButton);
    
    Hyperlink resetLink = new Hyperlink(GuiFactory.strings.resetLink(), true, "");
    resetLink.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        if (Window.confirm(GuiFactory.strings.reallyCompletelyReset())) {
          GuiFactory.resetApplication(GuiFactory.strings.newRequestUri());
        }
      }
    });
    buttonPanel.add(new HTML("&nbsp;"));
    buttonPanel.add(resetLink);
    containerPanel.add(buttonPanel); 
    
    wadlPanel.add(containerPanel);
      
    initWidget(wadlPanel);
	}

  /* (non-Javadoc)
   * @see com.google.gwt.user.client.ui.ClickListener#onClick(com.google.gwt.user.client.ui.Widget)
   */
  public void onClick(Widget sender) {
    if (sender == wadlPreviewButton) {
      preview();
    }
    else if (sender == wadlSaveButton) {
      save();
    }     
    else if (sender == resetButton) {
      reset();
    }
    else if (sender == fullscreenButton) {
      FullscreenDialog.show(wadlArea);
    }
    else if (sender == generateCodeButton) {
      generateCode();
    }
  }

  /**
   * 
   */
  public static void generateCode() {
    GuiFactory.toggleMode(GuiFactory.restCompile);       
  }

  public static void preview() {    
    String html = SyntaxHighlighter.toHtml(prepareXml());      
    WadlPreviewDialog.show(html);
  }
  
  /**
   * 
   */
  private static String prepareXml() {
    WadlXml wadlXml = new WadlXml(Analyzer.application);
    return wadlXml.dump();    
  }
  
  public static void save() {
    WadlSaveDialog.show(prepareXml());
  }

  /**
   * 
   */
  public static void reset() {
    GuiFactory.toggleMode(GuiFactory.restDescribe);
    if (Window.confirm(GuiFactory.strings.reallyReset())) {       
      Analyzer.application.reset();        
      WadlPanel.wadlArea.clear();
      GuiFactory.toggleButtonsEnabled(false);
    }    
  }
}
