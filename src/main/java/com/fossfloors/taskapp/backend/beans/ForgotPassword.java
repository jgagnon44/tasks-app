package com.fossfloors.taskapp.backend.beans;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ForgotPassword {

  @NotNull
  @NotEmpty
  private String email;

  @NotNull
  @NotEmpty
  private String userName;

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  @Override
  public String toString() {
    return "ForgotPassword [email=" + email + ", userName=" + userName + "]";
  }

}
