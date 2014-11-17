package com.google.code.apis.rest.client.GUI;

import com.google.code.apis.rest.client.GUI.AboutDialog;
import com.google.code.apis.rest.client.GUI.SettingsDialog;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;

/**
 * This class creates the main menu panel
 *
 */
public class MainMenuPanel extends Composite {  
  public static VerticalPanel menuPanel;
  public static AbsolutePanel topPanel;
  public static HorizontalPanel userLinks = new HorizontalPanel();
  protected static HTML logo = new HTML(); 
  protected static final String restCompileLogo = "<img src=\"./images/rest_compile_logo.png\" style=\"margin-bottom:4px;\" >";
  protected static final String restDescribeLogo = "<img src=\"./images/rest_describe_logo.png\" style=\"margin-bottom:4px;\" >";
  
  /**
   * Creates the main menu panel
   */
  public MainMenuPanel() {        
    menuPanel = new VerticalPanel();
    menuPanel.setWidth("100%");
    menuPanel.setStyleName("restDescribe-menuPanel");
    menuPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
    
    // logo
    topPanel = new AbsolutePanel();
    topPanel.setWidth("100%");    
     
    // user links in the upper right corner
    String separator = "&nbsp;|&nbsp;";    
    Hyperlink settingsLink = new Hyperlink(GuiFactory.strings.settingsLink(), "settings");
    settingsLink.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        SettingsDialog.show();
      }
    });
    userLinks.add(settingsLink);
    userLinks.setStyleName("restDescribe-userLinks");
    userLinks.add(new HTML(separator));
    Hyperlink helpLink = new Hyperlink("<nobr><img src=\"./images/new_window.png\" style=\"vertical-align:middle;margin-right:0.2em;\" />" + GuiFactory.strings.helpLink() + "</nobr>", true, "help");
    helpLink.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        HelpDialog.show();
      }
    });
    userLinks.add(new HTML(GuiFactory.strings.newFeature()));
    userLinks.add(helpLink);    
    topPanel.add(userLinks);  
    
    topPanel.add(logo);

    menuPanel.add(topPanel);    
    
    Command cmd = new Command() {
      public void execute() {  
        Window.alert("Not implemented yet :-(");
      }
    };

    // wadl menu
    MenuBar wadlMenu = new MenuBar(true);
    wadlMenu.addItem(GuiFactory.strings.save(), true, new Command() {
      public void execute() {
        WadlPanel.wadlSaveButton.click();
      }
    });
    wadlMenu.addItem(GuiFactory.strings.preview(), true, new Command() {
      public void execute() {
        WadlPanel.wadlPreviewButton.click();        
      }
      
    });
    wadlMenu.addItem(GuiFactory.strings.separator(), true, cmd);
    wadlMenu.addItem(GuiFactory.strings.compile(), true, new Command() {
      public void execute() {
        WadlPanel.generateCodeButton.click();
      }      
    });
    wadlMenu.addItem(GuiFactory.strings.separator(), true, cmd);
    wadlMenu.addItem(GuiFactory.strings.upload(), true, new Command() {
      public void execute() {
        WadlUploadDialog.show();
      }      
    });
    wadlMenu.addItem(GuiFactory.strings.separator(), true, cmd);
    wadlMenu.addItem(GuiFactory.strings.reset(), true, new Command() {
      public void execute() {
        WadlPanel.resetButton.click();
      }
    });
    
    // edit menu
    MenuBar editMenu = new MenuBar(true);
    editMenu.addItem(GuiFactory.strings.undo(), true, cmd);
    editMenu.addItem(GuiFactory.strings.redo(), true, cmd);

    // navigate menu
    MenuBar navigateMenu = new MenuBar(true);
    navigateMenu.addItem(GuiFactory.strings.jumpToParametersDeclaration(), true, cmd);
    navigateMenu.addItem(GuiFactory.strings.jumpToRequestDeclaration(), true, cmd);
    navigateMenu.addItem(GuiFactory.strings.jumpToResponseDeclaration(), true, cmd);
    navigateMenu.addItem(GuiFactory.strings.separator(), true, cmd);
    navigateMenu.addItem(GuiFactory.strings.jumpToBeginning(), true, cmd);
    navigateMenu.addItem(GuiFactory.strings.jumpToEnd(), true, cmd);
    
    // help menu
    MenuBar helpMenu = new MenuBar(true);
    helpMenu.addItem(GuiFactory.strings.userManual(), true, new Command() {
      public void execute() {
        HelpDialog.show();
      }
    });    
    helpMenu.addItem(GuiFactory.strings.about(), true, new Command() {
      public void execute() {
        AboutDialog.show();
      }
    });    
    
    MenuBar mainMenu = new MenuBar();     
    mainMenu.addItem(GuiFactory.strings.wadl(), true, wadlMenu);
    mainMenu.addItem(GuiFactory.strings.edit(), true, editMenu);
    mainMenu.addItem(GuiFactory.strings.navigate(), true, navigateMenu);
    mainMenu.addItem(GuiFactory.strings.help(), true, helpMenu);
    
    menuPanel.add(mainMenu);
    
    initWidget(menuPanel);
  } 
}