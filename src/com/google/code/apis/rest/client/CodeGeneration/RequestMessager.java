/**
 * 
 */
package com.google.code.apis.rest.client.CodeGeneration;

import java.util.Vector;

/**
 * @author tsteiner
 *
 */
public class RequestMessager {

  private String name;
  private Vector variables;
  private String httpMethodName;
  private String absoluteAdress;

  /**
   * @param name
   * @param variables
   * @param httpMethodName
   * @param absoluteAddress
   */
  public RequestMessager(String name, Vector variables, String httpMethodName, String absoluteAddress) {
    this.name = name;
    this.variables = variables;
    this.httpMethodName = httpMethodName;
    this.absoluteAdress = absoluteAddress;
  }

  /**
   * @return the absoluteAdress
   */
  public String getAbsoluteAdress() {
    return absoluteAdress;
  }

  /**
   * @param absoluteAdress the absoluteAdress to set
   */
  public void setAbsoluteAdress(String absoluteAdress) {
    this.absoluteAdress = absoluteAdress;
  }

  /**
   * @return the httpMethodName
   */
  public String getHttpMethodName() {
    return httpMethodName;
  }

  /**
   * @param httpMethodName the httpMethodName to set
   */
  public void setHttpMethodName(String httpMethodName) {
    this.httpMethodName = httpMethodName;
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
   * @return the variables
   */
  public Vector getVariables() {
    return variables;
  }

  /**
   * @param variables the variables to set
   */
  public void setVariables(Vector variables) {
    this.variables = variables;
  }
}
