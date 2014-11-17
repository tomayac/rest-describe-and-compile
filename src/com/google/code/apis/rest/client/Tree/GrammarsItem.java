package com.google.code.apis.rest.client.Tree;

import java.util.Vector;

import com.google.code.apis.rest.client.GUI.GuiFactory;
import com.google.code.apis.rest.client.GUI.SettingsDialog;
import com.google.code.apis.rest.client.Util.SyntaxHighlighter;
import com.google.code.apis.rest.client.Wadl.ApplicationNode;
import com.google.code.apis.rest.client.Wadl.GrammarsNode;
import com.google.code.apis.rest.client.Wadl.WadlXml;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class GrammarsItem extends Composite {
  public GrammarsItem(final GrammarsNode grammars, final TreeItem applicationTreeItem) {
    HorizontalPanel grammarsPanel = new HorizontalPanel();
    grammarsPanel.add(new HTML(SyntaxHighlighter.highlight("<" + WadlXml.grammarsNode + ">")));
    // remove resources hyperlink
    Hyperlink removeGrammarsLink = new Hyperlink(GuiFactory.strings.remove(), true, "");
    removeGrammarsLink.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {        
        grammars.getApplication().removeGrammars();        
        listGrammars(grammars.getApplication(), applicationTreeItem);
      }
    });      
    grammarsPanel.add(removeGrammarsLink);
    
    initWidget(grammarsPanel);
  }
  
  public static void listGrammars(final ApplicationNode application, final TreeItem applicationTreeItem) {
    Vector buttonTreeItems = WadlTreeRoot.removeChildrenXKeepButtons(applicationTreeItem, WadlXml.grammarsNode);
    
    if ((application.getGrammars() != null) && (application.getGrammars().getAllIncludes().size() > 0)) {
      GrammarsNode grammars = application.getGrammars(); 
      GrammarsItem grammarsItem = new GrammarsItem(grammars, applicationTreeItem);
      TreeItem grammarsTreeItem = new TreeItem(grammarsItem);
      grammarsTreeItem.setUserObject(WadlXml.grammarsNode);
      applicationTreeItem.addItem(grammarsTreeItem);
      final VerticalPanel includePanel = new VerticalPanel();      
      
      // include items      
      grammarsTreeItem.addItem(includePanel);
      IncludeItem.listIncludes(grammars, includePanel);    
      grammarsTreeItem.setState(SettingsDialog.treeItemsAlwaysOpen);
         
      // close grammars item       
      TreeItem grammarsCloseItem = new TreeItem(new GenericClosingItem(WadlXml.grammarsNode));
      grammarsCloseItem.setUserObject(WadlXml.grammarsNode);
      applicationTreeItem.addItem(grammarsCloseItem);
    }    
    else {
      if (!WadlTreeRoot.containsButton(buttonTreeItems, ApplicationItem.button + WadlXml.grammarsNode)) {      
        // add grammars button      
        HorizontalPanel addGrammarsPanel = new HorizontalPanel();
              
        Button addGrammarsButton = new Button(GuiFactory.strings.addGrammars());
        addGrammarsButton.addClickListener(new ClickListener() {
          public void onClick(Widget sender) {          
            application.addGrammars(new GrammarsNode(application));
            application.getGrammars().addInclude(GuiFactory.strings.newInclude());
            listGrammars(application, applicationTreeItem);                    
          }
        });
        addGrammarsPanel.add(addGrammarsButton);
        TreeItem addGrammarsTreeItem = new TreeItem(addGrammarsPanel);
        addGrammarsTreeItem.setUserObject(ApplicationItem.button + WadlXml.grammarsNode);
        applicationTreeItem.addItem(addGrammarsTreeItem);
      }
    }
    WadlTreeRoot.addButtons(buttonTreeItems, applicationTreeItem);
  }
}
