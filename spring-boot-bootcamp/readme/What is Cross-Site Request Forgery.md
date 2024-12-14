### **Cross-Site Request Forgery (CSRF)**

**Cross-Site Request Forgery (CSRF)** is a type of web security vulnerability where an attacker tricks a user into performing actions on a web application in which they are already authenticated. The attacker takes advantage of the trust that the web application has in the user's browser, using that trust to perform unauthorized actions on the user's behalf without their consent.

CSRF attacks exploit the fact that web browsers automatically include cookies (including session cookies) in HTTP requests to a website that a user is authenticated on. This means that if a user is logged into a web application, an attacker could craft a request that performs an action (such as transferring money, changing an email address, etc.) without the user's knowledge, but the request is treated as valid by the server because it comes from the user's browser with the correct session cookies.

### **How CSRF Works**

Here’s a simplified example of how a CSRF attack works:

1. **User Login**: The user logs into a website (e.g., their bank) and their browser stores a session cookie that keeps them logged in.

2. **Visit Malicious Site**: The user visits a malicious website created by the attacker while still logged in to their bank. This malicious website includes a form or a hidden script that triggers a request to the bank’s website.

3. **CSRF Attack**: When the user unknowingly submits this form or triggers the script (even by clicking on a link), the browser automatically includes the session cookie that proves the user is authenticated. The request is sent to the bank’s server, which accepts the request because the user’s session cookie is valid.

4. **Unauthorized Action**: The bank's server processes the request as if the user had initiated it themselves, performing the action (e.g., transferring money to the attacker's account) without the user's knowledge.

---

### **Example of a CSRF Attack**

Imagine a bank website with a transfer money functionality:

- The form to transfer money is on the page `/transfer` and looks something like this:

```html
<form action="https://www.example-bank.com/transfer" method="POST">
  <input type="hidden" name="toAccount" value="attackerAccount123">
  <input type="hidden" name="amount" value="1000">
  <input type="submit" value="Transfer Money">
</form>
```

- The user logs into the bank and is authenticated with a session cookie.

- Now, an attacker creates a malicious page that includes this form and tricks the user into visiting it. The form is automatically submitted without the user knowing.

```html
<img src="https://www.example-bank.com/transfer?toAccount=attackerAccount123&amount=1000" style="display:none">
```

- When the user visits the attacker’s page, the browser makes a request to the bank’s server with the user's authenticated session cookie. The bank's server processes this request, thinking it is a legitimate transfer initiated by the user.

---

### **Why CSRF is Dangerous**

- **Silent Attacks**: The user doesn’t even know the attack has happened, as the malicious request uses the user's session in the background.
- **Exploits Trust**: The server trusts that the request is coming from the user because it includes a valid session cookie, even though the user didn’t intend to make the request.
- **Widespread Vulnerability**: Any web application that relies on cookies for session management can potentially be vulnerable to CSRF.

---

### **Preventing CSRF Attacks**

There are several strategies that can be employed to mitigate or prevent CSRF attacks:

#### 1. **CSRF Tokens (Anti-CSRF Tokens)**
- This is one of the most common defenses against CSRF attacks.
- The server generates a **unique random token** (CSRF token) for each user session and includes it in all forms or requests.
- When the form is submitted, the server checks the token to ensure it matches the expected value.
- Since the attacker cannot guess this token, they cannot forge a valid request.

Example:
```html
<form action="/transfer" method="POST">
  <input type="hidden" name="csrf_token" value="random-generated-token">
  <!-- Other form fields -->
</form>
```

- The server then validates the `csrf_token` on every incoming request. If the token is missing or incorrect, the request is rejected.

#### 2. **Same-Site Cookies**
- **SameSite** is a cookie attribute that instructs browsers to only send cookies if the request originates from the same domain (i.e., prevents sending cookies during cross-site requests).
- **SameSite=Lax** or **SameSite=Strict** prevents the browser from including cookies when the request comes from an external site, blocking CSRF attacks.

Example:
```http
Set-Cookie: sessionId=abcd1234; SameSite=Lax
```

#### 3. **Double Submit Cookie**
- The server sets a CSRF token in both a **cookie** and a hidden form field or header.
- When the request is made, the server checks that both the token in the cookie and the one in the form/header match. If they don’t match, the request is rejected.

#### 4. **Referer Header Checking**
- The server can check the **Referer** header to ensure that the request came from a trusted source (the same origin as the application).
- However, this approach is less reliable because some browsers and proxies strip the `Referer` header.

#### 5. **CAPTCHA or User Interaction**
- Adding additional user interaction, like a **CAPTCHA** or confirming an action (e.g., clicking a button), can reduce the likelihood of CSRF attacks, especially for sensitive operations like money transfers.

#### 6. **Avoid GET Requests for Sensitive Actions**
- CSRF attacks are easier to exploit with **GET** requests because browsers are more likely to trigger them automatically (e.g., when loading images or executing JavaScript).
- **POST** requests are preferred for actions that modify data, and they can be more easily protected with CSRF tokens.

---

### **CSRF Protection in Spring Security**

If you’re using **Spring Security**, CSRF protection is enabled by default for any non-GET requests (like POST, PUT, DELETE). Here's a basic setup for enabling CSRF protection:

#### Step 1: Enable CSRF Protection (if not already enabled)
In your **Spring Security configuration**, ensure that CSRF protection is enabled:

```java
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            .and()
            .authorizeRequests()
                .antMatchers("/public/**").permitAll()
                .anyRequest().authenticated();
    }
}
```

#### Step 2: Using CSRF Tokens
- For HTML forms, Spring automatically generates a hidden input field for the CSRF token.
- You need to include this token in all **POST** requests. In Thymeleaf, you can use the `th:action` attribute to automatically include the CSRF token:

```html
<form th:action="@{/transfer}" method="post">
    <input type="hidden" name="_csrf" th:value="${_csrf.token}"/>
    <!-- Other form fields -->
</form>
```

---

### **Conclusion**

**CSRF** is a serious web vulnerability that can lead to unauthorized actions on behalf of an authenticated user. It’s important for developers to implement proper defenses, such as CSRF tokens and SameSite cookies, to mitigate the risks. Modern frameworks like **Spring Security** provide built-in support for CSRF protection, making it easier to secure applications against this type of attack.