/**
 * 
 */
package com.google.code.apis.rest.client.CodeGeneration;

/**
 * @author tsteiner
 *
 */
public class Parameter {
  private String type = "";
  private String access = "";
  private String name = "";
  private String defaultValue = "";
  private boolean isRequired = false;  
  private String language = "";
  private String sampleValue = "";
  private boolean isTemplateParam = false;
  
  protected static final String privateMarker = "_";
  protected static final String protectedMarker = "_T";
  
  protected static final String accessPublic = "public";
  protected static final String accessPrivate = "private";
  protected static final String accessProtected = "protected";  
  
  public Parameter(String varName, String varType, String varAccess, boolean required, String varDefaultValue, String language, String sampleValue) {
    type = varType;
    access = varAccess;
    defaultValue = varDefaultValue;
    name = (language.equals(CodeGenerator.php5) || language.equals(CodeGenerator.dogfood_php5))?
        followVariableNameConventions("$" + varName, access) : followVariableNameConventions(varName, access);
    isRequired = required;
    this.language = language;
    this.sampleValue = sampleValue;
  }
  
  public Parameter(String varName, String varType, String varAccess, boolean required, String language, boolean isTemplate) {
    type = varType;
    access = varAccess;    
    name = (language.equals(CodeGenerator.php5) || language.equals(CodeGenerator.dogfood_php5))?
        followVariableNameConventions("$" + varName, access) : followVariableNameConventions(varName, access);
    isRequired = required;
    this.language = language;
    this.sampleValue = sampleValue;
    this.isTemplateParam = isTemplate;
  }
    
  private String followVariableNameConventions(String varName, String varAccess) {
    
    // PHP
    //
    // 1. All private variables and functions always start with underscore "_" followed by lowercase letters like var $_myvar;
    // 2. All Protected variables and functions always start with "_T" followed by lowercase letters like var $_Tmyvar;
    // 3. All Public variables and functions do not start with underscore "_" like var $myvar;
    // 4. All variables and functions always start with lowercase letter (no uppercase) like var $_myvar; and NOT like var $_Myvar;   
    if (varName.startsWith("$")) {
      if ((varAccess.equals(accessPrivate)) && (!varName.substring(1).startsWith(privateMarker))) {
        varName = varName.replaceAll("\\$", "\\$" + privateMarker);
      }
      else if ((varAccess.equals(accessProtected)) && (!varName.substring(1).startsWith(protectedMarker))) {
        varName = varName.replaceAll("\\$", "\\$" + protectedMarker);
      }      
    }
    return varName;
  }
  
  /**
   * @return the access
   */
  public String getAccess() {
    return access;
  }  

  /**
   * @param access the access to set
   */
  public void setAccess(String access) {
    this.access = access;
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return the type
   */
  public String getType() {    
    return TypeMapper.mapType(type, getLanguage());        
  }

  /**
   * @param type the type to set
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * @return the value
   */
  public String getDefaultValue() {
    return defaultValue;
  }

  /**
   * @param value the value to set
   */
  public void setDefaultValue(String value) {
    this.defaultValue = value;
  }

  /**
   * @return the isRequired
   */
  public boolean isRequired() {
    return isRequired;
  }

  /**
   * @param isRequired the isRequired to set
   */
  public void setRequired(boolean isRequired) {
    this.isRequired = isRequired;
  }

  /**
   * @return the language
   */
  public String getLanguage() {
    return language;
  }

  /**
   * @param language the language to set
   */
  public void setLanguage(String language) {
    this.language = language;
  }

  /**
   * @return the sampleValue
   */
  public String getSampleValue() {
    return sampleValue;
  }

  /**
   * @param sampleValue the sampleValue to set
   */
  public void setSampleValue(String sampleValue) {
    this.sampleValue = sampleValue;
  }

  /**
   * @return the isTemplateParam
   */
  public boolean isTemplateParam() {
    return isTemplateParam;
  }

  /**
   * @param isTemplateParam the isTemplateParam to set
   */
  public void setTemplateParam(boolean isTemplateParam) {
    this.isTemplateParam = isTemplateParam;
  }
}
