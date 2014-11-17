/**
 * 
 */
package com.google.code.apis.rest.client.Wadl;

import com.google.code.apis.rest.client.GUI.GuiFactory;
import com.google.code.apis.rest.client.GUI.WadlPanel;
import com.google.code.apis.rest.client.GUI.WadlUploadDialog;
import com.google.code.apis.rest.client.Tree.WadlTreeRoot;
import com.google.code.apis.rest.client.Util.Tools;
import com.google.code.apis.rest.client.Util.TypeEstimator;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.NamedNodeMap;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;
import com.google.gwt.xml.client.impl.DOMParseException;

/**
 * This class analyzes a wadl xml file, trys to parse it and builds an abstract syntax tree   
 *
 */
public class WadlParser {
  private static final short ELEMENT_NODE = 1;
  //  private static final short ATTRIBUTE_NODE = 2;
  // private static final short TEXT_NODE = 3;
  //  private static final short CDATA_SECTION_NODE = 4;
  //  private static final short ENTITY_REFERENCE_NODE = 5;
  //  private static final short ENTITY_NODE = 6;
  //  private static final short PROCESSING_INSTRUCTION_NODE = 7;
  //  private static final short COMMENT_NODE = 8;
  //  private static final short DOCUMENT_NODE = 9;
  //  private static final short DOCUMENT_TYPE_NODE = 10;
  //  private static final short DOCUMENT_FRAGMENT_NODE = 11; 
  //  private static final short NOTATION_NODE = 12;
  
  private Node applicationElement;
  private ApplicationNode application;
  private DialogBox uploadDialogBox;
  /**
   */
  public WadlParser() {
    uploadDialogBox = WadlUploadDialog.dialog;
  }

  /**
   * @param wadlXmlString
   */
  public void parse(String wadlXmlString) {
    // remove unnecessary whitespaces
    wadlXmlString = wadlXmlString.replaceAll(">\\s*<", "><");
    //  remove <!-- comment nodes -->
    wadlXmlString = wadlXmlString.replaceAll("<!--.*?-->", "");    
    try {
      // reset the application
      GuiFactory.resetApplication();
      
      Document wadl = XMLParser.parse(wadlXmlString);
      
      if (startParsing(wadl)) {
        uploadDialogBox.setVisible(true);
        uploadDialogBox.hide();
        // if the parsing succeeded render a tree from the parsed wadl    
        WadlTreeRoot wadlTreeRoot = new WadlTreeRoot();
        Tree wadlTree = wadlTreeRoot.buildTree(application);       
        WadlPanel.wadlArea.setWidget(wadlTree);
        GuiFactory.toggleButtonsEnabled(true);
        WadlPanel.fullscreenButton.click();
      }
    } catch (DOMParseException e) {
      alertInvalidWadlAndRetry(GuiFactory.strings.invalidWadl());
    }    
  }
  
  /**
   * @param wadl
   */
  private boolean startParsing(Document wadl) {
    // the first node must be an application node with namespace attributes
    NodeList wadlChildren = wadl.getChildNodes();
    for (int i = 0; i < wadlChildren.getLength(); i++) {
      Node wadlChild = wadlChildren.item(i);
      if (wadlChild.getNodeType() == ELEMENT_NODE) {
        applicationElement = wadlChild;
        break;
      }
    }
    if (applicationElement.getNodeName().equals(WadlXml.applicationNode)) {        
      // allowed children are only doc, grammars, resources, resource_type, method, representation, param, fault      
      if (containsOnlyAllowedChildren(applicationElement, WadlXml.applicationChildren)) {
        if ((applicationElement.getAttributes().getNamedItem(WadlXml.application_xmlns) != null) &&
            (applicationElement.getAttributes().getNamedItem(WadlXml.application_xmlns_xsd) != null) &&
            (applicationElement.getAttributes().getNamedItem(WadlXml.application_xmlns_xsi) != null) &&
            (applicationElement.getAttributes().getNamedItem(WadlXml.application_xsi_schemaLocation) != null)) {
          Analyzer analyzer = new Analyzer("");        
          application = new ApplicationNode(analyzer);        
          analyzer.setApplication(application);
          
          NamedNodeMap attributes = applicationElement.getAttributes();
          for (int i = 0; i < attributes.getLength(); i++) {
            Node attribute = attributes.item(i);
            if ((attribute.getNodeName().equals(WadlXml.application_xmlns)) ||
                (attribute.getNodeName().equals(WadlXml.application_xmlns_xsd)) ||
                (attribute.getNodeName().equals(WadlXml.application_xmlns_xsi)) ||
                (attribute.getNodeName().equals(WadlXml.application_xsi_schemaLocation))) {
              continue;
            }
            NamespaceAttribute namespace = new NamespaceAttribute(attribute.getNodeName(), attribute.getNodeValue());
            application.addNamespace(namespace);            
          }
          
          // applicationChildren = {resourcesNode, grammarsNode, resourceTypeNode, methodNode, representationNode, faultNode, docNode}
          NodeList applicationChildren = applicationElement.getChildNodes();
          for (int i = 0; i < applicationChildren.getLength(); i++) {      
            if (applicationChildren.item(i).getNodeName().equals(WadlXml.resourcesNode)) {
              if (!parseResourcesNode(applicationChildren.item(i), application)) {
                return false;        
              }
            }
            else if (applicationChildren.item(i).getNodeName().equals(WadlXml.grammarsNode)) {
              if (!parseGrammarsNode(applicationChildren.item(i), application)) {
                return false;
              }        
            }
            else if (applicationChildren.item(i).getNodeName().equals(WadlXml.resourceTypeNode)) {
              if (!parseResourceTypeNode(applicationChildren.item(i), application)) {
                return false;        
              }
            }
            else if (applicationChildren.item(i).getNodeName().equals(WadlXml.methodNode)) {
              if (!parseMethodNode(applicationChildren.item(i), application, false)) {
                return false;        
              }
            }
            else if (applicationChildren.item(i).getNodeName().equals(WadlXml.representationNode)) {
              if (!parseRepresentationNode(applicationChildren.item(i), application, false)) {
                return false;        
              }
            }
            else if (applicationChildren.item(i).getNodeName().equals(WadlXml.faultNode)) {
              if (!parseFaultNode(applicationChildren.item(i), application, false)) {
                return false;        
              }
            }
            else if (applicationChildren.item(i).getNodeName().equals(WadlXml.paramNode)) {
              if (!parseParamNode(applicationChildren.item(i), application, false)) {
                return false;        
              }
            }
            else if (applicationChildren.item(i).getNodeName().equals(WadlXml.docNode)) {
              if (!parseDocNode(applicationChildren.item(i), application)) {
                return false;        
              }
            }
          }
        }
        else {
          alertInvalidWadlAndRetry(GuiFactory.notifications.mustContainAtLeast(WadlXml.applicationNode, allowedAttributes(WadlXml.applicationAttributes)));
          return false;
        }
      }        
      else {
        alertInvalidWadlAndRetry(GuiFactory.notifications.invalidChildren(WadlXml.applicationNode, allowedChildren(WadlXml.applicationChildren)));
        return false;
      }
    }
    else {      
      alertInvalidWadlAndRetry(GuiFactory.strings.firstChildMustBeAnApplicationElement());
      return false;
    }        
    return true;
  }

