package com.google.code.apis.rest.client.GUI;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * This class creates the accept wadl dialog
 *
 */
public class WadlSaveDialog {
  
  /**
   * Creates the accept wadl dialog
   * @param html The xhtml string containig the syntax-highlighted wadl
   */
  public static void show(final String wadlString) {
    final DialogBox dialog = new DialogBox();
    dialog.setText(GuiFactory.strings.wadlAcceptDialog());
    
    VerticalPanel acceptPanel = new VerticalPanel();      
    final TextArea wadlTextArea = new TextArea();
    wadlTextArea.setStyleName("restDescribe-wadlArea");
    wadlTextArea.setHeight(Math.floor(Window.getClientHeight() * 0.75) + "px");
    wadlTextArea.setWidth(Math.floor(Window.getClientWidth() * 0.9) + "px");
    wadlTextArea.setText(wadlString);    
    acceptPanel.add(wadlTextArea);    
    
    Button closeButton = new Button(GuiFactory.strings.close());
    closeButton.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        dialog.hide();
        GuiFactory.blockScreen(false);
      }
    });
    
    Button selectAllButton = new Button(GuiFactory.strings.selectAll());
    selectAllButton.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        selectAll(wadlTextArea);
      }
    });
    
    Button toJsonButton = new Button(GuiFactory.strings.wadl2Json());
    toJsonButton.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {        
        new XmlHttpRequest(wadlString, SettingsDialog.pathToJsonConverter);
      }
    });    
    
    acceptPanel.add(new HTML("<br />"));
    HorizontalPanel buttonPanel = new HorizontalPanel();
    buttonPanel.add(closeButton);
    buttonPanel.add(selectAllButton);
    if (wadlString.startsWith("<?xml")) {
      buttonPanel.add(new HTML("&nbsp;&nbsp;" + GuiFactory.strings.newFeature()));
      buttonPanel.add(toJsonButton);
    }
    acceptPanel.add(buttonPanel);
    acceptPanel.setCellHorizontalAlignment(buttonPanel, HasHorizontalAlignment.ALIGN_CENTER);
    
    dialog.setWidget(acceptPanel);
    dialog.setPopupPosition((int) Math.floor(Window.getClientWidth() * 0.05), (int) Math.floor(Window.getClientHeight() * 0.05));    
    GuiFactory.blockScreen(true);
    dialog.show();
  }

  /**
   * Selects the complete text area contents
   * 
   * @param wadlTextArea The text area
   */
  private static void selectAll(TextArea wadlTextArea) {
    wadlTextArea.setSelectionRange(0, wadlTextArea.getText().length());    
  }
}
