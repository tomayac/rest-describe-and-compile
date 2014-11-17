package com.google.code.apis.rest.client.Tree;

import java.util.Vector;

import com.google.code.apis.rest.client.GUI.GuiFactory;
import com.google.code.apis.rest.client.GUI.SettingsDialog;
import com.google.code.apis.rest.client.Util.SyntaxHighlighter;
import com.google.code.apis.rest.client.Wadl.MethodNode;
import com.google.code.apis.rest.client.Wadl.RequestNode;
import com.google.code.apis.rest.client.Wadl.WadlXml;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;

public class RequestItem extends Composite {  
  public RequestItem(final MethodNode method, final TreeItem methodTreeItem) {
    HorizontalPanel containerPanel = new HorizontalPanel();    
    HTML request = new HTML(SyntaxHighlighter.highlight("<" + WadlXml.requestNode + ">"));
    containerPanel.add(request);
    
    // remove request hyperlink
    Hyperlink removeRequestLink = new Hyperlink(GuiFactory.strings.remove(), true, "");
    removeRequestLink.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {        
        method.removeRequest(method.getRequest());         
        listRequest(method, methodTreeItem);
      }
    });      
    containerPanel.add(removeRequestLink);
    
    initWidget(containerPanel);
  }
  
  public static void listRequest(final MethodNode method, final TreeItem methodTreeItem) {
    Vector buttonTreeItems = WadlTreeRoot.removeChildrenXKeepButtons(methodTreeItem, WadlXml.requestNode);    
    RequestNode request = method.getRequest();
    if (request == null) {
      Button addRequestButton = new Button(GuiFactory.strings.addRequest());
      addRequestButton.addClickListener(new ClickListener() {
        public void onClick(Widget sender) {              
          method.addRequest(new RequestNode(method, method.getApplication()));          
          listRequest(method, methodTreeItem);          
        }
      });
      TreeItem addRequestTreeItem = new TreeItem(addRequestButton);
      addRequestTreeItem.setUserObject(WadlXml.requestNode);
      methodTreeItem.addItem(addRequestTreeItem);
      return;
    }
    RequestItem requestItem = new RequestItem(method, methodTreeItem);
    TreeItem requestTreeItem = new TreeItem(requestItem);
    requestTreeItem.setUserObject(WadlXml.requestNode);    
    methodTreeItem.addItem(requestTreeItem);
    methodTreeItem.setState(SettingsDialog.treeItemsAlwaysOpen);    
    
    // check whether the request came from an analyzed uri...
    String requestString = "";
    if (request.getApplication() != null) {
      requestString = request.getApplication().getAnalyzer().getRequestString();
    }        
    ParamItem.listParams(
        request,
        requestTreeItem,
        requestString);
        
    // representations
    RepresentationItem.listRepresentations(request, requestTreeItem);  
    
    // close request item
    GenericClosingItem requestCloseItem = new GenericClosingItem(WadlXml.requestNode);
    TreeItem requestCloseTreeItem = new TreeItem(requestCloseItem);
    requestCloseTreeItem.setUserObject(WadlXml.requestNode);
    methodTreeItem.addItem(requestCloseTreeItem);
    
    WadlTreeRoot.addButtons(buttonTreeItems, methodTreeItem);
    
    methodTreeItem.setState(SettingsDialog.treeItemsAlwaysOpen);
  }
}