  /**
   * @param docElement
   * @param parentNode
   * @return
   */
  private boolean parseDocNode(Node docElement, GenericNode parentNode) {    
    if (containsOnlyAllowedAttributes(docElement, WadlXml.docAttributes)) {      
      Node langNode = docElement.getAttributes().getNamedItem(WadlXml.doc_xml_lang);
      String lang = "";
      if (langNode != null) {
        lang = encodeDangerousContents(langNode.getNodeValue());
      }
      else {
        lang = "en";
      }
      
      Node titleNode = docElement.getAttributes().getNamedItem(WadlXml.doc_title);
      String title = "";
      if (titleNode != null) {        
        title = encodeDangerousContents(titleNode.getNodeValue());
      }
            
      String text = "";      
      if (docElement.getFirstChild() != null) {        
        text = docElement.getChildNodes().toString();
        text = decodeDangerousContents(text);
      }
      
      DocNode doc = new DocNode(lang, title, text, parentNode);
      parentNode.addDoc(doc);
    }
    else {
      alertInvalidWadlAndRetry(GuiFactory.notifications.invalidAttributes(WadlXml.docNode, allowedAttributes(WadlXml.docAttributes)));      
      return false;
    }
    return true;
  }

  /**
   * @param title
   * @return
   */
  private String encodeDangerousContents(String string) {
    string = string.replaceAll("&", "&amp;");
    string = string.replaceAll("\"", "&quot;");
    string = string.replaceAll("'", "&apos;");
    string = string.replaceAll("<", "&lt;");
    string = string.replaceAll(">", "&gt;");    
    return string;
  }

