package com.google.code.apis.rest.client.Tree;

import java.util.Iterator;
import java.util.Vector;

import com.google.code.apis.rest.client.CodeGeneration.CodeGenerator;
import com.google.code.apis.rest.client.GUI.GuiFactory;
import com.google.code.apis.rest.client.GUI.XmlHttpRequest;
import com.google.code.apis.rest.client.GUI.RestCompilePanel;
import com.google.code.apis.rest.client.GUI.SettingsDialog;
import com.google.code.apis.rest.client.Util.SyntaxHighlighter;
import com.google.code.apis.rest.client.Util.Uri;
import com.google.code.apis.rest.client.Wadl.Analyzer;
import com.google.code.apis.rest.client.Wadl.ApplicationNode;
import com.google.code.apis.rest.client.Wadl.NamespaceAttribute;
import com.google.code.apis.rest.client.Wadl.WadlXml;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ApplicationItem extends Composite {
  int namespaceCounter;  
  public static final String button = "button";
  
  public ApplicationItem(final ApplicationNode application) {
    // applicationChildren = {resourcesNode, grammarsNode, resourceTypeNode, methodNode, representationNode, faultNode, docNode}
    VerticalPanel applicationPanel = new VerticalPanel();
    applicationPanel.add(new HTML(SyntaxHighlighter.highlight("<" + WadlXml.applicationNode + " ")));
        
    HorizontalPanel namespacePanel = new HorizontalPanel();   
    
    VerticalPanel namespacePanelLeft = new VerticalPanel();
    namespacePanelLeft.setWidth("1em");
    namespacePanelLeft.add(new HTML("&nbsp;"));
    namespacePanel.add(namespacePanelLeft);
    
    final VerticalPanel namespacePanelRight = new VerticalPanel();    
    listExtNamespaces(application, namespacePanelRight);   
    
    namespacePanel.add(namespacePanelRight);
    applicationPanel.add(namespacePanel);  
    
    HorizontalPanel buttonPanel = new HorizontalPanel();
    buttonPanel.add(new HTML("&nbsp;&nbsp;&nbsp;"));
    Button addExtNamespaceButton = new Button(GuiFactory.strings.addExtNamespace());
    addExtNamespaceButton.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        NamespaceAttribute namespace = new NamespaceAttribute(null, GuiFactory.strings.newExtNamespace());
        application.addNamespace(namespace);
        listExtNamespaces(application, namespacePanelRight);
      }
    });
    buttonPanel.add(addExtNamespaceButton);
    applicationPanel.add(buttonPanel);

    HorizontalPanel autoDiscoverPanel = new HorizontalPanel();
    autoDiscoverPanel.add(new HTML("&nbsp;&nbsp;&nbsp;"));
    Button autoDiscoverButton = new Button(GuiFactory.strings.autoDiscover());
    autoDiscoverButton.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {        
        Vector requests = new Vector();
        for (Iterator allRequestUrisIterator = ReferenceManager.allRequestStrings.iterator(); allRequestUrisIterator.hasNext(); /* empty */) {
          String uriString = (String) allRequestUrisIterator.next();
          Uri uri = new Uri(uriString.substring(uriString.indexOf(" ") + 1));
          if (!requests.contains(uri.getRequest())) {
            requests.add(uri.getRequest());
          }
          // we have a request which appears twice, so force use of operation 
          // param value as name (to be guessed)
          else {
            SettingsDialog.useEndpointAsName = false;
            String[] parameters = uri.getQueryParameters();            
            paramSearch:
              for (int i = 0; i < parameters.length; i++) {
                for (int j = 0; j < RestCompilePanel.commonOpParamNames.length; j++) {
                  if (parameters[i].equalsIgnoreCase(RestCompilePanel.commonOpParamNames[j])) {
                    RestCompilePanel.paramToUseForName = parameters[i];                    
                    break paramSearch;
                  }
                }
              }            
            break;
          }
        }
        CodeGenerator codeGenerator = new CodeGenerator(Analyzer.application);
        String code = codeGenerator.generate(CodeGenerator.dogfood_php5);        
        new XmlHttpRequest(code, SettingsDialog.pathToDiscoverer);        
      }
    });
    autoDiscoverPanel.add(autoDiscoverButton);
    applicationPanel.add(autoDiscoverPanel);
    
    applicationPanel.add(new HTML(SyntaxHighlighter.highlight(">")));    
    
    initWidget(applicationPanel);
  }
 
  protected void listExtNamespaces(final ApplicationNode application, final VerticalPanel namespacePanelRight) {
    Vector namespaces = application.getAllNamespaces();
    namespacePanelRight.clear();
    namespacePanelRight.add(new HTML(SyntaxHighlighter.highlight(WadlXml.application_xmlns_xsi + "=\"" + WadlXml.xmlns_xsi + "\"")));
    namespacePanelRight.add(new HTML(SyntaxHighlighter.highlight(WadlXml.application_xsi_schemaLocation + "=\"" + WadlXml.xsi_schemaLocation + "\"")));
    namespacePanelRight.add(new HTML(SyntaxHighlighter.highlight(WadlXml.application_xmlns_xsd + "=\"" + WadlXml.xmlns_xsd + "\"")));    
    namespacePanelRight.add(new HTML(SyntaxHighlighter.highlight(WadlXml.application_xmlns + "=\"" + WadlXml.xmlns + "\"")));
    final Iterator namespaceIterator = namespaces.iterator();    
    namespaceCounter = 0;
    while (namespaceIterator.hasNext()) {
      final HorizontalPanel extNamespacePanel = new HorizontalPanel();
      final NamespaceAttribute namespace = (NamespaceAttribute) namespaceIterator.next();
      if (namespace.getName() == null) {
        extNamespacePanel.add(new HTML(SyntaxHighlighter.highlight(WadlXml.application_xmlns_extNs + namespaceCounter + "=\"")));
      }
      else {
        extNamespacePanel.add(new HTML(SyntaxHighlighter.highlight(namespace.getName() + "=\"")));
      }
      
      final TextBox input = new TextBox();      
      input.setText(namespace.getValue());
      input.addChangeListener(new ChangeListener() {
        public void onChange(Widget sender) {
          application.updateNamespace(namespace.getValue(), input.getText());          
        }
      });
      extNamespacePanel.add(input);     
      
      extNamespacePanel.add(new HTML(SyntaxHighlighter.highlight("\"")));
      
      Hyperlink removeExtNsLink = new Hyperlink(GuiFactory.strings.remove(), true, "");
      removeExtNsLink.addClickListener(new ClickListener() {
        public void onClick(Widget sender) {      
          application.removeNamespace(input.getText());
          listExtNamespaces(application, namespacePanelRight);                              
        }
      });      
      extNamespacePanel.add(removeExtNsLink);
      
      namespacePanelRight.add(extNamespacePanel);
      
      namespaceCounter++;      
    }        
  }  
}
