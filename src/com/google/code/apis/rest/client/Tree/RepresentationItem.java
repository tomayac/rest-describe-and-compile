package com.google.code.apis.rest.client.Tree;

import java.util.Iterator;
import java.util.Vector;

import com.google.code.apis.rest.client.GUI.GuiFactory;
import com.google.code.apis.rest.client.GUI.SettingsDialog;
import com.google.code.apis.rest.client.Util.SyntaxHighlighter;
import com.google.code.apis.rest.client.Wadl.ApplicationNode;
import com.google.code.apis.rest.client.Wadl.GenericNode;
import com.google.code.apis.rest.client.Wadl.RepresentationNode;
import com.google.code.apis.rest.client.Wadl.RequestNode;
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

public class RepresentationItem extends Composite {
  private static final String application_xml = "application/xml";
  
  public RepresentationItem(final RepresentationNode representation, final Object parentNode, final TreeItem parentTreeItem) {    
    VerticalPanel representationPanel = new VerticalPanel();
    
    if (representation.getHref() == null) {
      representationPanel.add(new HTML(SyntaxHighlighter.highlight("<" + WadlXml.representationNode + " ")));
      
      VerticalPanel representationPanelLeft = new VerticalPanel();
      representationPanelLeft.setWidth("1em");
      representationPanelLeft.add(new HTML("&nbsp;"));
      HorizontalPanel containerPanel = new HorizontalPanel();
      containerPanel.add(representationPanelLeft);
      
      VerticalPanel representationPanelRight = new VerticalPanel();    
      
      if (representation.getId() != null) {
        HorizontalPanel representationPanel0 = new HorizontalPanel();
        representationPanel0.add(new HTML(SyntaxHighlighter.highlight(WadlXml.representation_id + "=\"")));
        final TextBox idTextBox = new TextBox();
        idTextBox.addChangeListener(new ChangeListener() {
          public void onChange(Widget sender) {
            representation.updateId(representation.getId(), idTextBox.getText());
          }
        });    
        idTextBox.setText(representation.getId());
        representationPanel0.add(idTextBox);
        representationPanel0.add(new HTML(SyntaxHighlighter.highlight("\"")));
        representationPanelRight.add(representationPanel0);
      }
      
      HorizontalPanel representationPanel2 = new HorizontalPanel();
      representationPanel2.add(new HTML(SyntaxHighlighter.highlight(WadlXml.representation_element + "=\"")));
      final TextBox elementTextBox = new TextBox();
      elementTextBox.addChangeListener(new ChangeListener() {
        public void onChange(Widget sender) {
          representation.updateElement(representation.getElement(), elementTextBox.getText());
        }
      });    
      elementTextBox.setText(representation.getElement());
      representationPanel2.add(elementTextBox);
      representationPanel2.add(new HTML(SyntaxHighlighter.highlight("\"")));
      representationPanelRight.add(representationPanel2);
      
      HorizontalPanel representationPanel1 = new HorizontalPanel();
      representationPanel1.add(new HTML(SyntaxHighlighter.highlight(WadlXml.representation_mediaType + "=\"")));
      final TextBox mediaTypeTextBox = new TextBox();
      mediaTypeTextBox.addChangeListener(new ChangeListener() {
        public void onChange(Widget sender) {
          representation.updateMediaType(representation.getMediaType(), mediaTypeTextBox.getText());
        }
      });    
      mediaTypeTextBox.setText(representation.getMediaType());
      representationPanel1.add(mediaTypeTextBox);
      representationPanel1.add(new HTML(SyntaxHighlighter.highlight("\"")));
      representationPanelRight.add(representationPanel1);
          
      final VerticalPanel detailsPanel = new VerticalPanel();
      detailsPanel.setVisible(false);
      representationPanel1.add(new HTML("<img align=\"absmiddle\" src=\"./images/details.png\" style=\"margin-left:8em;\" hspace=\"2\" />"));
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
      representationPanel1.add(showDetails);    
  
      HorizontalPanel representationPanel3 = new HorizontalPanel();
      representationPanel3.add(new HTML(SyntaxHighlighter.highlight(WadlXml.representation_profile + "=\"")));
      final TextBox profileTextBox = new TextBox();
      profileTextBox.addChangeListener(new ChangeListener() {
        public void onChange(Widget sender) {
          representation.updateProfile(representation.getProfile(), profileTextBox.getText());
        }
      });    
      profileTextBox.setText(representation.getProfile());
      representationPanel3.add(profileTextBox);
      representationPanel3.add(new HTML(SyntaxHighlighter.highlight("\"")));
      detailsPanel.add(representationPanel3);
      
      HorizontalPanel representationPanel4 = new HorizontalPanel();
      representationPanel4.add(new HTML(SyntaxHighlighter.highlight(WadlXml.representation_status + "=\"")));
      final TextBox statusTextBox = new TextBox();
      statusTextBox.addChangeListener(new ChangeListener() {
        public void onChange(Widget sender) {
          representation.updateStatus(representation.getStatus(), statusTextBox.getText());
        }
      });    
      statusTextBox.setText(representation.getStatus());
      representationPanel4.add(statusTextBox);
      representationPanel4.add(new HTML(SyntaxHighlighter.highlight("\"")));
      detailsPanel.add(representationPanel4);
      
      representationPanelRight.add(detailsPanel);    
      
      HorizontalPanel representationEndPanel = new HorizontalPanel();
      representationEndPanel.add(new HTML(SyntaxHighlighter.highlight(">")));
      Hyperlink removeRepresentationLink = new Hyperlink(GuiFactory.strings.remove(), true, "");
      removeRepresentationLink.addClickListener(new ClickListener() {
        public void onClick(Widget sender) {
          if (parentNode instanceof RequestNode) {
            ((RequestNode) parentNode).removeRepresentation(representation);            
          }
          else if (parentNode instanceof ResponseNode) {
            ((ResponseNode) parentNode).removeRepresentation(representation);            
          }
          else if (parentNode instanceof ApplicationNode) {
            ((ApplicationNode) parentNode).removeRepresentation(representation);            
          }
          listRepresentations(parentNode, parentTreeItem);
        }
      });      
      representationEndPanel.add(removeRepresentationLink);
      containerPanel.add(representationPanelRight);
      representationPanel.add(containerPanel);
      
      representationPanel.add(representationEndPanel);
    }
    else {
      representationPanel.add(new HTML(
          SyntaxHighlighter.highlight("<" + WadlXml.representationNode + " ") +
          SyntaxHighlighter.highlight(WadlXml.representation_href + "=\"" + representation.getHref() + "\" />")));  
    }
    
    initWidget(representationPanel);
  }
  
