/**
 * 
 */
package com.google.code.apis.rest.client.CodeGeneration;

import com.google.code.apis.rest.client.Util.TypeEstimator;

/**
 * @author tsteiner
 *
 */
public class TypeMapper {
  protected static final String javaInt = "int";
  protected static final String javaString = "String";
  protected static final String javaFloat = "float";
  protected static final String javaBoolean = "boolean";
  
  public static String mapType(String type, String language) {
    String nativeType = "";
    if (language.equals(CodeGenerator.java)) {
      if (type.equals(TypeEstimator.typeInteger)) {
        nativeType = javaInt;
      }
      else if (type.equals(TypeEstimator.typeBoolean)) {
        nativeType = javaBoolean;
      }
      else if (type.equals(TypeEstimator.typeString)) {
        nativeType = javaString;
      }
      else if (type.equals(TypeEstimator.typeFloat)) {
        nativeType = javaFloat;
      }
      else {
        nativeType = javaString;
      }
    }
    
    return nativeType;
  }
}