  /**
   * @param title
   * @return
   */
  private String decodeDangerousContents(String string) {
    string = string.replaceAll("&amp;", "&");
    string = string.replaceAll("&quot;", "\"");
    string = string.replaceAll("&apos;", "'");
    string = string.replaceAll("&lt;", "<");
    string = string.replaceAll("&gt;", ">");    
    return string;
  }
  /**
   * @param resourceTypeElement
   * @param application
   * @return
   */
  private boolean parseResourceTypeNode(Node resourceTypeElement, ApplicationNode application) {
    if (containsOnlyAllowedChildren(resourceTypeElement, WadlXml.resource_typeChildren)) { 
      if  (containsOnlyAllowedAttributes(resourceTypeElement, WadlXml.resource_typeAttributes)) {
        Node idNode = resourceTypeElement.getAttributes().getNamedItem(WadlXml.resource_type_id);
        String id = "";
        if (idNode != null) {
          id = idNode.getNodeValue();
        }
        ResourceTypeNode resourceType = new ResourceTypeNode(id, application);
        application.addResourceType(resourceType);        
        
        // resource_typeChildren = {paramNode, methodNode, docNode}
        NodeList childNodes = resourceTypeElement.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
          if (childNodes.item(i).getNodeName().equals(WadlXml.methodNode)) {
            if (!parseMethodNode(childNodes.item(i), resourceType, false)) {
              return false;
            }
          }
          else if (childNodes.item(i).getNodeName().equals(WadlXml.paramNode)) {
            if (!parseParamNode(childNodes.item(i), resourceType, false)) {
              return false;
            }
          }
          else if (childNodes.item(i).getNodeName().equals(WadlXml.docNode)) {
            if (!parseDocNode(childNodes.item(i), resourceType)) {
              return false;
            }
          }
        }        
      }
      else {
        alertInvalidWadlAndRetry(GuiFactory.notifications.invalidAttributes(WadlXml.resourceTypeNode, allowedAttributes(WadlXml.resource_typeAttributes)));
        return false;
      }
    }
    else {
      alertInvalidWadlAndRetry(GuiFactory.notifications.invalidChildren(WadlXml.resourceTypeNode, allowedChildren(WadlXml.resource_typeChildren)));
      return false;
    }
    return true;
  }

  /**
   * @param resourcesElement 
   * @param application 
   * 
   */
  private boolean parseResourcesNode(Node resourcesElement, ApplicationNode application) {
    if (containsOnlyAllowedChildren(resourcesElement, WadlXml.resourcesChildren)) { 
      if  (containsOnlyAllowedAttributes(resourcesElement, WadlXml.resourcesAttributes)) {
        Node baseNode = resourcesElement.getAttributes().getNamedItem(WadlXml.resources_base);
        String base = "";
        if (baseNode != null) {
          base = baseNode.getNodeValue();
        }
        ResourcesNode resources = new ResourcesNode(base, application);
        application.addResources(resources);
        
        // resourcesChildren = {resourceNode, docNode}
        NodeList childNodes = resourcesElement.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
          if (childNodes.item(i).getNodeName().equals(WadlXml.resourceNode)) {
            if (!parseResourceNode(childNodes.item(i), resources, false)) {
              return false;
            }
          }
          else if (childNodes.item(i).getNodeName().equals(WadlXml.docNode)) {
            if (!parseDocNode(childNodes.item(i), resources)) {
              return false;
            }
          }
        }
      }
      else {
        alertInvalidWadlAndRetry(GuiFactory.notifications.invalidAttributes(WadlXml.resourcesNode, allowedAttributes(WadlXml.resourcesAttributes)));
        return false;
      }
    }
    else {
      alertInvalidWadlAndRetry(GuiFactory.notifications.invalidChildren(WadlXml.resourcesNode, allowedChildren(WadlXml.resourcesChildren)));
      return false;
    }
    return true;
  }

  /**
   * @param childNodes
   * @param parentNode
   * @param isReferenced
   */
  private boolean parseResourceNode(Node resourceElement, Object parentNode, boolean isReferenced) {        
    if (containsOnlyAllowedChildren(resourceElement, WadlXml.resourceChildren)) {
      if (containsOnlyAllowedAttributes(resourceElement, WadlXml.resourceAttributes)) {        
        Node hrefNode = resourceElement.getAttributes().getNamedItem(WadlXml.resource_href);
        if (hrefNode != null) {          
          getElementByIdThenParse(applicationElement, hrefNode.getNodeValue(), parentNode);
        }
        else {        
          Node pathAttribute = resourceElement.getAttributes().getNamedItem(WadlXml.resource_path);
          if (pathAttribute != null) {
            ResourceNode resource = null;
            if (!isReferenced) {  
              resource = new ResourceNode(pathAttribute.getNodeValue(), (GenericNode) parentNode, ((GenericNode) parentNode).getApplication());                            
              
              // resourceChildren = {paramNode, methodNode, resourceNode, docNode}
              NodeList childNodes = resourceElement.getChildNodes();
              for (int i = 0; i < childNodes.getLength(); i++) {            
                if (childNodes.item(i).getNodeName().equals(WadlXml.paramNode)) {
                  if (!parseParamNode(childNodes.item(i), resource, false)) {
                    return false;
                  }
                }
                else if (childNodes.item(i).getNodeName().equals(WadlXml.methodNode)) {
                  if (!parseMethodNode(childNodes.item(i), resource, false)) {
                    return false;
                  }
                }
                else if (childNodes.item(i).getNodeName().equals(WadlXml.resourceNode)) {                  
                  if (!parseResourceNode(childNodes.item(i), resource, false)) {
                    return false;
                  }
                }
                else if (childNodes.item(i).getNodeName().equals(WadlXml.docNode)) {
                  if (!parseDocNode(childNodes.item(i), resource)) {
                    return false;
                  }                
                }
              }
              Node idNode = resourceElement.getAttributes().getNamedItem(WadlXml.resource_id);
              if (idNode != null) {
                resource.setId(idNode.getNodeValue());
              }
            }
            else {
              Node idNode = resourceElement.getAttributes().getNamedItem(WadlXml.resource_id);
              resource = new ResourceNode(idNode.getNodeValue(), true, (GenericNode) parentNode, ((GenericNode) parentNode).getApplication());
            }
            // attach to parent
            if (parentNode instanceof ResourcesNode) {
              ((ResourcesNode) parentNode).addResource(resource);
            }
            else if (parentNode instanceof ResourceNode) {
              ((ResourceNode) parentNode).addResource(resource);
            }
          }
        }
      }
      else {
        alertInvalidWadlAndRetry(GuiFactory.notifications.invalidAttributes(WadlXml.resourceNode, allowedAttributes(WadlXml.resourceAttributes)));
        return false;
      }
    }
    else {          
      alertInvalidWadlAndRetry(GuiFactory.notifications.invalidChildren(WadlXml.resourceNode, allowedChildren(WadlXml.resourceChildren)));
      return false;
    }    
    return true;
  }

  /**
   * Runs recursively through the node hierarchy starting from the entry node and tries to find
   * the node with the given ID. When the node is found it gets parsed  
   * @param entryNode
   * @param id 
   * @param parentNode    
   */
  private void getElementByIdThenParse(Node entryNode, String id, Object parentNode) {    
    if (id.startsWith("#")) {
      id = id.substring(1);
    }    
    if ((entryNode.getNodeType() == ELEMENT_NODE) && (entryNode.hasAttributes())) {      
      Node idNode = entryNode.getAttributes().getNamedItem("id");
      if ((idNode != null) && (idNode.getNodeValue().equals(id))) {          
        parseFoundNode(entryNode, parentNode);
        return;
      }      
    }
    NodeList childNodes = entryNode.getChildNodes();   
    for (int i = 0; i < childNodes.getLength(); i++) {
      getElementByIdThenParse(childNodes.item(i), id, parentNode);      
    }    
  }

  /**
   * @param foundNode
   * @param parentNode
   * @param referenceNode 
   */
  private void parseFoundNode(Node foundNode, Object parentNode) {
    // either parse a method node...          
    if (foundNode.getNodeName().equals(WadlXml.methodNode)) {      
      parseMethodNode(foundNode, parentNode, true);      
    }
    // ...or parse a fault node...
    else if (foundNode.getNodeName().equals(WadlXml.faultNode)) {      
      parseFaultNode(foundNode, parentNode, true);      
    }
    
    // ...or parse a fault node...
    else if (foundNode.getNodeName().equals(WadlXml.resourceNode)) {      
      parseResourceNode(foundNode, parentNode, true);      
    }
    
    // ...or parse a representation node...
    else if (foundNode.getNodeName().equals(WadlXml.representationNode)) {      
      parseRepresentationNode(foundNode, parentNode, true);      
    }
    // ...or parse a param node...
    else if (foundNode.getNodeName().equals(WadlXml.paramNode)) {      
      parseParamNode(foundNode, parentNode, true);      
    }
  }

  /**
   * @param methodElement
   * @param parentNode
   * @param isReferenced
   */
  private boolean parseMethodNode(Node methodElement, Object parentNode, boolean isReferenced) {
    if (containsOnlyAllowedChildren(methodElement, WadlXml.methodChildren)) {
      if (containsOnlyAllowedAttributes(methodElement, WadlXml.methodAttributes)) {
        Node hrefNode = methodElement.getAttributes().getNamedItem(WadlXml.method_href);        
        if (hrefNode != null) {
          getElementByIdThenParse(applicationElement, hrefNode.getNodeValue(), parentNode);
        }
        else {
          Node nameNode = methodElement.getAttributes().getNamedItem(WadlXml.method_name);
          if (nameNode != null) {            
            // either create a full node, or just an href node
            MethodNode method;
            if (!isReferenced) {
              method = new MethodNode(nameNode.getNodeValue(), (GenericNode) parentNode, ((GenericNode) parentNode).getApplication());
              Node idNode = methodElement.getAttributes().getNamedItem(WadlXml.method_id);
              if (idNode != null) {
                method.setId(idNode.getNodeValue());
              }
                            
              // methodChildren = {requestNode, responseNode, docNode}
              NodeList childNodes = methodElement.getChildNodes();
              for (int i = 0; i < childNodes.getLength(); i++) {           
                if (childNodes.item(i).getNodeName().equals(WadlXml.requestNode)) {        
                  if (!parseRequestNode(childNodes.item(i), method)) {
                    return false;
                  }        
                }
                else if (childNodes.item(i).getNodeName().equals(WadlXml.responseNode)) {        
                  if (!parseResponseNode(childNodes.item(i), method)) {
                    return false;
                  }        
                }
                else if (childNodes.item(i).getNodeName().equals(WadlXml.docNode)) {        
                  if (!parseDocNode(childNodes.item(i), method)) {
                    return false;
                  }        
                }
              }
            }
            else {
              Node idNode = methodElement.getAttributes().getNamedItem(WadlXml.method_id);
              method = new MethodNode(idNode.getNodeValue(), true, (GenericNode) parentNode, ((GenericNode) parentNode).getApplication());              
            }   
            // add to the correct parent node
            if (parentNode instanceof ResourceNode) {
              ((ResourceNode) parentNode).directlyAddMethod(method);
            }
            else if (parentNode instanceof ApplicationNode) {
              ((ApplicationNode) parentNode).directlyAddMethod(method);
            }
            else if (parentNode instanceof ResourceTypeNode) {
              ((ResourceTypeNode) parentNode).directlyAddMethod(method);
            }
          }
        }
      }
      else {
        alertInvalidWadlAndRetry(GuiFactory.notifications.invalidAttributes(WadlXml.methodNode, allowedAttributes(WadlXml.methodAttributes)));
        return false;
      }
    }
    else {
      alertInvalidWadlAndRetry(GuiFactory.notifications.invalidChildren(WadlXml.methodNode, allowedChildren(WadlXml.methodChildren)));
      return false;
    }
    return true;
  }
  
  /**
   * @param requestElement
   * @param method
   */
  private boolean parseRequestNode(Node requestElement, MethodNode method) {    
    if (containsOnlyAllowedChildren(requestElement, WadlXml.requestChildren)) {      
      // requestChildren = {representationNode, paramNode, docNode};
      NodeList childNodes = requestElement.getChildNodes();
      RequestNode request = new RequestNode(method, method.getApplication());
      method.addRequest(request);
      for (int i = 0; i < childNodes.getLength(); i++) {
        if (childNodes.item(i).getNodeName().equals(WadlXml.representationNode)) {
          if (!parseRepresentationNode(childNodes.item(i), request, false)) {
            return false;
          }
        }
        else if (childNodes.item(i).getNodeName().equals(WadlXml.paramNode)) {
          if (!parseParamNode(childNodes.item(i), request, false)) {
            return false;
          }
        }       
        else if (childNodes.item(i).getNodeName().equals(WadlXml.docNode)) {
          if (!parseDocNode(childNodes.item(i), request)) {
            return false;
          }
        }
      }
    }
    else {
      alertInvalidWadlAndRetry(GuiFactory.notifications.invalidChildren(WadlXml.requestNode, allowedChildren(WadlXml.requestChildren)));
      return false;
    }    
    return true;
  }

  /**
   * @param responseElement
   * @param method
   */
  private boolean parseResponseNode(Node responseElement, MethodNode method) {            
    if (containsOnlyAllowedChildren(responseElement, WadlXml.responseChildren)) {     
      // responseChildren = {representationNode, faultNode, paramNode, docNode};
      NodeList childNodes = responseElement.getChildNodes();
      ResponseNode response = new ResponseNode(method, method.getApplication());
      method.addResponse(response);
      for (int i = 0; i < childNodes.getLength(); i++) {
        if (childNodes.item(i).getNodeName().equals(WadlXml.representationNode)) {          
          if (!parseRepresentationNode(childNodes.item(i), response, false)) {
            return false;
          }
        }
        else if (childNodes.item(i).getNodeName().equals(WadlXml.faultNode)) {        
          if (!parseFaultNode(childNodes.item(i), response, false)) {
            return false;
          }
        }
        else if (childNodes.item(i).getNodeName().equals(WadlXml.paramNode)) {
          if (!parseParamNode(childNodes.item(i), response, false)) {
            return false;
          }
        }        
        else if (childNodes.item(i).getNodeName().equals(WadlXml.docNode)) {
          if (!parseDocNode(childNodes.item(i), response)) {
            return false;
          }
        }
      }
    }
    else {
      alertInvalidWadlAndRetry(GuiFactory.notifications.invalidChildren(WadlXml.responseNode, allowedChildren(WadlXml.responseChildren)));
      return false;
    }
    return true;
  }

  /**
   * @param faultElement
   * @param parentNode
   * @param isReferenced 
   */
  private boolean parseFaultNode(Node faultElement, Object parentNode, boolean isReferenced) {    
    if (containsOnlyAllowedChildren(faultElement, WadlXml.faultChildren)) {
      if (containsOnlyAllowedAttributes(faultElement, WadlXml.faultAttributes)) {
        Node hrefNode = faultElement.getAttributes().getNamedItem(WadlXml.fault_href);      
        if (hrefNode != null) {        
          getElementByIdThenParse(applicationElement, hrefNode.getNodeValue(), parentNode);        
        }
        else {
          String mediaType = "";
          String element = "";
          String profile = "";
          String status = "";
          
          Node mediaTypeNode = faultElement.getAttributes().getNamedItem(WadlXml.fault_mediaType);
          if (mediaTypeNode != null) {
            mediaType = mediaTypeNode.getNodeValue();
          }
          
          Node elementNode = faultElement.getAttributes().getNamedItem(WadlXml.fault_element);
          if (elementNode != null) {
            element = elementNode.getNodeValue();
          }
          
          Node profileNode = faultElement.getAttributes().getNamedItem(WadlXml.fault_profile);
          if (profileNode != null) {
            profile = profileNode.getNodeValue();
          }
          
          Node statusNode = faultElement.getAttributes().getNamedItem(WadlXml.fault_status);
          if (statusNode != null) {
            status = statusNode.getNodeValue();
          }        
          
          FaultNode fault = null;
          if (!isReferenced) {
            if (parentNode instanceof ResponseNode) {          
              fault = new FaultNode(mediaType, element, profile, status, (ResponseNode) parentNode, ((ResponseNode) parentNode).getApplication());
              ((ResponseNode) parentNode).addFault(fault);
            }
            else if (parentNode instanceof ApplicationNode) {          
              fault = new FaultNode(mediaType, element, profile, status, (ApplicationNode) parentNode, (ApplicationNode) parentNode);
              ((ApplicationNode) parentNode).addFault(fault);
            }
            Node idNode = faultElement.getAttributes().getNamedItem(WadlXml.fault_id);
            if (idNode != null) {
              fault.setId(idNode.getNodeValue());
            }
            
            // faultChildren = {paramNode, docNode}
            NodeList childNodes = faultElement.getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) {
              if (childNodes.item(i).getNodeName().equals(WadlXml.paramNode)) {
                if (!parseParamNode(childNodes.item(i), fault, false));
              }
              else if (childNodes.item(i).getNodeName().equals(WadlXml.docNode)) {
                if (!parseDocNode(childNodes.item(i), fault));
              }
            }
          }
          else {
            Node idNode = faultElement.getAttributes().getNamedItem(WadlXml.fault_id);            
            if (parentNode instanceof ResponseNode) {          
              fault = new FaultNode(idNode.getNodeValue(), true, (ResponseNode) parentNode, ((ResponseNode) parentNode).getApplication());
              ((ResponseNode) parentNode).addFault(fault);
            }
            else if (parentNode instanceof ApplicationNode) {          
              fault = new FaultNode(idNode.getNodeValue(), true, (ApplicationNode) parentNode, (ApplicationNode) parentNode);
              ((ApplicationNode) parentNode).addFault(fault);
            }
          }          
        }      
      }        
      else {
        alertInvalidWadlAndRetry(GuiFactory.notifications.invalidAttributes(WadlXml.faultNode, allowedAttributes(WadlXml.faultAttributes)));
        return false;
      }
    }
    else {
      alertInvalidWadlAndRetry(GuiFactory.notifications.invalidChildren(WadlXml.faultNode, allowedChildren(WadlXml.faultChildren)));
      return false;
    }      
    return true;
  }

  /**
   * @param representationElement
   * @param parentNode
   * @param isReferenced 
   */
  private boolean parseRepresentationNode(Node representationElement, Object parentNode, boolean isReferenced) {
    if (containsOnlyAllowedChildren(representationElement, WadlXml.representationChildren)) {
      if (containsOnlyAllowedAttributes(representationElement, WadlXml.representationAttributes)) {
        Node hrefNode = representationElement.getAttributes().getNamedItem(WadlXml.representation_href);
        if (hrefNode != null) {
          getElementByIdThenParse(applicationElement, hrefNode.getNodeValue(), parentNode);
        }
        else {
          String mediaType = "";
          String element = "";
          String profile = "";
          String status = "";
          
          Node mediaTypeNode = representationElement.getAttributes().getNamedItem(WadlXml.representation_mediaType);
          if (mediaTypeNode != null) {
            mediaType = mediaTypeNode.getNodeValue();
          }
          
          Node elementNode = representationElement.getAttributes().getNamedItem(WadlXml.representation_element);
          if (elementNode != null) {
            element = elementNode.getNodeValue();
          }
          
          Node profileNode = representationElement.getAttributes().getNamedItem(WadlXml.representation_profile);
          if (profileNode != null) {
            profile = profileNode.getNodeValue();
          }
          
          Node statusNode = representationElement.getAttributes().getNamedItem(WadlXml.representation_status);
          if (statusNode != null) {
            status = statusNode.getNodeValue();
          }
          
          RepresentationNode representation = null;
          if (!isReferenced) {           
            if (parentNode instanceof ResponseNode) {              
              representation = new RepresentationNode(mediaType, element, profile, status, (ResponseNode) parentNode, ((ResponseNode) parentNode).getApplication());
              ((ResponseNode) parentNode).addRepresentation(representation);
            }
            else if (parentNode instanceof RequestNode) {              
              representation = new RepresentationNode(mediaType, element, profile, status, (RequestNode) parentNode, ((RequestNode) parentNode).getApplication());
              ((RequestNode) parentNode).addRepresentation(representation);
            }
            else if (parentNode instanceof ApplicationNode) {              
              representation = new RepresentationNode(mediaType, element, profile, status, (ApplicationNode) parentNode, (ApplicationNode) parentNode);
              ((ApplicationNode) parentNode).addRepresentation(representation);
            }
            
            Node idNode = representationElement.getAttributes().getNamedItem(WadlXml.fault_id);
            if (idNode != null) {
              representation.setId(idNode.getNodeValue());
            }
            
            // representationChildren = {paramNode, docNode}
            NodeList childNodes = representationElement.getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) {
              if (childNodes.item(i).getNodeName().equals(WadlXml.paramNode)) {
                if (!parseParamNode(childNodes.item(i), representation, false));
              }          
              else if (childNodes.item(i).getNodeName().equals(WadlXml.docNode)) {
                if (!parseDocNode(childNodes.item(i), representation));
              }
            }
          }
          else {            
            Node idNode = representationElement.getAttributes().getNamedItem(WadlXml.representation_id);            
            if (parentNode instanceof ResponseNode) {
              representation = new RepresentationNode(idNode.getNodeValue(), true, (ResponseNode) parentNode, ((ResponseNode) parentNode).getApplication());
              ((ResponseNode) parentNode).addRepresentation(representation);
            }
            else if (parentNode instanceof RequestNode) {              
              representation = new RepresentationNode(idNode.getNodeValue(), true, (RequestNode) parentNode, ((RequestNode) parentNode).getApplication());
              ((RequestNode) parentNode).addRepresentation(representation);
            }
            else if (parentNode instanceof ApplicationNode) {              
              representation = new RepresentationNode(idNode.getNodeValue(), true, (ApplicationNode) parentNode, (ApplicationNode) parentNode);
              ((ApplicationNode) parentNode).addRepresentation(representation);
            }
          }         
        }      
      }
      else {
        alertInvalidWadlAndRetry(GuiFactory.notifications.invalidAttributes(WadlXml.representationNode, allowedAttributes(WadlXml.representationAttributes)));
        return false;
      }
    }
    else {
      alertInvalidWadlAndRetry(GuiFactory.notifications.invalidChildren(WadlXml.representationNode, allowedChildren(WadlXml.representationChildren)));
      return false;
    }  
    return true;
  }

  /**
   * @param paramElement
   * @param parentNode
   * @param isReferenced  
   */
  private boolean parseParamNode(Node paramElement, Object parentNode, boolean isReferenced) {
    if (containsOnlyAllowedChildren(paramElement, WadlXml.paramChildren)) {
      if (containsOnlyAllowedAttributes(paramElement, WadlXml.paramAttributes)) {
        Node hrefNode = paramElement.getAttributes().getNamedItem(WadlXml.param_href);
        if (hrefNode != null) {
          getElementByIdThenParse(applicationElement, hrefNode.getNodeValue(), parentNode);          
        }
        else {        
          String name = "";
          String style = "";
          String type = "";
          String defaultValue = "";
          String path = "";
          boolean isRequired = false;
          boolean isRepeating = false;
          String fixed = "";
          
          Node nameNode = paramElement.getAttributes().getNamedItem(WadlXml.param_name);
          if (nameNode != null) {
            name = nameNode.getNodeValue();
          }
          
          Node styleNode = paramElement.getAttributes().getNamedItem(WadlXml.param_style);
          if (styleNode != null) {
            style = styleNode.getNodeValue();
          }
          
          Node pathNode = paramElement.getAttributes().getNamedItem(WadlXml.param_path);
          if (pathNode != null) {
            path = pathNode.getNodeValue();
          }
          
          Node typeNode = paramElement.getAttributes().getNamedItem(WadlXml.param_type);
          if (typeNode != null) {
            type = typeNode.getNodeValue();
          }
          // if no type is given use string as the default value
          if (type.equals("")) {
            type = TypeEstimator.typeString;
          }
          
          Node defaultValueNode = paramElement.getAttributes().getNamedItem(WadlXml.param_default);
          if (defaultValueNode != null) {
            defaultValue = defaultValueNode.getNodeValue();
          }
                    
          Node fixedNode = paramElement.getAttributes().getNamedItem(WadlXml.param_fixed);
          if (fixedNode != null) {
            fixed = fixedNode.getNodeValue();
          }
          
          Node isRequiredNode = paramElement.getAttributes().getNamedItem(WadlXml.param_required);
          if (isRequiredNode != null) {
            if (isRequiredNode.getNodeValue().equals("true")) {
              isRequired = true;
            }
          }
          
          Node isRepeatingNode = paramElement.getAttributes().getNamedItem(WadlXml.param_repeating);
          if (isRepeatingNode != null) {
            if (isRepeatingNode.getNodeValue().equals("true")) {
              isRepeating = true;
            }
          }
          
          ParamNode param = null;
          if (!isReferenced) {
            if (parentNode instanceof ResourceNode) {
              param = new ParamNode(name, type, TypeEstimator.manual, style, isRequired, isRepeating, defaultValue, fixed, path, "", (ResourceNode) parentNode, ((ResourceNode) parentNode).getApplication());          
              ((ResourceNode) parentNode).addParam(param);
            }
            else if (parentNode instanceof RequestNode) {
              param = new ParamNode(name, type, TypeEstimator.manual, style, isRequired, isRepeating, defaultValue, fixed, path, "", (RequestNode) parentNode, ((RequestNode) parentNode).getApplication());
              ((RequestNode) parentNode).addParam(param);
            }
            else if (parentNode instanceof ResponseNode) {
              param = new ParamNode(name, type, TypeEstimator.manual, style, isRequired, isRepeating, defaultValue, fixed, path, "", (ResponseNode) parentNode, ((ResponseNode) parentNode).getApplication());
              ((ResponseNode) parentNode).addParam(param); 
            }
            else if (parentNode instanceof RepresentationNode) {
              param = new ParamNode(name, type, TypeEstimator.manual, style, isRequired, isRepeating, defaultValue, fixed, path, "", (RepresentationNode) parentNode, ((RepresentationNode) parentNode).getApplication());
              ((RepresentationNode) parentNode).addParam(param);
            }
            else if (parentNode instanceof FaultNode) {
              param = new ParamNode(name, type, TypeEstimator.manual, style, isRequired, isRepeating, defaultValue, fixed, path, "", (FaultNode) parentNode, ((FaultNode) parentNode).getApplication());
              ((FaultNode) parentNode).addParam(param);
            }
            else if (parentNode instanceof ApplicationNode) {
              param = new ParamNode(name, type, TypeEstimator.manual, style, isRequired, isRepeating, defaultValue, fixed, path, "", (ApplicationNode) parentNode, ((ApplicationNode) parentNode));
              ((ApplicationNode) parentNode).addParam(param);
            }
            
            Node idNode = paramElement.getAttributes().getNamedItem(WadlXml.param_id);
            if (idNode != null) {
              param.setId(idNode.getNodeValue());
            }
            
            // paramChildren = {optionNode, linkNode, docNode}
            NodeList childNodes = paramElement.getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) {
              if (childNodes.item(i).getNodeName().equals(WadlXml.optionNode)) {
                if (!parseOptionNode(childNodes.item(i), param));
              }
              else if (childNodes.item(i).getNodeName().equals(WadlXml.linkNode)) {
                if (!parseLinkNode(childNodes.item(i), param));
              }
              else if (childNodes.item(i).getNodeName().equals(WadlXml.docNode)) {
                if (!parseDocNode(childNodes.item(i), param));
              }
            }
          }
          else {
            Node idNode = paramElement.getAttributes().getNamedItem(WadlXml.param_id);
            if (parentNode instanceof ResourceNode) {
              param = new ParamNode(idNode.getNodeValue(), true, (ResourceNode) parentNode, ((ResourceNode) parentNode).getApplication());          
              ((ResourceNode) parentNode).addParam(param);
            }
            else if (parentNode instanceof RequestNode) {
              param = new ParamNode(idNode.getNodeValue(), true, (RequestNode) parentNode, ((RequestNode) parentNode).getApplication());
              ((RequestNode) parentNode).addParam(param);
            }
            else if (parentNode instanceof ResponseNode) {
              param = new ParamNode(idNode.getNodeValue(), true, (ResponseNode) parentNode, ((ResponseNode) parentNode).getApplication());
              ((ResponseNode) parentNode).addParam(param); 
            }
            else if (parentNode instanceof RepresentationNode) {
              param = new ParamNode(idNode.getNodeValue(), true, (RepresentationNode) parentNode, ((RepresentationNode) parentNode).getApplication());
              ((RepresentationNode) parentNode).addParam(param);
            }
            else if (parentNode instanceof FaultNode) {
              param = new ParamNode(idNode.getNodeValue(), true, (FaultNode) parentNode, ((FaultNode) parentNode).getApplication());
              ((FaultNode) parentNode).addParam(param);
            }
            else if (parentNode instanceof ApplicationNode) {
              param = new ParamNode(idNode.getNodeValue(), true, (ApplicationNode) parentNode, (ApplicationNode) parentNode);
              ((ApplicationNode) parentNode).addParam(param);
            }
          }
        }  
      }
      else {
        alertInvalidWadlAndRetry(GuiFactory.notifications.invalidAttributes(WadlXml.paramNode, allowedAttributes(WadlXml.paramAttributes)));
        return false;
      }
    }
    else {
      alertInvalidWadlAndRetry(GuiFactory.notifications.invalidChildren(WadlXml.paramNode, allowedChildren(WadlXml.paramChildren)));
      return false;
    }
    return true;
  }

  /**
   * @param node
   * @param param
   * @return
   */
  private boolean parseLinkNode(Node node, ParamNode param) {
    // TODO Auto-generated method stub
    return false;
  }

  /**
   * @param node
   * @param param
   * @return
   */
  private boolean parseOptionNode(Node optionElement, ParamNode paramNode) {
    if (containsOnlyAllowedChildren(optionElement, WadlXml.optionChildren)) {
      if (containsOnlyAllowedAttributes(optionElement, WadlXml.optionAttributes)) {
        Node valueNode = optionElement.getAttributes().getNamedItem(WadlXml.option_value);
        if (valueNode != null) {
          paramNode.addOption(valueNode.getNodeValue());
        }
      }
      else {
        alertInvalidWadlAndRetry(GuiFactory.notifications.invalidAttributes(WadlXml.optionNode, allowedAttributes(WadlXml.optionAttributes)));
        return false;
      }
    }
    else {
      alertInvalidWadlAndRetry(GuiFactory.notifications.invalidChildren(WadlXml.optionNode, allowedChildren(WadlXml.optionChildren)));
      return false;
    }
    return true;
  }

  private String allowedChildren(String[] allowedChildren) {    
    if (allowedChildren.length == 1) {
      return "<" + allowedChildren[0] + ">";
    }
    String allowedChildrenString = "";
    for (int i = 0; i < allowedChildren.length; i++) {
      if (i < allowedChildren.length - 1) {
        allowedChildrenString += "<" + allowedChildren[i] + ">, ";
      }
      else {
        allowedChildrenString += " " + GuiFactory.strings.and() + " <" + allowedChildren[i] + ">";
      }
    }    
    return allowedChildrenString;
  }
  
  private String allowedAttributes(String[] allowedAttributes) {
    if (allowedAttributes.length == 1) {
      return "@" + allowedAttributes[0];
    }
    String allowedAttributesString = "";
    for (int i = 0; i < allowedAttributes.length; i++) {
      if (i < allowedAttributes.length - 1) {
        allowedAttributesString += "@" + allowedAttributes[i] + ", ";
      }
      else {
        allowedAttributesString += " " + GuiFactory.strings.and() + " @" + allowedAttributes[i];
      }
    }    
    return allowedAttributesString;
  }
  
  /**
   * @param grammarsElement 
   * @param application 
   * 
   */
  private boolean parseGrammarsNode(Node grammarsElement, ApplicationNode application) {
    if (containsOnlyAllowedChildren(grammarsElement, WadlXml.grammarsChildren)) {      
      GrammarsNode grammars = new GrammarsNode(application);
      application.addGrammars(grammars);
      
      // grammarsChildren = {includeNode, docNode}
      NodeList childNodes = grammarsElement.getChildNodes();
      for (int i = 0; i < childNodes.getLength(); i++) {
        if (childNodes.item(i).getNodeName().equals(WadlXml.includeNode)) {
          if (!parseIncludeNode(childNodes.item(i), grammars));
        }
        else if (childNodes.item(i).getNodeName().equals(WadlXml.docNode)) {
          if (!parseDocNode(childNodes.item(i), grammars));
        }        
      }
    }
    else {
      alertInvalidWadlAndRetry(GuiFactory.notifications.invalidChildren(WadlXml.grammarsNode, allowedChildren(WadlXml.grammarsChildren)));
      return false;
    }    
    return true;
  }

  /**
   * @param includeElement
   * @param grammars
   */
  private boolean parseIncludeNode(Node includeElement, GrammarsNode grammars) {    
    if (containsOnlyAllowedChildren(includeElement, WadlXml.includeChildren)) {
      if (containsOnlyAllowedAttributes(includeElement, WadlXml.includeAttributes)) {
        Node hrefNode = includeElement.getAttributes().getNamedItem(WadlXml.include_href);
        if (hrefNode != null) {
          grammars.addInclude(hrefNode.getNodeValue());
        }
      }
      else {
        alertInvalidWadlAndRetry(GuiFactory.notifications.invalidAttributes(WadlXml.includeNode, allowedAttributes(WadlXml.includeAttributes)));
        return false;
      }  
    }
    else {
      alertInvalidWadlAndRetry(GuiFactory.notifications.invalidChildren(WadlXml.includeNode, allowedChildren(WadlXml.includeChildren)));
      return false;
    }    
    return true;
  }

  /**
   * Ensures that a certain node contains only white-listed children 
   * @param node The node to check
   * @param allowedChildren The allowed children
   */
  private boolean containsOnlyAllowedChildren(Node node, String[] allowedChildren) {
    NodeList childNodes = node.getChildNodes();
    for (int i = 0; i < childNodes.getLength(); i++) {        
      if (!Tools.contains(allowedChildren, childNodes.item(i).getNodeName())) {        
        return false;       
      }      
    }
    return true;
  }

  /**
   * Ensures that a certain node contains only white-listed attributes 
   * @param node The node to check
   * @param allowedAttributes The allowed attributes
   */
  private boolean containsOnlyAllowedAttributes(Node node, String[] allowedAttributes) {    
    NamedNodeMap attributeNodes = node.getAttributes();
    for (int i = 0; i < attributeNodes.getLength(); i++) {      
      if (!Tools.contains(allowedAttributes, attributeNodes.item(i).getNodeName())) {        
        return false;       
      }      
    }
    return true;
  }
  
  /**
   * Alerts the user that the wadl file is invalid and displays the upload dialog again for retry
   */
  private void alertInvalidWadlAndRetry(String errorMessage) {
    Window.alert(errorMessage);
    uploadDialogBox.setVisible(true);    
  }
}
