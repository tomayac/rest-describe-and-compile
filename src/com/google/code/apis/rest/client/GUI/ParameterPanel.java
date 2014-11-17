package com.google.code.apis.rest.client.GUI;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * This class creates the parameter panel
 *
 */
public class ParameterPanel  extends Composite {
  public static VerticalPanel parameterPanel = new VerticalPanel();  
  public static VerticalPanel requestContainer = new VerticalPanel();
  public static ScrollPanel requestArea = new ScrollPanel();  
  private HTML requestPanelHeader;  
  public static Button addParamButton;
    
	/**
	 * Creates the parameter panel
	 */
	public ParameterPanel() {    
    parameterPanel.setStyleName("restDescribe-navigationPanel");
    parameterPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
			
		requestPanelHeader = new HTML("<h2>" + GuiFactory.strings.requestDetails() + "</h2>");	
		parameterPanel.add(requestPanelHeader);		
   
    requestArea.setStyleName("restDescribe-requestArea");    
    requestArea.add(requestContainer);
    parameterPanel.add(requestArea);       
        
    initWidget(parameterPanel);
	}  
}
