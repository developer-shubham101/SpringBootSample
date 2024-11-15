The **Bean Lifecycle** in Spring Boot (and Spring in general) represents the stages a bean goes through from creation to destruction. Understanding the bean lifecycle is essential for managing resources efficiently and controlling application behavior during startup and shutdown.

Here’s a breakdown of the key stages in the Spring Boot bean lifecycle:

### 1. **Bean Instantiation**
- **Creation**: When a bean is needed (for example, if another bean depends on it or when explicitly requested), Spring instantiates the bean using the constructor or a factory method.
- **Singleton Scope**: If the bean is a singleton (the default), it will be created once and shared across the application.
- **Prototype Scope**: If it's a prototype, a new instance will be created each time it's requested.

### 2. **Dependency Injection**
- After instantiation, Spring performs **dependency injection** (DI), where it injects dependencies into the bean. This happens after the bean has been created but before any initialization callbacks.
- Dependencies can be injected via:
    - Constructor injection (recommended in most cases).
    - Setter methods.
    - Field injection (less common and discouraged).

### 3. **Post-Initialization (Custom Initialization)**
- After DI, the bean can undergo **initialization** steps, where additional setup operations can be performed on the bean before it’s ready for use.
- Custom initialization can be done in various ways:
    - **`@PostConstruct`**: A method annotated with `@PostConstruct` will be called automatically after the DI is complete. It is often used for custom initialization logic.
    - **`InitializingBean`**: The bean class can implement `InitializingBean` and override the `afterPropertiesSet()` method for initialization logic.
    - **`@Bean(initMethod = "init")`**: When declaring a bean in a configuration class, you can specify an `initMethod` attribute that points to a custom initialization method.
    - **`BeanPostProcessor`**: This is an advanced mechanism to perform actions before and after initialization for every bean in the context. Custom `BeanPostProcessor` implementations can intercept each bean initialization and modify its behavior.

#### Example of Custom Initialization with `@PostConstruct`:
```java
@Component
public class MyBean {
    @PostConstruct
    public void init() {
        System.out.println("Bean is going through custom initialization");
    }
}
```

### 4. **Bean Ready for Use**
- After initialization, the bean is now ready for use. It remains in the Spring container and can be injected or used as needed throughout the application.

### 5. **Destruction (Bean Destruction and Cleanup)**
- When the application shuts down or when a bean is no longer needed, Spring can destroy the bean.
- Similar to initialization, destruction allows for cleanup operations. Here are ways to handle it:
    - **`@PreDestroy`**: A method annotated with `@PreDestroy` will be called before the bean is destroyed, which can be used for releasing resources or other cleanup tasks.
    - **`DisposableBean`**: The bean class can implement `DisposableBean` and override the `destroy()` method.
    - **`@Bean(destroyMethod = "cleanup")`**: When defining a bean in a configuration class, you can specify a custom `destroyMethod` attribute.

#### Example of Custom Destruction with `@PreDestroy`:
```java
@Component
public class MyBean {
    @PreDestroy
    public void cleanup() {
        System.out.println("Bean is going through custom cleanup before destruction");
    }
}
```

### Full Lifecycle Flow with Example Annotations

```java
@Component
public class LifecycleBean implements InitializingBean, DisposableBean {

    public LifecycleBean() {
        System.out.println("Bean instantiation - Constructor called");
    }

    @Override
    public void afterPropertiesSet() {
        System.out.println("InitializingBean: afterPropertiesSet() - Custom initialization logic");
    }

    @PostConstruct
    public void postConstruct() {
        System.out.println("@PostConstruct - Custom initialization logic after dependency injection");
    }

    @PreDestroy
    public void preDestroy() {
        System.out.println("@PreDestroy - Cleanup logic before bean destruction");
    }

    @Override
    public void destroy() {
        System.out.println("DisposableBean: destroy() - Custom destruction logic");
    }
}
```

### Bean Lifecycle Summary

The bean lifecycle in Spring Boot involves the following stages:

1. **Instantiation**: Spring creates an instance of the bean.
2. **Dependency Injection**: Spring injects dependencies.
3. **Custom Initialization**: Any custom initialization logic (e.g., `@PostConstruct` or `afterPropertiesSet()`).
4. **Bean Ready for Use**: The bean is available for use within the application.
5. **Pre-Destroy Phase**: When the application context is closing, `@PreDestroy` and other cleanup logic are invoked.
6. **Destruction**: The bean is destroyed, and resources are released.

By understanding and leveraging these stages, you can effectively manage initialization, cleanup, and resource management in your Spring Boot applications.