/**
 * 
 */
package com.google.code.apis.rest.client.Tree;

import java.util.Iterator;
import java.util.Vector;

import com.google.code.apis.rest.client.GUI.GuiFactory;
import com.google.code.apis.rest.client.GUI.SettingsDialog;
import com.google.code.apis.rest.client.Util.SyntaxHighlighter;
import com.google.code.apis.rest.client.Wadl.ApplicationNode;
import com.google.code.apis.rest.client.Wadl.ResourceTypeNode;
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

/**
 * @author tsteiner
 *
 */
public class ResourceTypeItem extends Composite {

  /**
   * @param resourceType
   * @param applicationTreeItem
   */
  public ResourceTypeItem(final ResourceTypeNode resourceType, final TreeItem applicationTreeItem) {        
    HorizontalPanel resourceTypePanel = new HorizontalPanel();
    resourceTypePanel.add(new HTML(SyntaxHighlighter.highlight("<" + WadlXml.resourceTypeNode + " " + WadlXml.resource_type_id + "=\"")));
        
    final TextBox input = new TextBox();
    input.setText(resourceType.getId());
    input.addChangeListener(new ChangeListener() {
      public void onChange(Widget sender) {
        resourceType.updateId(resourceType.getId(), input.getText());
      }    
    });
    resourceTypePanel.add(input);
    
    resourceTypePanel.add(new HTML(SyntaxHighlighter.highlight("\" >")));
        
    Hyperlink removeResourceTypeLink = new Hyperlink(GuiFactory.strings.remove(), true, "");
    removeResourceTypeLink.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {        
        resourceType.getApplication().removeResourceType(resourceType);         
        listResourceTypes(resourceType.getApplication(), applicationTreeItem);
      }
    });      
    resourceTypePanel.add(removeResourceTypeLink);
        
    initWidget(resourceTypePanel);
  }

  /**
   * @param application
   * @param applicationTreeItem
   */
  public static void listResourceTypes(final ApplicationNode application, final TreeItem applicationTreeItem) {
    Vector buttonTreeItems = WadlTreeRoot.removeChildrenXKeepButtons(applicationTreeItem, WadlXml.resourceTypeNode);
    
    Vector allResourceTypes = application.getAllResourceTypes();
    Iterator allResourceTypesIterator = allResourceTypes.iterator();
    while (allResourceTypesIterator.hasNext()) {
      ResourceTypeNode resourceType = (ResourceTypeNode) allResourceTypesIterator.next();
      ResourceTypeItem resourceTypeItem = new ResourceTypeItem(resourceType, applicationTreeItem);
      TreeItem resourceTypeTreeItem = new TreeItem(resourceTypeItem);
      applicationTreeItem.addItem(resourceTypeTreeItem);
      resourceTypeTreeItem.setUserObject(WadlXml.resourceTypeNode);
      resourceTypeTreeItem.setState(SettingsDialog.treeItemsAlwaysOpen);
      
      String requestString = "";
      if (resourceType.getApplication() != null) {
        requestString = resourceType.getApplication().getAnalyzer().getRequestString();
      }
      // methods      
      MethodItem.listMethods(resourceType, resourceTypeTreeItem, requestString);      
      
      // params      
      ParamItem.listParams(
          resourceType, 
          resourceTypeTreeItem, 
          requestString);
      
      // close resource type item
      GenericClosingItem resourceTypeCloseItem = new GenericClosingItem(WadlXml.resourceTypeNode);
      TreeItem resourceTypeCloseTreeItem = new TreeItem(resourceTypeCloseItem);
      resourceTypeCloseTreeItem.setUserObject(WadlXml.resourceTypeNode);
      applicationTreeItem.addItem(resourceTypeCloseTreeItem);      
    }   
    
    if (!WadlTreeRoot.containsButton(buttonTreeItems, ApplicationItem.button + WadlXml.resourceTypeNode)) {
      // add resource type button
      HorizontalPanel addResourceTypePanel = new HorizontalPanel();            
      Button addResourceTypeButton = new Button(GuiFactory.strings.addResourceType());
      addResourceTypeButton.addClickListener(new ClickListener() {
        public void onClick(Widget sender) {          
          application.addResourceType(new ResourceTypeNode(GuiFactory.strings.newResourceType(), application));                               
          listResourceTypes(application, applicationTreeItem);        
        }
      });
      addResourceTypePanel.add(addResourceTypeButton);
      TreeItem addResourceTypeTreeItem = new TreeItem(addResourceTypePanel);
      addResourceTypeTreeItem.setUserObject(ApplicationItem.button + WadlXml.resourceTypeNode);
      applicationTreeItem.addItem(addResourceTypeTreeItem);
    }    
    WadlTreeRoot.addButtons(buttonTreeItems, applicationTreeItem);
  }
}
