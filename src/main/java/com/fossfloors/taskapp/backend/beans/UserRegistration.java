package com.fossfloors.taskapp.backend.beans;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class UserRegistration {

  @NotNull
  @NotEmpty
  private String   firstName;

  @NotNull
  @NotEmpty
  private String   lastName;

  @NotNull
  @NotEmpty
  private String   email;

  @NotNull
  @Enumerated(EnumType.STRING)
  private UserRole role;

  @NotNull
  @NotEmpty
  private String   userName;

  // TODO need to think about this - security
  @NotNull
  @NotEmpty
  private String   pass1;

  // TODO need to think about this - security
  @NotNull
  @NotEmpty
  private String   pass2;

  public UserRegistration() {
    super();

    // Set defaults.
    role = UserRole.USER;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public UserRole getRole() {
    return role;
  }

  public void setRole(UserRole role) {
    this.role = role;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getPass1() {
    return pass1;
  }

  public void setPass1(String pass1) {
    this.pass1 = pass1;
  }

  public String getPass2() {
    return pass2;
  }

  public void setPass2(String pass2) {
    this.pass2 = pass2;
  }

  @Override
  public String toString() {
    return "UserRegistration [firstName=" + firstName + ", lastName=" + lastName + ", email="
        + email + ", role=" + role + ", userName=" + userName + ", pass1=" + pass1 + ", pass2="
        + pass2 + "]";
  }

}
