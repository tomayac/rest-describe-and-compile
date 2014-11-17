/**
 * 
 */
package com.google.code.apis.rest.client.GUI;

import java.util.Iterator;
import java.util.Vector;

import com.google.code.apis.rest.client.Tree.WadlTreeRoot;
import com.google.code.apis.rest.client.Wadl.Analyzer;
import com.google.code.apis.rest.client.Wadl.GrammarsNode;
import com.google.code.apis.rest.client.Wadl.NamespaceAttribute;
import com.google.code.apis.rest.client.Wadl.WadlXml;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

/**
 * @author tsteiner
 *
 */
public class DiscoveredItemsDialog {
  private static Vector errorStatuses = new Vector();
  private static Vector schemaLocations = new Vector();
  private static Vector defaultNamespace = new Vector();
  private static Vector otherNamespaces = new Vector();
  
  public DiscoveredItemsDialog() {
    
  }
  
  public static void show() {  
    String panelHeight = (int) Math.floor(com.google.gwt.user.client.Window.getClientHeight() / 8) + "px";
    final DialogBox dialog = new DialogBox();
    dialog.setText(GuiFactory.strings.requestDataDialog());    
    VerticalPanel containerPanel = new VerticalPanel();    
    
    // error statuses
    final Button addErrorsButton = new Button(GuiFactory.strings.addErrors());
    if (!errorStatuses.isEmpty()) {
      final Vector reallyAddFaults = new Vector();
      containerPanel.add(new HTML("<br />" + GuiFactory.strings.provokedFaults()));
      ScrollPanel errorContainerPanel = new ScrollPanel();
      errorContainerPanel.setStyleName("restDescribe-wadlArea");
      errorContainerPanel.setHeight(panelHeight);      
      VerticalPanel errorPanel = new VerticalPanel();
      errorContainerPanel.add(errorPanel);
      containerPanel.add(errorContainerPanel);
      
      Iterator errorIterator = errorStatuses.iterator();
      int i = 0;
      HorizontalPanel horizontalErrorPanel = new HorizontalPanel();      
      errorPanel.add(horizontalErrorPanel);
      Grid grid = new Grid(errorStatuses.size(), 2);      
      horizontalErrorPanel.add(grid);
      while (errorIterator.hasNext()) {        
        final String error = (String) errorIterator.next();
        reallyAddFaults.add(error);
        final CheckBox checkBox = new CheckBox();
        checkBox.setChecked(true);
        checkBox.addClickListener(new ClickListener() {
          public void onClick(Widget sender) {
            if (checkBox.isChecked() &&
                !reallyAddFaults.contains(error)) {              
              reallyAddFaults.add(error);
            }           
            else {
              if (reallyAddFaults.contains(error)) {
                reallyAddFaults.remove(error);
              }
            }
          }          
        });
        
        grid.setWidget(i, 0, checkBox);        
        grid.setText(i, 1, error);       
        i++;
      }      
      addErrorsButton.setEnabled(false);
      addErrorsButton.addClickListener(new ClickListener() {
        public void onClick(Widget sender) {
          // TODO          
        }
        
      });
      containerPanel.add(addErrorsButton);
    }
    
    // schema locations 
    final Button addSchemaButton = new Button(GuiFactory.strings.addSchema());
    if (!schemaLocations.isEmpty()) {
      final Vector reallyAddSchemaLocations = new Vector();
      containerPanel.add(new HTML("<br />" + GuiFactory.strings.schemaLocations()));
      ScrollPanel schemaLocationContainerPanel = new ScrollPanel();
      schemaLocationContainerPanel.setStyleName("restDescribe-wadlArea");
      schemaLocationContainerPanel.setHeight(panelHeight);
      VerticalPanel schemaLocationPanel = new VerticalPanel();
      schemaLocationContainerPanel.add(schemaLocationPanel);
      containerPanel.add(schemaLocationContainerPanel);
      
      Iterator schemaLocationIterator = schemaLocations.iterator();
      int i = 0;
      HorizontalPanel horizontalSchemaPanel = new HorizontalPanel();
      schemaLocationPanel.add(horizontalSchemaPanel);
      Grid grid = new Grid(schemaLocations.size(), 2);
      horizontalSchemaPanel.add(grid);
      while (schemaLocationIterator.hasNext()) {        
        final String schemaLocation = (String) schemaLocationIterator.next();
        reallyAddSchemaLocations.add(schemaLocation);
        final CheckBox checkBox = new CheckBox();
        checkBox.setChecked(true);
        checkBox.addClickListener(new ClickListener() {
          public void onClick(Widget sender) {
            if (checkBox.isChecked() &&
                !reallyAddSchemaLocations.contains(schemaLocation)) {              
              reallyAddSchemaLocations.add(schemaLocation);
            }           
            else {
              if (reallyAddSchemaLocations.contains(schemaLocation)) {
                reallyAddSchemaLocations.remove(schemaLocation);
              }
            }
          }          
        });
        
        grid.setWidget(i, 0, checkBox);        
        grid.setText(i, 1, schemaLocation);
        i++;
      }            
      addSchemaButton.addClickListener(new ClickListener() {
        public void onClick(Widget sender) {                    
          Iterator addSchemaIterator = reallyAddSchemaLocations.iterator();
          while (addSchemaIterator.hasNext()) {
            String include = (String) addSchemaIterator.next();
            if (Analyzer.application.getGrammars() == null) {
              Analyzer.application.addGrammars(new GrammarsNode(Analyzer.application));
            }
            Analyzer.application.grammars.addInclude(include);
          }
          WadlTreeRoot wadlTreeRoot = new WadlTreeRoot();
          Tree wadlTree = wadlTreeRoot.buildTree(Analyzer.application);
          WadlPanel.wadlArea.clear();
          WadlPanel.wadlArea.setWidget(wadlTree); 
        }          
      });
      containerPanel.add(addSchemaButton);
    }

    // default namespace
    final Button addNamespaceButton = new Button(GuiFactory.strings.addNamespace());
    if (!defaultNamespace.isEmpty()) {
      final Vector reallyAddDefaultNamespace = new Vector();
      containerPanel.add(new HTML("<br />" + GuiFactory.strings.defaultNamespaces()));
      ScrollPanel defaultNamespaceContainerPanel = new ScrollPanel();
      defaultNamespaceContainerPanel.setStyleName("restDescribe-wadlArea");
      defaultNamespaceContainerPanel.setHeight(panelHeight);
      VerticalPanel defaultNamespacePanel = new VerticalPanel();
      defaultNamespaceContainerPanel.add(defaultNamespacePanel);
      containerPanel.add(defaultNamespaceContainerPanel);
      
      Iterator defaultNamespaceIterator = defaultNamespace.iterator();
      int i = 0;
      HorizontalPanel horizontalNamespacePanel = new HorizontalPanel();
      defaultNamespacePanel.add(horizontalNamespacePanel);
      Grid grid = new Grid(defaultNamespace.size(), 2);
      horizontalNamespacePanel.add(grid);
      while (defaultNamespaceIterator.hasNext()) {        
        final String defaultNamespace = (String) defaultNamespaceIterator.next();
        reallyAddDefaultNamespace.add(defaultNamespace);
        final CheckBox checkBox = new CheckBox();
        checkBox.setChecked(true);
        checkBox.addClickListener(new ClickListener() {
          public void onClick(Widget sender) {
            if (checkBox.isChecked() &&
                !reallyAddDefaultNamespace.contains(defaultNamespace)) {              
              reallyAddDefaultNamespace.add(defaultNamespace);
            }           
            else {
              if (reallyAddDefaultNamespace.contains(defaultNamespace)) {
                reallyAddDefaultNamespace.remove(defaultNamespace);
              }
            }
          }          
        });
        
        grid.setWidget(i, 0, checkBox);        
        grid.setText(i, 1, defaultNamespace);
        i++;
      }            
      addNamespaceButton.addClickListener(new ClickListener() {
        public void onClick(Widget sender) {
          Iterator addNamespaceIterator = reallyAddDefaultNamespace.iterator();
          while (addNamespaceIterator.hasNext()) {
            String namespace = (String) addNamespaceIterator.next();            
            Analyzer.application.addNamespace(
                new NamespaceAttribute(null, namespace));
          }
          WadlTreeRoot wadlTreeRoot = new WadlTreeRoot();
          Tree wadlTree = wadlTreeRoot.buildTree(Analyzer.application);
          WadlPanel.wadlArea.clear();
          WadlPanel.wadlArea.setWidget(wadlTree);
        }          
      });
      containerPanel.add(addNamespaceButton);
    }
    final Button addOtherNamespacesButton = new Button(GuiFactory.strings.addOtherNamespace());
    // other namespaces
    if (!otherNamespaces.isEmpty()) {
      final Vector reallyAddOtherNamespaces = new Vector();
      containerPanel.add(new HTML("<br />" + GuiFactory.strings.otherNamespaces()));
      ScrollPanel otherNamespacesContainerPanel = new ScrollPanel();
      otherNamespacesContainerPanel.setStyleName("restDescribe-wadlArea");      
      otherNamespacesContainerPanel.setHeight(panelHeight);
      VerticalPanel otherNamespacesPanel = new VerticalPanel();
      otherNamespacesContainerPanel.add(otherNamespacesPanel);
      containerPanel.add(otherNamespacesContainerPanel);
      
      Iterator otherNamespacesIterator = otherNamespaces.iterator();
      int i = 0;
      HorizontalPanel horizontalNamespacePanel = new HorizontalPanel();
      otherNamespacesPanel.add(horizontalNamespacePanel);
      Grid grid = new Grid(otherNamespaces.size(), 2);
      horizontalNamespacePanel.add(grid);
      while (otherNamespacesIterator.hasNext()) {        
        final String otherNamespace = (String) otherNamespacesIterator.next();
        reallyAddOtherNamespaces.add(otherNamespace);
        final CheckBox checkBox = new CheckBox();
        checkBox.setChecked(true);
        checkBox.addClickListener(new ClickListener() {
          public void onClick(Widget sender) {
            if (checkBox.isChecked() &&
                !reallyAddOtherNamespaces.contains(otherNamespace)) {              
              reallyAddOtherNamespaces.add(otherNamespace);
            }           
            else {
              if (reallyAddOtherNamespaces.contains(otherNamespace)) {
                reallyAddOtherNamespaces.remove(otherNamespace);
              }
            }
          }          
        });
        
        grid.setWidget(i, 0, checkBox);        
        grid.setText(i, 1, otherNamespace);
        i++;
      }            
      addOtherNamespacesButton.addClickListener(new ClickListener() {
        public void onClick(Widget sender) {
          Iterator addNamespaceIterator = reallyAddOtherNamespaces.iterator();
          while (addNamespaceIterator.hasNext()) {
            String namespace = (String) addNamespaceIterator.next();            
            Analyzer.application.addNamespace(
                new NamespaceAttribute(null, namespace));
          }
          WadlTreeRoot wadlTreeRoot = new WadlTreeRoot();
          Tree wadlTree = wadlTreeRoot.buildTree(Analyzer.application);
          WadlPanel.wadlArea.clear();
          WadlPanel.wadlArea.setWidget(wadlTree);
        }          
      });
      containerPanel.add(addOtherNamespacesButton);
    }
    
    Button closeButton = new Button(GuiFactory.strings.ok());
    closeButton.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        if (!otherNamespaces.isEmpty()) {
          addOtherNamespacesButton.click();
        }
        if (!defaultNamespace.isEmpty()) {
          addNamespaceButton.click();
        }
        if (!errorStatuses.isEmpty()) {
          addErrorsButton.click();
        }
        if (!schemaLocations.isEmpty()) {
          addSchemaButton.click();
        }        
        dialog.hide();
        GuiFactory.blockScreen(false);
      }
    });
    
    Button cancelButton = new Button(GuiFactory.strings.cancel());
    cancelButton.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        dialog.hide();
        GuiFactory.blockScreen(false);
      }
    });
    
    containerPanel.add(new HTML("<br />"));
    HorizontalPanel buttonPanel = new HorizontalPanel();
    buttonPanel.add(closeButton);
    buttonPanel.add(cancelButton);
    containerPanel.add(buttonPanel);
    containerPanel.setCellHorizontalAlignment(buttonPanel, HasHorizontalAlignment.ALIGN_CENTER);    
     
    dialog.setWidget(containerPanel);
    dialog.setPopupPosition(
        (int) Math.round(com.google.gwt.user.client.Window.getClientWidth() * 0.2),
        (int) Math.round(com.google.gwt.user.client.Window.getClientHeight() * 0.05));
    dialog.show();
  }
  
  public static void parseServiceDataXml(String serviceDataXmlString) {
    errorStatuses.clear();
    schemaLocations.clear();
    defaultNamespace.clear();
    otherNamespaces.clear();
    
    Document response = XMLParser.parse(serviceDataXmlString);
    
    NodeList namespaces = response.getElementsByTagName("namespace");    
    for (int i = 0; i < namespaces.getLength(); i++) {
      Node namespace = namespaces.item(i);
      if (namespace.hasChildNodes()) {
        String namespaceString = namespace.getFirstChild().getNodeValue();
        if ((!otherNamespaces.contains(namespaceString)) &&
            (!namespaceString.equals(WadlXml.xmlns_xsd)) &&
            (!namespaceString.equals(WadlXml.xmlns_xsi))) {
          otherNamespaces.add(namespaceString);
        }
      }
    }
    
    NodeList requests = response.getElementsByTagName("requestData");    
    for (int i = 0; i < requests.getLength(); i++) {
      Node request = requests.item(i);            
      NodeList requestChildren = request.getChildNodes();      
      for (int j = 0; j < requestChildren.getLength(); j++) {
        Node requestChild = requestChildren.item(j);
        if (requestChild.getNodeName().equals("validRequest")) {          
          treatValidRequests(requestChild);
        }
        else if (requestChild.getNodeName().equals("erroneousRequests")) {
          treatErroneousRequests(requestChild);
        }
      }      
    }    
    show();    
  }
  
  /**
   * @param validRequest
   */
  private static void treatValidRequests(Node validRequest) {    
    NodeList children = validRequest.getChildNodes();
    for(int i = 0; i < children.getLength(); i++) {
      Node node = children.item(i);
      if (node.getNodeType() != Node.ELEMENT_NODE) {
        continue;
      }
      if (node.getNodeName().equals("schemaLocation")) {        
        if (node.hasChildNodes() &&            
            !schemaLocations.contains(node.getFirstChild().getNodeValue())) {
          schemaLocations.add(node.getFirstChild().getNodeValue());          
        }    
      }
      else if (node.getNodeName().equals("defaultNamespace")) {        
        if (node.hasChildNodes() &&
            !defaultNamespace.contains(node.getFirstChild().getNodeValue())) {
          defaultNamespace.add(node.getFirstChild().getNodeValue());          
        }    
      }
    }    
  }

  /**
   * @param erroneousRequests
   */
  private static void treatErroneousRequests(Node erroneousRequestChildren) {    
    NodeList erroneousRequests = erroneousRequestChildren.getChildNodes();    
    for (int i = 0; i < erroneousRequests.getLength(); i++) {      
      Node erroneousRequest = erroneousRequests.item(i);
      if (erroneousRequest.getNodeType() != Node.ELEMENT_NODE) {
        continue;      
      }
      String status = erroneousRequest.getAttributes().getNamedItem("status").getNodeValue();      
      if (!errorStatuses.contains(status) && (!status.equals("200"))) {        
        errorStatuses.add(status);        
      }    
      NodeList children = erroneousRequest.getChildNodes();
      for(int j = 0; j < children.getLength(); j++) {
        Node node = children.item(j);        
        if (node.getNodeType() != Node.ELEMENT_NODE) {
          continue;
        }
        if (node.getNodeName().equals("schemaLocation")) {          
          if (node.hasChildNodes() &&
              !schemaLocations.contains(node.getFirstChild().getNodeValue())) {            
            schemaLocations.add(node.getFirstChild().getNodeValue());          
          }    
        }
        else if (node.getNodeName().equals("defaultNamespace")) {          
          if (node.hasChildNodes() &&
              !defaultNamespace.contains(node.getFirstChild().getNodeValue())) {
            defaultNamespace.add(node.getFirstChild().getNodeValue());          
          }    
        }
      }            
    }    
  }
}
