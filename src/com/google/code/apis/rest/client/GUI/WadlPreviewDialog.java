/**
 * 
 */
package com.google.code.apis.rest.client.GUI;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * This class creates the preview wadl dialog
 *
 */
public class WadlPreviewDialog {
  
  /**
   * Creates the preview wadl dialog
   * @param html The xhtml string containig the syntax-highlighted wadl
   */
  public static void show(String html) {
    final DialogBox dialog = new DialogBox();
    dialog.setText(GuiFactory.strings.wadlPreviewDialog());
    
    VerticalPanel preview = new VerticalPanel();      
    ScrollPanel scrollPanel = new ScrollPanel();
    scrollPanel.setStyleName("restDescribe-wadlArea");
    scrollPanel.setHeight(Math.floor(Window.getClientHeight() * 0.75) + "px");
    scrollPanel.setWidth(Math.floor(Window.getClientWidth() * 0.9) + "px");    
    scrollPanel.add(new HTML(html));      
    preview.add(scrollPanel);
    
    Button closeButton = new Button(GuiFactory.strings.close());
    closeButton.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        dialog.hide();
        GuiFactory.blockScreen(false);
      }
    });
    preview.add(new HTML("<br />"));
    preview.add(closeButton);
    preview.setCellHorizontalAlignment(closeButton, HasHorizontalAlignment.ALIGN_CENTER);
    
    dialog.setWidget(preview);
    dialog.setPopupPosition((int) Math.floor(Window.getClientWidth() * 0.05), (int) Math.floor(Window.getClientHeight() * 0.05));    
    GuiFactory.blockScreen(true);
    dialog.show();
  }
}
