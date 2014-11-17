package com.google.code.apis.rest.client.GUI;

import java.util.Iterator;
import java.util.Vector;

import com.google.code.apis.rest.client.Tree.MethodItem;
import com.google.code.apis.rest.client.Tree.ReferenceManager;
import com.google.code.apis.rest.client.Tree.WadlTreeRoot;
import com.google.code.apis.rest.client.Util.Tools;
import com.google.code.apis.rest.client.Wadl.Analyzer;
import com.google.code.apis.rest.client.Wadl.ApplicationNode;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.WindowResizeListener;
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
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.Widget;

/**
 * This class creates the request uri tree
 *
 */
public class RequestUriTree extends Composite {
  public TextBox requestUriTextBox;  
  public ListBox httpMethodListBox;
  public Button startAnalyzeButton;
  public Button testRequestButton;
  private ParameterTree parameterTree;
  private HorizontalPanel requestSampleBoxPanel = new HorizontalPanel();   
   
  /**
   * Creates the request uri tree
   * @param requestString The request node
   */
  public RequestUriTree(String request) {    
    // split the request in http method and uri string
    request = request.trim();
    request = request.replaceAll("\\s\\s*", " ");    
    String httpMethod = request.substring(0, request.indexOf(" "));
    String uriString = request.substring(request.indexOf(" ") + 1);    
    
    // http method listbox
    httpMethodListBox = new ListBox();    
    addMethods(httpMethodListBox);    
    if (Tools.contains(MethodItem.methodNames, httpMethod)) {
      httpMethodListBox.setSelectedIndex(MethodItem.indexOfName(httpMethod));
    }
    else {
      httpMethodListBox.setSelectedIndex(0);
    }
    httpMethodListBox.addChangeListener(new ChangeListener() {
      public void onChange(Widget sender) {        
        if (httpMethodListBox.getItemText(httpMethodListBox.getSelectedIndex()).equals(MethodItem.get)) {
          testRequestButton.setEnabled(true);
        }
        else {
          testRequestButton.setEnabled(false);
        }                
        updateReferences();
      }      
    });
    requestSampleBoxPanel.add(httpMethodListBox);
    
    // request uri textbox
    requestUriTextBox = new TextBox();   
    requestUriTextBox.setWidth(Math.floor(0.62 * Window.getClientWidth()) + "px");
    requestUriTextBox.setText(uriString);
    requestUriTextBox.addChangeListener(new ChangeListener() {
      public void onChange(Widget sender) {
        updateReferences();
      }
    });
    requestUriTextBox.addKeyboardListener(new KeyboardListener() {      
      public void onKeyDown(Widget sender, char keyCode, int modifiers) {    
        // empty
      }

      public void onKeyPress(Widget sender, char keyCode, int modifiers) {
        // empty
      }
      
      public void onKeyUp(Widget sender, char keyCode, int modifiers) {
        updateReferences();
      }
    });                
    requestSampleBoxPanel.add(requestUriTextBox);    
    
    // remove request string hyperlink
    Hyperlink removeRequestStringHyperlink = new Hyperlink(GuiFactory.strings.remove(), true, "");
    removeRequestStringHyperlink.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {        
        ReferenceManager.removeAllReferences(parameterTree.requestString);
        // if there are other requests left, just delete the current one
        if (ReferenceManager.allRequestStrings.size() > 0) {          
          parameterTree.requestTree.removeFromParent();          
        }
        // if not, create the default init request
        else {
          ReferenceManager.allRequestStrings.add(GuiFactory.strings.newRequestUri());   
          ParameterPanel.requestContainer.clear();
        }        
        listRequestSamples(ReferenceManager.allRequestStrings);        
      }
    });
    requestSampleBoxPanel.add(removeRequestStringHyperlink);
    
    // start analyze button
    startAnalyzeButton = new Button(GuiFactory.strings.analyze());
    startAnalyzeButton.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {             
        Analyzer analyzer = new Analyzer(parameterTree.requestString);
        ApplicationNode application = analyzer.analyze();
            
        WadlTreeRoot wadlTreeRoot = new WadlTreeRoot();
        Tree wadlTree = wadlTreeRoot.buildTree(application);
        
        WadlPanel.wadlArea.setWidget(wadlTree);
        GuiFactory.toggleButtonsEnabled(true);
      }       
    });      
    requestSampleBoxPanel.add(startAnalyzeButton);    
    requestSampleBoxPanel.add(new HTML("&nbsp;"));    
    
    // test request button
    testRequestButton = new Button(GuiFactory.strings.testRequest());
    testRequestButton.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        TestRequestDialog.show(requestUriTextBox, RequestUriTree.this);
      }
    });    
    if (!httpMethodListBox.getItemText(httpMethodListBox.getSelectedIndex()).equals(MethodItem.get)) {
      testRequestButton.setEnabled(false);
    }
    requestSampleBoxPanel.add(testRequestButton);       
    
    // if no parameter tree exists for the new request
    // start listing the parameters in a new parameter tree    
    if (!ReferenceManager.alreadyExistingTrees.contains(request)) {           
      parameterTree = new ParameterTree(request);     
      Vector referencedObjects = new Vector();   
      referencedObjects.add(httpMethodListBox);
      referencedObjects.add(parameterTree);
      referencedObjects.add(requestUriTextBox);
      referencedObjects.add(startAnalyzeButton);    
      ReferenceManager.putAllReferences(request, referencedObjects);
      
      parameterTree.buildTree();      
    }   
    // else recycle the existing tree and
    // start listing the parameters in an already existing parameter tree
    else {      
      parameterTree = ReferenceManager.getParameterTree(request);        
      parameterTree.updateTree();      
    }  
    
    // handle window resize events and init widgets
    Window.addWindowResizeListener(new WindowResizeListener() {
      public void onWindowResized(int width, int height) {
        requestUriTextBox.setWidth(Math.floor(0.62 * width) + "px");        
      }
    });   
    
    requestSampleBoxPanel.setWidth("100%");
    initWidget(requestSampleBoxPanel);
  }
    
  /**
   * @param httpMethod
   */
  private void addMethods(ListBox httpMethod) {
    for (int i = 0; i < MethodItem.methodNames.length; i++) {
      httpMethod.addItem(MethodItem.methodNames[i]);
    }    
  }

  /**
   * Sets a new request uri string. Needed if the uri string is changed manually.
   * @param uriString The new uri string
   */
  public void setRequestUri(String uriString) {    
    requestUriTextBox.setText(uriString);  
    updateReferences();
  }
     
  private void updateReferences() {
    String newRequestString =
        httpMethodListBox.getItemText(httpMethodListBox.getSelectedIndex()) +
        " " + requestUriTextBox.getText();
    
    // remove old references
    ReferenceManager.removeAllReferences(parameterTree.requestString);
    // add new references
    Vector referencedObjects = new Vector();   
    referencedObjects.add(httpMethodListBox);
    referencedObjects.add(parameterTree);
    referencedObjects.add(requestUriTextBox);
    referencedObjects.add(startAnalyzeButton);    
    ReferenceManager.putAllReferences(newRequestString, referencedObjects);
    
    parameterTree.requestString = newRequestString;
    parameterTree.updateTree();    
  }  

  /**
   * Lists all the request uris
   * @param allRequestSamples The vector that holds all the request uris
   */
  public static void listRequestSamples(Vector allRequestSamples) {
    RequestUriPanel.uriTree.removeItems();
    Vector requestSamples = (Vector) allRequestSamples.clone();    
    for(Iterator allRequestSamplesIterator = requestSamples.iterator(); allRequestSamplesIterator.hasNext(); /* empty */) {
      String requestString = (String) allRequestSamplesIterator.next();
      RequestUriPanel.uriTree.addItem(new RequestUriTree(requestString));
    }    
  }
}
