package org.hisp.dhis.smsin.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.smsin.beans.DHISAPI;
import org.hisp.dhis.smsin.beans.SMSServiceBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SMSIncomingController
{
  protected final Log logger = LogFactory.getLog(getClass());
  @Autowired
  private SMSServiceBean[] smsService;
  @Autowired
  private DHISAPI dhisapi;
  
  @RequestMapping({"/sms"})
  public ModelAndView helloIndex(@RequestParam("sender") String sender, @RequestParam("message") String message)
  {
    ModelAndView mav = new ModelAndView();
    mav.setViewName("main.jsp");
    boolean handled = false;
    for (int i = 0; i < this.smsService.length; i++)
    {
      this.logger.info("Trying SMS Handler:" + this.smsService[i].getServiceName());
      if (this.smsService[i].accept(sender, message))
      {
        handled = true;
        break;
      }
    }
    if (!handled)
    {
      this.logger.info(this.smsService.length + " No local handler. Forwarding:" + message);
      System.out.println(this.smsService.length + " No local handler. Forwarding:" + message);
      try
      {
        String querystring = "originator=" + URLEncoder.encode(sender, "ISO-8859-1") + "&message=" + URLEncoder.encode(message, "ISO-8859-1");
        querystring = querystring.replace(' ', '+');
        this.dhisapi.forwardRequest(querystring);
      }
      catch (UnsupportedEncodingException e)
      {
        e.printStackTrace();
      }
    }
    return mav;
  }
  
  @RequestMapping({"/kannel"})
  public ModelAndView kannelIndex(@RequestParam("recipient") String recipient, @RequestParam("message") String message)
  {
    ModelAndView mav = new ModelAndView();
    mav.setViewName("kannel.jsp");
    try
    {
      String urlstring = "http://localhost:6013/cgi-bin/sendsms?username=peter&password=ford&to=%2B{recipient}&text={message}";
      urlstring = urlstring.replace("{recipient}", recipient);
      urlstring = urlstring.replace("{message}", message.replace(' ', '+'));
      
      URL url = new URL(urlstring);
      BufferedReader in = new BufferedReader(
        new InputStreamReader(url.openStream()));
      String inputLineKannel;
      while ((inputLineKannel = in.readLine()) != null)
      {
        //String inputLineKannel;
        System.out.println(inputLineKannel);
      }
      in.close();
    }
    catch (MalformedURLException e)
    {
      e.printStackTrace();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    return mav;
  }
  
  @RequestMapping({"/"})
  public ModelAndView helloWorld()
  {
    ModelAndView mav = new ModelAndView();
    mav.setViewName("main.jsp");
    mav.addObject("message", "That's wrong");
    return mav;
  }
}
