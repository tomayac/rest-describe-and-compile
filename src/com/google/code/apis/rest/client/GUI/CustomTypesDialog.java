package com.google.code.apis.rest.client.GUI;

import java.util.Iterator;
import java.util.Vector;

import com.google.code.apis.rest.client.Tree.ParamItem;
import com.google.code.apis.rest.client.Util.TypeEstimator;
import com.google.code.apis.rest.client.Wadl.GenericNode;
import com.google.code.apis.rest.client.Wadl.ParamNode;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * This class displays a define custom types dialog.
 *
 */
public class CustomTypesDialog {  
  private static TextArea optionsTextArea = new TextArea();
  
  /**
   * Creates and shows a dialog window which allows for custom type management.
   *  
   * @param parameterType The listbox of the parameter
   * @param param The parameter node itself
   * @param parentNode The request node of the parameter
   * @param parentTreeItem The tree item of the parameter's request node
   * @param paramCounter The parameter item counter
   * @param paramCounter The parameter item counter
   * @param estimationQualityHtml The type estimation quality string
   */
  public static void show(final ListBox parameterType, final ParamNode param, final GenericNode parentNode, final TreeItem parentTreeItem, final int paramCounter, final HTML estimationQualityHtml) {
    final Button newTypeButton = new Button(GuiFactory.strings.newType());    
    final Button acceptTypeButton = new Button(GuiFactory.strings.acceptType());
    final Button deleteTypeButton = new Button(GuiFactory.strings.deleteType());
    final Button editTypeButton = new Button(GuiFactory.strings.editType());
    final Button closeButton = new Button(GuiFactory.strings.cancel());    
    optionsTextArea.setEnabled(false);
    optionsTextArea.setText("");    
    
    final DialogBox dialog = new DialogBox();
    dialog.setText(GuiFactory.strings.newCustomType());    
    
    final VerticalPanel containerPanel = new VerticalPanel();           
    
    containerPanel.add(new HTML("<br />" + GuiFactory.strings.availableTypes()));
    final ListBox customTypesListBox = new ListBox();
    ParamItem.addCustomTypes(customTypesListBox);
    customTypesListBox.addChangeListener(new ChangeListener() {  
      public void onChange(Widget sender) {
        loadOptions(customTypesListBox.getItemText(customTypesListBox.getSelectedIndex()));
      }      
    });
    String listBoxAndTextBoxWidth = "12em";
    customTypesListBox.setWidth(listBoxAndTextBoxWidth);
    
    containerPanel.add(customTypesListBox);    
    
    // hidden inner panel, pops up and replaces the listbox when a new custom type is entered
    final VerticalPanel innerPanel = new VerticalPanel();
    containerPanel.add(innerPanel);
    innerPanel.setVisible(false);    
    final HorizontalPanel innerContainerPanel = new HorizontalPanel();
    innerPanel.add(innerContainerPanel);
    final TextBox typeTextBox = new TextBox();
    typeTextBox.setText("");
    typeTextBox.setWidth(listBoxAndTextBoxWidth);
    innerContainerPanel.add(typeTextBox);
    final HorizontalPanel innerErrorMessagepanel = new HorizontalPanel();
    innerErrorMessagepanel.setVisible(false);
    innerPanel.add(innerErrorMessagepanel);
    innerErrorMessagepanel.add(new HTML(GuiFactory.strings.fillNamesAndValues()));
    
    //  ok button of the hidden inner panel
    Button okButton = new Button(GuiFactory.strings.ok());    
    okButton.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        if ((typeTextBox.getText().replaceAll("\n", "").length() > 0) && (optionsTextArea.getText().length() > 0)) {
          innerErrorMessagepanel.setVisible(false);
          
          if (!ParamItem.customTypes.contains(typeTextBox.getText())) {
            ParamItem.customTypes.add(typeTextBox.getText());
          }
          Vector options = new Vector();
          String[] optionStrings = optionsTextArea.getText().split("\n");
          for (int i = 0; i < optionStrings.length; i++) {
            if (!options.contains(optionStrings[i])) options.add(optionStrings[i]);
          }
          ParamItem.customTypeOptions.put(typeTextBox.getText(), options);
          ParamItem.addCustomTypesAndClear(customTypesListBox);
          customTypesListBox.setSelectedIndex(getIndexOf(typeTextBox.getText()));
          
          customTypesListBox.setVisible(true);        
          innerPanel.setVisible(false);    
          optionsTextArea.setEnabled(false);
          newTypeButton.setEnabled(true);
          deleteTypeButton.setEnabled(true);
          editTypeButton.setEnabled(true);
          closeButton.setEnabled(true);
          acceptTypeButton.setEnabled(true);
        }
        else {
          innerErrorMessagepanel.setVisible(true);
        }                
      }
    });
    innerContainerPanel.add(okButton);    
    // cancel button of the hidden inner panel
    Button cancelButton = new Button(GuiFactory.strings.cancel());    
    cancelButton.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {   
        optionsTextArea.setText("");
        typeTextBox.setText("");
        customTypesListBox.setVisible(true);
        innerPanel.setVisible(false);  
        optionsTextArea.setEnabled(false);
        newTypeButton.setEnabled(true);
        deleteTypeButton.setEnabled(true);
        editTypeButton.setEnabled(true);
        closeButton.setEnabled(true);
        acceptTypeButton.setEnabled(true);
      }
    });
    innerContainerPanel.add(cancelButton);
    
    HorizontalPanel newTypePanel = new HorizontalPanel();
    newTypePanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
    containerPanel.add(newTypePanel);
    VerticalPanel typesPanel = new VerticalPanel();
    typesPanel.add(new HTML("<br />" + GuiFactory.strings.allowedOptions()));    
    optionsTextArea.setWidth("15em");
    optionsTextArea.setHeight("10em");    
    typesPanel.add(optionsTextArea);
    newTypePanel.add(typesPanel);
    
    VerticalPanel buttonPanel = new VerticalPanel();
    
    // new type button
    String buttonWidth = "10em";
    newTypeButton.setWidth(buttonWidth);
    newTypeButton.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {             
        customTypesListBox.setVisible(false);        
        innerPanel.setVisible(true);
        typeTextBox.setText("");
        typeTextBox.setFocus(true);
        optionsTextArea.setText("");
        optionsTextArea.setEnabled(true);
        newTypeButton.setEnabled(false);
        editTypeButton.setEnabled(false);
        deleteTypeButton.setEnabled(false);
        closeButton.setEnabled(false);
        acceptTypeButton.setEnabled(false);
      }
    });
    buttonPanel.add(newTypeButton);   
    
    // edit type button
    editTypeButton.setWidth(buttonWidth);
    buttonPanel.add(editTypeButton);        
    editTypeButton.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        if (customTypesListBox.getItemCount() > 0) {
          customTypesListBox.setVisible(false);    
          newTypeButton.setEnabled(false);
          editTypeButton.setEnabled(false);
          deleteTypeButton.setEnabled(false);
          closeButton.setEnabled(false);
          acceptTypeButton.setEnabled(false);
          String oldName = customTypesListBox.getItemText(customTypesListBox.getSelectedIndex());
          typeTextBox.setText(oldName);         
          optionsTextArea.setEnabled(true);
          innerPanel.setVisible(true);          
        }
      }
    });
    
    // delete type button
    deleteTypeButton.setWidth(buttonWidth);
    buttonPanel.add(deleteTypeButton);        
    newTypePanel.add(buttonPanel);
    deleteTypeButton.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        if (customTypesListBox.getItemCount() > 0) {             
          String selectedCustomType = customTypesListBox.getItemText(customTypesListBox.getSelectedIndex());
          ParamItem.customTypes.remove(selectedCustomType);
          ParamItem.customTypeOptions.remove(selectedCustomType);          
          customTypesListBox.removeItem(customTypesListBox.getSelectedIndex());
          optionsTextArea.setText("");
          if (param.getType().equals(selectedCustomType)) {
            param.setOptions(new Vector());            
          }
          if (customTypesListBox.getItemCount() > 0) {
            loadOptions(customTypesListBox.getItemText(customTypesListBox.getSelectedIndex()));
          }
        }
      }
    });
   
    // accept type button
    acceptTypeButton.addClickListener(new ClickListener() {    
      public void onClick(Widget sender) {
        GuiFactory.blockScreen(false);
        if (customTypesListBox.getItemCount() > 0) {
          ParamItem.addTypes(parameterType);        
          String selectedCustomType = customTypesListBox.getItemText(customTypesListBox.getSelectedIndex());
          parameterType.setSelectedIndex(ParamItem.indexOfValue(selectedCustomType));          
        
          param.updateType(param.getType(), selectedCustomType);
          param.setEstimateQuality(TypeEstimator.manual);
          param.setOptions((Vector) ParamItem.customTypeOptions.get(selectedCustomType));                    
        }
        ParamItem.listParams(
            parentNode,
            parentTreeItem, 
            parentNode.getApplication().getAnalyzer().getRequestString());       
        parentTreeItem.getChild(paramCounter).setSelected(true);
        parentTreeItem.getTree().ensureSelectedItemVisible();

        dialog.hide();        
      }
    });
    acceptTypeButton.setWidth(buttonWidth);
    
    // close button
    closeButton.setWidth(buttonWidth);
    closeButton.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {        
        ParamItem.listParams(
            parentNode,
            parentTreeItem,
            parentNode.getApplication().getAnalyzer().getRequestString());       
        parentTreeItem.getChild(paramCounter).setSelected(true);
        parentTreeItem.getTree().ensureSelectedItemVisible();
        GuiFactory.blockScreen(false);
        dialog.hide();
      }
    });
    
    containerPanel.add(new HTML("<br />&nbsp;"));
    HorizontalPanel okClosePanel = new HorizontalPanel();
    okClosePanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
    okClosePanel.setWidth("100%");    
    okClosePanel.add(acceptTypeButton);
    okClosePanel.add(closeButton);
    containerPanel.add(okClosePanel);
  
    if (ParamItem.customTypes.size() > 0) {
      loadOptions(customTypesListBox.getItemText(customTypesListBox.getSelectedIndex()));
    }
    
    dialog.setWidget(containerPanel);
    dialog.setPopupPosition((int) Math.floor(Window.getClientWidth() * 0.4), (int) Math.floor(Window.getClientHeight() * 0.2));
    GuiFactory.blockScreen(true);
    dialog.show();    
  }

  /**
   * Returns the index of a listbox item string
   * @param text The listbox item string
   * @return The item's index
   */
  private static int getIndexOf(String text) {    
    for (int i = 0; i < ParamItem.customTypes.size(); i++) {
      if (((String) ParamItem.customTypes.elementAt(i)).equals(text)) return i;
    }
    return -1;    
  }
  
  /**
   * Loads the valid type options for a given custom type
   * @param selected The custom type name
   */
  private static void loadOptions(String selected) {    
    Vector options = (Vector) ParamItem.customTypeOptions.get(selected);
    Iterator optionsIterator = options.iterator();
    optionsTextArea.setText("");
    while(optionsIterator.hasNext()) {
      optionsTextArea.setText(optionsTextArea.getText() + (String) optionsIterator.next() + "\n"); 
    }
  }
}
