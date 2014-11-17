package com.google.code.apis.rest.client.Util;

import java.util.Iterator;
import java.util.Vector;

import com.google.gwt.user.client.Window;

public class Tools {
  public static void dumpVector(String title, Vector vector) {
    String info = title + "\n===============\n";
    info += "Size: " + vector.size() + "\n===============\n";
    
    Iterator vectorIterator = vector.iterator();
    
    while (vectorIterator.hasNext()) {
      info += vectorIterator.next().toString() + "\n";
    }    
    
    info += "===============\nisEmpty: " + vector.isEmpty();
    Window.alert(info);
  }
  
  /**
   * Checks whether a needle is contained in a haystack
   * @param haystack
   * @param needle
   * @return
   */    
  public static boolean contains(String[] haystack, String needle) {
    for (int i = 0; i < haystack.length; i++) {
      if (haystack[i].equals(needle)) {
        return true;
      }      
    }
    return false;
  }
}
