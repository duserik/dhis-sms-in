package org.hisp.dhis.smsin.beans;

import java.util.Map;

public class SMSUsers
{
  private Map<String, String> users;
  
  public Map getUsers()
  {
    return this.users;
  }
  
  public void setUsers(Map users)
  {
    this.users = users;
  }
}
