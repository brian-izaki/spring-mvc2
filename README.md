# Spring MVC completo
O projeto será um sistema para uma cervejaria com relatórios, dashboard, vendas, etc.

## Necessário para rodar aplicação

- Java 1.8
- tomcat 8.5 com JDBC do mysql

## Tecnologias
- Backend
  - Spring MVC
  - hibernate (validações)
  
- Frontend
  - Thymeleaf
  - Bootstrap
  
- dependencias
  - Maven

---

## Anotações

#### Spring 🌼

- **arquivo 📄: AppInitializer.java**
  - nesse arquivo foi feito uma extensão da classe `AbstractAnnotationConfigDispatcherServletInitializer` que é responsável pela parte do Spring MVC que gerencia as "rotas" da aplicação. Nela foi sobrescrito métodos para saber sobre as nossas rotas. Esta é a parte de **DispatcherServelet** do Spring
  - o método `getServeletMapping` diz qual vai ser o padrão da URL, mas antes dele executar, ele precisa saber quais são as nossas controllers.
  - Para encontrar nossas controllers do projeto foi criado a classe `WebConfig` e utilizada no método `getServletConfigClasses`.
    - para que essa classe encontre os controllers, foi utilizado a annotation `@ComponentScan` e foi necessário passar `@Configuration` para dizer que a classe é de configuração.

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


#### Thymeleaf 🍃

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


#### Validações ✅

- é necessário utilizar a dependencia hibernate-validated no pom.xml
- é adicionado notations acima das propriedades da classe que é do model.
- na controller, antes de colocar a classe da model nos parametros deve adicionar a anotatios `@Valid` ou `@Validated`

#### Maven 🧮
  - pom.xml é o arquivo que possui as dependências que serão usadas no projeto.
  - o `dependencyManagement` auxilia a gerenciar versões das dependencias com o `spring-framework-bom`
  - o `scope` `provided` diz que quando for empacotar o projeto, não coloque esta dependencia dentro do empacotamento.
  - o `compile` diz que pode empacotar junto.


---

### Referências

- AlgaWorks Spring experts
- [Documentação do Thymeleaf](https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html)
  - [Comparação entre Thymeleaf e JSP](https://www.thymeleaf.org/doc/articles/thvsjsp.html)