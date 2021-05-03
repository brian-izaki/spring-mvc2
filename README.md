# Spring MVC completo
O projeto será um sistema para uma cervejaria com relatórios, dashboard, vendas, etc.

## Necessário para rodar aplicação

- Java 1.8
- tomcat 8.5 com JDBC do mysql

## Tecnologias
- Backend
  - Spring MVC
  
- Frontend
  - Thymeleaf
  
- dependencias
  - Maven

---

## Anotações

#### Spring

- arquivo: AppInitializer.java
  - nesse arquivo foi feito uma extensão da classe `AbstractAnnotationConfigDispatcherServletInitializer` que é responsável pela parte do Spring MVC que gerencia as "rotas" da aplicação. Nela foi sobrescrito métodos para saber sobre as nossas rotas. Esta é a parte de **DispatcherServelet** do Spring
  - o método `getServeletMapping` diz qual vai ser o padrão da URL, mas antes dele executar, ele precisa saber quais são as nossas controllers.
  - Para encontrar nossas controllers do projeto foi criado a classe `WebConfig` e utilizada no método `getServletConfigClasses`.
    - para que essa classe encontre os controllers, foi utilizado a annotation `@ComponentScan` e foi necessário passar `@Configuration` para dizer que a classe é de configuração.

- arquivo: WebConfig.java
  - nela foi configurado o thymeleaf. Esta é a parte de **ViewResolver** do Spring
  - Aqui é configurado o caminho do diretório que tem os layouts, e qual será a extensão desses layouts (HTML, XML)
  
  

#### Thymeleaf

- é necessário adicionar a dependencia dele no pom.xml para utilizar esta template engine.
- Para configurar ele, foi utilizado o arquivo `WebConfig.java`.

#### Maven
  - pom.xml é o arquivo que possui as dependências que serão usadas no projeto.
  - o `dependencyManagement` auxilia a gerenciar versões das dependencias com o `spring-framework-bom`
  - o `scope` provided diz que quando for empacotar o projeto, não coloque esta dependencia dentro do empacotamento.

#### outros

---

### Referências

- AlgaWorks Spring experts