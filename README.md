# Spring MVC completo

O projeto será um sistema para uma cervejaria com relatórios, dashboard, vendas, etc.

## Necessário para rodar aplicação

- Java 1.8
- tomcat 8.5 com JDBC do mysql

## Utilizado no Projeto

- Backend
  - Spring MVC
  - Spring Data JPA
  - Spring Security
  - hibernate (validações, queries)
  - Jackson (Conversão json para objetos Java)
  - Guava (cache)
  - JUnit
- Frontend
  - Thymeleaf (template engine que monta no server)
  - Bootstrap
  - [JQuery MaskMoney](https://github.com/plentz/jquery-maskmoney)
  - [JQuery Mask Plugin](https://igorescobar.github.io/jQuery-Mask-Plugin/)(máscara para telefone, cpf, cnpj, entre outros)
  - [bootstrap-switch](https://github.com/Bttstrp/bootstrap-switch)
  - [bootstrap-datepicker](https://github.com/uxsolutions/bootstrap-datepicker)
  - [UIKit](https://getuikit.com/docs/introduction) (para utilizar componente de upload de imagem)
  - handlebars (template engine na parte front)
- gerenciador de dependências
  - Maven

## Sumário das anotações
1. [Spring e padrões de projeto](#Spring-🌼)
2. [Tymeleaf](#Thymeleaf-🍃)
3. [Validações](#Validações-✅)
4. [Maven](#Maven-🧮)
5. [JPA e Hibernate](#JPA-e-Hibernate)
6. [Tratando imagens no Java](#Tratando-imagens-📷)
7. [Paginação com hibernate](#paginação-page_facing_up)
8. [Cache](#Cache)
9. [Segurança](#Segurança-🔑)
10. [Partes do Java](#Do-Java)
11. [http session](#HTTP-Session)
12. [testes unitários](#Testes-unitarios)
13. [Links de Referencia](#Referências)

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
  - método `mvcConversionService`:
    - nele está sendo configurado a utilização da package converter
    - também está sendo feito a conversão de valores BigDecimal e Integer para ser aceito no Java.

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
    - ex: envio uma string do código de uma cidade para o servidor porém, é necessário o tipo Cidade. O converter é responsável por tornar esse código em um código do tipo cidade (pois cidade possui um atributo código) 
  - Foi necessário adicionar configurações no WebConfig com o método `FormattingConversionService`

- **package 🎁: repository**
  - Utilizado para fazer querys no banco de dados.
  - Foi necessário add o arquvivo `JpaConfig` para configurar essa funcionalidade do Spring
  - ver seção [JPA e Hibernate](#jpa-e-hibernate)

- **package 🎁: service**
  - é responsável por ter códigos que envolvem as Regras de negócios (formatação de dados, execução dos comandos DML, etc).

- **package 🎁: validation**
  - responsável por ter as Annotations (beans) personalizadas (no contexto desse projeto é para os atributos das models);
  - ver seção [validações](#validações-✅)

- **package 🎁: exception**
  - Ficam as classes que são de exceções customizadas que a nossa aplicação irá lançar quando houver erros de execução.
  - Deve notar que os nomes são compridos mas, explicam bem o motivo da classe.

- **package 🎁: handler**
  - nela tem a classe de Controller Advice. (veja mais a baixo o tópico sobrre ele)

- **package 🎁: event**
  - é um padrão de projeto que auxiliam a desacoplar código. 
  - o evento é apenas uma classe normal.
  - o evento será chamada pelo `ApplicationEventPublisher` (dentro da classe service)
    - essa classe possui métodos para reallizar a publicação do evento.
    - é possível ter diferentes eventos que serão escutados para ações diferentes. 
    - é colocado dentro do contexto.
  - foi criado classes com nome listener, elas são responsáveis por executar as ações quando um evento for chamado.

- **package 🎁: helper dentro de repository**
  - foi montado as consultas SQL para o filtro de cerveja.
  - a classe com `NomeClasseImpl` deve respeitar esse padrão de nome ao ser criado pois ela será lida pelo Spring para implementar a consulta.
    - na classe `JPAConfig` é dito qual deve ser o final (nesse caso `Impl`).
    - O nome da Classe deve ser o msm da classe com a notation `@Repository`.
  - vale ressaltar que na classe de implementação, está sendo utilizado `criteria` e `EntityManager`, isso auxilia a diminuir querys caso a tabela tenha `foreign keys`, a solução que o criteria traz é de um join automático com essas tabelas.
  - deve notar tambem que na classe de repository da Cerveja está extendendo a interface de `CervejaQuery` para que na aplicação possa ser utilizada o filtro em apenas um lugar.

- **package 🎁: page**
  - foi criado uma classe wrapper para o `Page` que é responsável pelos dados de paginação da busca.
  - com a criação dele, foi necessário refazer os métodos que são diretos do `Page`
  - essa classe foi instanciada na controller.

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

- **Controller Advice**
  - O Spring fica monitorando se algum código irá lançar alguma exceção e ele mesmo vai fazer o tratamento dela.
  - `@ControllerAdvice`: define que uma classe será a controller advice.
  - nos métodos da classe, *deve ser passado as exceptions que deseja fazer o tratamento* quando alguma controller fizer o lançamento de alguma.
  > *com essa classe é evitado de fazer o try catch em métodos que tem o exception no control advice.*

- **Requisições pelo JavaScript**
  - As requisições foram feitas enviando dados em JSON dentro de uma string (JSON.stringify) com o método POST. Porém, o Spring não consegue converter JSON como nos métodos que só recebiam o submit (cadastro de items), logo, foi necessário utilizar a dependencia `jackson-databind` para realizar essa leitura de JSON.
  - a rota no controller que recebe os dados de requisições Fetch, tiveram que utilizar a annotation `@RequestBody` e retornar o tipo `ResponseEntity` com a annotation `@ResponseBody`, [ver classe estiloController](/src/main/java/com/projetojava/brewer/controller/EstilosController.java)
  - `ResponseEntity<?>`: ele vai ajudar a **manipular o status de uma resposta** do servidor, podendo controlar para ser 200 (ok), 400 (BadRequest), etc.
    - a `?` é para quando o método acabe retornando diferentes tipos para condições diferentes. Mas quando tem apenas um tipo de retorno deve especificar o tipo dentro do `<>`
  
- **Controller**
  - É onde ficam as ações que cada rota irá fazer.
  - Pode ser utilizado uma **url igual para ações diferentes**, basta adicionar o argumento `params`, foi utilizado na 
    classe `VendasController`
  - 

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
  - `th:with`: cria variaveis para o escopo da tag
  - `th:if`: condicional, `true` irá mostrar o elemento, `false` não mostra.
  - `th:block`: foi utiilzado para usar scripts do JS que seriam utilizado apenas uma vez e que precisava ser executado após o JQuery.
  - `th:classappend`: Serve para adicionar texto no atributo `class`

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

- **Dialeto e Processador**:
  - _dialeto_ é o th e o layout que está no arquivo, é um nome com um conjunto de processadores.
  - _processador_ é quem entende e implementa os th:value, th:text, etc.
  - `th:text` = `dialeto:processador`
  - dialetos podem ser extendidos, criando dialetos personalizados.
  - Foi necessário criar uma classe para implementar o [novo dialeto](/src/main/java/com/projetojava/brewer/thymeleaf/BrewerDialect.java).
    ```java
    public BrewerDialect() {
		  super("AlgaWorks Brewer", "brewer", StandardDialect.PROCESSOR_PRECEDENCE);
	  }
    ```
    - o segundo argumento do super se refere a qual prefixo será utilizado no html (no padrão é usado o `th:`, agora também pode ser usado `brewer:`)
  - O arquivo que terá os processadores está no [diretório "processor"](/src/main/java/com/projetojava/brewer/thymeleaf/processor)
    ```java
    private static final String NOME_ATRIBUTO = "classforerror";
    private static final int PRECEDENCIA = 1000;
    
    public ClassForErrorAttributeTagProcessor(String dialectPrefix) {
      super(TemplateMode.HTML, dialectPrefix, null, false, NOME_ATRIBUTO, true, PRECEDENCIA, true);
    }
    ```
    - neste exemplo está a classe `ClassForErrorAttributeTagProcessor` que irá validar se um campo está errado.
    - o `dialectPrefix`, é um valor passado pela classe de dialeto dialeto.
    - no `NOME_ATRIBUTO` é o nome do processador
    - para usar o dialeto seria necessário passar no html:
      ```HTML
      <html lang="pt" xmlns="http://www.w3.org/1999/xhtml" xmlns:brewer="http://brewer.projetojava.com">
      </html>
      ```
    - para usar o processador: 
      ```HTML
      <div brewer:classforerror="valor">
      </div>
      ```


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
  

- Existem também para **CPF e CNPJ**
  - `@CPF` e `@CNPJ`
  - Caso utilize os dois para uma mesma coluna deve adicionar como parâmetro o `groups` ele aceita como valor interfaces simples (nesse projeto foi criado em [validations da model](/src/main/java/com/projetojava/brewer/model/validation/group)), dentro da interface não precisa adicionar mais nada, apenas criar a interface.
  - Teve que utilizar o `@GroupSequenceProvider` na model para "ensinar" qual a sequencia de Beans que deve seguir para validar os atributos.


- dentro de validations com beans, possui o diretorio validator, nele foi criado um **mais avançado** na qual confere se dois campos estão repetindo para a validação de senha. 
  Veja na package [validation](/src/main/java/com/projetojava/brewer/validation/validator/AtributoConfirmacaoValidator.java)

<br/>
  
- em [**controller.validator**](/src/main/java/com/projetojava/brewer/controller/validator/VendaValidator.java)  
  foi criado o `VendaValidator` uma validação customizada para a classe de venda.
  - Foi implementado a interface `Validate` e seus métodos.
  - no método `supports`, ele faz a seguinte pergunta "Quem eu vou validar?", suporto qual classe?
  - `validate` são as validações em si.
    - Deve-se notar o uso do `reject` e `rejectValue`, são eles os responsáveis por dizer que algum dado esteja errado.
  - Na _Controller de venda_ foi criado o método `inicializarValidador` para inicializar esta validação. <br/>
      Foi necessário utilizar a annotation `@InitBinder` (ele é o responsável por reconhecer o método criado). Assim, os 
      métodos que possuírem a annotation `@Valid` vão funcionar msm na model não tendo feito as validações.
    - A validação agora pode ser feita sem o uso do `@Valid`, pois com a classe de validação pode ser utilizado o método:
      ```
      vendaValidator.validate(venda, result);
      ``` 
      Ele foi utilizado para validar a listagem de itens, pois, estava sendo montado dentro do método e não durante a 
      requisição.
  

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
    - Existem outras notations de relacionamento, veja as [referencias](#Referências) 
    
- **CRUD**
  - É feito dentro da package repositories, lá é extendido o JpaRepository que auxilia na parte de queries.
  - o método `.save(objeto)` realiza o insert
  - ao realizar uma query as models possuem papel importante pois é por ela que o Hibernate e o JPA vão saber:
    - quais são as colunas de uma tabela, 
    - se devem realizar ações antes ou depois de algum comando DML (ver seção de métodos de callback), 
    - configurações que alteram a forma padrão do framework como a annotation `@DynamicUpdate` que permite alterar apenas o valor desejado, e não tudo msm que apenas esteja alterando um valor.
  - `Cascade`: na model de venda está sendo feito o cadastro de itens além da venda em si. Com o cascade será feito a 
    inserção dos itens que foram cadastrados (item e venda são tabelas diferentes)

- **Embedded** 
  - foi utilizado na model Cliente com o Endereço, pois na tabela dentro do BD, dados de endereço e cliente estão juntos mas, dentro do projeto foram separados para ser de fácil leitura.
  - a classe entidade irá receber um atributo com `@Embedded`.
  - a classe que for embedded irá receber a anotação `@Embeddable` no lugar da `@Entity`.
  - A classe Endereço não recebe um atributo para código pois ele **não é uma tabela**.
  - Caso **não queira que um atributo vá para o BD**, deve adicionar a notation `@Transient` do javax.persistence

- **Aplicar migração com Flyway** (é criar as tabelas no Banco de dados), assim cria tabelas aos poucos.
  - é parecido com as migrações do Knex, Sequelize. precisa criar um diretório para armazenar os scripts de sql.
    - _Obs: é necessario criar a pasta "db/migration" no diretório resources do projeto, se não tiver o nome corretamente o flyway não irá encontrar os aqurivos SQL_ 
  - é necessário configurar o maven para executar o flyway e conectar no Banco de dados mysql:
    - Deve ir nas opções do run > runs configuration > maven build > (icone da esquerda para nova config)
    - Depois deve escolher no Browse directory o projeto que vai usar o flyway
    - no **goals**: `flyway:migrate`
    - no Parameters name:
      - `flyway.user` e valor de usuario do SGBD
      - `flyway.password` e senha do usuario
      - `flyway.url`: jdbc:mysql://localhost/nome_database
    - ao executar o o maven, o flyway cria uma tabela com as versões das migrações.
  - Caso execute uma migration e ela ocorra erros, utilize um **goals** de: `flyway:repair`
    - ele irá retornar uma migração tirando do schema_version a ultima execução

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

- **Criteria** 
  - Para iniciar uma criteria foi necessário utilizar o `EntityManager` com o método `.unwrap`:
    ```Java
    Criteria criteria = manager.unwrap(Session.class).createCriteria(Cidade.class);
    ```
  - com ele é possível realizar:
    - `Where` com o método `.add(Restriction)` e o restriction possui os métodos para igualdade, maior que, menor que, etc.
    - `Joins` com o `.createAlias()`
    - `order` com o `.addOrder()`
    - `max, min` com o `.setMaresult` e `.setFirstResult`
  - No final, pode ser **retornado um List** com o resultado da query.

- **Métodos de callback do JPA**
  - neste projeto foi feito na classe da model para realizar formatação do dado pra inserir no BD de forma padronizada.
  - o método deve retornar void
    ```Java
    @PrePersist
    @PreUpdate
    private void prePersistUpdate() {
      atributo = atributo.toUpperCase();
    }
    ```
    - _as anotações (são as callbacks)_ indicam que o método deve ser executado antes de realizar ações que vão persisistir os dados. Elas também são chamadas de **JPA Entity life cicle events**.
    - `@PrePersist` para antes de inserir
    - `@PreUpdate` para antes de alterar um dado.

### Tratando imagens 📷
- Frontend
  - foi utilizado a lib do UIKit para realizar as requisições de imagens (por detrás dos panos ele utiliza o ajax)

- Backend
  - Para receber os dados de imagens, foi necessário criar uma nova rota (controller), ela foi desenvolvida especificamente para receber requisições do JS.
  - foi  utilizado o tipo `MultipartFile` que permite o recebimento de imagens no parametro. 
    - antes de usar ele, foi configurado sua utilização no [arquivo `AppinItializer.java`](/src/main/java/com/projetojava/brewer/config/init/AppInitializer.java) para o tomcat saber onde deveria armazenar as imagens de forma temporária
  - Para que a aplicação mantenha uma alta disponibilidade, foi feito uma implementação de **Java com retorno Assíncrono**, cria-se uma nova thread para que ele faça o processamento da parte de imagem sendo salva em uma pasta temporaria. Pois assim, caso o client fizer uma requisição diferente ele vai conseguir.
    - essa parte foi desenvolvida na package `storage`
  - Para realizar o salvamento de imagens de forma local, foi criado dentro do package `storage` o `local`, nele está o código que permite manipular arquivos.

### Paginação :page_facing_up:

- são os dados que são trafegados, pode ser **enviado todos os dados de uma tabela** ou **enviar por partes**.
- a forma de **enviar tudo d uma vez** ganha vantagem que será feita apenas uma requisição
  - porém, se houver milhares de dados, o servidor irá processar muita coisa.
  - esta forma é a mais simples de se implementar
- já a forma de **enviar por partes** (é a paginação propriamente dita) necessita realizar mais d uma requisição para ver os dados por completo.
  - no entanto, ele ganha vantagem no processamento do servidor, pois envia pequenas partes d dados.
  - esta é a forma mais complexa de se implementar.

- na prática:
  - é necessário configurar o `WEBConfig` e add a anotação `@EnableSpringDataWebSupport` para que habilite algumas funcionalidades, como a paginação por exemplo.
  - Existe uma integração entre Spring MVC com o Spring Data que ajuda na paginação.
    - quando fizer requisições de páginas, deve utilizar o parametro de URL `?page=indiceDaPagina`
    - na controller deve adicionar a classe `Pageable` do spring data, ele traz os dados da páginação requisitada pelo front. <br />
    _obs: ele possui valore padrões que podem ser alterados usando `@PageableDefault(size = 2)`_
    - na classe de implementação do filtro, foi add métodos do `criteria` para montar um `select` com `limit`
    

### Cache
- ele utiliza o espaço na memória RAM do servidor, por isso é bom ter pouca coisa lá
- torna o sistema mais rápido pois vai evitar de acessar o BD o tempo todo.
  - o cache é o intermediário entre BD e a aplicação. A consulta ocorre no cache primeiro e dps no BD.
  - **Não é bom** colocar coisa que **ocorrem alteração** o tempo todo.
- Existem **diferentes tipos de implementações** do Cache, depende do projeto qual utilizar.
- No projeto pode ser visto na classe `CidadeController`.
- em código:
  - deve ser colocado a annotation `@Cacheable` do spring e definir um nome para ela (será por ela que eu poderei manipular esse espaço de cache)
  - No webConfig deve adicionar `@EnableCaching` para habilitar ele, e ainda adicionar um `@Bean` de `CacheManager` (lugar que será feito a manipulação do cache).
  - Para que **invalide o cache** é necessário utilizar a annotation `@CacheEvict(value = "nome_do_Cacheable", allEntries = true)`
    - `allEntries` irá apagar todo o cache.
    - Caso queira apenas tirar de um valor especifico deve adicionar no `@Cacheable` o atributo `key = "#nome_exato_do_parametro"` e no `@CacheEvict` o mesmo atributo, com a diferença que pode navegar dentro do objeto ex: `"#cidade.estado.codigo"`
    - pode ser adicionado condições com o atributo `condition`

### Segurança 🔑
- **Auntenticação e autorização**
  - **_AUTENTICAÇÃO_** é a verificação das credenciais (verifica se **vc é quem realmente diz ser**) ex: usuario e senha sendo verificados no BD.
  - **_AUTORIZAÇÃO_** vêm depois de algo ser autenticado (verifica **se Pode ou não fazer algo**) ex: usuario é permitido a cadastrar novos usuários?

- **Spring Security**
  - **arquivo 📄: SecurityInitializer.java**
    - Nele foi feito as configurações para inicializar o Spring Security.
    - Quando é utilizado, o filtro dele altera as acentuações que vem UTF-8. Logo, é necessário retirar o filtro add no AppInitializer e adicionar na classe SecurityInitializer dentro do método herdado `beforeSpringSecurityFilterChain`.
      - Há momentos que a aplicação mostra o JSessionId na url e isso é um  risco para aplicação. Para evitar isso, é melhor trafegar utilizando cookies, para configurar isso, deve ser adicionado o seguinte código dentro do método : 
        ```Java
        servletContext.setSessionTrackingModes(EnumSet.of(SessionTrackingMode.COOKIE));
        ```
    - Para terminar uma sessão caso o sistema fique inativo, foi implementando no diretorio webapp dentro de WEB-INF no web.xml a config para expirar a sessão no tempo que desejar.

  - **arquivo 📄: SecurityConfig.java**
    - São as configurações do Spring Security
    - nele possui o método responsável pela encriptação das senhas.
    - é necessário sobrescrever o método `configurer(AuthenticationManagerBuilder auth)` com o parâmetro `AuthenticationManagerBuilder` ele irá servir para configurar o **login**.
    - o `configurer(HttpSecurity http)` é polimórfico e caso utilize ele com o parâmetro de `HttpSecurity` ele irá servir para configurar o **logout**.
      - métodos do httpSecurity
        - `and()` ele faz com que retorne ao objeto anterior para permitir encadear mais métodos.
        - `antMatchers()` utilizado para mapear uma url do sistema.
        - `permitAll()` concede a permissão ao método que o chamar.
        - `loginPage()` no argumento deve ser passado a página que deseja mostrar para o usuário, se não utilizar este método é mostrado a página padrão que vem com o Spring Security
      - Nele que é feito as **autorizações de acesso** de uma página.
        - Antes de tudo, no [AppUserDetailsService](/src/main/java/com/projetojava/brewer/security/AppUserDetailsService.java) deve:
          - no método loadUserByUsername definir no terceiro argumento do retorno de User uma Collection
          - nessa collection deve ter o tipo `SimpleGrantedAuthority`, as permissões que estão em String no BD são convertidas para esse tipo e adicionadas na collection.
        - foi utilizado o `antMatchers("/rota-que-precisa-autorizacao").hasRole("NOME_DA_ROLE")` 
        - o `hasRole()` foi utilizado para adicionar uma permissão para apenas usuários que tiverem a role especificada possa acessar a página, caso uma página necessite ter mais de uma role para ser acessada pode ser usado o método `hasAnyRole(varArgs)` com ele pode ser passado mais de uma string como argumento.
          - No BD a role que for utilizada no projeto deve ser sempre salva com "ROLE_" logo, ficaria "ROLE_NOME_DA_ROLE", caso não queira adicionar isso no momento de inserir, deve ser utilizado o método `hasAuthority` no lugar de `hasRole()`
        - `anyRequest().denyAll()` irá bloquear todas as rotas da aplicação.
      - máximo de sessões de usuário, é utilizado o seguinte método:
        ```Java
        http.authorizeRequests()
          .sessionManagement()
          .maximumSessions(1)
          .expiredUrl("/login");
        ```
      - Tempo para expirar uma sessão de usuário.


  - **CSRF (Cross-Site Request Forgery)**
    - Falsificação de informção entre sites em tradução livre. ex: Caso  o usuário esteja logado e ele acesse uma página maliciosa que ao cllicar algo envia uma requisição para uma rota que o usuário logado tem acesso, essa requisição é o CSRF acontecendo. **Altamente perigoso**.
    - Por padrão, no Spring Security já vem habilitado.
    - serve para segurança, apenas formulários que tenham ele será permitido enviar dados, caso não tenha o **servidor retorna o status 405**
    - nos formulários que possuem o `th:action` do thymeleaf é gerado automaticamente, pois a própria _engine_ gera.
    - Quando é feito **requisição via JS, ocorre erro** de status 405. 
      - Para isso, foi necessário colocar um token do csrf igual aos que tem nos formulários dentro do `layoutPadrão.html` para que o JS capture esse token.
      - o thymeleaf auxilia nessa parte pois ele possui o objeto `_csrf.token` que gera esse token para a página.

  - **Página de login customizada**
    - deve ser enviado via POST os inputs "username" e "password" (devem ser exatamente o mesmo nome)
    - a página de login apenas é mostrada na url `.../raiz/login`
    - qnd é feito um login que a **autenticação não conferem**, o spring security passa o parâmetro `raiz/login?error` na URL
    - após ser autenticado, se quiser deslogar, deve ser passado o parâmetro o caminho `raiz/logout` na url
    - controller de login
      - foi utilizado o `@AuthenticationPrincipal` ele só serve para injetar o usuário logado, qualquer controller que eu utilizar ele dá para saber se existe um usuário logado. qnd não tem logado, não ocorre a injeção de dependencia.
      - Também foi usado a **Model User** que vem **do próprio Spring Security**, com os dados referentes ao de login (essa model pode ser customizada).

### Do Java

- **Enum**:
  - pode ser implementado um método abstrato que irá ser obrigatório para cada valor do Enum. 
    - no caso desse projeto, foi implementado para realizar a formatação do CPF e CNPJ no Enum `TipoPessoa`, assim evitando o uso de condicionais quando desejar formatar.

---

### HTTP Session

No projeto está sendo utilizado para armazenar os itens de uma venda.

- HTTP é stateless (não mantém estado)
- Para funcionar uma sessão, no servidor é mantido um objeto que faz referência a uma sessão.
  - na resposta de uma requisição é enviado um atributo de sessão para identificação, assim, qnd o usuário fizer uma requisição novamente ele envia o valor do atributo da sessão recebido junto com essa nova requisição e o servidor irá verificar para ver se já existe uma aberta.
- Desenvolvimento:
  - Um objeto fica disponivel para toda a aplicação (as classes de repository, onde eu uso autowired), mas, qnd se 
    trata de sessão de usuário um objeto pode ter que ser instanciado mais d uma vez para que tenham dados diferentes para cada usuario,
    para isso, foi utilizado o `@SessionScope` na classe [TabelaItensVenda](/src/java/com/projetojava/brewer/session/TabelaItensVenda.java).
    
- Sessão para view.
  - foi implementado manualmente (o Spring na versão utilizada não fornece)
  - utilizado para quando o usuário acessar a página de venda não tenha conflito com outras páginas de venda abertas.
  - Usado o UUID para ser salvo em cada TabelaItensVenda assim identificando diferentes abas do navegador de uma mesma sessão.
  - também foi adicionado no HTML o UUID para enviar ao servidor as requisições. (em cada aba o UUID é diferente)
  - as listas de cada aba do navegador é armazenado em uma lista Set, assim o Java saberá separar.

### Testes unitarios

Foi usado para testar no momento de carrinho de compras.

- Testam uma pequena parte do sistema (métodos pequenos).
- No projeto:
  - está no diretório [test](/src/test/java/com/projetojava/brewer/)
  - Foi utilizado o JUnit
  
- **Desenvolvimento**:
  As classes que são tests devem ter no final do nome a palavra `Test`, isso diz ao JUnit que a classe é para este fim. <br/>
  - _annotations_: 
    - `@Test`: diz que o método serve para essa finalidade.
    - `@Before`: será executado antes dos testes (foi utilizado para instanciar uma classe).
  - _métodos_:
    - `assertEquals(esperado, valorParaTeste)`: faz comparação de igualdade.

## Referências

- AlgaWorks Spring experts
- [Comparação entre Thymeleaf e JSP](https://www.thymeleaf.org/doc/articles/thvsjsp.html)

- JPA [notations de relacionamento](https://pt.stackoverflow.com/questions/234755/diferen%C3%A7as-onetomany-manytomany-manytoone-onetoone)

- Documentações:
  - [log4j 2](https://logging.apache.org/log4j/2.x/)
  - Thymeleaf: 
    - [Documentação](https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html)
    - [extendendo dialetos](https://www.thymeleaf.org/doc/tutorials/3.0/extendingthymeleaf.html#dialects-and-processors)
  - [Spring JPA Methods](https://docs.spring.io/spring-data/jpa/docs/2.5.0/reference/html/#jpa.query-methods)
  - [Spring Security](https://spring.io/projects/spring-security)
  - [JPA Entity life cicle events](https://www.baeldung.com/jpa-entity-lifecycle-events)
    - fonte: [hibernate comunity](https://docs.jboss.org/hibernate/stable/entitymanager/reference/en/html/listeners.html)
  - [Consultas utilizando Criteria](https://docs.jboss.org/hibernate/orm/3.5/reference/pt-BR/html/querycriteria.html) do hibernate
