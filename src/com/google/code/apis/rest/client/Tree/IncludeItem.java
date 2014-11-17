package com.google.code.apis.rest.client.Tree;  

import java.util.Iterator;
import java.util.Vector;

import com.google.code.apis.rest.client.GUI.GuiFactory;
import com.google.code.apis.rest.client.Util.SyntaxHighlighter;
import com.google.code.apis.rest.client.Wadl.GrammarsNode;
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

public class IncludeItem extends Composite {
  public IncludeItem(final String href, final GrammarsNode grammars, final VerticalPanel includesPanel) {              
    final HorizontalPanel includePanel = new HorizontalPanel();
    
    includePanel.add(new HTML(SyntaxHighlighter.highlight("<" + WadlXml.includeNode + " " + WadlXml.include_href + "=\"")));
    
    final TextBox input = new TextBox();      
    input.setText(href);
    input.addChangeListener(new ChangeListener() {
      public void onChange(Widget sender) {
        grammars.updateInclude(href, input.getText());
      }
    });
    includePanel.add(input);
    
    includePanel.add(new HTML(SyntaxHighlighter.highlight("\" />")));
    
    Hyperlink removeIncludeLink = new Hyperlink(GuiFactory.strings.remove(), true, "");
    removeIncludeLink.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {        
        grammars.removeInclude(input.getText());       
        listIncludes(grammars, includesPanel);
      }
    });      
    includePanel.add(removeIncludeLink);
    
    initWidget(includePanel);    
  }

  public static void listIncludes(final GrammarsNode grammars, final VerticalPanel includePanel) {
    includePanel.clear();
    Vector allIncludes = grammars.getAllIncludes();
    Iterator includeIterator = allIncludes.iterator();
    VerticalPanel includeItems = new VerticalPanel();
    while (includeIterator.hasNext()) {      
      IncludeItem includeItem = new IncludeItem((String) includeIterator.next(), grammars, includePanel);
      includeItems.add(includeItem);     
    }
    includePanel.add(includeItems);
    
    // add include button        
    Button addIncludeButton = new Button(GuiFactory.strings.addInclude());
    addIncludeButton.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        grammars.addInclude(GuiFactory.strings.newInclude());
        IncludeItem.listIncludes(grammars, includePanel);
      }
    });
    includePanel.add(addIncludeButton);    

  }    
}
