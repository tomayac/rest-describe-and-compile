package com.google.code.apis.rest.client.Tree;

import java.util.Iterator;
import java.util.Vector;

import com.google.code.apis.rest.client.GUI.GuiFactory;
import com.google.code.apis.rest.client.GUI.SettingsDialog;
import com.google.code.apis.rest.client.Util.SyntaxHighlighter;
import com.google.code.apis.rest.client.Wadl.ApplicationNode;
import com.google.code.apis.rest.client.Wadl.FaultNode;
import com.google.code.apis.rest.client.Wadl.GenericNode;
import com.google.code.apis.rest.client.Wadl.ResponseNode;
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
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class FaultItem extends Composite {
  public FaultItem(final FaultNode fault, final Object parentNode, final TreeItem parentTreeItem) {    
    VerticalPanel faultPanel = new VerticalPanel();
    if (fault.getHref() == null) {
      faultPanel.add(new HTML(SyntaxHighlighter.highlight("<" + WadlXml.faultNode + " ")));
      
      VerticalPanel faultPanelLeft = new VerticalPanel();
      faultPanelLeft.setWidth("1em");
      faultPanelLeft.add(new HTML("&nbsp;"));
      HorizontalPanel containerPanel = new HorizontalPanel();
      containerPanel.add(faultPanelLeft);
      
      VerticalPanel faultPanelRight = new VerticalPanel();    
      
      if (fault.getId() != null) {
        HorizontalPanel faultPanel0 = new HorizontalPanel();
        faultPanel0.add(new HTML(SyntaxHighlighter.highlight(WadlXml.fault_id + "=\"")));
        final TextBox idTextBox = new TextBox();
        idTextBox.addChangeListener(new ChangeListener() {
          public void onChange(Widget sender) {
            fault.updateId(fault.getId(), idTextBox.getText());
          }
        });    
        idTextBox.setText(fault.getId());
        faultPanel0.add(idTextBox);
        faultPanel0.add(new HTML(SyntaxHighlighter.highlight("\"")));
        faultPanelRight.add(faultPanel0);
      }
      
      HorizontalPanel faultPanel4 = new HorizontalPanel();
      faultPanel4.add(new HTML(SyntaxHighlighter.highlight(WadlXml.fault_status + "=\"")));
      final TextBox statusTextBox = new TextBox();
      statusTextBox.addChangeListener(new ChangeListener() {
        public void onChange(Widget sender) {
          fault.updateStatus(fault.getStatus(), statusTextBox.getText());
        }
      });    
      statusTextBox.setText(fault.getStatus() + "");
      faultPanel4.add(statusTextBox);
      faultPanel4.add(new HTML(SyntaxHighlighter.highlight("\"")));
      faultPanelRight.add(faultPanel4);
      
      HorizontalPanel faultPanel1 = new HorizontalPanel();
      faultPanel1.add(new HTML(SyntaxHighlighter.highlight(WadlXml.fault_mediaType + "=\"")));
      final TextBox mediaTypeTextBox = new TextBox();
      mediaTypeTextBox.addChangeListener(new ChangeListener() {
        public void onChange(Widget sender) {
          fault.updateMediaType(fault.getMediaType(), mediaTypeTextBox.getText());
        }
      });    
      mediaTypeTextBox.setText(fault.getMediaType());
      faultPanel1.add(mediaTypeTextBox);
      faultPanel1.add(new HTML(SyntaxHighlighter.highlight("\"")));
      faultPanelRight.add(faultPanel1);
      
      final VerticalPanel detailsPanel = new VerticalPanel();
      detailsPanel.setVisible(false);
      faultPanel1.add(new HTML("<img align=\"absmiddle\" src=\"./images/details.png\" style=\"margin-left:8em;\" hspace=\"2\" />"));
      final Hyperlink showDetails = new Hyperlink(GuiFactory.strings.showDetails(), "");
      showDetails.addClickListener(new ClickListener() {
        public void onClick(Widget sender) {
          if (detailsPanel.isVisible()) {
            detailsPanel.setVisible(false);
            showDetails.setText(GuiFactory.strings.showDetails());
          }
          else {
            detailsPanel.setVisible(true);
            showDetails.setText(GuiFactory.strings.hideDetails());          
          }
        }
      });
      faultPanel1.add(showDetails);    
  
      HorizontalPanel faultPanel2 = new HorizontalPanel();
      faultPanel2.add(new HTML(SyntaxHighlighter.highlight(WadlXml.fault_element + "=\"")));
      final TextBox elementTextBox = new TextBox();
      elementTextBox.addChangeListener(new ChangeListener() {
        public void onChange(Widget sender) {
          fault.updateElement(fault.getElement(), elementTextBox.getText());
        }
      });    
      elementTextBox.setText(fault.getElement());
      faultPanel2.add(elementTextBox);
      faultPanel2.add(new HTML(SyntaxHighlighter.highlight("\"")));
      detailsPanel.add(faultPanel2);
      
      HorizontalPanel faultPanel3 = new HorizontalPanel();
      faultPanel3.add(new HTML(SyntaxHighlighter.highlight(WadlXml.fault_profile + "=\"")));
      final TextBox profileTextBox = new TextBox();
      profileTextBox.addChangeListener(new ChangeListener() {
        public void onChange(Widget sender) {
          fault.updateProfile(fault.getProfile(), profileTextBox.getText());
        }
      });    
      profileTextBox.setText(fault.getProfile());
      faultPanel3.add(profileTextBox);
      faultPanel3.add(new HTML(SyntaxHighlighter.highlight("\"")));
      detailsPanel.add(faultPanel3);
          
      faultPanelRight.add(detailsPanel);    
      
      HorizontalPanel faultEndPanel = new HorizontalPanel();
      faultEndPanel.add(new HTML(SyntaxHighlighter.highlight("/>")));
      Hyperlink removeFaultLink = new Hyperlink(GuiFactory.strings.remove(), true, "");
      removeFaultLink.addClickListener(new ClickListener() {
        public void onClick(Widget sender) {
          if (parentNode instanceof ApplicationNode) {
            ((ApplicationNode) parentNode).removeFault(fault);
          }
          else if (parentNode instanceof ResponseNode) {
            ((ResponseNode) parentNode).removeFault(fault);
          } 
          listFaults(parentNode, parentTreeItem);     
        }
      });      
      faultEndPanel.add(removeFaultLink);
      containerPanel.add(faultPanelRight);
      faultPanel.add(containerPanel);
      
      faultPanel.add(faultEndPanel);
    }
    else {
      faultPanel.add(new HTML(
          SyntaxHighlighter.highlight("<" + WadlXml.faultNode+ " ") +
          SyntaxHighlighter.highlight(WadlXml.fault_href + "=\"" + fault.getHref() + "\" />")));
    }
    initWidget(faultPanel);
  } 
  
  public static void listFaults(final Object parentNode, final TreeItem parentTreeItem) {    
    // fault items        
    Vector allFaults = null;
    if (parentNode instanceof ApplicationNode) {
      allFaults = ((ApplicationNode) parentNode).getAllFaults();
    }
    else if (parentNode instanceof ResponseNode) {
      allFaults = ((ResponseNode) parentNode).getAllFaults();
    }
        
    Vector buttonTreeItems = WadlTreeRoot.removeChildrenXKeepButtons(parentTreeItem, WadlXml.faultNode);
       
    Iterator allfaultsIterator = allFaults.iterator();
    while (allfaultsIterator.hasNext()) {
      FaultNode fault = (FaultNode) allfaultsIterator.next();      
      TreeItem faultTreeItem = new TreeItem(new FaultItem(fault, parentNode, parentTreeItem));
      faultTreeItem.setUserObject(WadlXml.faultNode);
      parentTreeItem.addItem(faultTreeItem);      
    }    
    
    if (!WadlTreeRoot.containsButton(buttonTreeItems, ApplicationItem.button + WadlXml.faultNode)) {
      // add fault button
      HorizontalPanel addFaultPanel = new HorizontalPanel();
      
      Button addFaultButton = new Button(GuiFactory.strings.addFault());
      addFaultButton.addClickListener(new ClickListener() {
        public void onClick(Widget sender) {        
          if (parentNode instanceof ApplicationNode) {
            FaultNode fault = new FaultNode(GuiFactory.strings.newFault(), (GenericNode) parentNode, (ApplicationNode) parentNode);
            fault.setId(GuiFactory.strings.newId());
            ((ApplicationNode) parentNode).addFault(fault);
          }
          else if (parentNode instanceof ResponseNode) {
            ((ResponseNode) parentNode).addFault(new FaultNode(GuiFactory.strings.newFault(), (GenericNode) parentNode, ((ResponseNode) parentNode).getApplication()));
          }
          listFaults(parentNode, parentTreeItem);           
        }
      });
      addFaultPanel.add(addFaultButton);
      TreeItem addFaultTreeItem = new TreeItem(addFaultPanel);
      addFaultTreeItem.setUserObject(ApplicationItem.button + WadlXml.faultNode);
      parentTreeItem.addItem(addFaultTreeItem);
    }
    
    WadlTreeRoot.addButtons(buttonTreeItems, parentTreeItem);
    
    parentTreeItem.setState(SettingsDialog.treeItemsAlwaysOpen);    
  } 
}