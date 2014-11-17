package com.google.code.apis.rest.client.Tree;

import java.util.Vector;

import com.google.code.apis.rest.client.GUI.GuiFactory;
import com.google.code.apis.rest.client.GUI.SettingsDialog;
import com.google.code.apis.rest.client.Util.SyntaxHighlighter;
import com.google.code.apis.rest.client.Wadl.ApplicationNode;
import com.google.code.apis.rest.client.Wadl.ResourcesNode;
import com.google.code.apis.rest.client.Wadl.WadlXml;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;

public class ResourcesItem extends Composite {
  public ResourcesItem(final ResourcesNode resources, final TreeItem applicationTreeItem) {    
    HorizontalPanel resourcesPanel = new HorizontalPanel();
    resourcesPanel.add(new HTML(SyntaxHighlighter.highlight("<" + WadlXml.resourcesNode + " " + WadlXml.resources_base + "=\"")));
        
    final TextBox input = new TextBox();
    input.setText(resources.getBase());
    input.addChangeListener(new ChangeListener() {
      public void onChange(Widget sender) {
        resources.updateBase(resources.getBase(), input.getText());
      }    
    });
    resourcesPanel.add(input);
        
    resourcesPanel.add(new HTML(SyntaxHighlighter.highlight("\" >")));
    
    // remove resources hyperlink
    Hyperlink removeResourcesLink = new Hyperlink(GuiFactory.strings.remove(), true, "");
    removeResourcesLink.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {        
        resources.getApplication().removeResources();        
        listResources(resources.getApplication(), applicationTreeItem);
      }
    });      
    resourcesPanel.add(removeResourcesLink);
        
    initWidget(resourcesPanel);
  }
  
  public static void listResources(final ApplicationNode application, final TreeItem applicationTreeItem) {
    Vector buttonTreeItems = WadlTreeRoot.removeChildrenXKeepButtons(applicationTreeItem, WadlXml.resourcesNode);
    
    ResourcesNode resources = application.getResources();
    if (resources != null) {      
      TreeItem resourcesTreeItem = new TreeItem(new ResourcesItem(resources, applicationTreeItem));
      resourcesTreeItem.setUserObject(WadlXml.resourcesNode);
      applicationTreeItem.addItem(resourcesTreeItem);      
          
      //  list all resource items    
      ResourceItem.listResources(resources, resourcesTreeItem);
      resourcesTreeItem.setState(SettingsDialog.treeItemsAlwaysOpen);
      
      // close resources item
      TreeItem resourcesCloseItem = new TreeItem(new GenericClosingItem(WadlXml.resourcesNode));
      resourcesCloseItem.setUserObject(WadlXml.resourcesNode);
      applicationTreeItem.addItem(resourcesCloseItem);
    } 
    else {
      if (!WadlTreeRoot.containsButton(buttonTreeItems, ApplicationItem.button + WadlXml.resourcesNode)) {      
        // add resources button      
        HorizontalPanel addResourcesPanel = new HorizontalPanel();
              
        Button addResourcesButton = new Button(GuiFactory.strings.addResources());
        addResourcesButton.addClickListener(new ClickListener() {
          public void onClick(Widget sender) {          
            application.addResources(new ResourcesNode(GuiFactory.strings.newResources(), application));
            listResources(application, applicationTreeItem);                    
          }
        });
        addResourcesPanel.add(addResourcesButton);
        TreeItem addResourcesTreeItem = new TreeItem(addResourcesPanel);
        addResourcesTreeItem.setUserObject(ApplicationItem.button + WadlXml.resourcesNode);
        applicationTreeItem.addItem(addResourcesTreeItem);
      }
    }
    WadlTreeRoot.addButtons(buttonTreeItems, applicationTreeItem);
    
  }
}
