package com.google.code.apis.rest.client.GUI;

import com.google.code.apis.rest.client.Wadl.WadlParser;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * This class creates the upload wadl dialog
 *
 */
public class WadlUploadDialog {
  private static final String deliciousWadl = "<?xml version=\"1.0\"?>\n" +
"<!-- This is a bootleg WADL file for the del.icio.us API. -->\n" +
"\n" +
"<!-- Based on a WADL from http://www.crummy.com/software/wadl.rb/delicious.wadl -->\n" +
"<!-- Adapted for REST Describe & Compile -->\n" +
"\n" +
"<application\n" +
"  xmlns:html=\"http://www.w3.org/1999/xhtml\"\n" +
"  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
"  xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"\n" +
"  xsi:schemaLocation=\"http://research.sun.com/wadl/2006/10 wadl.xsd\"\n" +
"  xmlns=\"http://research.sun.com/wadl/2006/10\">\n" +
"\n" +
"  <resources base=\"https://api.del.icio.us/\">\n" +
"    <doc xml:lang=\"en\" title=\"The del.icio.us API v1\">\n" +
"      Post or retrieve your bookmarks from the social networking website.\n" +
"      Limit requests to one per second.\n" +
"    </doc>\n" +
"\n" +
"    <resource path=\"v1\">\n" +
"      <param name=\"Authorization\" style=\"header\" required=\"true\">\n" +
"\n" +
"        <doc xml:lang=\"en\">All del.icio.us API calls must be authenticated\n" +
"          using Basic HTTP auth.\n" +
"        </doc>\n" +
"      </param>\n" +
"\n" +
"      <resource path=\"tags\">\n" +
"        <resource path=\"get\"><method href=\"#getTags\" /></resource>\n" +
"        <resource path=\"rename\"><method href=\"#renameTag\" /></resource>\n" +
"\n" +
"        <resource path=\"bundles\">\n" +
"          <resource path=\"all\"><method href=\"#getBundles\" /></resource>\n" +
"          <resource path=\"set\"><method href=\"#makeBundle\" /></resource>\n" +
"          <resource path=\"delete\"><method href=\"#deleteBundle\" /></resource>\n" +
"        </resource>\n" +
"      </resource>\n" +
"\n" +
"      <resource path=\"posts\">\n" +
"        <resource path=\"update\"><method href=\"#getLastUpdateTime\" /></resource>\n" +
"        <resource path=\"get\"><method href=\"#getPosts\" /></resource>\n" +
"        <resource path=\"recent\"><method href=\"#getRecentPosts\" /></resource>\n" +
"        <resource path=\"all\"><method href=\"#getAllPosts\" /></resource>\n" +
"        <resource path=\"dates\"><method href=\"#getDates\" /></resource>\n" +
"        <resource path=\"add\"><method href=\"#addPost\" /></resource>\n" +
"        <resource path=\"delete\"><method href=\"#deletePost\" /></resource>\n" +
"      </resource>\n" +
"    </resource>\n" +
"  </resources>\n" +
"\n" +
"  <!-- Methods -->\n" +
"\n" +
"  <!-- \"update\" method -->\n" +
"\n" +
"  <method id=\"getLastUpdateTime\" name=\"GET\">\n" +
"    <request />\n" +
"    <response>\n" +
"      <representation mediaType=\"application/xml\" element=\"update\">\n" +
"        <doc xml:lang=\"en\" title=\"A note on structure\">\n" +
"          The \"time\" attribute contains the last update time for the\n" +
"          authenticated user, in ISO8661 format.\n" +
"        </doc>\n" +
"      </representation>\n" +
"      <fault href=\"#AuthorizationRequired\" />\n" +
"    </response>\n" +
"  </method>\n" +
"\n" +
"  <!-- \"tags\" methods -->\n" +
"\n" +
"  <method id=\"getTags\" name=\"GET\">\n" +
"    <request />\n" +
"    <response>\n" +
"      <representation mediaType=\"application/xml\" element=\"tags\">\n" +
"        <doc xml:lang=\"en\" title=\"Schema description\">\n" +
"          Contains \"tag\" tags, each with \"count\" and \"tag\" attributes.\n" +
"        </doc>\n" +
"      </representation>\n" +
"      <fault href=\"#AuthorizationRequired\" />\n" +
"    </response>\n" +
"  </method>\n" +
"\n" +
"  <method id=\"renameTag\" name=\"POST\">\n" +
"    <request>\n" +
"      <param name=\"old\" required=\"true\" style=\"query\">\n" +
"        <doc xml:lang=\"en\" title=\"Tag to rename.\" />\n" +
"      </param>\n" +
"      <param name=\"new\" required=\"true\" style=\"query\">\n" +
"        <doc xml:lang=\"en\" title=\"New name.\" />\n" +
"      </param>\n" +
"    </request>\n" +
" \n" +
"    <response>\n" +
"      <representation href=\"#resultCodeDone\" />\n" +
"      <fault href=\"#AuthorizationRequired\" />\n" +
"    </response>\n" +
"\n" +
"  </method>\n" +
"\n" +
"  <!-- \"posts\" methods part I: ways of getting posts -->\n" +
"\n" +
"  <method id=\"getPosts\" name=\"GET\">\n" +
"    <doc xml:lang=\"en\" title=\"Returns posts matching the arguments.\">\n" +
"      If no date or url is given, most recent date will be used.\n" +
"    </doc>\n" +
"\n" +
"    <request>\n" +
"      <param name=\"tag\" style=\"query\">\n" +
"        <doc xml:lang=\"en\" title=\"Filter by this tag.\" />\n" +
"      </param>\n" +
"      <param name=\"dt\" style=\"query\">\n" +
"        <doc xml:lang=\"en\" title=\"Filter by this date (CCYY-MM-DDThh:mm:ssZ).\" />\n" +
"      </param>\n" +
"      <param name=\"url\" style=\"query\">\n" +
"        <doc xml:lang=\"en\" title=\"Filter by this URL.\" />\n" +
"      </param>\n" +
"    </request>\n" +
"\n" +
"    <response>\n" +
"      <representation mediaType=\"application/xml\" element=\"posts\">\n" +
"        <doc xml:lang=\"en\" title=\"Sample response\">\n" +
"          &lt;posts dt=&quot;2005-11-28&quot; tag=&quot;webdev&quot; user=&quot;user&quot;&gt;\n" +
"          &lt;post href=&quot;http://www.howtocreate.co.uk/tutorials/texterise.php?dom=1&quot; \n" +
"          description=&quot;JavaScript DOM reference&quot; \n" +
"          extended=&quot;dom reference&quot; \n" +
"          hash=&quot;c0238dc0c44f07daedd9a1fd9bbdeebd&quot; \n" +
"          others=&quot;55&quot; tag=&quot;dom javascript webdev&quot; time=&quot;2005-11-28T05:26:09Z&quot; /&gt;\n" +
"          &lt;/posts&gt;\n" +
"        </doc>\n" +
"      </representation>\n" +
"    </response>\n" +
"  </method>\n" +
"\n" +
"  <method id=\"getRecentPosts\" name=\"GET\">\n" +
"\n" +
"    <doc xml:lang=\"en\" title=\"Returns a list of the most recent posts.\">\n" +
"      Filtered by argument. Maximum 100.\n" +
"    </doc>\n" +
"\n" +
"    <request>\n" +
"      <param name=\"tag\" style=\"query\">\n" +
"        <doc xml:lang=\"en\" title=\"Filter by this tag.\" />\n" +
"      </param>\n" +
"      <param name=\"count\" style=\"query\" default=\"15\">\n" +
"        <doc xml:lang=\"en\" title=\"Number of items to retrieve.\">Maximum: 100</doc>\n" +
"      </param>\n" + 
"    </request>\n" +
"\n" +
"    <response>\n" +
"      <representation href=\"#postList\" />\n" +
"      <fault href=\"#AuthorizationRequired\" />\n" +
"    </response>\n" +
"  </method>\n" +
"\n" +
"  <method id=\"getAllPosts\" name=\"GET\">\n" +
"    <doc xml:lang=\"en\" title=\"Returns all posts\">\n" +
"      Please use sparingly. Call the update function to see if you need\n" +
"      to fetch this at all.\n" +
"    </doc>\n" +
"\n" +
"    <request>\n" +
"      <param name=\"tag\" style=\"query\">\n" +
"        <doc xml:lang=\"en\" title=\"Filter by this tag.\" />\n" +
"      </param>\n" +
"    </request>\n" +
"\n" +
"    <response>\n" +
"      <representation href=\"#postList\" />\n" +
"      <fault href=\"#AuthorizationRequired\" />\n" +
"    </response>\n" +
"  </method>\n" +
"\n" +
"  <method id=\"getDates\" name=\"GET\">\n" +
"    <doc xml:lang=\"en\" \n title=\"Returns a list of dates with the number of posts at each date.\" />\n" +
"\n" +
"    <request>\n" +
"      <param name=\"tag\" style=\"query\">\n" +
"        <doc xml:lang=\"en\" title=\"Filter by this tag.\" />\n" +
"      </param>\n" +
"    </request>\n" +
"    <response>\n" +
"      <representation mediaType=\"application/xml\">\n" +
"        <doc xml:lang=\"en\" title=\"Sample\">\n" +
"          &lt;dates tag=&quot;&quot; user=&quot;user&quot;&gt;\n" +
"          &lt;date count=&quot;5&quot; date=&quot;2005-11-29&quot; /&gt;\n" +
"          &lt;date count=&quot;15&quot; date=&quot;2005-11-28&quot; /&gt;\n" +
"          &lt;date count=&quot;2&quot; date=&quot;2005-11-26&quot; /&gt;\n" +
"          &lt;date count=&quot;2&quot; date=&quot;2005-11-25&quot; /&gt;\n" +
"          &lt;date count=&quot;7&quot; date=&quot;2005-11-23&quot; /&gt;\n" +
"          &lt;date count=&quot;20&quot; date=&quot;2005-11-22&quot; /&gt;\n" +
"          &lt;date count=&quot;16&quot; date=&quot;2005-11-21&quot; /&gt;\n" +
"          &lt;date count=&quot;4&quot; date=&quot;2005-11-19&quot; /&gt;\n" +
"          &lt;/dates&gt;\n" +
"        </doc>\n" +
"      </representation>\n" +
"    </response>\n" +
"  </method>\n" +
"\n" +
"  <!-- \"posts\" methods part II: ways of manipulating posts -->\n" +
"\n" +
"  <method id=\"addPost\" name=\"GET\">\n" +
"\n" +
"    <doc xml:lang=\"en\" title=\"Add a post to del.icio.us\" />\n" +
"    <request>\n" +
"      <param name=\"url\" required=\"true\" style=\"query\">\n" +
"        <doc xml:lang=\"en\" title=\"The URL of the item.\" />\n" +
"      </param>\n" +
"      <param name=\"description\" required=\"true\" style=\"query\">\n" +
"        <doc xml:lang=\"en\" title=\"The description of the item.\" />\n" +
"      </param>\n" +
"      <param name=\"extended\" style=\"query\">\n" +
"        <doc xml:lang=\"en\" title=\"Notes for the item.\" />\n" +
"      </param>\n" +
"      <param name=\"tags\" style=\"query\">\n" +
"        <doc xml:lang=\"en\" title=\"Tags for the item.\">Space delimited</doc>\n" +
"      </param>\n" +
"      <param name=\"dt\" style=\"query\"> \n" +
"        <doc xml:lang=\"en\" title=\"Datestamp of the item.\">\n" +
"          Format: \"CCYY-MM-DDThh:mm:ssZ\". Requires a LITERAL \"T\" and \"Z\"\n" +
"          like in \n" +
"          <html:a href=\"http://www.cl.cam.ac.uk/~mgk25/iso-time.html\">ISO8601</html:a>.\n" +
"          For example: \"1984-09-01T14:21:31Z\"\n" +
"        </doc>\n" +
"      </param>\n" +
"      <param name=\"replace\" default=\"\" style=\"query\"> \n" +
"        <doc xml:lang=\"en\" \n title=\"Unless set to &quot;no&quot;, a post will overwrite an\n" +
"          earlier post with the same URL.\" />\n" +
"        <option value=\"\" />\n" +
"        <option value=\"no\" />\n" +
"      </param>\n" +
"      <param name=\"shared\" style=\"query\"> \n" +
"        <doc xml:lang=\"en\" title=\"Set to &quot;no&quot; to make the item private.\" />\n" +
"        <option value=\"\" />\n" +
"        <option value=\"no\" />\n" +
"      </param>\n" +
"    </request>\n" +
" \n" +
"    <response>\n" +
"      <representation href=\"#resultCode\" />\n" +
"      <fault href=\"#AuthorizationRequired\" />\n" +
"    </response>\n" +
"  </method>\n" +
"\n" +
"  <method id=\"deletePost\" name=\"GET\">\n" +
"    <doc xml:lang=\"en\" title=\"Delete a post from del.icio.us\" />\n" +
"\n" +
"    <request>\n" +
"      <param name=\"url\" required=\"true\" style=\"query\">\n" +
"        <doc xml:lang=\"en\" title=\"The URL of the item.\" />\n" +
"      </param>\n" +
"    </request>\n" +
"\n" +
"    <response>\n" +
"      <representation href=\"#resultCodeDone\" />\n" +
"      <fault href=\"#AuthorizationRequired\" />\n" +
"    </response>\n" +
"  </method>\n" +
"\n" +
"  <!-- \"bundles\" methods -->\n" +
"\n" +
"  <method id=\"getBundles\" name=\"GET\">\n" +
"    <doc xml:lang=\"en\" title=\"Retrieve all of a user's bundles.\" />\n" +
"\n" +
"    <request />\n" +
"    <response>\n" +
"      <representation mediaType=\"application/xml\" element=\"bundles\">\n" +
"        <doc xml:lang=\"en\" title=\"Sample\">\n" +
"          &lt;bundles&gt;\n" +
"          &lt;bundle name=&quot;music&quot; tags=&quot;ipod mp3 music&quot; /&gt;\n" +
"          &lt;/bundles&gt;\n" +
"        </doc>\n" +
"      </representation>\n" +
"      <fault href=\"#AuthorizationRequired\" />\n" +
"    </response>\n" +
"  </method>\n" +
" \n" +
"  <method id=\"makeBundle\" name=\"GET\">\n" +
"    <doc xml:lang=\"en\" title=\"Assign a set of tags to a single bundle.\">\n" +
"      Wipes away previous settings for bundle.\n" +
"    </doc>\n" +
"\n" +
"    <request>\n" +
"      <param name=\"bundle\" style=\"query\">\n" +
"        <doc xml:lang=\"en\" title=\"The bundle name.\" />\n" +
"      </param>\n" +
"      <param name=\"tags\" required=\"true\" style=\"query\">\n" +
"        <doc xml:lang=\"en\" title=\"List of tags.\">Space-separated.</doc>\n" +
"      </param>\n" +
"    </request>\n" +
"\n" +
"    <response>\n" +
"      <representation mediaType=\"application/xml\" element=\"result\">\n" +
"        <doc xml:lang=\"en\" title=\"A note on structure\">\n" +
"          On success, contents are &quot;done&quot; On failure,\n" +
"          contents are &quot;you must supply a bundle name and at least one\n" +
"          tag&quot;\n" +
"        </doc>\n" +
"      </representation>\n" +
"      <fault href=\"#AuthorizationRequired\" />\n" +
"    </response>\n" +
"  </method>\n" +
"\n" +
"  <method id=\"deleteBundle\" name=\"GET\">\n" +
"    <doc xml:lang=\"en\" title=\"Deletes a bundle.\" />\n" +
"    <request>\n" +
"      <param name=\"bundle\" style=\"query\">\n" +
"        <doc xml:lang=\"en\" title=\"The bundle name.\" />\n" +
"      </param>\n" +
"    </request>\n" +
"\n" +
"   <response>\n" +
"     <representation href=\"#resultCodeDone\" />\n" +
"     <fault href=\"#AuthorizationRequired\" />\n" +
"    </response>\n" +
"\n" +
"  </method> \n" +
"\n" +
"  <!-- Commonly used representations -->\n" +
"\n" +
"  <representation id=\"postList\" mediaType=\"text/xml\" element=\"posts\">\n" +
"    <doc xml:lang=\"en\" title=\"Sample\">\n" +
"      &lt;posts tag=&quot;&quot; user=&quot;user&quot;&gt;\n" +
"      &lt;post href=&quot;http://www.weather.com/&quot; description=&quot;weather.com&quot; \n" +
"      hash=&quot;6cfedbe75f413c56b6ce79e6fa102aba&quot; tag=&quot;weather reference&quot; \n" +
"      time=&quot;2005-11-29T20:30:47Z&quot; /&gt;\n" +
"      &lt;post href=&quot;http://www.nytimes.com/&quot; \n" +
"      description=&quot;The New York Times - Breaking News, World News &amp; Multimedia&quot; \n" +
"      extended=&quot;requires login&quot; hash=&quot;ca1e6357399774951eed4628d69eb84b&quot; \n" +
"      tag=&quot;news media&quot; time=&quot;2005-11-29T20:30:05Z&quot; /&gt;\n" +
"      &lt;/posts&gt;\n" +
"    </doc>\n" +
"  </representation>\n" +
"\n" +
"  <representation id=\"resultCode\" mediaType=\"application/xml\" element=\"result\">\n" +
"    <doc xml:lang=\"en\" title=\"A note on structure\">\n" +
"      This representation has the same structure whether or not the\n" +
"      operation succeeded. If it succeeded, the value of the 'code'\n" +
"      attribute is \"done\". Otherwise, it's \"something went wrong\".\n" +
"    </doc>\n" +
"  </representation>\n" +
"\n" +
"  <representation id=\"resultCodeDone\" mediaType=\"application/xml\" element=\"result\">\n" +
"    <doc xml:lang=\"en\" title=\"A note on structure\">\n" +
"      Allegedly, this representation has a \"code\" attribute whose value\n" +
"      is always \"done\". I think it might actually be the same as the\n" +
"      resultCode representation.\n" +
"    </doc>\n" +
"  </representation>\n" +
"\n" +
"  <fault id=\"AuthorizationRequired\" status=\"401\" />\n" +
"\n" +
"</application>";
  public static DialogBox dialog = new DialogBox();;
  
