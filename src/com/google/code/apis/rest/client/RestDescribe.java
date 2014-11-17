package com.google.code.apis.rest.client;

import com.google.code.apis.rest.client.GUI.GuiFactory;
import com.google.gwt.core.client.EntryPoint;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 * 
 */
public class RestDescribe implements EntryPoint {
  private static RestDescribe singleton;

  /**
   * Gets the singleton RestDescribe instance. 
   */
  public static RestDescribe get() {
    return singleton;
  }

  /**
   * This is the entry point method. 
   */
  public void onModuleLoad() {
    new GuiFactory();
  }
}
