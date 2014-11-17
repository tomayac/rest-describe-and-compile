package com.google.code.apis.rest.client.Tree;

import java.util.Vector;

import com.google.code.apis.rest.client.GUI.GuiFactory;
import com.google.code.apis.rest.client.GUI.SettingsDialog;
import com.google.code.apis.rest.client.Util.SyntaxHighlighter;
import com.google.code.apis.rest.client.Wadl.MethodNode;
import com.google.code.apis.rest.client.Wadl.ResponseNode;
import com.google.code.apis.rest.client.Wadl.WadlXml;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;

public class ResponseItem extends Composite {
  public ResponseItem(final MethodNode method, final TreeItem methodTreeItem) {
    HorizontalPanel containerPanel = new HorizontalPanel();    
    HTML response = new HTML(SyntaxHighlighter.highlight("<" + WadlXml.responseNode + ">"));
    containerPanel.add(response);    
    
    // remove response hyperlink
    Hyperlink removeResponseLink = new Hyperlink(GuiFactory.strings.remove(), true, "");
    removeResponseLink.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {        
        method.removeResponse(method.getResponse());         
        listResponse(method, methodTreeItem);
      }
    });      
    containerPanel.add(removeResponseLink);
    
    initWidget(containerPanel);
  }

  public static void listResponse(final MethodNode method, final TreeItem methodTreeItem) {
    Vector buttonTreeItems = WadlTreeRoot.removeChildrenXKeepButtons(methodTreeItem, WadlXml.responseNode);
    
    ResponseNode response = method.getResponse();
    if (response == null) {
      Button addResponseButton = new Button(GuiFactory.strings.addResponse());
      addResponseButton.addClickListener(new ClickListener() {
        public void onClick(Widget sender) {              
          method.addResponse(new ResponseNode(method, method.getApplication()));          
          listResponse(method, methodTreeItem);          
        }
      });
      TreeItem addResponseTreeItem = new TreeItem(addResponseButton);
      addResponseTreeItem.setUserObject(WadlXml.responseNode);
      methodTreeItem.addItem(addResponseTreeItem);
      return;
    }
    ResponseItem responseItem = new ResponseItem(method, methodTreeItem);
    TreeItem responseTreeItem = new TreeItem(responseItem);
    responseTreeItem.setUserObject(WadlXml.responseNode);
    
    methodTreeItem.addItem(responseTreeItem);
    responseTreeItem.setState(SettingsDialog.treeItemsAlwaysOpen);
    
    // representations
    RepresentationItem.listRepresentations(response, responseTreeItem);
    
    // faults
    FaultItem.listFaults(response, responseTreeItem);
    
    // parameters
    String requestString = "";
    if (response.getApplication() != null) {
      requestString = response.getApplication().getAnalyzer().getRequestString();
    }
    ParamItem.listParams(response,
        responseTreeItem,
        requestString);
    
    GenericClosingItem responseCloseItem = new GenericClosingItem(WadlXml.responseNode);
    TreeItem responseCloseTreeItem = new TreeItem(responseCloseItem);
    responseCloseTreeItem.setUserObject(WadlXml.responseNode);
    methodTreeItem.addItem(responseCloseTreeItem);
    
    WadlTreeRoot.addButtons(buttonTreeItems, methodTreeItem);
    
    methodTreeItem.setState(SettingsDialog.treeItemsAlwaysOpen);    
  }
}