  public static void show() {    
    dialog.setText(GuiFactory.strings.wadlUploadDialog());
    
    VerticalPanel uploadPanel = new VerticalPanel();
    uploadPanel.add(new HTML("<br />"));
    HorizontalPanel textPanel = new HorizontalPanel();
    uploadPanel.add(textPanel);    
    textPanel.add(new HTML(GuiFactory.strings.uploadWadlHere()));
       
    final TextArea wadlTextArea = new TextArea();    
    wadlTextArea.setStyleName("restDescribe-wadlArea");
    wadlTextArea.setHeight(Math.floor(Window.getClientHeight() * 0.75) + "px");
    wadlTextArea.setWidth(Math.floor(Window.getClientWidth() * 0.75) + "px");
    
    HTML demoLink = new HTML("<a href=\"#\"><span style=\"font-size:0.8em; padding-left:4em;\">" + GuiFactory.strings.loadWadl() + "</span></a>");
    demoLink.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        wadlTextArea.setText(deliciousWadl);
      }
    });
    textPanel.add(demoLink);
    
    uploadPanel.add(wadlTextArea);
        
    Button cancelButton = new Button(GuiFactory.strings.cancel());
    cancelButton.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        dialog.hide();
        GuiFactory.blockScreen(false);
      }
    });
    
    Button okButton = new Button(GuiFactory.strings.ok());
    okButton.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        dialog.setVisible(false);          
        if (wadlTextArea.getText().length() > 0) {          
          WadlParser wadlParser = new WadlParser();
          wadlParser.parse(wadlTextArea.getText());          
        }
        GuiFactory.blockScreen(false);
      }
    });
    
    Button selectAllButton = new Button(GuiFactory.strings.selectAll());
    selectAllButton.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        selectAll(wadlTextArea);
      }
    });
    
    uploadPanel.add(new HTML("<br />"));
    HorizontalPanel buttonPanel = new HorizontalPanel();
    buttonPanel.add(okButton);    
    buttonPanel.add(cancelButton);
    buttonPanel.add(selectAllButton);
    uploadPanel.add(buttonPanel);
    uploadPanel.setCellHorizontalAlignment(buttonPanel, HasHorizontalAlignment.ALIGN_CENTER);
    
    dialog.setWidget(uploadPanel);
    dialog.setPopupPosition((int) Math.floor(Window.getClientWidth() * 0.05), (int) Math.floor(Window.getClientHeight() * 0.05));
    
    GuiFactory.blockScreen(true);
    dialog.show();
  }
  
  private static void selectAll(TextArea wadlTextArea) {
    wadlTextArea.setSelectionRange(0, wadlTextArea.getText().length());    
  }
}
