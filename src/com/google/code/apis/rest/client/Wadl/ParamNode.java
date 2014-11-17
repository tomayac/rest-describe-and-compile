package com.google.code.apis.rest.client.Wadl;

import java.util.Vector;

import com.google.code.apis.rest.client.GUI.GuiFactory;
import com.google.code.apis.rest.client.Tree.ParamItem;
import com.google.code.apis.rest.client.Util.TypeEstimator;

public class ParamNode extends GenericNode {
  private String name;  
  private String type;
  private String estimateQuality; 
  private String style;
  private String id;
  private String href;
  private boolean isRequired;
  private boolean isRepeating;
  private String defaultValue;
  private String fixedValue;
  private String value;
  private String path;
  private Vector options = new Vector();
  private Vector allLinks = new Vector();
  
  public ParamNode(String paramName, String paramType, String typeEstimateQuality, String paramStyle, boolean paramIsRequired, boolean paramIsRepeating, String paramDefaultValue, String paramFixedValue, String paramPath, String paramValue, GenericNode parent, ApplicationNode application) {
    name = paramName;    
    type = paramType;
    estimateQuality = typeEstimateQuality;
    style = paramStyle;
    href = null;
    isRequired = paramIsRequired;
    isRepeating = paramIsRepeating;
    defaultValue = paramDefaultValue;
    fixedValue = paramFixedValue;   
    path = paramPath;
    value = paramValue;
    super.parent = parent;
    super.application = application;
  }

  public ParamNode(String paramName, GenericNode parent, ApplicationNode application) {
    name = paramName;    
    type = TypeEstimator.typeString;
    estimateQuality = TypeEstimator.manual;
    style = ParamItem.query;
    isRequired = false;
    isRepeating = false;
    href = null;
    defaultValue = "";
    fixedValue = "";
    path = "";
    value = GuiFactory.strings.paramValue();
    super.parent = parent;
    super.application = application;
  }
  
  public ParamNode(String paramId, boolean isReferenced, GenericNode parent, ApplicationNode application) {
    id = null;    
    href = "#" + paramId;
    type = "";
    estimateQuality = "";
    style = "";
    isRequired = false;
    isRepeating = false;
    defaultValue = "";
    fixedValue = "";
    path = "";
    value = "";
    super.parent = parent;
    super.application = application;
  }

  /**
   * This is a template parameter constructor for resources with a path like .../shop/{cartId}/...
   * @param templateParamName
   * @param typeString
   * @param template
   * @param resource
   * @param application
   */
  public ParamNode(String templateParamName, String typeString, String template, ResourceNode resource, ApplicationNode application) {
    name = templateParamName;
    type = typeString;
    style = template;
    estimateQuality = "";
    defaultValue = "";
    fixedValue = "";
    path = "";
    super.parent = resource;
    super.application = application;
  }

  public String getName() {
    return name;    
  }
  
  public String getId() {
    return id;
  }
 
  public String getPath() {
    return path;    
  }
  
  public String getValue() {
    return value;
  }
  
  public String getType() {
    return type;
  }
  
  public String getEstimateQuality() {
    return estimateQuality;
  }
  
  public void setEstimateQuality(String quality) {
    estimateQuality = quality;
  }
  
  public String getStyle() {
    return style;
  }
  
  public boolean getIsRequired() {
    return isRequired;
  }
  
  public String getDefaultValue() {
    return defaultValue;
  }
  
  public String getFixedValue() {
    return fixedValue;
  }
  
  public boolean getIsRepeating() {
    return isRepeating;
  }

  public void updateName(String oldName, String newName) {
    if (oldName.equals(getName())) {
      name = newName;
    }
  }
 
  public void updateId(String oldId, String newId) {
    if (oldId.equals(getId())) {
      id = newId;      
    }
  }
  
  public void updateType(String oldType, String newType) {
    if (oldType.equals(getType())) {
      type = newType;
      estimateQuality = TypeEstimator.manual;
    }
  }
  
  public void updatePath(String oldPath, String newPath) {
    if (oldPath.equals(getPath())) {
      path = newPath;
      estimateQuality = TypeEstimator.manual;
    }
  }
  
  public void updateDefaultValue(String oldDefaultValue, String newDefaultValue) {
    if (oldDefaultValue.equals(getDefaultValue())) {
      defaultValue = newDefaultValue;
    }
  }
  
  public void updateFixedValue(String oldFixedValue, String newFixedValue) {
    if (oldFixedValue.equals(getFixedValue())) {
      fixedValue = newFixedValue;
    }
  }
  
  public void updateStyle(String oldStyle, String newStyle) {
    if (oldStyle.equals(getStyle())) {
      style = newStyle;
    }
  }
  
  public void updateIsRequired(boolean oldIsRequired, boolean newIsRequired) {
    if (oldIsRequired == getIsRequired()) {
      isRequired = newIsRequired;
    }
  }
  
  public void updateIsRepeating(boolean oldIsRepeating, boolean newIsRepeating) {
    if (oldIsRepeating == getIsRepeating()) {
      isRepeating = newIsRepeating;
    }
  }

  public Vector getOptions() {    
    return options ;
  }

  public void setOptions(Vector optionValues) {
    options = optionValues;   
  }

  public void addOption(String option) {
    if (!options.contains(option)) {
      options.add(option);
    }
  }
  
  /**
   * @return
   */
  public Vector getAllLinks() {    
    return allLinks;
  }

  /**
   * @return
   */
  public String getHref() {
    return href;
  }

  /**
   * @param nodeValue
   */
  public void setId(String newId) {
    id = newId;    
  }

  /**
   * @param string
   */
  public void setHref(String href) {
    this.href = href;    
  }  
}
