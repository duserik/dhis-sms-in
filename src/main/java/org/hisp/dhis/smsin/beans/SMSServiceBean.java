package org.hisp.dhis.smsin.beans;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class SMSServiceBean
{
  private String serviceName;
  private String username;
  private String password;
  private String dhisUrl;
  @Autowired
  private DHISAPI dhisapi;
  protected final Log logger = LogFactory.getLog(getClass());
  
  public boolean accept(String sender, String message)
  {
    if (message.toUpperCase().startsWith(this.serviceName.toUpperCase()))
    {
      this.logger.info(this.serviceName + " handling this SMS: " + message);
      System.out.println(this.serviceName + " handling this SMS: " + message + " (to system.out.println)");
      handleMessage(sender, message);
      return true;
    }
    return false;
  }
  
  public void handleMessage(String sender, String message)
  {
    try
    {
      String querystring = "originator=" + URLEncoder.encode(sender, "ISO-8859-1") + "&message=" + URLEncoder.encode(message, "ISO-8859-1");
      querystring = querystring.replace(' ', '+');
      this.dhisapi.forwardRequest(this.dhisUrl, this.username, this.password, querystring);
    }
    catch (UnsupportedEncodingException e)
    {
      e.printStackTrace();
    }
  }
  
  public String getServiceName()
  {
    return this.serviceName;
  }
  
  public void setServiceName(String serviceName)
  {
    this.serviceName = serviceName;
  }
  
  public String getUsername()
  {
    return this.username;
  }
  
  public void setUsername(String username)
  {
    this.username = username;
  }
  
  public String getPassword()
  {
    return this.password;
  }
  
  public void setPassword(String password)
  {
    this.password = password;
  }
  
  public String getDhisUrl()
  {
    return this.dhisUrl;
  }
  
  public void setDhisUrl(String dhisUrl)
  {
    this.dhisUrl = dhisUrl;
  }
}
