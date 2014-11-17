/**
 * 
 */
package com.google.code.apis.rest.client.CodeGeneration;

/**
 * @author tsteiner
 *
 */
public class HTTPError {
  private String status;
  private String message;
  
  private static final String default400 = "<b>Error 400: Bad Request</b> The parameters passed to the service did not match as expected.";
  private static final String default401 = "<b>Error 401: Unauthorized</b> Authentication has failed or not yet been provided.";  
  private static final String default403 = "<b>Error 403: Forbidden</b> The request was legal, but the server is refusing to respond to it.";
  private static final String default404 = "<b>Error 404: Not Found</b> The requested data or endpoint does not exist.";
  private static final String default500 = "<b>Error 500: Internal Server Error</b> An internal server error prevented the server from processing your request.";
  private static final String default503 = "<b>Error 503: Service Unavailable</b> An internal problem prevented the data from being returned to you.";
  
  public HTTPError(String errorStatus, String errorMessage) {
    status = errorStatus;
    message = errorMessage;
  }

  public HTTPError(String errorStatus) {
    status = errorStatus;
    if (status.equals("400")) message = default400;
    else if (status.equals("401")) message = default401;
    else if (status.equals("403")) message = default403;
    else if (status.equals("404"))   message = default404;
    else if (status.equals("500"))   message = default500;
    else if (status.equals("503"))   message = default503;
    else message = "<b>Error " + status + "</b>";        
  }
  
  
  /**
   * @return the message
   */
  public String getMessage() {
    return message;
  }

  /**
   * @param message the message to set
   */
  public void setMessage(String message) {
    this.message = message;
  }

  /**
   * @return the status
   */
  public String getStatus() {
    return status;
  }

  /**
   * @param status the status to set
   */
  public void setStatus(String status) {
    this.status = status;
  }
}
