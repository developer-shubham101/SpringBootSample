package com.example.sbbloginfo.utility;

public class RoleConstants {
  public static final String INTERNAL = "hasAuthority('SCOPE_internal')";
  public static final String USER_CRUD = "hasAuthority('ROLE_USER', 'ROLE_ADMIN')";
}
