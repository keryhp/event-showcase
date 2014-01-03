package org.uob.event.showcase.model;

/**
 * User entity interface.
 *
 */
public interface ESUser extends ESEntity {
  String getUserId();

  String getEmail();

  void setEmail(String email);

  String getName();

  void setName(String name);
  
  String getRole();

  void setRole(String role);

}
