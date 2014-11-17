package com.google.code.apis.rest.client.Wadl;

public class DocNode {
  private String xmlLang;
  private String title;
  private String text;
  private GenericNode parent;
  
  public DocNode(String docLang, String docTitle, String docText, GenericNode parentNode) {
    xmlLang = docLang;
    title = docTitle;
    text = docText; 
    parent = parentNode;
  }
  
  public GenericNode getParent() {
    return parent;
  }
  
  public String getXmlLang() {
    return xmlLang;
  }
  
  public String getTitle() {
    return title;
  }
  
  public String getText() {
    return text;
  }
  
  public void setXmlLang(String lang) {
    xmlLang = lang;
  }
  
  public void setTitle(String titleString) {
    title = titleString;
  }
  
  public void setText(String textString) {
    text = textString;
  }
}