package com.google.code.apis.rest.client.Tree;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import com.google.code.apis.rest.client.GUI.CustomTypesDialog;
import com.google.code.apis.rest.client.GUI.GuiFactory;
import com.google.code.apis.rest.client.GUI.ParameterTree;
import com.google.code.apis.rest.client.GUI.SettingsDialog;
import com.google.code.apis.rest.client.Util.SyntaxHighlighter;
import com.google.code.apis.rest.client.Util.TypeEstimator;
import com.google.code.apis.rest.client.Util.Uri;
import com.google.code.apis.rest.client.Wadl.Analyzer;
import com.google.code.apis.rest.client.Wadl.ApplicationNode;
import com.google.code.apis.rest.client.Wadl.FaultNode;
import com.google.code.apis.rest.client.Wadl.GenericNode;
import com.google.code.apis.rest.client.Wadl.ParamNode;
import com.google.code.apis.rest.client.Wadl.RepresentationNode;
import com.google.code.apis.rest.client.Wadl.RequestNode;
import com.google.code.apis.rest.client.Wadl.ResourceNode;
import com.google.code.apis.rest.client.Wadl.ResourceTypeNode;
import com.google.code.apis.rest.client.Wadl.ResponseNode;
import com.google.code.apis.rest.client.Wadl.WadlXml;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ParamItem extends Composite {  
  private static final String[] primitiveTypes = {
      TypeEstimator.typeString,      
      TypeEstimator.typeBoolean,
      TypeEstimator.typeInteger,
      TypeEstimator.typeFloat,  
      TypeEstimator.typeDateTime, 
      TypeEstimator.typeTime, 
      TypeEstimator.typeDate,       
      TypeEstimator.typeAnyUri
  };
  
  private static final String[] complexTypes = {
    TypeEstimator.typeLanguage,
    TypeEstimator.typeCountry
  };
  
  public static Vector customTypes = new Vector();
  public static HashMap customTypeOptions = new HashMap();
  
  public static final String template = "template";
  public static final String matrix = "matrix";
  public static final String query = "query";
  public static final String header = "header";
  public static final String plain = "plain";
  private static final String[] paramStyles = {
    template,
    matrix,    
    query,
    header,
    plain
  };

  private HTML estimationQualityHtml;
  private final HTML manualHtml = new HTML("<img align=\"absmiddle\" src='./images/manual.png' hspace='2' width='16' height='16' /> <small>(" + GuiFactory.strings.manual() + ")</small>");  
  private final HTML unsureHtml = new HTML("<img align=\"absmiddle\" src='./images/red.png' hspace='2' width='16' height='16' /> <small>(" + GuiFactory.strings.unsure() + ")</small>");
  private final HTML sureHtml = new HTML("<img align=\"absmiddle\" src='./images/green.png' hspace='2' width='16' height='16' /> <small>(" + GuiFactory.strings.sure() + ")</small>");
  private final HTML believedHtml = new HTML("<img align=\"absmiddle\" src='./images/yellow.png' hspace='2' width='16' height='16' /> <small>(" + GuiFactory.strings.supposed() + ")</small>");
  
  private ParamNode param;
  private Object parentNode;
  private int paramCounter;
  private TreeItem parentTreeItem;
  private String requestString;
  
  public ParamItem(ParamNode reqParam, final Object parent, TreeItem parentItem, int reqParamCounter, final String reqString) {
    VerticalPanel parameterPanelContainer = new VerticalPanel();
    if (reqParam.getHref() == null) {
      param = reqParam;
      parentNode = parent;
      parentTreeItem = parentItem;
      paramCounter = reqParamCounter;
      requestString = reqString;
  
      parameterPanelContainer.add(new HTML(SyntaxHighlighter.highlight("<" + WadlXml.paramNode + " ")));
      
      HorizontalPanel parameterPanel = new HorizontalPanel();
      
      VerticalPanel parameterPanelLeft = new VerticalPanel();
      parameterPanelLeft.setWidth("1em");
      parameterPanelLeft.add(new HTML("&nbsp;"));
      parameterPanel.add(parameterPanelLeft);
      
      VerticalPanel parameterPanelRight = new VerticalPanel();
      
      if (param.getId() != null) {
        HorizontalPanel parameterPanel0 = new HorizontalPanel();    
        parameterPanel0.add(new HTML(SyntaxHighlighter.highlight(WadlXml.param_id + "=\"")));
        String id = param.getId();
        final TextBox parameterId = new TextBox();
        parameterId.setText(id);
        parameterId.addChangeListener(new ChangeListener() {
          public void onChange(Widget sender) {
            param.updateId(param.getId(), parameterId.getText());         
          }
        });         
        parameterPanel0.add(parameterId);
        parameterPanel0.add(new HTML(SyntaxHighlighter.highlight("\"")));   
        parameterPanelRight.add(parameterPanel0);
      }
      
      HorizontalPanel parameterPanel1 = new HorizontalPanel();    
      parameterPanel1.add(new HTML(SyntaxHighlighter.highlight(WadlXml.param_name + "=\"")));
      String name = param.getName();
      final TextBox parameterName = new TextBox();
      parameterName.setText(name);
      parameterName.addChangeListener(new ChangeListener() {
        public void onChange(Widget sender) {
          requestString = param.getApplication().getAnalyzer().getRequestString();
          updateParamName(parameterName.getText());
          param.getApplication().getAnalyzer().setRequestString(requestString);
        }
      });    
      parameterName.addKeyboardListener(new KeyboardListener() {
        public void onKeyDown(Widget sender, char keyCode, int modifiers) {               
        }
  
        public void onKeyPress(Widget sender, char keyCode, int modifiers) {               
        }
  
        public void onKeyUp(Widget sender, char keyCode, int modifiers) {
          requestString = param.getApplication().getAnalyzer().getRequestString();
          updateParamName(parameterName.getText());
          param.getApplication().getAnalyzer().setRequestString(requestString);
        }      
      });    
      parameterPanel1.add(parameterName);
      parameterPanel1.add(new HTML(SyntaxHighlighter.highlight("\"")));   
      parameterPanelRight.add(parameterPanel1);
  
      HorizontalPanel parameterPanel2 = new HorizontalPanel();
      parameterPanel2.add(new HTML(SyntaxHighlighter.highlight(WadlXml.param_type + "=\"")));
      final ListBox parameterType = new ListBox();
      parameterType.setVisibleItemCount(1);
      addTypes(parameterType);        
      String supposedType = param.getType();   
      parameterType.setSelectedIndex(indexOfValue(supposedType));    
      parameterPanel2.add(parameterType);    
      parameterPanel2.add(new HTML(SyntaxHighlighter.highlight("\"")));    
     
      if (param.getEstimateQuality().equals(TypeEstimator.sure)) {
        estimationQualityHtml = sureHtml; 
      }
      else if (param.getEstimateQuality().equals(TypeEstimator.supposed)) {
        estimationQualityHtml = believedHtml;
      }
      else if (param.getEstimateQuality().equals(TypeEstimator.unsure)) {
        estimationQualityHtml = unsureHtml;
      }
      else if (param.getEstimateQuality().equals(TypeEstimator.manual)) {
        estimationQualityHtml = manualHtml;
      }
      else {
        estimationQualityHtml = new HTML();
      }
      parameterPanel2.add(estimationQualityHtml);
      parameterType.addChangeListener(new ChangeListener() {
        public void onChange(Widget sender) {
          String selectedType = parameterType.getItemText(parameterType.getSelectedIndex());
          if (selectedType.equals(GuiFactory.strings.newCustomType())) {
            CustomTypesDialog.show(parameterType, param, (GenericNode) parentNode, parentTreeItem, paramCounter, estimationQualityHtml);                   
          }
          else {
            updateParam(selectedType);
          }
        }    
      });
  
      parameterPanelRight.add(parameterPanel2);
      
      HorizontalPanel parameterPanel4 = new HorizontalPanel();
      parameterPanel4.add(new HTML(SyntaxHighlighter.highlight(WadlXml.param_style + "=\"")));
      final ListBox parameterStyle = new ListBox();
      parameterStyle.setVisibleItemCount(1);      
      if (parentNode instanceof ResourceTypeNode) {       
        addResourceTypeStyles(parameterStyle);
      }
      else if (parentNode instanceof ResourceNode) {       
        addResourceStyles(parameterStyle);
      }
      else if (parentNode instanceof RequestNode) {       
        addRequestStyles(parameterStyle);
      }
      else if (parentNode instanceof ResponseNode) {       
        addResponseStyles(parameterStyle);
      }
      else if (parentNode instanceof RepresentationNode) {       
        addRepresentationStyles(parameterStyle);
      }
      else if (parentNode instanceof FaultNode) {       
        addFaultStyles(parameterStyle);
      }
      else {
        addStyles(parameterStyle);
      }
      
      parameterStyle.setSelectedIndex(indexOfStyle(param.getStyle(), parameterStyle));
      parameterStyle.addChangeListener(new ChangeListener() {
        public void onChange(Widget sender) {
          param.updateStyle(param.getStyle(), parameterStyle.getItemText(parameterStyle.getSelectedIndex()));
        }
      });
      parameterPanel4.add(parameterStyle);
      parameterPanel4.add(new HTML(SyntaxHighlighter.highlight("\"")));
      parameterPanelRight.add(parameterPanel4);
      
      final VerticalPanel detailsPanel = new VerticalPanel();
      detailsPanel.setVisible(SettingsDialog.alwaysShowDetails);
      
      parameterPanel4.add(new HTML("<img align=\"absmiddle\" src=\"./images/details.png\" style=\"margin-left:10em;\" hspace=\"2\" />"));
      final Hyperlink showDetails;
      if (SettingsDialog.alwaysShowDetails) {
        showDetails = new Hyperlink(GuiFactory.strings.hideDetails(), true, "");
      }
      else {
        showDetails = new Hyperlink(GuiFactory.strings.showDetails(), true, "");
      }
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
      parameterPanel4.add(showDetails);    
      
      HorizontalPanel parameterPanel5 = new HorizontalPanel();    
      parameterPanel5.add(new HTML(SyntaxHighlighter.highlight(WadlXml.param_default + "=\"")));
      final TextBox parameterDefault = new TextBox();
      parameterDefault.addChangeListener(new ChangeListener() {
        public void onChange(Widget sender) {
          param.updateDefaultValue(param.getDefaultValue(), parameterDefault.getText());
        }
      });
      parameterDefault.setText(param.getDefaultValue());
      parameterPanel5.add(parameterDefault);
      parameterPanel5.add(new HTML(SyntaxHighlighter.highlight("\"")));    
      detailsPanel.add(parameterPanel5);
      
      HorizontalPanel parameterPanel9 = new HorizontalPanel();    
      parameterPanel9.add(new HTML(SyntaxHighlighter.highlight(WadlXml.param_path + "=\"")));
      final TextBox parameterPath = new TextBox();
      parameterPath.addChangeListener(new ChangeListener() {
        public void onChange(Widget sender) {
          param.updatePath(param.getPath(), parameterPath.getText());
        }
      });
      parameterPath.setText(param.getPath());
      parameterPanel9.add(parameterPath);
      parameterPanel9.add(new HTML(SyntaxHighlighter.highlight("\"")));    
      detailsPanel.add(parameterPanel9);
      
      HorizontalPanel parameterPanel6 = new HorizontalPanel();
      parameterPanel6.add(new HTML((SyntaxHighlighter.highlight(WadlXml.param_fixed + "=\""))));
      final TextBox parameterFixed = new TextBox();
      parameterFixed.addChangeListener(new ChangeListener() {
        public void onChange(Widget sender) {
          param.updateFixedValue(param.getFixedValue(), parameterFixed.getText());
        }
      });
      parameterFixed.setText(param.getFixedValue());
      parameterPanel6.add(parameterFixed);
      parameterPanel6.add(new HTML(SyntaxHighlighter.highlight("\"")));
      detailsPanel.add(parameterPanel6);
      
      HorizontalPanel parameterPanel7 = new HorizontalPanel();    
      parameterPanel7.add(new HTML(SyntaxHighlighter.highlight(WadlXml.param_required + "=\"")));
      final ListBox parameterRequired = new ListBox();
      parameterRequired.addChangeListener(new ChangeListener() {
        public void onChange(Widget sender) {
          boolean newIsRequired = false;
          if (parameterRequired.getItemText(parameterRequired.getSelectedIndex()).equals("true")) {
            newIsRequired = true;
          }
          param.updateIsRequired(param.getIsRequired(), newIsRequired);
        }
      });
      parameterRequired.setVisibleItemCount(1);
      parameterRequired.addItem("true");
      parameterRequired.addItem("false");
      if (param.getIsRequired()) {
        parameterRequired.setSelectedIndex(0);
      }
      else {
        parameterRequired.setSelectedIndex(1);
      }
      parameterPanel7.add(parameterRequired);
      parameterPanel7.add(new HTML(SyntaxHighlighter.highlight("\"")));
      detailsPanel.add(parameterPanel7);
  
      HorizontalPanel parameterPanel8 = new HorizontalPanel();
      parameterPanel8.add(new HTML(SyntaxHighlighter.highlight(WadlXml.param_repeating + "=\"")));
      final ListBox parameterRepeating = new ListBox();
      parameterRepeating.addChangeListener(new ChangeListener() {
        public void onChange(Widget sender) {
          boolean newIsRepeating = false;
          if (parameterRepeating.getItemText(parameterRepeating.getSelectedIndex()).equals("true")) {
            newIsRepeating = true;
          }
          param.updateIsRepeating(param.getIsRepeating(), newIsRepeating);
        }
      });
      parameterRepeating.setVisibleItemCount(1);
      parameterRepeating.addItem("true");
      parameterRepeating.addItem("false");
      if (param.getIsRepeating()) {
        parameterRepeating.setSelectedIndex(0);
      }
      else {
        parameterRepeating.setSelectedIndex(1);
      }
      parameterPanel8.add(parameterRepeating);
      parameterPanel8.add(new HTML(SyntaxHighlighter.highlight("\"")));
      detailsPanel.add(parameterPanel8);
          
      parameterPanelRight.add(detailsPanel);
      parameterPanel.add(parameterPanelRight);
      
      parameterPanelContainer.add(parameterPanel);
      
      HorizontalPanel paramEndPanel = new HorizontalPanel();
      if (param.getOptions().size() > 0) {
        paramEndPanel.add(new HTML(SyntaxHighlighter.highlight(">")));    
      }
      else {
        paramEndPanel.add(new HTML(SyntaxHighlighter.highlight("/>")));
      }    
      Hyperlink removeParamLink = new Hyperlink(GuiFactory.strings.remove(), true, "");
      removeParamLink.addClickListener(new ClickListener() {
        public void onClick(Widget sender) {
          if (parent instanceof RequestNode) {
            ((RequestNode) parent).removeParam(param);             
          }
          else if (parent instanceof ResourceNode) {
            ((ResourceNode) parent).removeParam(param);            
          }          
          else if (parentNode instanceof ResponseNode) {      
            ((ResponseNode) parent).removeParam(param);      
          }          
          else if (parentNode instanceof ResourceTypeNode) {      
            ((ResourceTypeNode) parent).removeParam(param);      
          }
          else if (parentNode instanceof FaultNode) {      
            ((FaultNode) parent).removeParam(param);      
          }
          else if (parentNode instanceof RepresentationNode) {      
            ((RepresentationNode) parent).removeParam(param);      
          }
          else if (parentNode instanceof ApplicationNode) {      
            ((ApplicationNode) parent).removeParam(param);      
          }
          requestString = param.getApplication().getAnalyzer().getRequestString();
          listParams(parent, parentTreeItem, requestString);         
          
          TextBox requestUriTextBox = ReferenceManager.getRequestUriTextBox(requestString);
          Uri uri = new Uri(requestUriTextBox.getText());
          String newUri = uri.getScheme() + "://" + uri.getAuthority() + uri.getPath() + "?";
          String[] queryParameters = uri.getQueryParameters();
          String[] queryValues = uri.getQueryValues();
          for (int i = 0; i < queryParameters.length; i++) {
            if (!queryParameters[i].equals(parameterName.getText())) {
              if (i > 0) newUri += "&";       
              newUri += queryParameters[i] + "=" + queryValues[i];
            }
          }                     
          newUri += uri.getFragment();          
          ListBox httpMethodListBox = ReferenceManager.getHttpMethodListBox(requestString);
          ParameterTree parameterTree = ReferenceManager.getParameterTree(requestString);          
          parameterTree.updateReferences(requestUriTextBox, newUri);
          parameterTree.updateTree();                       
          requestString = 
            httpMethodListBox.getItemText(httpMethodListBox.getSelectedIndex()) + " " + newUri;
          param.getApplication().getAnalyzer().setRequestString(requestString);
        }
      });      
      paramEndPanel.add(removeParamLink);
      parameterPanelContainer.add(paramEndPanel);
      
      if (param.getOptions().size() > 0) {
        HorizontalPanel optionsPanel = new HorizontalPanel();      
        VerticalPanel optionsPanelLeft = new VerticalPanel();
        optionsPanelLeft.setWidth("1em");
        optionsPanelLeft.add(new HTML("&nbsp;"));
        optionsPanel.add(optionsPanelLeft);
        
        VerticalPanel optionsPanelRight = new VerticalPanel();
        Iterator optionsIterator = param.getOptions().iterator();
        while(optionsIterator.hasNext()) {
          String option = (String) optionsIterator.next();
          optionsPanelRight.add(new HTML(SyntaxHighlighter.highlight("<" + WadlXml.optionNode + " " + WadlXml.option_value + "=\"" + option + "\"") + SyntaxHighlighter.highlight("/>")));       
        }
        optionsPanel.add(optionsPanelRight);
        parameterPanelContainer.add(optionsPanel);
        parameterPanelContainer.add(new HTML(SyntaxHighlighter.highlight("</" + WadlXml.paramNode + ">")));        
      } 
    }
    else {      
      parameterPanelContainer.add(new HTML(
            SyntaxHighlighter.highlight("<" + WadlXml.paramNode + " ") +
            SyntaxHighlighter.highlight(WadlXml.param_href + "=\"" + reqParam.getHref() + "\" />")));      
    }    
    
    initWidget(parameterPanelContainer);
  }
    
  /**
   * 
   */
  protected void updateParamName(String parameterName) {
    param.updateName(param.getName(), parameterName);
    
    TextBox requestUriTextBox = ReferenceManager.getRequestUriTextBox(requestString);  
    Uri uri = new Uri(requestUriTextBox.getText());
    String newUri = uri.getScheme() + "://" + uri.getAuthority() + uri.getPath() + "?";
    String[] queryParameters = uri.getQueryParameters();
    String[] queryValues = uri.getQueryValues();
    for (int i = 0; i < queryParameters.length; i++) {
      if (i > 0) newUri += "&";            
      if (queryValues[i].equals(param.getValue())) {              
        newUri += parameterName + "=" + queryValues[i];
      }
      else {
        newUri += queryParameters[i] + "=" + queryValues[i];
      }
    }         
    newUri += uri.getFragment();    
    ListBox httpMethodListBox = ReferenceManager.getHttpMethodListBox(requestString);
    ParameterTree parameterTree = ReferenceManager.getParameterTree(requestString);
    parameterTree.updateReferences(requestUriTextBox, newUri);
    parameterTree.updateTree();   
    requestString = 
      httpMethodListBox.getItemText(httpMethodListBox.getSelectedIndex()) + " " + newUri;    
  }

  public static void listParams(final Object parentNode, final TreeItem parentTreeItem, final String requestString) {
    Vector buttonTreeItems = WadlTreeRoot.removeChildrenXKeepButtons(parentTreeItem, WadlXml.paramNode);
    
    Vector allParams = new Vector();    
    if (parentNode instanceof RequestNode) {    
      allParams = ((RequestNode) parentNode).getAllParams();      
    }
    else if (parentNode instanceof ResponseNode) {      
      allParams = ((ResponseNode) parentNode).getAllParams();      
    }
    else if (parentNode instanceof ResourceTypeNode) {      
      allParams = ((ResourceTypeNode) parentNode).getAllParams();      
    }
    else if (parentNode instanceof ResourceNode) {      
      allParams = ((ResourceNode) parentNode).getAllParams();      
    }
    else if (parentNode instanceof FaultNode) {      
      allParams = ((FaultNode) parentNode).getAllParams();      
    }
    else if (parentNode instanceof RepresentationNode) {      
      allParams = ((RepresentationNode) parentNode).getAllParams();      
    }
    else if (parentNode instanceof ApplicationNode) {      
      allParams = ((ApplicationNode) parentNode).getAllParams();      
    }
    
    int paramCounter = 0;
    Iterator paramIterator = allParams.iterator();    
    while (paramIterator.hasNext()) {      
      ParamNode param = (ParamNode) paramIterator.next();
      ParamItem paramItem = new ParamItem(param, (GenericNode) parentNode, parentTreeItem, paramCounter, requestString);
      TreeItem parameterTreeItem = new TreeItem(paramItem);
      parameterTreeItem.setUserObject(WadlXml.paramNode);
      parentTreeItem.addItem(parameterTreeItem);      
      parameterTreeItem.setState(SettingsDialog.treeItemsAlwaysOpen);
      paramCounter++;
    }
    
    if (!WadlTreeRoot.containsButton(buttonTreeItems, ApplicationItem.button + WadlXml.paramNode)) {
      // add param button
      HorizontalPanel addParamPanel = new HorizontalPanel();
          
      Button addParamButton = new Button(GuiFactory.strings.addParam());
      addParamButton.addClickListener(new ClickListener() {
        public void onClick(Widget sender) {          
          Analyzer analyzer = null;
          if (parentNode instanceof RequestNode) {    
            ((RequestNode) parentNode).addParam(new ParamNode(GuiFactory.strings.newParam(), (GenericNode) parentNode, ((GenericNode) parentNode).getApplication()));            
            analyzer = ((RequestNode) parentNode).getApplication().getAnalyzer();
          }
          else if (parentNode instanceof ResponseNode) {      
            ((ResponseNode) parentNode).addParam(new ParamNode(GuiFactory.strings.newParam(), (GenericNode) parentNode, ((GenericNode) parentNode).getApplication()));            
            analyzer = ((ResponseNode) parentNode).getApplication().getAnalyzer();
          }
          else if (parentNode instanceof ResourceTypeNode) {      
            ((ResourceTypeNode) parentNode).addParam(new ParamNode(GuiFactory.strings.newParam(), (GenericNode) parentNode, ((GenericNode) parentNode).getApplication()));            
            analyzer = ((ResourceTypeNode) parentNode).getApplication().getAnalyzer(); 
          }
          else if (parentNode instanceof ResourceNode) {      
            ((ResourceNode) parentNode).addParam(new ParamNode(GuiFactory.strings.newParam(), (GenericNode) parentNode, ((GenericNode) parentNode).getApplication()));            
            analyzer = ((ResourceNode) parentNode).getApplication().getAnalyzer(); 
          }
          else if (parentNode instanceof FaultNode) {      
            ((FaultNode) parentNode).addParam(new ParamNode(GuiFactory.strings.newParam(), (GenericNode) parentNode, ((GenericNode) parentNode).getApplication()));            
            analyzer = ((FaultNode) parentNode).getApplication().getAnalyzer(); 
          }
          else if (parentNode instanceof RepresentationNode) {      
            ((RepresentationNode) parentNode).addParam(new ParamNode(GuiFactory.strings.newParam(), (GenericNode) parentNode, ((GenericNode) parentNode).getApplication()));            
            analyzer = ((RepresentationNode) parentNode).getApplication().getAnalyzer(); 
          }
          else if (parentNode instanceof ApplicationNode) {
            ParamNode param = new ParamNode(GuiFactory.strings.newParam(), (GenericNode) parentNode, (ApplicationNode) parentNode);
            param.setId(GuiFactory.strings.newId());
            ((ApplicationNode) parentNode).addParam(param);            
            analyzer = ((ApplicationNode) parentNode).getAnalyzer();
          }         
          String newRequestString = analyzer.getRequestString();
          if (!newRequestString.equals("")) {
            TextBox requestUriTextBox = ReferenceManager.getRequestUriTextBox(newRequestString);
            Uri uri = new Uri(requestUriTextBox.getText());
            String newUri = uri.getScheme() + "://" + uri.getAuthority() + uri.getPath() + "?";
            String[] queryParameters = uri.getQueryParameters();
            String[] queryValues = uri.getQueryValues();
            for (int i = 0; i < queryParameters.length; i++) {          
              if (i > 0) newUri += "&";       
              newUri += queryParameters[i] + "=" + queryValues[i];
            }
            if (queryParameters.length > 0) {
              newUri += "&";
            }
            newUri += GuiFactory.strings.newParam() + "=" + GuiFactory.strings.paramValue();          
            ListBox httpMethodListBox = ReferenceManager.getHttpMethodListBox(newRequestString);
            ParameterTree parameterTree = ReferenceManager.getParameterTree(newRequestString);
            parameterTree.updateReferences(requestUriTextBox, newUri);
            parameterTree.updateTree();          
            newRequestString = httpMethodListBox.getItemText(httpMethodListBox.getSelectedIndex()) + " " + newUri;
            analyzer.setRequestString(newRequestString);
          }
          listParams(parentNode, parentTreeItem, newRequestString);
        }
      });
      addParamPanel.add(addParamButton);
      TreeItem addParamTreeItem = new TreeItem(addParamPanel);
      addParamTreeItem.setUserObject(ApplicationItem.button + WadlXml.paramNode);    
      parentTreeItem.addItem(addParamTreeItem);
   }
    
    WadlTreeRoot.addButtons(buttonTreeItems, parentTreeItem);
    
    parentTreeItem.setState(SettingsDialog.treeItemsAlwaysOpen);    
  }
  
  public static void addTypes(ListBox parameterType) {
    parameterType.clear();
    parameterType.addItem(GuiFactory.strings.primitiveTypes());            
    for (int i = 0; i < primitiveTypes.length; i++) {
      parameterType.addItem(primitiveTypes[i]);
    }
    parameterType.addItem(GuiFactory.strings.complexTypes());    
    for (int i = 0; i < complexTypes.length; i++) {
      parameterType.addItem(complexTypes[i]);
    }    
    parameterType.addItem(GuiFactory.strings.customTypes());
    parameterType.addItem(GuiFactory.strings.newCustomType());
    addCustomTypes(parameterType);
  }
  
  public static void addCustomTypes(ListBox parameterType) {    
    Iterator customTypesIterator = customTypes.iterator();
    while(customTypesIterator.hasNext()) {
      parameterType.addItem((String) customTypesIterator.next());
    }
  }
  
  public static void addCustomTypesAndClear(ListBox parameterType) {
    parameterType.clear();
    addCustomTypes(parameterType);
  }
  
  private void addStyles(ListBox parameterStyle) {
    for (int i = 0; i < paramStyles.length; i++) {
      parameterStyle.addItem(paramStyles[i]);
    }    
  }
  
  private void addResourceTypeStyles(ListBox parameterStyle) {
    for (int i = 0; i < paramStyles.length; i++) {
      if ((paramStyles[i].equals(query)) || paramStyles[i].equals(header)) {
        parameterStyle.addItem(paramStyles[i]);
      }
    }    
  }
  
  private void addRequestStyles(ListBox parameterStyle) {
    for (int i = 0; i < paramStyles.length; i++) {
      if ((paramStyles[i].equals(query)) || paramStyles[i].equals(header)) {
        parameterStyle.addItem(paramStyles[i]);
      }
    }    
  }
  
  private void addResponseStyles(ListBox parameterStyle) {
    for (int i = 0; i < paramStyles.length; i++) {
      if (paramStyles[i].equals(header)) {
        parameterStyle.addItem(paramStyles[i]);
      }
    }    
  }
  
  private void addRepresentationStyles(ListBox parameterStyle) {
    for (int i = 0; i < paramStyles.length; i++) {
      if ((paramStyles[i].equals(query)) || paramStyles[i].equals(plain)) {
        parameterStyle.addItem(paramStyles[i]);
      }
    }    
  }
  
  private void addFaultStyles(ListBox parameterStyle) {
    for (int i = 0; i < paramStyles.length; i++) {
      if ((paramStyles[i].equals(query)) || paramStyles[i].equals(plain)) {
        parameterStyle.addItem(paramStyles[i]);
      }
    }    
  }
  
  private void addResourceStyles(ListBox parameterStyle) {
    for (int i = 0; i < paramStyles.length; i++) {
      if ((paramStyles[i].equals(matrix)) || paramStyles[i].equals(header)  || paramStyles[i].equals(query)  || paramStyles[i].equals(template)) {
        parameterStyle.addItem(paramStyles[i]);
      }
    }    
  }
  
  private int indexOfStyle(String style, ListBox paramStylesListBox) {    
    // the param styles listbox contains different values, dependent of the parent node
    // so pass the listbox for real time check of the index
    for (int i = 0; i < paramStylesListBox.getItemCount(); i++) {
      if (paramStylesListBox.getItemText(i).equals(style)) return i;
    }
    return -1;
  }
  
  public static int indexOfValue(String value) {
    int offset = 1;
    for (int i = 0; i < primitiveTypes.length; i++) {
      if (primitiveTypes[i].equals(value)) return i + offset;
    }
    for (int i = 0; i < complexTypes.length; i++) {
      if (complexTypes[i].equals(value)) return i + 2 * offset + primitiveTypes.length;
    }    
    for (int i = 0; i < customTypes.size(); i++) {
      if (((String) customTypes.elementAt(i)).equals(value)) return i + 4 * offset + primitiveTypes.length + complexTypes.length;
    }
    return -1;
  }

  private void updateParam(String selectedType) {
    if (!param.getType().equals(selectedType)) {
      param.updateType(param.getType(), selectedType);
      param.setEstimateQuality(TypeEstimator.manual);      
      if (customTypes.contains(selectedType)) {
        param.setOptions((Vector) customTypeOptions.get(selectedType)); 
      }
      else {
        param.setOptions(new Vector());
      }      
      
      listParams(parentNode, parentTreeItem, requestString);
      parentTreeItem.getChild(paramCounter).setSelected(true);
      parentTreeItem.getTree().ensureSelectedItemVisible();
    }    
  }
}