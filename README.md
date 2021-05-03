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
  
- package: Controller
  - nas controllers, quando é feito uma requisição que enviam dados para o servidor o Spring faz um parseamento automático dos valores enviados. 
    - Porém, é necessário que nos parâmetros do método da controller tenha o mesmo nome que o `name` dos input, 
    - também pode ser feito utilizando Classes, mas elas devem ter os atributos com os mesmos nomes dos atributos `name` de cada input, **obs**: essa classe deve ter um construtor padrão (um construtor que não receba argumentos), caso queira um com argumento, deve criar uma sem antes (nesse caso estpa sendo feito polimorfismo)
  - forwards: é o comportamento padrão na controller com o Spring, que qnd chega no método para roteamento ele retorna uma resposta 200 com o conteudo e não faz um recarregamento completo da página
    - com ele pode ser utilizado a Classe Model para enviar dados com o método `addAttribute`.
  - redirect: força o browser a fazer uma nova requisição, redireciona para uma nova url, 
    - com ele deve ser utilizado a Classe RedirectAttributes para enviar dados do servidor com o método `addFlachAttribute`.


#### Thymeleaf

- é necessário adicionar a dependencia dele no pom.xml para utilizar esta template engine.
- Para configurar ele, foi utilizado o arquivo `WebConfig.java`.

#### Maven
  - pom.xml é o arquivo que possui as dependências que serão usadas no projeto.
  - o `dependencyManagement` auxilia a gerenciar versões das dependencias com o `spring-framework-bom`
  - o `scope` `provided` diz que quando for empacotar o projeto, não coloque esta dependencia dentro do empacotamento.
  - o `compile` diz que pode empacotar junto.

#### Validações

- é necessário utilizar a dependencia hibernate-validated no pom.xml
- é adicionado notations acima das propriedades da classe que é do model.
- na controller, antes de colocar a classe da model nos parametros deve adicionar a anotatios `@Valid` ou `@Validated`

---

### Referências

- AlgaWorks Spring experts