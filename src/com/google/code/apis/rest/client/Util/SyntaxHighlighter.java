package com.google.code.apis.rest.client.Util;

public class SyntaxHighlighter {
	public SyntaxHighlighter() {
		return;
	}
  
  public static String toHtml(String code) {
    code = encodeEntities(code);
    
    // <openTag>
    String regExp = "(&lt;\\??\\w+)(.*?)(\\??/?&gt;)";
    code = code.replaceAll(regExp, "<span class=\"tag\">$1</span>$2<span class=\"tag\">$3</span>\n<br />");
    
    // attrib = "string"
    regExp = "([a-zA-Z0-9_:]+\\s*=)(\\s*[&quot;a-zA-Z0-9_:#\\/\\.\\-\\s\\(\\)\\+]+&quot;|[&#39;a-zA-Z0-9_:#\\/\\.\\-\\s\\(\\)\\+]+&#39;)";
    code = code.replaceAll(regExp, "<span class=\"attribute\">$1</span><span class=\"string\">$2</span>");
    
    // </closeTag>
    regExp = "(&lt;/\\w+&gt;)";
    code = code.replaceAll(regExp, "<span class=\"tag\">$1</span>\n<br />");
        
    return code;
  }
  
  public static String encodeEntities(String code) {
    code = code.replaceAll("'", "&#39;");   
    code = code.replaceAll("\"", "&quot;");   
    code = code.replaceAll("<", "&lt;");    
    code = code.replaceAll(">", "&gt;");    
    return code;
  }
  
  public static String htmlifyLineBreaksAndTabs(String code, int tabWidth) {
    code = encodeEntities(code);
    code = code.replaceAll("\n", "<br />");
    String tab = "";
    for (int i = 0; i < tabWidth; i++) tab += "&nbsp;";
    code = code.replaceAll("\t", tab);
    return code;
  }
  
  public static String highlight(String code) {    
    code = encodeEntities(code);
    // complete tag
    String regExp = "(^&lt;/?.*?&gt;$)";    
    code = code.replaceAll(regExp, "<span class=\"tag\">$1</span>");

    // opening tag    
    regExp = "(^&lt;.*?\\s)";    
    code = code.replaceAll(regExp, "<span class=\"tag\">$1</span><span class=\"attribute\">");
    
    // closing tag
    regExp = "(/?&gt;$)";    
    code = code.replaceAll(regExp, "</span><span class=\"tag\">$1</span>");    
       
    // attribute strings
    regExp = "(.*?)(&quot;(.*?&quot;)*)";
    code = code.replaceAll(regExp, "<span class=\"attribute\">$1</span><span class=\"string\">$2</span>");
    regExp = "(&#39;.*?&#39;)";
    code = code.replaceAll(regExp, "<span class=\"attribute\">$1</span><span class=\"string\">$2</span>");

    // xml comments
    regExp = "(&lt;!--?.*?--&gt;)";    
    code = code.replaceAll(regExp, "<span class=\"comment\">$1</span>");  
    
    return code;    
  }
}
