# Spring MVC completo

O projeto será um sistema para uma cervejaria com relatórios, dashboard, vendas, etc.

## Necessário para rodar aplicação

- Java 1.8
- tomcat 8.5 com JDBC do mysql

## Tecnologias

- Backend
  - Spring MVC
  - Spring Data JPA
  - hibernate (validações)
- Frontend
  - Thymeleaf
  - Bootstrap
  - [JQuery MaskMoney](https://github.com/plentz/jquery-maskmoney)
  - [bootstrap-switch](https://github.com/Bttstrp/bootstrap-switch)
- dependencias
  - Maven

---

## Anotações

### Spring 🌼

- **arquivo 📄: AppInitializer.java**

  - nesse arquivo foi feito uma extensão da classe `AbstractAnnotationConfigDispatcherServletInitializer` que é responsável pela parte do Spring MVC que gerencia as "rotas" da aplicação. Nela foi sobrescrito métodos para saber sobre as nossas rotas. Esta é a parte de **DispatcherServelet** do Spring
  - o método `getServeletMapping` diz qual vai ser o padrão da URL, mas antes dele executar, ele precisa saber quais são as nossas controllers.
  - Para encontrar nossas controllers do projeto foi criado a classe `WebConfig` e utilizada no método `getServletConfigClasses` (tudo que é relacionado à controller e à parte web).
    - para que essa classe encontre os controllers, foi utilizado a annotation `@ComponentScan` e foi necessário passar `@Configuration` para dizer que a classe é de configuração.
  - `getRootConfigClasses` é executado primeiro que o getServlet, nele é passado as especificações para utilizar as configurações do JpaRepository, e tudo que estiver pra trás da parte web.

- **arquivo 📄: WebConfig.java**
  - nela foi configurado o thymeleaf. Esta é a parte de **ViewResolver** do Spring
  - Aqui é configurado o caminho do diretório que tem os layouts, e qual será a extensão desses layouts (HTML, XML)
- **package 🎁: Controller**

  - nas controllers, quando é feito uma requisição que enviam dados para o servidor o Spring faz um parseamento automático dos valores enviados.
    - Porém, é necessário que nos parâmetros do método da controller tenha o mesmo nome que o `name` dos input,
    - também pode ser feito utilizando Classes, mas elas devem ter os atributos com os mesmos nomes dos atributos `name` de cada input, **obs**: essa classe deve ter um construtor padrão (um construtor que não receba argumentos), caso queira um com argumento, deve criar uma sem antes (nesse caso estpa sendo feito polimorfismo)
  - **forwards**: é o comportamento padrão na controller com o Spring, que qnd chega no método para roteamento ele retorna uma resposta 200 com o conteudo e não faz um recarregamento completo da página
    - com ele pode ser utilizado a Classe Model para enviar dados com o método `addAttribute`.
  - **redirect**: força o browser a fazer uma nova requisição, redireciona para uma nova url,
    - com ele deve ser utilizado a Classe RedirectAttributes para enviar dados do servidor com o método `addFlachAttribute`.

- **package 🎁: converter**
  - Criado para realizar conversões de tipos quando a página web envia dados de um tipo mas na controller eu precisa ser de outro.
  - Foi necessário adicionar configurações no WebConfig com o método `FormattingConversionService`

- **package 🎁: repository**
  - Utilizado para fazer querys no banco de dados.
  - Foi necessário add o arquvivo `JpaConfig` para configurar essa funcionalidade do Spring
  - ver seção [JPA e Hibernate](#jpa-e-hibernate)

- **package 🎁: service**
  - é responsável por ter códigos que envolvem as Regras de negócios (formatação de dados, execução dos comandos DML, etc).

- **package 🎁: validations**
  - responsável por ter as Annotations (beans) personalizadas (no contexto desse projeto é para os atributos das models);
  - ver seção [validações](#validações-✅)

- **Logs de sistema** 🖨

  - O Spring por padrão usar o commons log, ele recomenda configurar o log para melhorar a produtividade.
  - **Log4j 2**: é o loging que será utilizado.
  - **slf4j**: lib para logs de sistema. Ele faz uma ponte com os logs da aplicação.
  - Os logs _podem ser mandados para além do console_, pode ser mandado para banco de dados, arquivos de texto, etc.
  - Utilizando outras libs para logging, será necessário criar um [arquivo de configuração](/src/main/resources/log4j2.xml). Exemplo de conteudo:

  ```XML
  <!-- o nivel de warn vai ser mostrado -->
  <Configuration status="WARN">

    <!-- é para onde vc que enviar os logs do sistemas (aqui é console) e como será enviado (System_out)-->
    <Appenders>
      <Console name="Console" target="SYSTEM_OUT">

        <!-- formata como o log será mostrado -->
        <PatternLayout pattern="%d{HH:mm:ss.SSS} %-5level $logger{36}: %msg%n" />
      </Console>
    </Appenders>

    <Loggers>
      <!-- isso limita a inspeção para apenas um pacote e mostra logs d info -->
      <Logger name="com.algaworks.brewer.controller" level="info" />
      
      <Logger name="org.hibernate.SQL" level="debug" />		
      
      <Root level="error">
        <AppenderRef ref="Console" />
      </Root>
    </Loggers>
    
  </Configuration>
  ```

  - `%-5level`: nível do log (info, debug)
  - `$logger{36}`: nome da classe que gerou o log. o numero dentro de {} é a quantidade máxima de caracteres que vai pertmitir mostrar.
  - Para utilizar esses logs personalizados, deve utilizar o `<Logger>` que foi especificado, e é possível ver logs de diferentes packages.

- Spring Data JPA
  - Possui facilidades para realizar querys, utilizando apenas métodos.
  - Datasource no projeto: no diretorio webapp deve criar `META-INF` e um arquivo context.xml.
    - este será um arquivo de configuração do tomcat.
    - nela terá configurações do datasource, com dados que serão utilizados para conexão do SGBD do projeto.
    - os poolsize são os numeros de conexões que vão ter com o banco de dados.
  - No Java, deve ser criado um arquivo de configuração do JPA, `JPAConfig` que irá pegar os dados do Datasource.
    - Deve dar atenção no método `jpaVendorAdapter`, nele é especificado se gostaria de gerar tabelas a partir das models, porém como nesse projeto está sendo o flyway para gerar tabelas (por causa das migrations) é colocado no método `setGenerateDdl` o argumento **false**.
    - Esse arquivo também será a configuração para poder utilizar a annotation `@Repositories` que são interfaces que vão permitir realizar as querys com o hibernate apenas na forma de métodos.
  - Foi criado uma package `repository` para deixar as interfaces de repositorys das models, 
    - elas são criadas usando o plural da model que ela representa.
    - elas são extendidas com o `JpaRepository<Nome_Model, tipo_da_PrimaryKey_da_model>`

- Padrão de Injeção de dependência
  - Ela tenta evitar que uma classe fique instanciando classes em uma classe de serviço.
  - No conceito geral, é a ideia de ter parametros no construtor e alguem (frameworks - Spring -, etc) automaticamente injetar essas dependencias para vc no construtor. Elas ficam de forma implicita ("escondida")
  - ajuda a concentrar em apenas um local para instanciar classe, para quando for dar manutenção apenas ter um lugar para alterar.
  - Também existe o padrão Factory que torna uma classe unicamente responsável por instanciar uma classe com new e entregar ela pronta.
  - No Spring o gerenciador de injeção é chamado de ApplicationContext.
  - Existem annotations especificas que o Spring reconhece para fazer a IDP, `@Component` que diz que essa classe deve ser encontrada pelo `@Autowired`.
  - Escopo padrão é singleton, ou seja, uma classe injetada com autowired tem apenas uma instancia. Isto será aproveitado para a criação de sessão.
  - _obs: Ela é diferente do principio de inversão de dependência._

- Camada para acessar o BD
  - DAO e Repository auxiliam nessa parte, porém os dois trazem conceitos diferentes
  - **Data Access Object (DAO)**
    - é mais relacionado ao domínio, regras de negócios.

  - **Repository**
    - Trata as entidades como coleções.
    - Está mais relacionado com o DDD (Domain Driven Design)

### Thymeleaf 🍃

- é necessário adicionar a dependencia dele no pom.xml para utilizar esta template engine.
  - **Template engine**: auxilia para montar layouts, utilizando dados que recebe do servidor (controller) para utilizar no layout da View.
- Para configurar ele, foi utilizado o arquivo `WebConfig.java`.
- **É extensível**, pode ser criado tags personalizadas.
- **JSP**: poderia ser utilizado como template engine, mas o Thymeleaf possui mais sentido nesse projeto por ter tags mais legíveis.
- Possui tags especificas para poder trabalhar com os dados enviados. As tags do thymeleaf vão possuir o `th:` antes ou depois de um tag html.
- [Expressions do thymeleaf](https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html#standard-expression-syntax):
  - para utilizar dados ou variaveis dentro de uma tag dele, deve ser utilizado dentro das strings `"${}"`
    ```HTML
    <span th:text="${nomeDoController}"></span>
    ```
- Tags utilizadas no projeto:
  - `th:text`: para pegar um texto enviado pelo `.addAttribute` ou `.addFlashAttribute`.
  - `th:object`: pega um objeto enviado pelo servidor.
  - `th:each`: utilizado para **fazer um loop for** sob uma lista de dados. Ele possui o msm conceito do método forEach do JavaScript, a tag que possui ele é que irá se repetir, junto com as tags aninhadas.
  - `th:block`: foi utiilzado para usar scripts do JS que seriam utilizado apenas uma vez e que precisava ser executado após o JQuery.
- [Objetos](https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html#appendix-b-expression-utility-objects):
  - `#fields`: se refere aos campos de um objeto. Esse objeto deve ser declarado na tag mãe com o `th:object` para que possa pefar os valores dos "fields" desse objeto.
- Reutilização de layout (**Fragments**)
  - _conceito de herdado_: ele cria um padrão e vai adicionando fragmentos dentro desse padrão.
    - para usar o herdado foi necessário usar uma [dependencia externa](https://www.thymeleaf.org/ecosystem.html#community-dialects): `thymeleaf-layout-dialect`
    - no arquivo HTML que será a base deve adicionar `xmlns:layout="http://www.ultraq.net.nz/thymeleaf/layout"`
      - para add o fragmento deve adicionar como atributo o `layout:fragment="conteudo"` na tag de sua escolha
    - nos que serão os fragmentos `layout:decorator="'diretorio/arquivo_HTML'"` (as aspas simples são usadas para evitar erro pois o arquivo base está dentro de um diretório)
      - para enviar o fragmento deve adicionar o mesmo atributo de cima `layout:fragment="conteudo"`
  - _conceito de Fragments_ 🧩:
    - `th:replace`: ele adiciona o aquivo html inteiro no lugar (head, Doctype, etc)
      - para que não ocorra isso e apenas adicione o conteudo que vc deseja, deve adicionar o seguinte atributo `th:replace = "caminho_do_arquivo_html :: nome_dado_no_fragmento"` no lugar que for adicionar.
      - quem for o fragmento, deve adicionar `th:fragment = "nome_dado_no_fragmento"` na tag dele
    - `th:insert`: ele vai inserir o conteudo que vai ter dentro da tag, ou seja, caso tenha alguma classe na tag que colocou `th:fragment`, essa classe não irá ser mandada, apenas o que for filho da tag.

### Validações ✅

- é necessário utilizar a dependencia hibernate-validated no pom.xml
- é adicionado notations acima das propriedades da classe que é do model.
- na controller, antes de colocar a classe da model nos parametros deve adicionar a anotatios `@Valid` ou `@Validated`
- Pode criar annotations(beans) personalizadas para realizar validações.
  ```Java
  @Target({ ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
  @Retention(RetentionPolicy.RUNTIME)
  @Constraint(validatedBy = {})
  @Pattern(regexp = "([a-zA-Z]{2}\\d{4})?")
  public @interface SKU {
    
    @OverridesAttribute(constraint = Pattern.class, name = "message")
    String message() default "SKU deve seguir o padrão XX9999";
    
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    
  }
  ```
    - `@Target`: onde essa validação pode ser aplicada. Dentro dela possui os lugares de forma mais especifica usando o `ElementType`
    - `@Retention`: em que momento ela será avaliada
    - `@Constraint`: restrição da validação.
    - (opcional) `@Pattern`: é utilizado para aplicar padrões com o regexp (poderia ser utilizado lá nos atributos da model)
    - o método `message()` está sendo utilizado para sobrescrever o `message` do `@Pattern` (como pode ver no `@Override`)
    - os métodos com o class são obrigatórios para que não ocorram erros.
      - `payload()`: auxilia a classificar o nível do erro.

### Maven 🧮

- pom.xml é o arquivo que possui as dependências que serão usadas no projeto.
- o `dependencyManagement` auxilia a gerenciar versões das dependencias com o `spring-framework-bom`
- o `scope` `provided` diz que quando for empacotar o projeto, não coloque esta dependencia dentro do empacotamento.
- o `compile` diz que pode empacotar junto.
- o `exclusions` permite tirar alguma dependencia que esteja dentro da dependencia principal (commons logging faz parte do Spring core que por sua vez faz parte do Spring MVC).

### JPA e Hibernate

- JPA são as especificações Java para persistir dados no BD. Hibernate (é um ORM) vai ser responsável para implementar essas especificações.
- Com eles será possível ter comunicação entre o SGBD e o Java.
- Flyway é um framework que ajuda a evoluir um SGBD, voltar uma ação feita no BD por exemplo.
- Java Persistence Query Language (JPQL): ajuda a abstrair comandos DML no banco de dados (pois os comandos pode variar entre os SGBD)
- Hibernate Criteria: forma de fazer os comandos DML
- Diferença entre Criteria e JPQL, nenhuma, apenas que no JPQL pode ficar mais verboso e outros não.
- No projeto:

  - Foi utilizado as dependencias `hibernate-entitymanager` (já possui o JPA) e `hibernate-java8` para datas
  - Para criar uma tabela é necessário utilizar:

    ```Java
    import javax.persistence.Entity;
    import javax.persistence.Table;

    @Entity
    @Table(name = "nomeQueVaiSerNoBD")
    public class Cerveja {
      	@Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long codigo;

        @Column(name = "quantidade_estoque")
        private int quantidadeEstoque;

        @Enumerated(EnumType.STRING)
        private Sabor sabor;

        @ManyToOne
        @JoinColumn(name = "codigo_estilo")
        private Estilo estilo;
    }
    ```

    - o codigo é obrigatório em uma entidade
    - `@Column` permite que vc altere o nome da coluna que será gerado na tabela
    - `@ManyToOne` significa relacionamento de muitos para 1, na tabela de Estilo terá uma variavel que será uma lista que tem o `@OneToMany(mappedBy = "nome_da_coluna_que_conecta")`.
    - `@JoinColumn` especifica o nome da coluna que será a Foreign Key.

- **Aplicar migração com Flyway** (é criar as tabelas no Banco de dados), assim cria tabelas aos poucos.
  - é parecido com as migrações do Knex, Sequelize. precisa criar um diretório para armazenar os scripts de sql.
    - _Obs: é necessario criar a pasta "db/migration" no diretório resources do projeto, se não tiver o nome corretamente o flyway não irá encontrar os aqurivos SQL_ 
  - é necessário configurar o maven para executar o flyway e conectar no Banco de dados mysql:
    - Deve ir nas opções do run > runs configuration > maven build > (icone da esquerda para nova config)
    - Depois deve escolher no Browse directory o projeto que vai usar o flyway
    - no **goals**: flyway:migrate
    - no Parameters name:
      - `flyway.user` e valor de usuario do SGBD
      - `flyway.password` e senha do usuario
      - `flyway.url`: jdbc:mysql://localhost/nome_database
    - ao executar o o maven, o flyway cria uma tabela com as versões das migrações.

- **Transações** (de BD, begin ... commit -ou rollback-)
  - A aplicação por padrão realiza transações de forma automática
  - pode ser alterado para ser de forma manual, utilizando a annotation `@EnableTransactionManagement`.
    ```Java
    @EnableJpaRepositories(basePackageClasses = Cervejas.class, enableDefaultTransactions = false)
    @EnableTransactionManagement
    public class JPAConfig { }
    ```
    - Deve notar que `enableDefaultTransactions` está como false, isto desabilita o automático.
  - Nos arquivos que forem realizar os comandos DML deve utilizar o `@Transactional` para especificar que irá iniciar uma transação, pois agora está de forma manual.
    ```Java
    @Transactional
    public void salvar(Classe nomeClasse) {
      // comando DML
    }
    ```

---

## Referências

- AlgaWorks Spring experts
- [Comparação entre Thymeleaf e JSP](https://www.thymeleaf.org/doc/articles/thvsjsp.html)

- Documentações:
  - [log4j 2](https://logging.apache.org/log4j/2.x/)
  - [Documentação do Thymeleaf](https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html)
  - [Spring JPA Methods](https://docs.spring.io/spring-data/jpa/docs/2.5.0/reference/html/#jpa.query-methods)
