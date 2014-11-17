package com.google.code.apis.rest.client.Wadl;

import java.util.Iterator;
import java.util.Vector;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.XMLParser;

public class WadlXml {
  private Document wadl = XMLParser.createDocument();
  private static final String xmlHeader = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";
  
  // list of elements
  public static final String applicationNode = "application";  
  public static final String resourcesNode = "resources";
  public static final String resourceNode = "resource";
  public static final String resourceTypeNode = "resource_type";
  public static final String requestNode = "request";
  public static final String responseNode = "response";  
  public static final String methodNode = "method";
  public static final String paramNode = "param";
  public static final String docNode = "doc";
  public static final String grammarsNode = "grammars";
  public static final String includeNode = "include";
  public static final String representationNode = "representation";
  public static final String faultNode = "fault";
  public static final String optionNode = "option";
  public static final String linkNode = "link";
  
  // allowed child elements
  public static final String[] applicationChildren = {resourcesNode, grammarsNode, resourceTypeNode, methodNode, representationNode, paramNode, faultNode, docNode};
  public static final String[] resourcesChildren = {resourceNode, docNode};
  public static final String[] grammarsChildren = {includeNode, docNode};
  public static final String[] includeChildren = {docNode};
  public static final String[] optionChildren = {docNode};
  public static final String[] linkChildren = {docNode};
  public static final String[] resourceChildren = {paramNode, methodNode, resourceNode, docNode};
  public static final String[] resource_typeChildren = {paramNode, methodNode, docNode};
  public static final String[] methodChildren = {requestNode, responseNode, docNode};
  public static final String[] requestChildren = {representationNode, paramNode, docNode};
  public static final String[] responseChildren = {representationNode, faultNode, paramNode, docNode};
  public static final String[] representationChildren = {paramNode, docNode};
  public static final String[] paramChildren = {optionNode, linkNode, docNode};
  public static final String[] faultChildren = {paramNode, docNode};

  // list of attributes
  public static final String application_xmlns_xsi = "xmlns:xsi";
  public static final String application_xmlns_xsd = "xmlns:xsd";  
  public static final String application_xsi_schemaLocation = "xsi:schemaLocation";
  public static final String application_xmlns = "xmlns";  
  public static final String application_xmlns_extNs = "xmlns:extNs"; // for additional external namespaces
  public static final String doc_xml_lang = "xml:lang";
  public static final String doc_title = "title";
  public static final String include_href = "href";
  public static final String resources_base = "base";
  public static final String resource_href = "href";
  public static final String resource_id = "id";
  public static final String resource_path = "path";
  public static final String resource_type = "type";
  public static final String resource_queryType = "queryType";
  public static final String resource_type_id = "id";
  public static final String method_href = "href";
  public static final String method_id = "id";
  public static final String method_name = "name";
  public static final String representation_href = "href";
  public static final String representation_id = "id";
  public static final String representation_mediaType = "mediaType";
  public static final String representation_element = "element";
  public static final String representation_profile = "profile";
  public static final String representation_status = "status";
  public static final String fault_href = "href";
  public static final String fault_id = "id";
  public static final String fault_mediaType = "mediaType";
  public static final String fault_element = "element";
  public static final String fault_profile = "profile";
  public static final String fault_status = "status";
  public static final String param_id = "id";
  public static final String param_href = "href";
  public static final String param_name = "name";
  public static final String param_style = "style";
  public static final String param_type = "type";
  public static final String param_default = "default";
  public static final String param_path = "path";
  public static final String param_required = "required";
  public static final String param_repeating = "repeating";
  public static final String param_fixed = "fixed";
  public static final String option_value = "value";
  public static final String link_resource_type = "resource_type";
  public static final String link_rel = "rel";
  public static final String link_rev = "rev";
  
  // allowed attributes
  public static final String[] applicationAttributes = {application_xmlns_xsi, application_xmlns_xsd, application_xsi_schemaLocation, application_xmlns};  
  public static final String[] docAttributes = {doc_title, doc_xml_lang};
  public static final String[] includeAttributes = {include_href};
  public static final String[] resourcesAttributes = {resources_base};
  public static final String[] resourceAttributes = {resource_id, resource_path, resource_type, resource_queryType};
  public static final String[] resource_typeAttributes = {resource_type_id};
  public static final String[] methodAttributes = {method_href, method_id, method_name};
  public static final String[] representationAttributes = {representation_element, representation_href, representation_id, representation_mediaType, representation_profile, representation_status};
  public static final String[] faultAttributes = {fault_element, fault_href, fault_id, fault_mediaType, fault_profile, fault_status};
  public static final String[] paramAttributes = {param_id, param_href, param_name, param_style, param_type, param_default, param_path, param_required, param_repeating, param_fixed};
  public static final String[] optionAttributes = {option_value};
  public static final String[] linkAttributes = {link_rel, link_resource_type, link_rev};
  
