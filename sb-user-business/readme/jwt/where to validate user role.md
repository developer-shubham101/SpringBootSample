In a **Spring Boot** application, you should validate user roles in the **authorization layer**, typically using **Spring Security**. You can restrict access to resources based on user roles at multiple levels within your application, such as:

1. **Method-Level Authorization** (using annotations like `@PreAuthorize` and `@Secured`)
2. **URL-Level Authorization** (using Spring Security configuration)
3. **Custom Access Decision Managers** (for advanced role-based or permission-based checks)

Here’s a breakdown of each option:

### 1. Method-Level Authorization (Recommended)
Method-level authorization checks are typically handled through annotations placed directly on controller or service methods.

**Example using `@PreAuthorize`:**
```kotlin
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/admin")
class AdminController {

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun getAdminDashboard(): String {
        return "Admin Dashboard"
    }
}
```

In this example:
- Only users with the `ROLE_ADMIN` role can access the `getAdminDashboard()` method.

To enable `@PreAuthorize` and `@PostAuthorize` annotations, add `@EnableGlobalMethodSecurity(prePostEnabled = true)` to your `@Configuration` class.

```kotlin
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfig : WebSecurityConfigurerAdapter() {
    // Other configurations (e.g., authentication providers)
}
```

### 2. URL-Level Authorization (Securing Routes in Configuration)

You can define role-based access directly within the `configure` method of your `WebSecurityConfigurerAdapter` by specifying which URL patterns require which roles.

**Example:**
```kotlin
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@Configuration
@EnableWebSecurity
class SecurityConfig : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http
            .authorizeRequests()
            .antMatchers("/api/admin/**").hasRole("ADMIN")  // Only ADMIN role can access
            .antMatchers("/api/user/**").hasAnyRole("USER", "ADMIN")  // USER or ADMIN roles can access
            .anyRequest().authenticated()  // All other requests require authentication
            .and()
            .formLogin()
    }
}
```

In this configuration:
- Only users with the `ROLE_ADMIN` role can access `/api/admin/**` endpoints.
- Both `ROLE_USER` and `ROLE_ADMIN` users can access `/api/user/**` endpoints.

### 3. Custom Access Decision Managers (Advanced Use Case)

If you need more complex authorization logic, you can define a custom **Access Decision Manager** that checks user roles, permissions, or other conditions. This is more flexible but requires custom code.

**Example:**
1. Create an Access Decision Manager to implement custom logic:
    ```kotlin
    import org.springframework.security.access.AccessDecisionVoter
    import org.springframework.security.access.AccessDeniedException
    import org.springframework.security.access.vote.AffirmativeBased
    import org.springframework.security.core.Authentication
    import org.springframework.security.core.authority.SimpleGrantedAuthority
    import org.springframework.security.web.FilterInvocation

    class CustomAccessDecisionManager : AccessDecisionVoter<FilterInvocation> {
        override fun vote(authentication: Authentication, fi: FilterInvocation, attributes: Collection<ConfigAttribute>): Int {
            if (authentication.authorities.contains(SimpleGrantedAuthority("ROLE_ADMIN"))) {
                return ACCESS_GRANTED
            }
            return ACCESS_DENIED
        }
    }
    ```

2. Register the custom Access Decision Manager in the `SecurityConfig` class.

### Summary

**For most use cases, combining URL-based or method-level annotations (`@PreAuthorize`, `@Secured`) in controllers or services is sufficient and preferred.** For highly customizable role-based access control or permission-based access, consider a custom Access Decision Manager or a third-party solution like Spring Security ACL.