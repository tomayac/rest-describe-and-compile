/**
 * 
 */
package com.google.code.apis.rest.client.GUI;

import java.util.Iterator;
import java.util.Vector;

import com.google.code.apis.rest.client.CodeGeneration.CodeGenerator;
import com.google.code.apis.rest.client.Wadl.Analyzer;
import com.google.code.apis.rest.client.Wadl.ApplicationNode;
import com.google.code.apis.rest.client.Wadl.MethodNode;
import com.google.code.apis.rest.client.Wadl.ParamNode;
import com.google.code.apis.rest.client.Wadl.RequestNode;
import com.google.code.apis.rest.client.Wadl.ResourceNode;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.WindowResizeListener;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author tsteiner
 *
 */
public class RestCompilePanel extends Composite implements WindowResizeListener {
  public static VerticalPanel restCompilePanel = new VerticalPanel();
  private static VerticalPanel restCompileArea = new VerticalPanel();
  public static HorizontalPanel buttonPanel = new HorizontalPanel();
  public static TextArea codeTextArea = new TextArea();  
  public static HorizontalPanel compilePanel = new HorizontalPanel();
  private static VerticalPanel requestClassesPanel = new VerticalPanel();
  private static ListBox paramsListBox = new ListBox();
  private static VerticalPanel namingParamPanel = new VerticalPanel();
  private static HorizontalPanel namingDepthPanel = new HorizontalPanel();
  public static String paramToUseForName = "";
  private static ScrollPanel usedClassesPanel = new ScrollPanel();
  private static RadioButton useParameterAsNameRadio = new RadioButton("paramOrEndpoint", GuiFactory.strings.useParam());
  private static RadioButton useEndpointAsNameRadio = new RadioButton("paramOrEndpoint", GuiFactory.strings.useEndpoint());
  public static final String[] commonOpParamNames = {"operation", "method", "function", "request", "call"};
  
