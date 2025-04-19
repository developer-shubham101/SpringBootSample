package in.newdevpoint.bootcamp.utility;

public class RoleConstants {
  public static final String ADMIN_CRUD = "hasAnyRole('ROLE_ADMIN')";
  public static final String USER_CRUD = "hasAnyRole('ROLE_USER', 'ROLE_ADMIN')";
}