  // default wadl namespace and schema stuff
  public static final String xmlns_xsi = "http://www.w3.org/2001/XMLSchema-instance";
  public static final String xmlns_xsd = "http://www.w3.org/2001/XMLSchema";  
  public static final String xsi_schemaLocation = "http://research.sun.com/wadl/2006/10 wadl.xsd";
  public static final String xmlns = "http://research.sun.com/wadl/2006/10"; 
  
  public WadlXml(ApplicationNode application) {    
    // <application>        
    createApplicationElement(application);        
  }  
    
  public String dump() {
    if (wadl.hasChildNodes()) {
      String regExp = "><";       
      return (xmlHeader + wadl.toString()).replaceAll(regExp, ">\n<");
    }
    else {
      return "";
    }
  }
  
  private void createDocElements(Vector documentation, Node parentElement) {    
    Iterator iterator = documentation.iterator();
    while (iterator.hasNext()) {
      // <doc xml:lang="" title="xsd:string">bla</doc>
      DocNode doc = (DocNode) iterator.next();
      Element docElement = wadl.createElement(docNode);
      docElement.setAttribute(doc_xml_lang, doc.getXmlLang());
      docElement.setAttribute(doc_title, doc.getTitle());      
      docElement.appendChild(wadl.createTextNode(doc.getText()));
      
      parentElement.appendChild(docElement);
    }    
  }
  
  private void createApplicationElement(ApplicationNode application) {
    // applicationChildren = {resourcesNode, grammarsNode, resourceTypeNode, methodNode, representationNode, faultNode, docNode}
    Element applicationElement = wadl.createElement(applicationNode);
    applicationElement.setAttribute(application_xmlns_xsi, xmlns_xsi);
    applicationElement.setAttribute(application_xmlns_xsd, xmlns_xsd);
    applicationElement.setAttribute(application_xsi_schemaLocation, xsi_schemaLocation);
    applicationElement.setAttribute(application_xmlns, xmlns);    
  
    // external additional namespaces
    Vector allNamespaces = application.getAllNamespaces();
    Iterator namespaceIterator = allNamespaces.iterator();
    int namespaceCounter = 0;
    // add additional application-specific namespaces
    while (namespaceIterator.hasNext()) {
      NamespaceAttribute namespace = (NamespaceAttribute) namespaceIterator.next();
      if (namespace.getName() == null) {
        applicationElement.setAttribute(application_xmlns_extNs + namespaceCounter, namespace.getValue());
      }
      else {
        applicationElement.setAttribute(namespace.getName(), namespace.getValue());
      }
      namespaceCounter++;
    }  
    
    // <grammars>
    if (application.getGrammars() != null) {
      createGrammarsElement(application.getGrammars(), applicationElement);          
    }
    
    // <resources base="xsd:anyURI">
    if (application.getResources() != null) {      
      createResourcesElement(application.getResources(), applicationElement);      
    }
    
    // <resource_type>
    if (application.getAllResourceTypes() != null) {
      Vector allResourceTypes = application.getAllResourceTypes();
      Iterator allResourceTypesIterator = allResourceTypes.iterator();
      while (allResourceTypesIterator.hasNext()) {
        createResourceTypeElement((ResourceTypeNode) allResourceTypesIterator.next(), applicationElement);
      }
    }
    
    // <method>
    if (application.getAllMethods() != null) {
      Vector allMethods = application.getAllMethods();
      Iterator allMethodsIterator = allMethods.iterator();
      while (allMethodsIterator.hasNext()) {
        createMethodElement((MethodNode) allMethodsIterator.next(), applicationElement);
      }
    }
    
    // <representation>
    if (application.getAllRepresentations() != null) {
      Vector allRepresentations = application.getAllRepresentations();
      Iterator allRepresentationsIterator = allRepresentations.iterator();
      while (allRepresentationsIterator.hasNext()) {
        createRepresentationElement((RepresentationNode) allRepresentationsIterator.next(), applicationElement);
      }
    }
    
    // <param>
    if (application.getAllParams() != null) {
      Vector allParams = application.getAllParams();
      Iterator allParamsIterator = allParams.iterator();
      while (allParamsIterator.hasNext()) {
        createParamElement((ParamNode) allParamsIterator.next(), applicationElement);
      }
    }
    
    // <fault>
    if (application.getAllFaults() != null) {
      Vector allFaults = application.getAllFaults();
      Iterator allFaultsIterator = allFaults.iterator();
      while (allFaultsIterator.hasNext()) {
        createFaultElement((FaultNode) allFaultsIterator.next(), applicationElement);
      }
    }
    
    // <doc>
    if (application.getAllDocs() != null) {      
      createDocElements(application.getAllDocs(), applicationElement);      
    }
    
    wadl.appendChild(applicationElement);
  }
  
