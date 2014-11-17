package com.google.code.apis.rest.client.Util;

public class TypeEstimator {
  public final static String sure = "sure"; 
  public final static String supposed = "supposed";
  public final static String unsure = "unsure";
  public final static String manual = "manual";  
  
  public final static String typeString = "xsd:string";
  public final static String typeBoolean = "xsd:boolean";
  public final static String typeInteger = "xsd:integer";
  public final static String typeFloat = "xsd:float";  
  public final static String typeDateTime = "xsd:dateTime"; 
  public final static String typeTime = "xsd:time"; 
  public final static String typeDate = "xsd:date";       
  public final static String typeAnyUri = "xsd:anyURI";
  
  public final static String typeLanguage = "language";
  public final static String typeCountry = "country";
  
  private final static String regExString = ".*?\\w+.*?";
  
  private final static String regExInteger = "[0-9]+";  
  
  private final static String regExFloat = "[0-9]*[\\.|,][0-9]+";
  
  private final static String regExBoolean = "false|true|1|0|yes|no";
  // unsure if 1 or 0, check for param name is.+?, .+?ed, has.+?    
  private final static String regExBooleanParam = "^(is).+?|^(has).+?|.+?(ed)$|.+?(ok)$";    
  
  private final static String regExCountry = "AX|AF|AL|DZ|AS|AD|AO|AI|AQ|AG|AR|AM|AW|AU|AT|AZ|BS|BH|BD|BB|BY|BE|BZ|BJ|BM|BT|BO|BA|BW|BV|BR|IO|BN|BG|BF|BI|KH|CM|CA|CV|KY|CF|TD|CL|CN|CX|CC|CO|KM|CD|CG|CK|CR|CI|HR|CU|CY|CZ|DK|DJ|DM|DO|EC|EG|SV|GQ|ER|EE|ET|FK|FO|FJ|FI|FR|GF|PF|TF|GA|GM|GE|DE|GH|GI|GR|GL|GD|GP|GU|GT|GN|GW|GY|HT|HM|HN|HK|HU|IS|IN|ID|IR|IQ|IE|IL|IT|JM|JP|JO|KZ|KE|KI|KP|KR|KW|KG|LA|LV|LB|LS|LR|LY|LI|LT|LU|MO|MK|MG|MW|MY|MV|ML|MT|MH|MQ|MR|MU|YT|MX|FM|MD|MC|MN|MS|MA|MZ|MM|NA|NR|NP|NL|AN|NC|NZ|NI|NE|NG|NU|NF|MP|NO|OM|PK|PW|PS|PA|PG|PY|PE|PH|PN|PL|PT|PR|QA|RE|RO|RU|RW|SH|KN|LC|PM|VC|WS|SM|ST|SA|SN|CS|SC|SL|SG|SK|SI|SB|SO|ZA|GS|ES|LK|SD|SR|SJ|SZ|SE|CH|SY|TW|TJ|TZ|TH|TL|TG|TK|TO|TT|TN|TR|TM|TC|TV|UG|UA|AE|GB|US|UM|UY|UZ|VU|VA|VE|VN|VG|VI|WF|EH|YE|ZM|ZW";
  private final static String regExCountryParam = "country|region|land|c.*?try.?code";
  
  private final static String regExLanguage = "aa|ab|af|am|ar|as|ay|az|ba|be|bg|bh|bi|bn|bo|br|ca|co|cs|cy|da|de|dz|el|en|eo|es|et|eu|fa|fi|fj|fo|fr|fy|ga|gd|gl|gn|gu|ha|he|hi|hr|hu|hy|ia|id|ie|ik|is|it|iu|ja|jw|ka|kk|kl|km|kn|ko|ks|ku|ky|la|ln|lo|lt|lv|mg|mi|mk|ml|mn|mo|mr|ms|mt|my|na|ne|nl|no|oc|om|or|pa|pl|ps|pt|qu|rm|rn|ro|ru|rw|sa|sd|sg|sh|si|sk|sl|sm|sn|so|sq|sr|ss|st|su|sv|sw|ta|te|tg|th|ti|tk|tl|tn|to|tr|ts|tt|tw|ug|uk|ur|uz|vi|vo|wo|xh|yi|yo|za|zh|zu";
  private final static String regExLanguageParam = "lang|language|hl|lr|local.?|lang.*?.?code";
  
  // 2007-01-01T12:34:56
  private final static String regExDateTime = "^([0-9]{4})-?([0-9]{2})-?([0-9]{2})T([0-9]{2}):?([0-9]{2}):?([0-9]{2})$";
  
  // 2007-01-01
  private final static String regExDate = "^([0-9]{4})-?([0-9]{2})-?([0-9]{2})$";
  
  // 12:34:56
  private final static String regExTime = "^([0-9]{2}):?([0-9]{2}):?([0-9]{2})$";
  
  // https://...
  private final static String regExAnyUri = "^(http://).*?|^(https://).*?|^(www\\.).*?";
  
  private final static String regExBooleanWeak = "1|0";
  private final static String regExBooleanStrong = "false|true|yes|no";
  
  public static String[] estimate(String value, String param) {
    String estimateQuality = unsure;
    String probableType = "";       
    
    if (value.matches(regExBoolean)) {      
      if (value.matches(regExBooleanWeak)) {
        // still believe it's int, check for param name...
        estimateQuality = supposed; 
        probableType = typeInteger;
        if (param.matches(regExBooleanParam)) {
          estimateQuality = sure; 
          probableType = typeBoolean;
        }
      }      
      else if (value.matches(regExBooleanStrong)) {
        estimateQuality = sure; 
        probableType = typeBoolean;
      }
    }
    else if (value.matches(regExInteger)) {      
      estimateQuality = sure; 
      probableType = typeInteger;
    }
    else if (value.matches(regExFloat)) {      
      estimateQuality = sure; 
      probableType = typeFloat;
    }
    else if (value.matches(regExAnyUri)) {      
      estimateQuality = sure; 
      probableType = typeAnyUri;
    }    
    else if (value.matches(regExDateTime)) {      
      estimateQuality = sure; 
      probableType = typeDateTime;
    }    
    else if (value.matches(regExDate)) {      
      estimateQuality = sure; 
      probableType = typeDate;
    }
    else if (value.matches(regExTime)) {      
      estimateQuality = sure; 
      probableType = typeTime;
    }    
    else if (value.matches(regExCountry)) {
      estimateQuality = supposed;
      probableType = typeCountry;
      if (param.matches(regExCountryParam)) {      
        estimateQuality = sure;
        probableType = typeCountry;
      }
    }
    else if (value.matches(regExLanguage)) {
      estimateQuality = supposed;
      probableType = typeLanguage;
      if (param.matches(regExLanguageParam)) {
        estimateQuality = sure;
        probableType = typeLanguage;
      }
    }
    else if (value.matches(regExString)) {
      probableType = typeString;
      estimateQuality = supposed;
    }   
    else {
      estimateQuality = unsure;
    }
    String[] estimate = {probableType, estimateQuality};
    return estimate;    
  }
}
