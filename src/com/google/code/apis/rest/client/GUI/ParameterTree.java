package com.google.code.apis.rest.client.GUI;

import com.google.code.apis.rest.client.CodeGeneration.CodeGenerator;
import com.google.code.apis.rest.client.Tree.ReferenceManager;
import com.google.code.apis.rest.client.Util.Uri;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;

/**
 * This class creates the request parameter tree on the left side.
 *
 */
public class ParameterTree {  
  public Button addParamButton;  
  private Button reAnalyzeButton;    
  public TreeItem requestItem;
  public Tree requestTree = new Tree();
  public String requestString;
     
  /**
   * Initializes the request parameter tree
   * @param requestString 
   * @param requestSampleTextBox The current request uri textbox
   * @param analyzeButton The current start analyze button
   */
  public ParameterTree(String request) {
    this.requestString = request;    
  }
  
  /**
   * Creates the request parameter tree
   * @return The request parameter tree
   */
  public void buildTree() {        
    requestItem = new TreeItem();    
    requestTree.addItem(requestItem);
    
    updateTree();
    
    // reanalyze button
    HorizontalPanel buttonPanel = new HorizontalPanel();
    reAnalyzeButton = new Button(GuiFactory.strings.reAnalyzeRequest());
    reAnalyzeButton.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {       
        Button startAnalyzeButton = ReferenceManager.getStartAnalyzeButton(requestString);
        startAnalyzeButton.click();
      }      
    });
    buttonPanel.add(reAnalyzeButton);    
    buttonPanel.add(new HTML("&nbsp;"));
    
    // add param button
    addParamButton = new Button(GuiFactory.strings.addParameter());
    addParamButton.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {       
        addParam();        
      }      
    });    
    buttonPanel.add(addParamButton);
    requestTree.addItem(buttonPanel);
        
    ParameterPanel.requestContainer.add(requestTree);
  }

  private void updateRequestName(Uri uri) { 
    String[] parameters = uri.getQueryParameters();
    String requestName = null;
    paramSearch:
      for (int i = 0; i < parameters.length; i++) {
        for (int j = 0; j < RestCompilePanel.commonOpParamNames.length; j++) {
          if (parameters[i].equalsIgnoreCase(RestCompilePanel.commonOpParamNames[j])) {
            requestName = uri.getQueryValues()[i];
            SettingsDialog.useEndpointAsName = false;
            break paramSearch;
          }
        }
      }
    if (requestName == null) {
      requestName = uri.getRequest();
      SettingsDialog.useEndpointAsName = true;
    }
    requestName = CodeGenerator.cleanName(requestName, false);
    requestItem.setHTML("<b>" + requestName + "</b> " + GuiFactory.strings.listOfParams());
  }
      
  /**
   * Updates the current references of the class in case where the request uri tree has been rebuild 
   * @param newUri 
   * @param requestUriTextBox
   */
  public void updateReferences(TextBox requestUriTextBox, String newUri) {
    requestUriTextBox.setText(newUri);    
    ListBox httpMethod = ReferenceManager.getHttpMethodListBox(requestString);
    String newRequestString =
        httpMethod.getItemText(httpMethod.getSelectedIndex()) + " " + newUri;
    if (!requestString.equals(newRequestString)) {
      ReferenceManager.updateAllReferences(requestString, newRequestString);
    }
    requestString = newRequestString;
    
  }

  /**
   * Updates the request parameter tree in case of where the request uri got changed
   * @param uri The new request uri
   */
  public void updateTree() {
    Uri uri = new Uri(requestString.substring(requestString.indexOf(" ") + 1));   
    updateRequestName(uri);
    requestItem.removeItems();    
        
    String[] queryParameters = uri.getQueryParameters();
    String[] queryValues = uri.getQueryValues();     
    for (int i = 0; i < queryParameters.length; i++) {
      final HorizontalPanel containerPanel = new HorizontalPanel(); 
      final TreeItem parameterItem = new TreeItem(containerPanel);
      // parameter name label
      HTML parameterName = new HTML("<nobr/>" + queryParameters[i] + " =&nbsp;</nobr>");            
      containerPanel.add(parameterName);
      // parameter value textbox
      final TextBox queryValueTextBox = new TextBox(); 
      queryValueTextBox.setWidth("10em");
      queryValueTextBox.setText(queryValues[i]);
      // track parameter value changes
      final String currentParameter = queryParameters[i];      
      queryValueTextBox.addKeyboardListener(new KeyboardListener() {
        public void onKeyDown(Widget sender, char keyCode, int modifiers) {
          // empty
        }

        public void onKeyPress(Widget sender, char keyCode, int modifiers) {
          // empty
        }

        public void onKeyUp(Widget sender, char keyCode, int modifiers) {
          updateParameterValue(currentParameter, queryValueTextBox.getText());
        }        
      });
      queryValueTextBox.addChangeListener(new ChangeListener() {
        public void onChange(Widget sender) {
          updateParameterValue(currentParameter, queryValueTextBox.getText());
        }
      });
      containerPanel.add(queryValueTextBox);
      
      // remove parameter link
      Hyperlink removeParameterLink = new Hyperlink(GuiFactory.strings.remove(), true, "");
      removeParameterLink.addClickListener(new ClickListener() {
        public void onClick(Widget sender) {
          removeHyperlink(parameterItem, queryValueTextBox.getText());
        }
      });
      containerPanel.add(removeParameterLink);      
      requestItem.addItem(parameterItem);
      parameterItem.setState(true);
    }    
    requestItem.setState(true);    
  }

  /**
   * 
   */
  private void removeHyperlink(TreeItem parameterItem, String queryValue) {
    parameterItem.remove();
    TextBox requestUriTextBox = ReferenceManager.getRequestUriTextBox(requestString);
    Uri uri = new Uri(requestUriTextBox.getText());
    String newUri = uri.getScheme() + "://" + uri.getAuthority() + uri.getPath() + "?";
    String[] queryParameters = uri.getQueryParameters();
    String[] queryValues = uri.getQueryValues();
    for (int i = 0; i < queryParameters.length; i++) {
      if (!queryValues[i].equals(queryValue)) {
        if (i > 0) newUri += "&";       
        newUri += queryParameters[i] + "=" + queryValues[i];
      }
    }
    newUri += uri.getFragment();
    updateReferences(requestUriTextBox, newUri);    
  }

  private void updateParameterName(String queryParameter, String queryValue) {
    TextBox requestUriTextBox = ReferenceManager.getRequestUriTextBox(requestString);
    Uri uri = new Uri(requestUriTextBox.getText());
    String newUri = uri.getScheme() + "://" + uri.getAuthority() + uri.getPath() + "?";
    String[] queryParameters = uri.getQueryParameters();
    String[] queryValues = uri.getQueryValues();
    for (int i = 0; i < queryParameters.length; i++) {
      if (i > 0) newUri += "&";            
      if (queryValues[i].equals(queryValue)) {              
        newUri += queryParameter + "=" + queryValues[i];
      }
      else {
        newUri += queryParameters[i] + "=" + queryValues[i];
      }          
    }  
    newUri += uri.getFragment();
    updateReferences(requestUriTextBox, newUri);
  }  
  
  /**
   * @param queryParameter 
   * @param queryValueTextBox 
   * 
   */
  private void updateParameterValue(String queryParameter, String queryValue) {
    TextBox requestUriTextBox = ReferenceManager.getRequestUriTextBox(requestString);
    String newRequestUriString = requestUriTextBox.getText();
    Uri uri = new Uri(newRequestUriString);
    String newUri = uri.getScheme() + "://" + uri.getAuthority() + uri.getPath() + "?";
    String[] queryParameters = uri.getQueryParameters();
    String[] queryValues = uri.getQueryValues();
    for (int i = 0; i < queryParameters.length; i++) {
      if (i > 0) newUri += "&";            
      if (queryParameters[i].equals(queryParameter)) {              
        newUri += queryParameters[i] + "=" + queryValue;
      }
      else {
        newUri += queryParameters[i] + "=" + queryValues[i];
      }            
    }  
    newUri += uri.getFragment();
    updateReferences(requestUriTextBox, newUri);    
  }

  /**
   * Adds a parameter to the current request uri tree and in consequence to the request parameter tree.
   */
  public void addParam() { 
    TextBox requestUriTextBox = ReferenceManager.getRequestUriTextBox(requestString);
    Uri uri = new Uri(requestUriTextBox.getText());    
    final HorizontalPanel containerPanel = new HorizontalPanel();
    final TreeItem parameterItem = new TreeItem(containerPanel);
    // parameter name textbox
    final TextBox queryParamNameTextBox = new TextBox();
    queryParamNameTextBox.setWidth("10em");
    // parameter value textbox
    final TextBox queryValueTextBox = new TextBox(); 
    queryValueTextBox.setWidth("10em");
    final String currentParameter = GuiFactory.strings.paramName();
    final String currentValue = GuiFactory.strings.paramValue();    
    String newUri = uri.getScheme() + "://" + uri.getAuthority() + uri.getPath() + "?";
    String[] queryParameters = uri.getQueryParameters();
    String[] queryValues = uri.getQueryValues();
    for (int i = 0; i < queryParameters.length; i++) {
      if (i > 0) {
        newUri += "&";        
      }
      newUri += queryParameters[i] + "=" + queryValues[i];       
    }
    newUri += "&" + currentParameter + "=" + currentValue + uri.getFragment();
    updateReferences(requestUriTextBox, newUri);    
    queryParamNameTextBox.addKeyboardListener(new KeyboardListener() {
      public void onKeyDown(Widget sender, char keyCode, int modifiers) {
        // empty
      }

      public void onKeyPress(Widget sender, char keyCode, int modifiers) {
        // empty
      }

      public void onKeyUp(Widget sender, char keyCode, int modifiers) {
        updateParameterName(queryParamNameTextBox.getText(), queryValueTextBox.getText());
      }
    });
    queryParamNameTextBox.addChangeListener(new ChangeListener() {
      public void onChange(Widget sender) {
        updateParameterName(queryParamNameTextBox.getText(), queryValueTextBox.getText());
      }
    });
    queryParamNameTextBox.setText(GuiFactory.strings.paramName());
    containerPanel.add(queryParamNameTextBox);
    queryParamNameTextBox.setSelectionRange(0, queryParamNameTextBox.getText().length());      
    HTML parameterName = new HTML("<nobr/> =&nbsp;</nobr>");
    containerPanel.add(parameterName);     
    queryValueTextBox.addKeyboardListener(new KeyboardListener() {
      public void onKeyDown(Widget sender, char keyCode, int modifiers) {
        // empty
      }

      public void onKeyPress(Widget sender, char keyCode, int modifiers) {
        // empty
      }

      public void onKeyUp(Widget sender, char keyCode, int modifiers) {
        updateParameterValue(queryParamNameTextBox.getText(), queryValueTextBox.getText());        
      }
      
    });
    queryValueTextBox.addChangeListener(new ChangeListener() {
      public void onChange(Widget sender) {      
        updateParameterValue(queryParamNameTextBox.getText(), queryValueTextBox.getText());        
      }
    });
    queryValueTextBox.setText(GuiFactory.strings.paramValue());
    containerPanel.add(queryValueTextBox);    
    
    Hyperlink removeParameterLink = new Hyperlink(GuiFactory.strings.remove(), true, "");
    removeParameterLink.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        removeHyperlink(parameterItem, queryValueTextBox.getText());
      }
    });      
    containerPanel.add(removeParameterLink);
    requestItem.addItem(parameterItem);    
    parameterItem.setState(true);    
  }  
}
