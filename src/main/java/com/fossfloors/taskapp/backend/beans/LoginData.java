package com.fossfloors.taskapp.backend.beans;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class LoginData {

  @NotNull
  @NotEmpty
  private String user;

  @NotNull
  @NotEmpty
  private String password;

  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public String toString() {
    return "LoginData [user=" + user + ", password=" + password + "]";
  }

}
