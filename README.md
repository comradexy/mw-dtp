# Dynamic Thread Pool

## 项目介绍
### 环境
- JDK 11
- Spring Boot 2.7.12
- Maven 3.9.5

## 学习笔记

idea：

- 持久化线程信息，以可视化图表展示历史信息变化







### Spring自动装配实现线程池信息读取

#### 代码实现



#### @Configuration和@Bean的作用

`@Configuration`用于定义Spring配置类，取代传统的XML配置文件。

`@Bean`用于定义Spring容器中的Bean，可以在配置类中声明多个`@Bean`方法，每个方法的返回值作为一个Bean注册到容器中。



#### @Configurable

**依赖注入**：允许在Spring容器之外创建的对象也能够享受到Spring的依赖注入功能。

**透明管理**：使得开发者不需要显式地将对象交给Spring容器管理，也可以透明地注入其依赖。

**使用场景：**

- **传统对象**：在传统的Java应用程序中，有些对象可能是在Spring容器之外创建的，比如通过`new`操作符创建的对象。这些对象如果需要注入依赖，可以使用`@Configurable`来实现。
- **领域模型对象**：在面向领域驱动设计（DDD）的应用程序中，领域模型对象通常不是由Spring容器创建的，但它们可能需要依赖Spring的服务。

> 参考：
>
> - https://blog.csdn.net/u013066244/article/details/89061676
> - https://cloud.tencent.com/developer/article/1657859



#### @SpringBootApplication

`@SpringBootApplication`注解是以下三个注解的组合：

1. **`@SpringBootConfiguration`**：是`@Configuration`注解的特殊变体，表示这是一个Spring Boot配置类。
2. **`@EnableAutoConfiguration`**：启用Spring Boot的自动配置机制，根据类路径中的依赖自动配置Spring应用程序。
3. **`@ComponentScan`**：启用组件扫描，从该类所在的包开始扫描注解，如`@Component`、`@Service`、`@Repository`、`@Controller`等。

以下是这三个注解的详细作用：

**`@SpringBootConfiguration`** 注解是一个特殊的`@Configuration`注解，表明这是一个Spring Boot配置类。它的作用类似于Spring的`@Configuration`注解，用于定义Spring的bean。

**`@EnableAutoConfiguration`** 注解启用Spring Boot的自动配置机制。它会尝试根据项目中的依赖自动配置Spring应用的各个部分。例如，如果类路径中有HSQLDB数据库依赖，Spring Boot会自动配置HSQLDB数据库相关的bean。

**`@ComponentScan`** 注解启用组件扫描，它会自动扫描当前包及其子包中的Spring组件（如带有`@Component`、`@Service`、`@Repository`、`@Controller`等注解的类），并将这些类注册为Spring Bean。