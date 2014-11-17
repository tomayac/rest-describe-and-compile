package com.google.code.apis.rest.client.GUI;

import com.google.code.apis.rest.client.CodeGeneration.CodeGenerator;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * This class creates the settings dialog
 *
 */
public class SettingsDialog {
  public static boolean alwaysShowDetails = false;
  public static boolean alwaysShowDetailsTemp = alwaysShowDetails;
  public static boolean treeItemsAlwaysOpen = true;
  public static boolean treeItemsAlwaysOpenTemp = treeItemsAlwaysOpen;
  public static int requestTimeoutSeconds = 180;
  public static int namingDepthLevel = 1;
  public static String pathToDiscoverer = "./resources/Discoverer.php5";
  public static String pathToJsonConverter = "./resources/XML2JSON.php5";
  public static boolean useEndpointAsName = true;
  public static String separationCharacter = "_";
  public static boolean useSeparationCharacter = false;
  public static boolean useSeparationCharacterTemp = useSeparationCharacter;
  public static String classDefaultSuffix = "";
  public static int indentWidth = 2;
  public static boolean indentWithSpaces = true; 
  public static String templateSuffix = ""; //"_TEMPLATE";  
  private static final String confirm_de = "<a href=\"RestDescribe.html?locale=de\">" + GuiFactory.strings.confirmAndRestart() + "</a>";   
  private static final String confirm_enUS = "<a href=\"RestDescribe.html?locale=en\">" + GuiFactory.strings.confirmAndRestart() + "</a>";
  private static final String confirm_ca = "<a href=\"RestDescribe.html?locale=ca\">" + GuiFactory.strings.confirmAndRestart() + "</a>";