  public RestCompilePanel() {
    Window.addWindowResizeListener(this);
    
    restCompilePanel.setStyleName("restDescribe-restCompilePanel");
    restCompilePanel.setWidth("100%");       
        
    HTML restCompileHeader = new HTML ("<h2>" + GuiFactory.strings.restCompile() + "</h2>");
    restCompilePanel.add(restCompileHeader);    
    
    restCompileArea.setStyleName("restDescribe-restCompileArea");
    restCompilePanel.add(restCompileArea);    
    
    HorizontalPanel containerPanel = new HorizontalPanel();
    HorizontalPanel languagesPanel = new HorizontalPanel();
    containerPanel.add(languagesPanel);
    VerticalPanel reviewPanel = new VerticalPanel();
    containerPanel.add(new HTML("&nbsp;&nbsp;"));
    containerPanel.add(reviewPanel);    
    restCompileArea.add(containerPanel);
         
    final Button phpLink = new Button("<nobr>" + imageHtml("php.png") + CodeGenerator.php5 + "</nobr>");
    phpLink.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        generateCode(CodeGenerator.php5);
      }     
    });
    languagesPanel.add(phpLink);        
                     
    final Button rubyLink = new Button("<nobr>" + imageHtml("ruby.png") + CodeGenerator.ruby + "</nobr>");    
    rubyLink.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        generateCode(CodeGenerator.ruby);
      }     
    });
    languagesPanel.add(rubyLink);   
     
    final Button pythonLink = new Button("<nobr>" + imageHtml("python.png") + CodeGenerator.python + "</nobr>");
    pythonLink.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        generateCode(CodeGenerator.python);
      }     
    });
    languagesPanel.add(pythonLink);   

    final Button javaLink = new Button("<nobr>" + imageHtml("java.png") + CodeGenerator.java + "</nobr>");
    javaLink.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        generateCode(CodeGenerator.java);
      }     
    });
    languagesPanel.add(javaLink);   

    final Button csharpLink = new Button("<nobr>" + imageHtml("csharp.png") + CodeGenerator.csharp + "</nobr>");
    csharpLink.setEnabled(false);
    csharpLink.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        generateCode(CodeGenerator.csharp);
      }     
    });    
    languagesPanel.add(csharpLink);   
    
    Hyperlink reviewWadl = new Hyperlink(GuiFactory.strings.reviewWadl(), true, "");
    reviewWadl.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        WadlPanel.wadlPreviewButton.click();
      }
    });    
    reviewPanel.add(reviewWadl);
    
    reviewPanel.add(new HTML("<br />"));
    
    Hyperlink backLink = new Hyperlink(GuiFactory.strings.backToDescribe(), true, "");
    backLink.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        GuiFactory.toggleMode(GuiFactory.restDescribe);
      }
    });    
    reviewPanel.add(backLink);

    restCompileArea.add(new HTML("&nbsp;<br />"));
    restCompileArea.add(compilePanel);
    compilePanel.setVisible(false);           
    codeTextArea.setText("");    
    compilePanel.add(codeTextArea);  
    compilePanel.add(new HTML("&nbsp;"));
    VerticalPanel codeOptionsPanel = new VerticalPanel();
    compilePanel.add(codeOptionsPanel);
    compilePanel.setCellVerticalAlignment(codeOptionsPanel, HasVerticalAlignment.ALIGN_BOTTOM);
    
    // source for class names    
    useParameterAsNameRadio.setChecked(!SettingsDialog.useEndpointAsName);
    useParameterAsNameRadio.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {        
        SettingsDialog.useEndpointAsName = !useParameterAsNameRadio.isChecked();   
        toggleNamingPanels(SettingsDialog.useEndpointAsName);
      }      
    });
        
    useEndpointAsNameRadio.setChecked(SettingsDialog.useEndpointAsName);
    useEndpointAsNameRadio.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {        
        SettingsDialog.useEndpointAsName = useEndpointAsNameRadio.isChecked();
        toggleNamingPanels(SettingsDialog.useEndpointAsName);
      }      
    });
    
    VerticalPanel radioContainerPanel = new VerticalPanel();
    radioContainerPanel.add(new HTML("<h2>" + GuiFactory.strings.className() + ":</h2>"));   
    codeOptionsPanel.add(radioContainerPanel);
        
    final TextBox namingDepthTextBox = new TextBox();    
    namingDepthTextBox.setWidth("2em");
    namingDepthTextBox.setText(SettingsDialog.namingDepthLevel + "");
    namingDepthTextBox.addChangeListener(new ChangeListener() {
      public void onChange(Widget sender) {
        try {
          SettingsDialog.namingDepthLevel =
            Integer.parseInt(namingDepthTextBox.getText());
          namingDepthTextBox.removeStyleName("restDescribe-error");
          if (SettingsDialog.namingDepthLevel == 0) {
            namingDepthTextBox.setStyleName("restDescribe-error");
          }
        } catch (NumberFormatException e) {    
          namingDepthTextBox.setStyleName("restDescribe-error");
        }                  
      }      
    });            
       
    codeOptionsPanel.add(useEndpointAsNameRadio);
    codeOptionsPanel.add(namingDepthPanel);
    namingDepthPanel.add(new HTML("&nbsp;"));
    namingDepthPanel.add(namingDepthTextBox);
    namingDepthPanel.add(new HTML(GuiFactory.strings.namingDepth()));    
        
    codeOptionsPanel.add(useParameterAsNameRadio);
    codeOptionsPanel.add(namingParamPanel);            
    namingParamPanel.add(paramsListBox);    
    paramsListBox.setWidth("12em");
    paramsListBox.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        if (paramsListBox.getItemCount() > 0) {
          paramToUseForName = paramsListBox.getValue(paramsListBox.getSelectedIndex());
        }
      }      
    });    
    
    toggleNamingPanels(SettingsDialog.useEndpointAsName);
    
    Button recompileButton = new Button(GuiFactory.strings.recompile());
    recompileButton.setWidth("12em");
    codeOptionsPanel.add(recompileButton);
    recompileButton.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        if (CodeGenerator.language.equals(CodeGenerator.java)) {
          javaLink.click();
        }
        else if (CodeGenerator.language.equals(CodeGenerator.php5)) {
          phpLink.click();
        } 
        else if (CodeGenerator.language.equals(CodeGenerator.python)) {
          pythonLink.click();
        }
        else if (CodeGenerator.language.equals(CodeGenerator.csharp)) {
          csharpLink.click();
        }
        else if (CodeGenerator.language.equals(CodeGenerator.ruby)) {
          rubyLink.click();
        }
      }
    });
        
    VerticalPanel requestClassesContainerPanel = new VerticalPanel();
    requestClassesContainerPanel.add(new HTML("<br />"));
    requestClassesContainerPanel.add(new HTML("<h2>" + GuiFactory.strings.generatedClasses() + ":</h2>"));    
    usedClassesPanel.add(requestClassesPanel); 
    usedClassesPanel.setStyleName("restDescribe-classNamesPanel");
    requestClassesContainerPanel.add(usedClassesPanel);
    
    codeOptionsPanel.add(requestClassesContainerPanel);   
    
    onWindowResized(Window.getClientWidth(), Window.getClientHeight());
        
    Button okButton = new Button(GuiFactory.strings.ok());
    okButton.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        GuiFactory.toggleMode(GuiFactory.restDescribe);
      }
    });
    
    Button selectAllButton = new Button(GuiFactory.strings.selectAll());
    selectAllButton.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        selectAll(codeTextArea);
      }
    });    
    
    restCompileArea.add(new HTML("<br />"));    
    buttonPanel.add(okButton);    
    buttonPanel.add(selectAllButton);    
    restCompileArea.add(buttonPanel);
    restCompileArea.setCellHorizontalAlignment(buttonPanel, HasHorizontalAlignment.ALIGN_CENTER);
    
    initWidget(restCompilePanel);
  }

  /**
   * @param useEndpointAsName
   */
  public static void toggleNamingPanels(boolean useEndpointAsName) {    
    namingDepthPanel.setVisible(useEndpointAsName);
    namingParamPanel.setVisible(!useEndpointAsName);
    useEndpointAsNameRadio.setChecked(useEndpointAsName);
    useParameterAsNameRadio.setChecked(!useEndpointAsName);   
  }

  /**
   * 
   */
  private static void listUsedClassNames() {
    Iterator classNameIterator = CodeGenerator.usedClassNames.iterator();
    requestClassesPanel.clear();
    while(classNameIterator.hasNext()) {      
      final String className = (String) classNameIterator.next();
      Hyperlink hyperlink = new Hyperlink("<nobr><img align=\"absmiddle\" src=\"./images/details.png\" />" + className + "</nobr>", true, "");
      hyperlink.addClickListener(new ClickListener() {
        public void onClick(Widget sender) {
          int pos = codeTextArea.getText().indexOf(className);
          codeTextArea.setCursorPos(pos);          
          codeTextArea.setSelectionRange(pos, className.length());
        }        
      });
      requestClassesPanel.add(hyperlink);      
    }    
  }
  
  private static void listParams() {
    if (Analyzer.application != null &&
        Analyzer.application.getResources() != null &&
        Analyzer.application.getResources().getAllResources() != null) {
      paramsListBox.clear(); 
      addParamsToListBox(Analyzer.application.getResources().getAllResources(), true);      
    }
  }

  /**
   * @param language
   */
  protected static void generateCode(String language) {
    CodeGenerator codeGenerator = new CodeGenerator(Analyzer.application);    
    buttonPanel.setVisible(true);
    compilePanel.setVisible(true);    
    listParams();    
    toggleNamingPanels(SettingsDialog.useEndpointAsName);
    String code = codeGenerator.generate(language);
    codeTextArea.setText(code);
    listUsedClassNames();
  }

  private static void guessNamingParameter() {      
    for (int i = 0; i < paramsListBox.getItemCount(); i++) {
      for (int j = 0; j < commonOpParamNames.length; j++) {
        if (paramsListBox.getValue(i).equalsIgnoreCase(commonOpParamNames[j])) {
          paramsListBox.setSelectedIndex(i);
          paramToUseForName = paramsListBox.getValue(i);
          return;      
        }
      }
    }        
  }
  /* (non-Javadoc)
   * @see com.google.gwt.user.client.WindowResizeListener#onWindowResized(int, int)
   */
  public void onWindowResized(int width, int height) {
    restCompileArea.setWidth(Math.floor(0.97 * width) + "px");
    codeTextArea.setWidth(Math.floor(0.7 * width) + "px");
    codeTextArea.setHeight(Math.floor(0.5 * height) + "px");
    usedClassesPanel.setHeight(Math.floor(0.18 * height) + "px");
    usedClassesPanel.setWidth(Math.floor(0.2 * width) + "px");
  }
  
  private static String imageHtml(String src) {
    return "<img src=\"./images/" + src + "\" border=\"0\" height=\"30\" width=\"30\" align=\"absmiddle\" style=\"padding-left:20px; padding-right:5px;\" />";
  }
  private static void selectAll(TextArea textArea) {
    textArea.setSelectionRange(0, textArea.getText().length());    
  }
  
  private static void addParamsToListBox(Vector allResources, boolean firstCall) {
    // do this the first time only
    if (firstCall) {      
      if (Analyzer.application != null) {
        ApplicationNode application = Analyzer.application;      
        if (!application.getAllParams().isEmpty()) {          
          examineParams(application.getAllParams());
          return;
        }    
        // if we still found nothing, check the referenced methods
        if (!application.getAllMethods().isEmpty()) {          
          Vector allMethods = application.getAllMethods();
          Iterator allMethodsIterator = allMethods.iterator();
          while (allMethodsIterator.hasNext()) {
            MethodNode method = (MethodNode) allMethodsIterator.next();                    
            if (method.getRequest() != null) {              
              RequestNode request = method.getRequest();                        
              if (!request.getAllParams().isEmpty()) {                
                examineParams(request.getAllParams());  
                return;
              }
            }
          }
        }
      }
    }  
    // do this every time
    Iterator allResourcesIterator = allResources.iterator();
    while(allResourcesIterator.hasNext()) {
      ResourceNode resource = (ResourceNode) allResourcesIterator.next();      
      if (!resource.getAllMethods().isEmpty()) {
        Vector allMethods = resource.getAllMethods();
        Iterator allMethodsIterator = allMethods.iterator();
        while(allMethodsIterator.hasNext()) {
          MethodNode method = (MethodNode) allMethodsIterator.next();          
          if (method.getRequest() != null) {
            RequestNode request = method.getRequest();                    
            if (!request.getAllParams().isEmpty()) {             
              examineParams(request.getAllParams());
              return;
            }
          }          
        }
      }    
      // recursive call for inner resources (a resource in a resource)
      addParamsToListBox(resource.getAllResources(), false);      
    }
  }   

  /**
   * @param allParams
   */
  private static void examineParams(Vector allParams) {
    Iterator allParamsIterator = allParams.iterator();
    int i = 0;
    while(allParamsIterator.hasNext()) {
      ParamNode param = (ParamNode) allParamsIterator.next();      
      if (param.getValue().length() > 0) {
        paramsListBox.addItem(param.getName() + " (\"" + param.getValue() + "\")", param.getName());
        if (paramToUseForName.equalsIgnoreCase(param.getName())) {
          paramsListBox.setSelectedIndex(i);
          return;
        }
      }
      else {
        paramsListBox.addItem("[N/A] " + param.getName() + " (N/A)", param.getName());        
      }
      i++;      
    }
    // if no paramToUseForName was given, try to guess it
    if (paramToUseForName.equals("")) {
      guessNamingParameter(); 
      // if one was found, use it
      if (paramToUseForName.length() > 0) {
        SettingsDialog.useEndpointAsName = false;
        useParameterAsNameRadio.setChecked(true);
      }
      // if nothing found, use the endpoint as name
      else {
        useEndpointAsNameRadio.setChecked(true);
      }
    }       
  }
}