  public static void listRepresentations(final Object parentNode, final TreeItem parentTreeItem) {
    Vector buttonTreeItems = WadlTreeRoot.removeChildrenXKeepButtons(parentTreeItem, WadlXml.representationNode);
    
    Vector allRepresentations = null;
    if (parentNode instanceof RequestNode) {
      allRepresentations = ((RequestNode) parentNode).getAllRepresentations();            
    }
    else if (parentNode instanceof ResponseNode) {
      allRepresentations = ((ResponseNode) parentNode).getAllRepresentations();            
    }
    else if (parentNode instanceof ApplicationNode) {
      allRepresentations = ((ApplicationNode) parentNode).getAllRepresentations();            
    }    
    Iterator allRepresentationsIterator = allRepresentations.iterator();
    while (allRepresentationsIterator.hasNext()) {
      RepresentationNode representation = (RepresentationNode) allRepresentationsIterator.next();
      RepresentationItem representationItem = new RepresentationItem(representation, parentNode, parentTreeItem);
      TreeItem representationTreeItem = new TreeItem(representationItem);
      representationTreeItem.setUserObject(WadlXml.representationNode);
      parentTreeItem.addItem(representationTreeItem);      
      
      if (representation.getHref() == null) {
        String requestString = "";
        if (representation.getApplication() != null) {
          requestString = representation.getApplication().getAnalyzer().getRequestString();
        }
        ParamItem.listParams(
            representation,
            representationTreeItem,
            requestString);     
      
        // close representation item
        GenericClosingItem representationCloseItem = new GenericClosingItem(WadlXml.representationNode);
        TreeItem representationCloseTreeItem = new TreeItem(representationCloseItem);
        representationCloseTreeItem.setUserObject(WadlXml.representationNode);
        parentTreeItem.addItem(representationCloseTreeItem);
      }
    }
    
    if (!WadlTreeRoot.containsButton(buttonTreeItems, ApplicationItem.button + WadlXml.representationNode)) {
      // add representation button            
      HorizontalPanel addRepresentationPanel = new HorizontalPanel();      
      Button addRepresentationButton = new Button(GuiFactory.strings.addRepresentation());
      addRepresentationButton.addClickListener(new ClickListener() {
        public void onClick(Widget sender) {
          if (parentNode instanceof RequestNode) {
            ((RequestNode) parentNode).addRepresentation(new RepresentationNode(GuiFactory.strings.newRepresentation(), application_xml, (GenericNode) parentNode, ((GenericNode) parentNode).getApplication()));            
          }
          else if (parentNode instanceof ResponseNode) {
            ((ResponseNode) parentNode).addRepresentation(new RepresentationNode(GuiFactory.strings.newRepresentation(), application_xml, (GenericNode) parentNode, ((GenericNode) parentNode).getApplication()));            
          }
          else if (parentNode instanceof ApplicationNode) {
            RepresentationNode representation = new RepresentationNode(GuiFactory.strings.newRepresentation(), application_xml, (GenericNode) parentNode, ((GenericNode) parentNode).getApplication());
            representation.setId(GuiFactory.strings.newId());
            ((ApplicationNode) parentNode).addRepresentation(representation);            
          }        
          listRepresentations(parentNode, parentTreeItem);                
        }
      });
      addRepresentationPanel.add(addRepresentationButton);
      TreeItem addRepresentationTreeItem = new TreeItem(addRepresentationPanel);   
      
      addRepresentationTreeItem.setUserObject(ApplicationItem.button + WadlXml.representationNode);
      parentTreeItem.addItem(addRepresentationTreeItem);
    }
    
    WadlTreeRoot.addButtons(buttonTreeItems, parentTreeItem);
    
    parentTreeItem.setState(SettingsDialog.treeItemsAlwaysOpen);   
  }  
}
