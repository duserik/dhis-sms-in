package org.hisp.dhis.smsin.beans;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLConnection;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.codec.binary.Base64;

public class DHISAPI
{
  protected final Log logger = LogFactory.getLog(getClass());
  private String username;
  private String password;
  private String dhisUrl;
  Base64 base64 = new Base64();
  
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
  
  public boolean submitXML(String username, String password, String argUrl, String requestXml)
  {
    try
    {
      String authStr = username + ":" + password;
      String authEncoded = new String(base64.encode(authStr.getBytes()));
      
      URL url = new URL(argUrl);
      URLConnection con = url.openConnection();
      con.setRequestProperty("Authorization", "Basic " + authEncoded);
      
      con.setDoInput(true);
      con.setDoOutput(true);
      
      con.setConnectTimeout(20000);
      con.setReadTimeout(20000);
      
      con.setUseCaches(false);
      con.setDefaultUseCaches(false);
      
      con.setRequestProperty("Content-Type", "application/xml");
      
      OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
      writer.write(requestXml);
      writer.flush();
      writer.close();
      
      InputStreamReader reader = new InputStreamReader(con.getInputStream());
      
      StringBuilder buf = new StringBuilder();
      char[] cbuf = new char['\n'];
      int num;
      while (-1 != (num = reader.read(cbuf)))
      {
        //int num;
        buf.append(cbuf, 0, num);
      }
      String result = buf.toString();
      System.out.println("\nResponse from server after POST:\n" + result);
      
      return true;
    }
    catch (Throwable t)
    {
      System.out.println("Exception while uploading to DHIS: " + t);
    }
    return false;
  }
  
  public boolean forwardRequest(String queryString)
  {
    return forwardRequest(this.dhisUrl, this.username, this.password, queryString);
  }
  
  public boolean forwardRequest(String urlString, String u, String p, String queryString)
  {
    try
    {
      String authStr = u + ":" + p;
      String authEncoded = new String(base64.encode(authStr.getBytes()));
      
      URL url = new URL(urlString + "?" + queryString);
      
      URLConnection con = url.openConnection();
      con.setRequestProperty("Authorization", "Basic " + authEncoded);
      
      con.setDoInput(true);
      con.setDoOutput(true);
      
      con.setConnectTimeout(20000);
      con.setReadTimeout(20000);
      
      con.setUseCaches(false);
      con.setDefaultUseCaches(false);
      
      con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
      
      OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
      
      writer.write("");
      writer.flush();
      writer.close();
      
      InputStreamReader reader = new InputStreamReader(con.getInputStream());
      
      StringBuilder buf = new StringBuilder();
      char[] cbuf = new char['\n'];
      int numInput;
      while (-1 != (numInput = reader.read(cbuf)))
      {
        //int numAppend;
        buf.append(cbuf, 0, numInput);
      }
      String result = buf.toString();
      System.out.println("\nResponse from server after POST:\n" + result);
      System.out.println("\nServer url:" + urlString);
      
      return true;
    }
    catch (Throwable t)
    {
      System.out.println("Exception while uploading to DHIS: " + t);
    }
    return false;
  }
}
