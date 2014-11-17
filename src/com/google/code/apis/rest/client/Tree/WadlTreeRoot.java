package com.google.code.apis.rest.client.Tree;

import java.util.Iterator;
import java.util.Vector;

import com.google.code.apis.rest.client.GUI.SettingsDialog;
import com.google.code.apis.rest.client.Wadl.ApplicationNode;
import com.google.code.apis.rest.client.Wadl.WadlXml;

import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;

public class WadlTreeRoot {
  public Tree tree = new Tree();  

  public WadlTreeRoot() {   
    return;  
  }
  
  public Tree buildTree(ApplicationNode application) {
    // application root item
    ApplicationItem applicationItem = new ApplicationItem(application);
    TreeItem applicationTreeItem = new TreeItem(applicationItem);  
    tree.addItem(applicationTreeItem);
    
    // grammars item    
    GrammarsItem.listGrammars(application, applicationTreeItem);    
    
    // resources item          
    ResourcesItem.listResources(application, applicationTreeItem);             
    
    String requestString = "";
    if (application.getApplication() != null) {
      requestString = application.getApplication().getAnalyzer().getRequestString();
    }
    // method items    
    MethodItem.listMethods(application, applicationTreeItem, requestString);   
    
    // representations
    RepresentationItem.listRepresentations(application, applicationTreeItem);
    
    // faults
    FaultItem.listFaults(application, applicationTreeItem);
    
    // params    
    ParamItem.listParams(
        application, 
        applicationTreeItem, 
        requestString);    
    
    // resource_types
    ResourceTypeItem.listResourceTypes(application, applicationTreeItem);
    
    // close applications item
    GenericClosingItem applicationCloseItem = new GenericClosingItem(WadlXml.applicationNode);
    tree.addItem(applicationCloseItem);
    applicationTreeItem.setState(SettingsDialog.treeItemsAlwaysOpen);
    
    return tree;  
  }  
  
  public static Vector removeChildrenXKeepButtons(TreeItem parentTreeItem, String name) {    
    Vector itemsToDelete = new Vector();
    Vector buttonTreeItems = new Vector();    
    for (int i = 0; i < parentTreeItem.getChildCount(); i++) {
      TreeItem treeItem = parentTreeItem.getChild(i);      
      if (treeItem.getUserObject() != null) {        
        if (treeItem.getUserObject().equals(name)) {         
          itemsToDelete.add(treeItem);        
        }
        else if (((String) treeItem.getUserObject()).startsWith(ApplicationItem.button)) {          
          buttonTreeItems.add(treeItem);
          itemsToDelete.add(treeItem);      
        }
      }      
    }    
    Iterator itemsToDeleteIterator = itemsToDelete.iterator();
    while(itemsToDeleteIterator.hasNext()) {
      parentTreeItem.removeItem((TreeItem) itemsToDeleteIterator.next());
    }    
    return buttonTreeItems;
  }
  
  public static void addButtons(Vector buttonTreeItems, TreeItem parentTreeItem) {    
    Iterator buttonTreeItemIterator = buttonTreeItems.iterator();
    while(buttonTreeItemIterator.hasNext()) {      
      TreeItem treeItem = (TreeItem) buttonTreeItemIterator.next();   
      parentTreeItem.addItem(treeItem);      
    }
  }
  
  public static boolean containsButton(Vector buttonTreeItems, String buttonName) {    
    Iterator buttonTreeItemIterator = buttonTreeItems.iterator();
    while(buttonTreeItemIterator.hasNext()) {      
      TreeItem treeItem = (TreeItem) buttonTreeItemIterator.next();      
      if (treeItem.getUserObject().equals(buttonName)) {        
        return true;
      }
    }    
    return false;
  }
}
