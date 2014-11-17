/**
 * 
 */
package com.google.code.apis.rest.client.Wadl;

/**
 * @author tsteiner
 *
 */
public class NamespaceAttribute {
  String name;
  String value;
  
  public NamespaceAttribute(String attributeName, String attributeValue) {
    name = attributeName;
    value = attributeValue;
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
   * @return the value
   */
  public String getValue() {
    return value;
  }

  /**
   * @param value the value to set
   */
  public void setValue(String value) {
    this.value = value;
  }  
}
