package com.google.code.apis.rest.client.Tree;

import com.google.code.apis.rest.client.Util.SyntaxHighlighter;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;

public class GenericClosingItem extends Composite {  
  public GenericClosingItem(String tagName) {    
    HTML tag = new HTML(SyntaxHighlighter.highlight("</" + tagName + ">"));  
    initWidget(tag);
  }
}
