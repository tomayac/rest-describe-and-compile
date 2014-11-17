package com.google.code.apis.rest.client.Tree;

import java.util.Iterator;
import java.util.Vector;

import com.google.code.apis.rest.client.GUI.GuiFactory;
import com.google.code.apis.rest.client.GUI.SettingsDialog;
import com.google.code.apis.rest.client.Util.SyntaxHighlighter;
import com.google.code.apis.rest.client.Wadl.GenericNode;
import com.google.code.apis.rest.client.Wadl.ResourceNode;
import com.google.code.apis.rest.client.Wadl.ResourcesNode;
import com.google.code.apis.rest.client.Wadl.WadlXml;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;

public class ResourceItem extends Composite {
  public ResourceItem(final ResourceNode resource, final Object parentNode, final TreeItem parentTreeItem) {    
    HorizontalPanel resourcePanel = new HorizontalPanel();
    if (resource.getHref() == null) { 
      resourcePanel.add(new HTML(SyntaxHighlighter.highlight("<" + WadlXml.resourceNode + " " + WadlXml.resource_path + "=\"")));
      
      final TextBox input = new TextBox();
      input.setText(resource.getPath());
      input.addChangeListener(new ChangeListener() {
        public void onChange(Widget sender) {
          resource.updatePath(resource.getPath(), input.getText());
        }
      });
      resourcePanel.add(input);
      
      resourcePanel.add(new HTML(SyntaxHighlighter.highlight("\" >")));
  
      Hyperlink removeResourceLink = new Hyperlink(GuiFactory.strings.remove(), true, "");
      removeResourceLink.addClickListener(new ClickListener() {
        public void onClick(Widget sender) {
          if (parentNode instanceof ResourceNode) {
            ((ResourceNode) parentNode).removeResource(resource);
          }
          else if (parentNode instanceof ResourcesNode) {
            ((ResourcesNode) parentNode).removeResource(resource);
          } 
          listResources(parentNode, parentTreeItem);
        }
      });      
      resourcePanel.add(removeResourceLink);
    }
    else {
      resourcePanel.add(new HTML(
          SyntaxHighlighter.highlight("<" + WadlXml.resourceNode + " ") +
          SyntaxHighlighter.highlight(WadlXml.resource_href + "=\"" + resource.getHref() + "\" />")));
    }
    
    initWidget(resourcePanel);
  }

  public static void listResources(final Object parentNode, final TreeItem parentTreeItem) {
    Vector buttonTreeItems = WadlTreeRoot.removeChildrenXKeepButtons(parentTreeItem, WadlXml.resourceNode);
    
    Vector allResources = null;
    if (parentNode instanceof ResourceNode) {
      allResources = ((ResourceNode) parentNode).getAllResources();
    }
    else if (parentNode instanceof ResourcesNode) {
      allResources = ((ResourcesNode) parentNode).getAllResources();
    }     
    Iterator allResourcesIterator = allResources.iterator();
    while (allResourcesIterator.hasNext()) {      
      final ResourceNode resource = (ResourceNode) allResourcesIterator.next(); 
      ResourceItem resourceItem = new ResourceItem(resource, parentNode, parentTreeItem);
      final TreeItem resourceTreeItem = new TreeItem(resourceItem);      
      resourceTreeItem.setUserObject(WadlXml.resourceNode);
      parentTreeItem.addItem(resourceTreeItem);
      resourceTreeItem.setState(SettingsDialog.treeItemsAlwaysOpen);     
                  
      String requestString = "";
      if (resource.getApplication() != null) {
        requestString = resource.getApplication().getAnalyzer().getRequestString();
      }
      // parameters      
      ParamItem.listParams(
          resource,
          resourceTreeItem,
          requestString);
      
      // methods      
      MethodItem.listMethods(resource, resourceTreeItem, requestString);     
      
      // resource     
      listResources((Object) resource, resourceTreeItem);     
      
      // close resource item
      GenericClosingItem resourceCloseItem = new GenericClosingItem(WadlXml.resourceNode);
      TreeItem resourceCloseTreeItem = new TreeItem(resourceCloseItem);
      resourceCloseTreeItem.setUserObject(WadlXml.resourceNode);
      parentTreeItem.addItem(resourceCloseTreeItem);
    }
    
   if (!WadlTreeRoot.containsButton(buttonTreeItems, ApplicationItem.button + WadlXml.resourceNode)) {
      // add resource button
      HorizontalPanel addResourcePanel = new HorizontalPanel();            
      Button addResourceButton = new Button(GuiFactory.strings.addResource());
      addResourceButton.addClickListener(new ClickListener() {
        public void onClick(Widget sender) {     
          if (parentNode instanceof ResourceNode) {
            ((ResourceNode) parentNode).addResource(new ResourceNode(GuiFactory.strings.newResource(), (GenericNode) parentNode, ((GenericNode) parentNode).getApplication()));
          }
          else if (parentNode instanceof ResourcesNode) {
            ((ResourcesNode) parentNode).addResource(new ResourceNode(GuiFactory.strings.newResource(), (GenericNode) parentNode, ((GenericNode) parentNode).getApplication()));
          }                      
          listResources(parentNode, parentTreeItem);        
        }
      });
      addResourcePanel.add(addResourceButton);
      TreeItem addResourceTreeItem = new TreeItem(addResourcePanel);
      addResourceTreeItem.setUserObject(ApplicationItem.button + WadlXml.resourceNode);
      parentTreeItem.addItem(addResourceTreeItem);
    }
    
    WadlTreeRoot.addButtons(buttonTreeItems, parentTreeItem);
    
    parentTreeItem.setState(SettingsDialog.treeItemsAlwaysOpen);    
  }  
}
