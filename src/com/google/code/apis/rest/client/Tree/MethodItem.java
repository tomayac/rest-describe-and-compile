package com.google.code.apis.rest.client.Tree;

import java.util.Iterator;
import java.util.Vector;

import com.google.code.apis.rest.client.GUI.GuiFactory;
import com.google.code.apis.rest.client.GUI.ParameterTree;
import com.google.code.apis.rest.client.GUI.SettingsDialog;
import com.google.code.apis.rest.client.Util.SyntaxHighlighter;
import com.google.code.apis.rest.client.Wadl.ApplicationNode;
import com.google.code.apis.rest.client.Wadl.GenericNode;
import com.google.code.apis.rest.client.Wadl.MethodNode;
import com.google.code.apis.rest.client.Wadl.ResourceNode;
import com.google.code.apis.rest.client.Wadl.ResourceTypeNode;
import com.google.code.apis.rest.client.Wadl.WadlXml;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class MethodItem extends Composite {
  public static final String get = "GET";
  public static final String post = "POST";
  public static final String put = "PUT";
  public static final String delete = "DELETE";
  public static final String head = "HEAD";
  public static final String[] methodNames = {
    get,
    post,
    put,
    delete,
    head
  };
  private String requestString;
   
  public MethodItem(final MethodNode method, final Object parentNode, final TreeItem parentTreeItem, String reqString) {
    requestString = reqString;   
    
    if (method.getHref() == null) {
      // name listbox
      final ListBox nameListBox = new ListBox();
      addNames(nameListBox);          
      nameListBox.addChangeListener(new ChangeListener() {
        public void onChange(Widget sender) {
          method.updateName(method.getName(), nameListBox.getItemText(nameListBox.getSelectedIndex()));
          requestString = method.getApplication().getAnalyzer().getRequestString();
          updateHttpMethod(method.getName());
          method.getApplication().getAnalyzer().setRequestString(requestString);
        }      
      });     
      
      // remove method link
      Hyperlink removeMethodLink = new Hyperlink(GuiFactory.strings.remove(), true, "");
      removeMethodLink.addClickListener(new ClickListener() {
        public void onClick(Widget sender) {            
          if (parentNode instanceof ResourceNode) {
            ((ResourceNode) parentNode).removeMethod(method);              
            listMethods(parentNode,  parentTreeItem, requestString);
          }
          else if (parentNode instanceof ApplicationNode) {
            ((ApplicationNode) parentNode).removeMethod(method);              
            listMethods(parentNode, parentTreeItem, requestString);
          }
          else if (parentNode instanceof ResourceTypeNode) {
            ((ResourceTypeNode) parentNode).removeMethod(method);
            listMethods(parentNode, parentTreeItem, requestString);
          }
        }
      });
      
      // need more than one line
      if (method.getId() != null) { 
        VerticalPanel methodPanel = new VerticalPanel();
        
        methodPanel.add(new HTML(SyntaxHighlighter.highlight("<" + WadlXml.methodNode + " ")));
        HorizontalPanel containerPanel = new HorizontalPanel();
        methodPanel.add(containerPanel);
        VerticalPanel methodPanelLeft = new VerticalPanel();  
        methodPanelLeft.setWidth("1em");
        methodPanelLeft.add(new HTML("&nbsp;"));
        VerticalPanel methodPanelRight = new VerticalPanel();  
        containerPanel.add(methodPanelLeft);        
        containerPanel.add(methodPanelRight);
        HorizontalPanel namePanel = new HorizontalPanel();
        methodPanelRight.add(namePanel);
        namePanel.add(new HTML(SyntaxHighlighter.highlight(WadlXml.method_name + "=\"")));
        namePanel.add(nameListBox);
        namePanel.add(new HTML(SyntaxHighlighter.highlight("\"")));
        
        HorizontalPanel methodIdPanel = new HorizontalPanel();
        methodPanelRight.add(methodIdPanel);
        methodIdPanel.add(new HTML(SyntaxHighlighter.highlight(WadlXml.method_id + "=\"")));
        final TextBox idTextBox = new TextBox();
        idTextBox.addChangeListener(new ChangeListener() {
          public void onChange(Widget sender) {
            method.updateId(method.getId(), idTextBox.getText());
          }
        });    
        idTextBox.setText(method.getId());
        methodIdPanel.add(idTextBox);
        methodIdPanel.add(new HTML(SyntaxHighlighter.highlight("\"")));
        
        HorizontalPanel lastPanel = new HorizontalPanel();
        methodPanel.add(lastPanel);
        lastPanel.add(new HTML(SyntaxHighlighter.highlight(">")));
        lastPanel.add(removeMethodLink);
        initWidget(methodPanel);
      }
      // put it all in one line
      else {      
        HorizontalPanel methodPanel = new HorizontalPanel();
        methodPanel.add(new HTML(SyntaxHighlighter.highlight("<" + WadlXml.methodNode + " " + WadlXml.method_name + "=\"")));        
        methodPanel.add(nameListBox);
        methodPanel.add(new HTML(SyntaxHighlighter.highlight("\">")));
        methodPanel.add(removeMethodLink);
        initWidget(methodPanel);
      }
    }
    else {      
      HorizontalPanel methodPanel = new HorizontalPanel();
      methodPanel.add(new HTML(
          SyntaxHighlighter.highlight("<" + WadlXml.methodNode + " ") +
          SyntaxHighlighter.highlight(WadlXml.method_href + "=\"" + method.getHref() + "\" />")));
      initWidget(methodPanel);
    }   
  }
  
  /**
   * @param name 
   * 
   */
  protected void updateHttpMethod(String name) {                
    ListBox httpMethodListBox = ReferenceManager.getHttpMethodListBox(requestString);
    httpMethodListBox.setSelectedIndex(indexOfName(name));
    TextBox requestUriTextBox = ReferenceManager.getRequestUriTextBox(requestString);
    ParameterTree parameterTree = ReferenceManager.getParameterTree(requestString);
    parameterTree.updateReferences(requestUriTextBox, requestUriTextBox.getText());
    parameterTree.updateTree();   
    requestString = 
      httpMethodListBox.getItemText(httpMethodListBox.getSelectedIndex()) +
      " " + requestUriTextBox.getText();
    
  }

  private void addNames(ListBox nameListBox) {
    for (int i = 0; i < methodNames.length; i++) {
      nameListBox.addItem(methodNames[i]);
    }    
  }
  
  public static int indexOfName(String name) {
    for (int i = 0; i < methodNames.length; i++) {
      if (methodNames[i].equals(name)) return i;
    }
    return -1;
  }

  public static void listMethods(final Object parentNode, final TreeItem parentTreeItem, final String requestString) {
    Vector methods = null;
    if (parentNode instanceof ApplicationNode) {
      methods = ((ApplicationNode) parentNode).getAllMethods();  
    }
    else if (parentNode instanceof ResourceNode) {
      methods = ((ResourceNode) parentNode).getAllMethods();
    }
    else if (parentNode instanceof ResourceTypeNode) {
      methods = ((ResourceTypeNode) parentNode).getAllMethods();
    }    
    
    Vector buttonTreeItems = WadlTreeRoot.removeChildrenXKeepButtons(parentTreeItem, WadlXml.methodNode);
    
    Iterator methodIterator = methods.iterator();    
    while (methodIterator.hasNext()) {
      MethodNode method = (MethodNode) methodIterator.next();
      MethodItem methodItem = new MethodItem(method, method.getParent(), parentTreeItem, requestString);
      TreeItem methodTreeItem = new TreeItem(methodItem);      
      methodTreeItem.setUserObject(WadlXml.methodNode);
      parentTreeItem.addItem(methodTreeItem);     
      
      if (method.getHref() == null) {
        // request
        RequestItem.listRequest(method, methodTreeItem);
        
        // response
        ResponseItem.listResponse(method, methodTreeItem);
       
        // close method item
        GenericClosingItem methodCloseItem = new GenericClosingItem(WadlXml.methodNode);
        TreeItem methodCloseTreeItem = new TreeItem(methodCloseItem);
        methodCloseTreeItem.setUserObject(WadlXml.methodNode);
        parentTreeItem.addItem(methodCloseTreeItem);
      }
      methodTreeItem.setState(SettingsDialog.treeItemsAlwaysOpen);      
    }
        
    if (!WadlTreeRoot.containsButton(buttonTreeItems, ApplicationItem.button + WadlXml.methodNode)) {      
      // add method button      
      HorizontalPanel addMethodPanel = new HorizontalPanel();
            
      Button addMethodButton = new Button(GuiFactory.strings.addMethod());
      addMethodButton.addClickListener(new ClickListener() {
        public void onClick(Widget sender) {
          if (parentNode instanceof ResourceNode) {
            ((ResourceNode) parentNode).directlyAddMethod(new MethodNode(GuiFactory.strings.newMethod(), (GenericNode) parentNode, ((GenericNode) parentNode).getApplication()));
            listMethods(parentNode, parentTreeItem, requestString);
          }
          else if (parentNode instanceof ApplicationNode) {
            MethodNode method = new MethodNode(GuiFactory.strings.newMethod(), (GenericNode) parentNode, (ApplicationNode) parentNode);
            method.setId(GuiFactory.strings.newId());
            ((ApplicationNode) parentNode).directlyAddMethod(method);
            listMethods(parentNode, parentTreeItem, requestString);
          }
          else if (parentNode instanceof ResourceTypeNode) {
            ((ResourceTypeNode) parentNode).directlyAddMethod(new MethodNode(GuiFactory.strings.newMethod(), (GenericNode) parentNode, ((GenericNode) parentNode).getApplication()));
            listMethods(parentNode, parentTreeItem, requestString);
          }          
        }
      });
      addMethodPanel.add(addMethodButton);
      TreeItem addMethodTreeItem = new TreeItem(addMethodPanel);
      addMethodTreeItem.setUserObject(ApplicationItem.button + WadlXml.methodNode);
      parentTreeItem.addItem(addMethodTreeItem);
    }
    
    WadlTreeRoot.addButtons(buttonTreeItems, parentTreeItem);
    
    parentTreeItem.setState(SettingsDialog.treeItemsAlwaysOpen);    
  }
}