  /**
   * @param resourceType
   * @param applicationElement
   */
  private void createResourceTypeElement(ResourceTypeNode resourceType, Element applicationElement) {
    //resource_typeChildren = {paramNode, methodNode, docNode};
    Element resourceTypeElement = wadl.createElement(resourceTypeNode);
    
    // <param>
    Vector allResourceParams = resourceType.getAllParams();
    Iterator resourceParamIterator = allResourceParams.iterator();
    while (resourceParamIterator.hasNext()) {                  
      createParamElement((ParamNode) resourceParamIterator.next(), resourceTypeElement);     
    }
    
    // <method>
    Vector allMethods =  resourceType.getAllMethods();
    Iterator methodIterator = allMethods.iterator();
    while (methodIterator.hasNext()) {      
      // <method name="{GET|POST|PUT|DELETE}">      
      createMethodElement((MethodNode) methodIterator.next(), resourceTypeElement);                        
    }
    
    // <doc>
    createDocElements(resourceType.getAllDocs(), resourceTypeElement);
    
    applicationElement.appendChild(resourceTypeElement);
  }

  private void createGrammarsElement(GrammarsNode grammars, Element applicationElement) {
    // grammarsChildren = {includeNode, docNode}    
    Element grammarsElement = wadl.createElement(grammarsNode);
    // <include>    
    if (grammars != null) {
      Vector allIncludes = grammars.getAllIncludes();
      Iterator includeIterator = allIncludes.iterator();
      while (includeIterator.hasNext()) {
        Element includeElement =  wadl.createElement(includeNode);
        includeElement.setAttribute(include_href, (String) includeIterator.next());
        grammarsElement.appendChild(includeElement);
      }      
    }  
    
    // <doc>
    createDocElements(grammars.getAllDocs(), grammarsElement);
    
    applicationElement.appendChild(grammarsElement);    
  }

  private void createResourcesElement(ResourcesNode resources, Element applicationElement) {
    // resourcesChildren = {resourceNode, docNode}
    Element resourcesElement = wadl.createElement(resourcesNode);    
    resourcesElement.setAttribute(resources_base, resources.getBase());
    
    // <resource path="xsd:string">
    Vector allResources =  resources.getAllResources();
    Iterator resourceIterator = allResources.iterator(); 
    while (resourceIterator.hasNext()) {      
      createResourceElement((ResourceNode) resourceIterator.next(), resourcesElement);                  
    }
    
    // <doc>
    createDocElements(resources.getAllDocs(), resourcesElement);
    
    applicationElement.appendChild(resourcesElement);
  }
    
