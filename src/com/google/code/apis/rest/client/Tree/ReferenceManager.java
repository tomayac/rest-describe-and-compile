/**
 * 
 */
package com.google.code.apis.rest.client.Tree;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import com.google.code.apis.rest.client.GUI.ParameterTree;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;

/**
 * @author tsteiner
 *
 */
public class ReferenceManager {
  public static Vector allRequestStrings = new Vector();
  public static Vector alreadyExistingTrees = new Vector();
  public static HashMap parameterTrees = new HashMap();
  public static HashMap httpMethodListBoxes = new HashMap();
  public static HashMap requestUriTextBoxes = new HashMap();
  public static HashMap startAnalyzeButtons = new HashMap();
  
  public ReferenceManager() {
    // empty
  }
  
  public static void removeAllReferences(String requestString) {
    ReferenceManager.allRequestStrings.remove(requestString);    
    ReferenceManager.alreadyExistingTrees.remove(requestString);
    ReferenceManager.httpMethodListBoxes.remove(requestString);    
    ReferenceManager.parameterTrees.remove(requestString);    
    ReferenceManager.requestUriTextBoxes.remove(requestString);    
    ReferenceManager.startAnalyzeButtons.remove(requestString);    
  }
  
  public static Vector getAllRequestStrings() {
    return allRequestStrings;    
  } 
  
  public static Vector getAlreadyExistingParameterTrees() {
    return alreadyExistingTrees;
  }
  
  public static ParameterTree getParameterTree(String requestString) {
    if (parameterTrees.get(requestString) == null) Window.alert("Error retrieving Parameter Tree\n" + requestString);
    return (ParameterTree) parameterTrees.get(requestString);
  }
  
  public static TextBox getRequestUriTextBox(String requestString) {
    if (requestUriTextBoxes.get(requestString) == null) Window.alert("Error retrieving Request URI TextBox\n" + requestString);
    return (TextBox) requestUriTextBoxes.get(requestString);
  }  
  
  public static ListBox getHttpMethodListBox(String requestString) {
    if (httpMethodListBoxes.get(requestString) == null) Window.alert("Error retrieving HTTP Method ListBox\n" + requestString);
    return (ListBox) httpMethodListBoxes.get(requestString);
  }

  public static Button getStartAnalyzeButton(String requestString) {
    if (startAnalyzeButtons.get(requestString) == null) Window.alert("Error retrieving Start Analyze Button\n" + requestString);
    return (Button) startAnalyzeButtons.get(requestString); 
  }

  public static void clearReferences() {
    allRequestStrings.clear();
    alreadyExistingTrees.clear();
    parameterTrees.clear();
    httpMethodListBoxes.clear();
    requestUriTextBoxes.clear();
    startAnalyzeButtons.clear();
  }

  /**
   * @param requestString
   * @param referencedObjects
   */
  public static void putAllReferences(String newRequestString, Vector referencedObjects) {    
    ReferenceManager.allRequestStrings.add(newRequestString);    
    ReferenceManager.alreadyExistingTrees.add(newRequestString);    
    
    for(Iterator referencedObjectsIterator = referencedObjects.iterator(); referencedObjectsIterator.hasNext(); /* empty */) {
      Object object = referencedObjectsIterator.next();
      if (object instanceof ParameterTree) {
        ReferenceManager.parameterTrees.put(newRequestString, (ParameterTree) object);
      }
      else if (object instanceof ListBox) {        
        ReferenceManager.httpMethodListBoxes.put(newRequestString, (ListBox) object);
      }
      else if (object instanceof TextBox) {
        ReferenceManager.requestUriTextBoxes.put(newRequestString, (TextBox) object);
      }
      else if (object instanceof Button) {
        ReferenceManager.startAnalyzeButtons.put(newRequestString, (Button) object);
      }      
    }    
  }

  /**
   * @param requestString
   * @param newRequestString
   */
  public static void updateAllReferences(String oldRequestString, String newRequestString) {    
    // add the new request strings
    allRequestStrings.add(newRequestString);
    alreadyExistingTrees.add(newRequestString);
    
    // change all old key request strings for the new key request strings
    ParameterTree parameterTree = (ParameterTree) ReferenceManager.parameterTrees.get(oldRequestString);
    if (parameterTree != null) {
      ReferenceManager.parameterTrees.put(newRequestString, parameterTree);
    }
    ListBox listBox = (ListBox) ReferenceManager.httpMethodListBoxes.get(oldRequestString);
    if (listBox != null) {
      ReferenceManager.httpMethodListBoxes.put(newRequestString, listBox);
    }
    Button startAnalyzeButton = (Button) ReferenceManager.startAnalyzeButtons.get(oldRequestString);
    if (startAnalyzeButton != null) {
      ReferenceManager.startAnalyzeButtons.put(newRequestString, startAnalyzeButton);
    }
    TextBox requestUriTextBox = (TextBox) ReferenceManager.requestUriTextBoxes.get(oldRequestString);
    if (requestUriTextBox != null) {
      ReferenceManager.requestUriTextBoxes.put(newRequestString, requestUriTextBox);
    }
    
    // remove all the now invalid old references
    removeAllReferences(oldRequestString);    
  }  
}