  /**
   * Creates the settings dialog
   */
  public static void show() {
    String panelHeight = "3em";
    String panelWidthLeft = "15em";
    String panelWidthRight = "20em";
    String textBoxWidth = "8em";
    final DialogBox dialogBox = new DialogBox();    
    dialogBox.setText(GuiFactory.strings.settingsLink());    
       
    VerticalPanel settingsPanel = new VerticalPanel();   
    TabPanel tabPanel = new TabPanel();   
        
    final ListBox languageListBox = new ListBox();   
    languageListBox.addItem(GuiFactory.strings.english_US());
    languageListBox.addItem(GuiFactory.strings.german());
    languageListBox.addItem(GuiFactory.strings.catalan());
        
    VerticalPanel generalPanel = new VerticalPanel(); 
     
    HorizontalPanel languagePanel = new HorizontalPanel();   
    languagePanel.setHeight(panelHeight);
    HorizontalPanel languagePanelLeft = new HorizontalPanel();
    HorizontalPanel languagePanelRight = new HorizontalPanel();
     
    languagePanelLeft.setWidth(panelWidthLeft);
    languagePanelLeft.add(new HTML("<b>" + GuiFactory.strings.language() + "</b>"));   
    languagePanelRight.setWidth(panelWidthRight);
    languagePanelRight.add(new HTML(GuiFactory.strings.displayRestDescribe() + ":&nbsp;"));
    languagePanelRight.add(languageListBox);   
        
    languagePanel.add(languagePanelLeft);
    languagePanel.add(languagePanelRight);   
    generalPanel.add(languagePanel);
     
    final HorizontalPanel confirmLangChange =  new HorizontalPanel();
    confirmLangChange.setVisible(false);
    confirmLangChange.add(new HTML(GuiFactory.strings.cautionChangesGetLost() + "&nbsp;"));
       
    final HTML confirmLink = new HTML("");
    confirmLangChange.add(confirmLink);
    confirmLangChange.add(new HTML("."));   
    generalPanel.add(confirmLangChange);   
    languageListBox.addChangeListener(new ChangeListener() {
      public void onChange(Widget sender) {       
        updateAndDisplayConfirmLink(languageListBox.getItemText(languageListBox.getSelectedIndex()), confirmLink, confirmLangChange);
      }
    });
    languageListBox.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        updateAndDisplayConfirmLink(languageListBox.getItemText(languageListBox.getSelectedIndex()), confirmLink, confirmLangChange);
      }     
    });
     
    final CheckBox detailsCheckBox = new CheckBox();
    detailsCheckBox.setChecked(alwaysShowDetails);
    detailsCheckBox.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        if (detailsCheckBox.isChecked()) {
          alwaysShowDetailsTemp = true;        
        }
        else {
          alwaysShowDetailsTemp = false;
        }
      }
    });
       
    final CheckBox treesCheckBox = new CheckBox();
    treesCheckBox.setChecked(treeItemsAlwaysOpen);
    treesCheckBox.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        if (treesCheckBox.isChecked()) {
          treeItemsAlwaysOpenTemp = true;        
        }
        else {
          treeItemsAlwaysOpenTemp = false;
        }
      }
    });
    
    final HorizontalPanel miniSepTextPanel = new HorizontalPanel();
    final CheckBox useSeparationCharacterCheckBox = new CheckBox();
    useSeparationCharacterCheckBox.setChecked(useSeparationCharacter);
    useSeparationCharacterCheckBox.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {        
        if (useSeparationCharacterCheckBox.isChecked()) {
          useSeparationCharacterTemp = true;        
        }
        else {
          useSeparationCharacterTemp = false;
        }
        miniSepTextPanel.setVisible(useSeparationCharacterTemp);
      }
    });
     
    final TextBox timeoutSecondsTextBox = new TextBox();
    timeoutSecondsTextBox.setText(Integer.toString(requestTimeoutSeconds));
    timeoutSecondsTextBox.setWidth(panelHeight);
    timeoutSecondsTextBox.addChangeListener(new ChangeListener() {
      public void onChange(Widget sender) {
        try {
          requestTimeoutSeconds = Integer.parseInt(timeoutSecondsTextBox.getText());
          timeoutSecondsTextBox.removeStyleName("restDescribe-error");
        } catch (NumberFormatException e) {    
          timeoutSecondsTextBox.setStyleName("restDescribe-error");
        }
      }     
    });   
    
    miniSepTextPanel.setVisible(useSeparationCharacter);
    final TextBox separationCharacterTextBox = new TextBox();    
    separationCharacterTextBox.setText(separationCharacter);
    separationCharacterTextBox.setWidth(panelHeight);
    separationCharacterTextBox.addChangeListener(new ChangeListener() {
      public void onChange(Widget sender) {
        separationCharacter = separationCharacterTextBox.getText();
      }     
    });
     
    final TextBox namingDepthTextBox = new TextBox();
    namingDepthTextBox.setText(Integer.toString(namingDepthLevel));
    namingDepthTextBox.setWidth(panelHeight);
    namingDepthTextBox.addChangeListener(new ChangeListener() {
      public void onChange(Widget sender) {
        try {
          namingDepthLevel = Integer.parseInt(namingDepthTextBox.getText());
          namingDepthTextBox.removeStyleName("restDescribe-error");
          if (SettingsDialog.namingDepthLevel == 0) {
            namingDepthTextBox.setStyleName("restDescribe-error");
          }
        } catch (NumberFormatException e) {    
          namingDepthTextBox.setStyleName("restDescribe-error");
        }
      }     
    });
    
    final RadioButton useParameterAsNameRadio = new RadioButton("paramOrEndpoint", GuiFactory.strings.useParam());
    useParameterAsNameRadio.setChecked(!useEndpointAsName);
    useParameterAsNameRadio.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {        
        useEndpointAsName = !useParameterAsNameRadio.isChecked();        
      }      
    });
    
    final RadioButton useEndpointAsNameRadio = new RadioButton("paramOrEndpoint", GuiFactory.strings.useEndpoint());
    useEndpointAsNameRadio.setChecked(useEndpointAsName);
    useEndpointAsNameRadio.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {        
        useEndpointAsName = useEndpointAsNameRadio.isChecked();
      }      
    });
    
    final TextBox indentWidthTextBox = new TextBox();
    indentWidthTextBox.setWidth(panelHeight);
    indentWidthTextBox.setText(indentWidth + "");
    indentWidthTextBox.addChangeListener(new ChangeListener() {
      public void onChange(Widget sender) {
        try {
          indentWidth = Integer.parseInt(indentWidthTextBox.getText());
          indentWidthTextBox.removeStyleName("restDescribe-error");
          if (SettingsDialog.indentWidth == 0) {
            indentWidthTextBox.setStyleName("restDescribe-error");
          }
        } catch (NumberFormatException e) {    
          indentWidthTextBox.setStyleName("restDescribe-error");
        }        
      }      
    });
    
    final HorizontalPanel indentMiniPanel = new HorizontalPanel();
    final RadioButton indentWithSpacesRadio = new RadioButton("indent", GuiFactory.strings.indentSpaces());
    indentWithSpacesRadio.setChecked(indentWithSpaces);
    indentWithSpacesRadio.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {        
        indentWithSpaces = indentWithSpacesRadio.isChecked();
        indentMiniPanel.setVisible(true);
      }      
    });
    
    final RadioButton indentWithTabsRadio = new RadioButton("indent", GuiFactory.strings.indentTabs());
    indentWithTabsRadio.setChecked(!indentWithSpaces);
    indentWithTabsRadio.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {        
        indentWithSpaces = !indentWithTabsRadio.isChecked();
        indentMiniPanel.setVisible(false);
      }      
    });
    
    final TextBox classSuffixTextBox = new TextBox();
    classSuffixTextBox.setText(classDefaultSuffix);
    classSuffixTextBox.setWidth(textBoxWidth);
    classSuffixTextBox.addChangeListener(new ChangeListener() {
      public void onChange(Widget sender) {
        classDefaultSuffix = classSuffixTextBox.getText();        
      }
    });
     
    final TextBox pathToDiscovererTextBox = new TextBox();
    pathToDiscovererTextBox.setText(pathToDiscoverer);
    pathToDiscovererTextBox.setWidth(textBoxWidth);
    pathToDiscovererTextBox.addChangeListener(new ChangeListener() {
      public void onChange(Widget sender) {        
        pathToDiscoverer = pathToDiscovererTextBox.getText();        
      }     
    });
    
    // preferences
    VerticalPanel preferencesPanel = new VerticalPanel(); 
    preferencesPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
     
    // details
    HorizontalPanel detailsPanel = new HorizontalPanel(); 
    detailsPanel.setHeight(panelHeight);
    HorizontalPanel detailsPanelLeft = new HorizontalPanel();
    HorizontalPanel detailsPanelRight = new HorizontalPanel();   
    detailsPanelLeft.setWidth(panelWidthLeft);
    detailsPanelLeft.add(new HTML("<b>" + GuiFactory.strings.details() + "</b>"));
    detailsPanelRight.setWidth(panelWidthRight);
    detailsPanelRight.add(detailsCheckBox);
    detailsPanelRight.add(new HTML(GuiFactory.strings.displayDetails()));
    detailsPanel.add(detailsPanelLeft);
    detailsPanel.add(detailsPanelRight);   
    preferencesPanel.add(detailsPanel);
     
    // tree items open
    HorizontalPanel treesPanel = new HorizontalPanel(); 
    treesPanel.setHeight(panelHeight);
    HorizontalPanel treesPanelLeft = new HorizontalPanel();
    HorizontalPanel treesPanelRight = new HorizontalPanel();   
    treesPanelLeft.setWidth(panelWidthLeft);
    treesPanelLeft.add(new HTML("<b>" + GuiFactory.strings.treesOpen() + "</b>"));
    treesPanelRight.setWidth(panelWidthRight);
    treesPanelRight.add(treesCheckBox);
    treesPanelRight.add(new HTML(GuiFactory.strings.treesAlwaysOpen()));
    treesPanel.add(treesPanelLeft);
    treesPanel.add(treesPanelRight);   
    preferencesPanel.add(treesPanel);
         
    // request tab
    VerticalPanel requestPanel = new VerticalPanel(); 
    requestPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);

    // path to discoverer
    HorizontalPanel pathPanel = new HorizontalPanel();
    pathPanel.setHeight(panelHeight);
    HorizontalPanel pathPanelLeft = new HorizontalPanel();
    HorizontalPanel pathPanelRight = new HorizontalPanel();
    pathPanelLeft.setWidth(panelWidthLeft);
    pathPanelLeft.add(new HTML("<b>" + GuiFactory.strings.pathDiscover() + "</b>"));
    pathPanelRight.setWidth(panelWidthRight);
    pathPanelRight.add(pathToDiscovererTextBox);
    pathPanelRight.add(new HTML(GuiFactory.strings.pathToDiscoverer()));   
    pathPanel.add(pathPanelLeft);
    pathPanel.add(pathPanelRight);   
    requestPanel.add(pathPanel);
    
    // timeout
    HorizontalPanel timeoutPanel = new HorizontalPanel();
    timeoutPanel.setHeight(panelHeight);
    HorizontalPanel timeoutPanelLeft = new HorizontalPanel();
    HorizontalPanel timeoutPanelRight = new HorizontalPanel();
    timeoutPanelLeft.setWidth(panelWidthLeft);
    timeoutPanelLeft.add(new HTML("<b>" + GuiFactory.strings.timeout() + "</b>"));
    timeoutPanelRight.setWidth(panelWidthRight);
    timeoutPanelRight.add(timeoutSecondsTextBox);
    timeoutPanelRight.add(new HTML(GuiFactory.strings.timeoutSeconds()));   
    timeoutPanel.add(timeoutPanelLeft);
    timeoutPanel.add(timeoutPanelRight);   
    requestPanel.add(timeoutPanel);
    
    // code generation tab
    VerticalPanel codeGenerationPanel = new VerticalPanel(); 
    codeGenerationPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
    
    // naming depth
    HorizontalPanel namingDepthPanel = new HorizontalPanel();
    namingDepthPanel.setHeight(panelHeight);
    HorizontalPanel namingDepthPanelLeft = new HorizontalPanel();
    HorizontalPanel namingDepthPanelRight = new HorizontalPanel();
    namingDepthPanelLeft.setWidth(panelWidthLeft);
    namingDepthPanelLeft.add(new HTML("<b>" + GuiFactory.strings.namingDepth() + "</b>"));
    namingDepthPanelRight.setWidth(panelWidthRight);
    namingDepthPanelRight.add(namingDepthTextBox);
    namingDepthPanelRight.add(new HTML(GuiFactory.strings.namingDepthLevel()));   
    namingDepthPanel.add(namingDepthPanelLeft);
    namingDepthPanel.add(namingDepthPanelRight);   
    codeGenerationPanel.add(namingDepthPanel);
    
    // class default suffix
    HorizontalPanel classSuffixPanel = new HorizontalPanel();
    classSuffixPanel.setHeight(panelHeight);
    HorizontalPanel classSuffixPanelLeft = new HorizontalPanel();
    HorizontalPanel classSuffixPanelRight = new HorizontalPanel();
    classSuffixPanelLeft.setWidth(panelWidthLeft);
    classSuffixPanelLeft.add(new HTML("<b>" + GuiFactory.strings.classSuffix() + "</b>"));
    classSuffixPanelRight.setWidth(panelWidthRight);
    classSuffixPanelRight.add(classSuffixTextBox);
    classSuffixPanelRight.add(new HTML(GuiFactory.strings.classDefaultSuffix()));   
    classSuffixPanel.add(classSuffixPanelLeft);
    classSuffixPanel.add(classSuffixPanelRight);   
    codeGenerationPanel.add(classSuffixPanel);
    
    // source for class names
    HorizontalPanel classNamePanel = new HorizontalPanel();
    classNamePanel.setHeight(panelHeight);
    HorizontalPanel classNamePanelLeft = new HorizontalPanel();
    HorizontalPanel classNamePanelRight = new HorizontalPanel();
    classNamePanelLeft.setWidth(panelWidthLeft);
    classNamePanelLeft.add(new HTML("<b>" + GuiFactory.strings.className() + "</b>"));
    classNamePanelRight.setWidth(panelWidthRight);
    VerticalPanel radioPanel = new VerticalPanel();    
    radioPanel.add(useEndpointAsNameRadio);
    radioPanel.add(useParameterAsNameRadio);
    classNamePanelRight.add(radioPanel);       
    classNamePanel.add(classNamePanelLeft);
    classNamePanel.add(classNamePanelRight);   
    codeGenerationPanel.add(classNamePanel);
    
    // source for class names
    HorizontalPanel indentionPanel = new HorizontalPanel();
    indentionPanel.setHeight(panelHeight);
    HorizontalPanel indentionPanelLeft = new HorizontalPanel();
    HorizontalPanel indentionPanelRight = new HorizontalPanel();
    indentionPanelLeft.setWidth(panelWidthLeft);
    indentionPanelLeft.add(new HTML("<b>" + GuiFactory.strings.indentionStyle() + "</b>"));
    indentionPanelRight.setWidth(panelWidthRight);
    VerticalPanel indentionRadioPanel = new VerticalPanel();
    indentionRadioPanel.add(indentWithSpacesRadio);    
    indentMiniPanel.add(indentWidthTextBox);
    indentMiniPanel.add(new HTML(GuiFactory.strings.indentionWidth()));
    indentionRadioPanel.add(indentMiniPanel);
    indentionRadioPanel.add(indentWithTabsRadio);
    indentionPanelRight.add(indentionRadioPanel);       
    indentionPanel.add(indentionPanelLeft);
    indentionPanel.add(indentionPanelRight);   
    codeGenerationPanel.add(indentionPanel);
    
    // use separation character
    HorizontalPanel useSeparationCharacterPanel = new HorizontalPanel(); 
    useSeparationCharacterPanel.setHeight(panelHeight);
    HorizontalPanel useSeparationCharacterPanelLeft = new HorizontalPanel();
    HorizontalPanel useSeparationCharacterPanelRight = new HorizontalPanel();   
    useSeparationCharacterPanelLeft.setWidth(panelWidthLeft);
    useSeparationCharacterPanelLeft.add(new HTML("<b>" + GuiFactory.strings.sepChar() + "</b>"));
    useSeparationCharacterPanelRight.setWidth(panelWidthRight);
    VerticalPanel miniSepPanel = new VerticalPanel();    
    HorizontalPanel miniSepCheckPanel = new HorizontalPanel();
    miniSepPanel.add(miniSepCheckPanel);
    miniSepCheckPanel.add(useSeparationCharacterCheckBox);
    miniSepCheckPanel.add(new HTML(GuiFactory.strings.useSepChar()));    
    miniSepTextPanel.add(separationCharacterTextBox);
    miniSepTextPanel.add(new HTML(GuiFactory.strings.sepWithChar()));
    miniSepPanel.add(miniSepTextPanel);
    useSeparationCharacterPanelRight.add(miniSepPanel);    
    useSeparationCharacterPanel.add(useSeparationCharacterPanelLeft);
    useSeparationCharacterPanel.add(useSeparationCharacterPanelRight);   
    codeGenerationPanel.add(useSeparationCharacterPanel);
    
    tabPanel.add(generalPanel, "<a href=\"#\">" + GuiFactory.strings.generalTab() + "</a>", true);
    tabPanel.add(preferencesPanel, "<a href=\"#\">" + GuiFactory.strings.preferencesTab() + "</a>", true);
    tabPanel.add(requestPanel, "<a href=\"#\">" + GuiFactory.strings.requestTab() + "</a>", true);
    tabPanel.add(codeGenerationPanel, "<a href=\"#\">" + GuiFactory.strings.codeGenerationTab() + "</a>", true);
    tabPanel.selectTab(0);
       
    // ok button
    HorizontalPanel buttonPanel = new HorizontalPanel();
    Button okButton = new Button(GuiFactory.strings.ok());
    buttonPanel.add(okButton);
    okButton.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        // apply changes
        alwaysShowDetails = alwaysShowDetailsTemp;
        treeItemsAlwaysOpen = treeItemsAlwaysOpenTemp;
        useSeparationCharacter = useSeparationCharacterTemp;        
        RestCompilePanel.generateCode(CodeGenerator.language);
        GuiFactory.blockScreen(false);       
        dialogBox.hide();
      }
    });
     
    // cancel button
    Button closeButton = new Button(GuiFactory.strings.cancel());
    buttonPanel.add(closeButton);
    closeButton.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {    
        GuiFactory.blockScreen(false);
        dialogBox.hide();
      }
    });
     
    settingsPanel.add(new HTML("<br />"));
    settingsPanel.add(tabPanel);
    settingsPanel.add(new HTML("<br />"));
    settingsPanel.add(buttonPanel);
    settingsPanel.setCellHorizontalAlignment(buttonPanel, HasHorizontalAlignment.ALIGN_CENTER);
     
    dialogBox.setPopupPosition((int) Math.floor(Window.getClientWidth() / 4), (int) Math.floor(Window.getClientHeight() / 4));
    dialogBox.setWidget(settingsPanel);   
    GuiFactory.blockScreen(true);    
    dialogBox.show();   
  }
 
  private static void updateAndDisplayConfirmLink(String selectedLanguage, HTML confirmLink, HorizontalPanel confirmLangChange) {
    confirmLangChange.setVisible(true);
    confirmLink.setHTML("");
    if (selectedLanguage.equals(GuiFactory.strings.german())) {      
      confirmLink.setHTML(confirm_de);
    }
    else if (selectedLanguage.equals(GuiFactory.strings.english_US())) {
      confirmLink.setHTML(confirm_enUS);
    }
    else if (selectedLanguage.equals(GuiFactory.strings.catalan())) {
      confirmLink.setHTML(confirm_ca);
    }    
  }
}