  private void createResourceElement(ResourceNode resource, Element parentElement) {
    // resourceChildren = {paramNode, methodNode, resourceNode, docNode}
    Element resourceElement = wadl.createElement(resourceNode);
    if (resource.getHref() != null) {
      resourceElement.setAttribute(resource_href, resource.getHref());
    }
    else {    
      resourceElement.setAttribute(resource_path, resource.getPath());
      if (resource.getId() != null) resourceElement.setAttribute(resource_id, resource.getId());
    }
    
    // <resource>
    Vector allInnerResources =  resource.getAllResources();
    Iterator innerResourceIterator = allInnerResources.iterator(); 
    while (innerResourceIterator.hasNext()) {                     
      createResourceElement((ResourceNode) innerResourceIterator.next(), resourceElement);      
    }
    
    // <doc>
    createDocElements(resource.getAllDocs(), resourceElement);              
    
    // <param>
    Vector allResourceParams = resource.getAllParams();
    Iterator resourceParamIterator = allResourceParams.iterator();
    while (resourceParamIterator.hasNext()) {                  
      createParamElement((ParamNode) resourceParamIterator.next(), resourceElement);     
    }
    
    // <method>
    Vector allMethods =  resource.getAllMethods();
    Iterator methodIterator = allMethods.iterator();
    while (methodIterator.hasNext()) {      
      // <method name="{GET|POST|PUT|DELETE}">      
      createMethodElement((MethodNode) methodIterator.next(), resourceElement);                        
    }    
    
    parentElement.appendChild(resourceElement);
  }
  
  private void createMethodElement(MethodNode method, Element parentElement) {
    // methodChildren = {requestNode, responseNode, docNode};
    Element methodElement = wadl.createElement(methodNode);
    if (method.getHref() != null) {
      methodElement.setAttribute(method_href, method.getHref());
    }
    else {
      methodElement.setAttribute(method_name, method.getName());
      if (method.getId() != null) methodElement.setAttribute(method_id, method.getId());
    }
        
    // <request>      
    createRequestElement(method.getRequest(), methodElement);            
    
    // <response>
    createResponseElement(method.getResponse(), methodElement);
    
    // <doc>
    createDocElements(method.getAllDocs(), methodElement);
    
    parentElement.appendChild(methodElement);
  }
  
  private void createRequestElement(RequestNode request, Element parentElement) {
    if (request == null) {
      return;
    }
    // requestChildren = {representationNode, paramNode, docNode}
    Element requestElement = wadl.createElement(requestNode);
    
    // <param>
    Vector allParams = request.getAllParams();
    Iterator paramIterator = allParams.iterator();
    while (paramIterator.hasNext()) {        
      // <param id=""? name=""1 style="{query|header}"1 type="xsd:string"? default=""? required=""? repeating=""? fixed="xsd:string"? />              
      createParamElement((ParamNode) paramIterator.next(), requestElement);      
    }
    
    // <representation>
    Vector allRepresentations = request.getAllRepresentations();
    Iterator allRepresentationsIterator = allRepresentations.iterator();
    while (allRepresentationsIterator.hasNext()) {
      // <representation mediaType=""? element=""? profile=""? status=""? />*      
      createRepresentationElement((RepresentationNode) allRepresentationsIterator.next(), requestElement);     
    }
    
    // <doc>
    createDocElements(request.getAllDocs(), requestElement);

    parentElement.appendChild(requestElement);
  }
  
  private void createParamElement(ParamNode param, Element parentElement) {
    // paramChildren = {optionNode, linkNode, docNode}
    Element paramElement = wadl.createElement(paramNode);
    if (param.getHref() != null) {
      paramElement.setAttribute(param_href, param.getHref());
    }
    else {
      if (!param.getName().equals("")) paramElement.setAttribute(param_name, param.getName());          
      if (!param.getType().equals("")) paramElement.setAttribute(param_type, param.getType());
      if (!param.getStyle().equals("")) paramElement.setAttribute(param_style, param.getStyle());
      if (!param.getDefaultValue().equals("")) paramElement.setAttribute(param_default, param.getDefaultValue());
      if (!param.getFixedValue().equals("")) paramElement.setAttribute(param_fixed, param.getFixedValue());
      if (!param.getPath().equals("")) paramElement.setAttribute(param_path, param.getPath());
      if (param.getIsRequired()) paramElement.setAttribute(param_required, "" + param.getIsRequired());
      if (param.getIsRepeating()) paramElement.setAttribute(param_repeating, "" + param.getIsRepeating());
      if (param.getId() != null) paramElement.setAttribute(param_id, param.getId());
    }    
    
    // <option>
    if (param.getOptions().size() > 0) {
      Iterator optionsIterator = param.getOptions().iterator();
      while (optionsIterator.hasNext()) {
        Element optionElement = wadl.createElement(optionNode);
        optionElement.setAttribute(option_value, (String) optionsIterator.next());
        paramElement.appendChild(optionElement);
      }
    }
    
    // <link>
    if (param.getAllLinks() != null) {
      
    }   
    
    // <doc>
    Vector paramDoc = param.getAllDocs();
    Iterator paramDocIterator = paramDoc.iterator();
    while (paramDocIterator.hasNext()) {
      DocNode docNode = (DocNode) paramDocIterator.next();
      if (docNode.getText().equals("")) {
        docNode.setText("Estimated Type ("  + param.getEstimateQuality() + "): [" + param.getType() + "]");
      }
    }
    createDocElements(paramDoc, paramElement);
    
    parentElement.appendChild(paramElement);
  }
  
