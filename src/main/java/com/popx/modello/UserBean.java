package com.popx.modello;

import java.io.Serializable;

/*@
  @ invariant username == null || !username.isEmpty();
  @ invariant email == null || !email.isEmpty();
  @ invariant password == null || !password.isEmpty();
  @ invariant role == null || !role.isEmpty();
@*/
public class UserBean implements Serializable {

    private String username;
    private String email;
    private String password;
    private String role;

    /*@
      @ ensures this.username == null && this.email == null && this.password == null && this.role == null;
    @*/
    public UserBean() {}

    /*@
      @ requires username != null && email != null && password != null && role != null;
      @ ensures this.username.equals(username)
      @      && this.email.equals(email)
      @      && this.password.equals(password)
      @      && this.role.equals(role);
    @*/
    public UserBean(String username, String email, String password, String role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    /*@ ensures \result == username; @*/
    public String getUsername() { return username; }

    /*@
      @ requires username != null;
      @ ensures this.username.equals(username);
    @*/
    public void setUsername(String username) { this.username = username; }

    /*@ ensures \result == email; @*/
    public String getEmail() { return email; }

    /*@
      @ requires email != null;
      @ ensures this.email.equals(email);
    @*/
    public void setEmail(String email) { this.email = email; }

    /*@ ensures \result == password; @*/
    public String getPassword() { return password; }

    /*@
      @ requires password != null;
      @ ensures this.password.equals(password);
    @*/
    public void setPassword(String password) { this.password = password; }

    /*@ ensures \result == role; @*/
    public String getRole() { return role; }

    /*@
      @ requires role != null;
      @ ensures this.role.equals(role);
    @*/
    public void setRole(String role) { this.role = role; }
}
