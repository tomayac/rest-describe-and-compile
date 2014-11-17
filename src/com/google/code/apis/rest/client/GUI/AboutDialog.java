package com.google.code.apis.rest.client.GUI;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * This class displays a fundamental About dialog.
 *
 */
public class AboutDialog {  
  /**
   * Creates and shows the About dialog.
   * 
   */
  public static void show() {
    final DialogBox dialogBox = new DialogBox();   
    dialogBox.setText(GuiFactory.strings.aboutRestDescribe());    
    
    VerticalPanel aboutPanel = new VerticalPanel();    
    HTML logoHtml = new HTML(MainMenuPanel.restDescribeLogo + "&nbsp;&nbsp;" + MainMenuPanel.restCompileLogo);
    aboutPanel.setCellHorizontalAlignment(logoHtml, HasHorizontalAlignment.ALIGN_CENTER);
    aboutPanel.add(new HTML("&nbsp;"));
    aboutPanel.add(logoHtml);   
    
    HTML textHtml = new HTML(GuiFactory.strings.aboutHtml());
    aboutPanel.add(textHtml);
    
    Button okButton = new Button(GuiFactory.strings.ok());
    okButton.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        GuiFactory.blockScreen(false);
        dialogBox.hide();
      }
    });
    aboutPanel.add(new HTML("<br />"));
    aboutPanel.add(okButton);
    aboutPanel.setCellHorizontalAlignment(okButton, HasHorizontalAlignment.ALIGN_CENTER);       
    
    dialogBox.setPopupPosition((int) Math.floor(Window.getClientWidth() / 5), (int) Math.floor(Window.getClientHeight() / 2.6));
    dialogBox.setWidget(aboutPanel);    
    
    GuiFactory.blockScreen(true);    
    dialogBox.show();
  }  
}