  private void createResponseElement(ResponseNode response, Element parentElement) {
    if (response == null) {
      return;
    }
    // responseChildren = {representationNode, faultNode, paramNode, docNode}
    Element responseElement = wadl.createElement(responseNode);
    
    // <representation>
    Vector allRepresentations = response.getAllRepresentations();
    Iterator allRepresentationsIterator = allRepresentations.iterator();
    while (allRepresentationsIterator.hasNext()) {
      // <representation mediaType=""? element=""? profile=""? status=""? />*      
      createRepresentationElement((RepresentationNode) allRepresentationsIterator.next(), responseElement);      
    }

    // <fault>
    Vector allFaults = response.getAllFaults();
    Iterator allFaultsIterator = allFaults.iterator();
    while (allFaultsIterator.hasNext()) {
      // <fault mediaType=""? element=""? profile=""? status=""? />*      
      createFaultElement((FaultNode) allFaultsIterator.next(), responseElement);      
    }
    
    // <param>
    Vector allParams = response.getAllParams();
    Iterator paramIterator = allParams.iterator();
    while (paramIterator.hasNext()) {        
      // <param id=""? name=""1 style="{query|header}"1 type="xsd:string"? default=""? required=""? repeating=""? fixed="xsd:string"? />              
      createParamElement((ParamNode) paramIterator.next(), responseElement);
    }
    
    // <doc>
    createDocElements(response.getAllDocs(), responseElement);
    
    parentElement.appendChild(responseElement);
  }
  
  private void createRepresentationElement(RepresentationNode representation, Element parentElement) {
    // representationChildren = {paramNode, docNode}
    Element representationElement = wadl.createElement(representationNode);
    if (representation.getHref() != null) {
      representationElement.setAttribute(representation_href, representation.getHref());
    }
    else {
      if (!representation.getMediaType().equals("")) representationElement.setAttribute(representation_mediaType, representation.getMediaType());
      if (!representation.getElement().equals("")) representationElement.setAttribute(representation_element, representation.getElement());
      if (!representation.getStatus().equals("")) representationElement.setAttribute(representation_status, representation.getStatus());
      if (!representation.getProfile().equals("")) representationElement.setAttribute(representation_profile, representation.getProfile());
      if (representation.getId() != null) representationElement.setAttribute(representation_id, representation.getId());
    }    
    
    // <param>
    Vector allRepresentationParams = representation.getAllParams();
    Iterator representationParamIterator = allRepresentationParams.iterator();
    while (representationParamIterator.hasNext()) {        
      // <param id=""? name=""1 style="{query|header}"1 type="xsd:string"? default=""? required=""? repeating=""? fixed="xsd:string"? />            
      createParamElement((ParamNode) representationParamIterator.next(), representationElement);      
    }
    
    // <doc>
    createDocElements(representation.getAllDocs(), representationElement);
    
    parentElement.appendChild(representationElement);
  }
  
  private void createFaultElement(FaultNode fault, Element parentElement) {
    // faultChildren = {paramNode, docNode}
    Element faultElement = wadl.createElement(faultNode);
    
    // <fault>
    if (fault.getHref() != null) {
      faultElement.setAttribute(fault_href, fault.getHref());
    }
    else {      
      if (!fault.getMediaType().equals("")) faultElement.setAttribute(fault_mediaType, fault.getMediaType());
      if (!fault.getElement().equals("")) faultElement.setAttribute(fault_element, fault.getElement());
      if (!fault.getStatus().equals("")) faultElement.setAttribute(fault_status, fault.getStatus());
      if (!fault.getProfile().equals("")) faultElement.setAttribute(fault_profile, fault.getProfile());
      if (fault.getId() != null) faultElement.setAttribute(fault_id, fault.getId());
    }    
    
    // <doc>
    createDocElements(fault.getAllDocs(), faultElement);
    
    parentElement.appendChild(faultElement);
  }
}
