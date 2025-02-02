# Tutorial on How to Run the Project

### To run the project::
Import the project into IntelliJ IDEA.

Right-click on TestdevApplication and run it.


### After the project starts:
#### 3.1 - The API documentation is available via Swagger:
    http://localhost:8080/swagger-ui/index.html#/
#### 3.2 - The project uses H2 Database, an in-memory database:
    http://localhost:8080/h2-console/login.jsp
#### Config to access H2:
    Driver Class: No changes needed.
    JDBC URL: jdbc:h2:mem:walletdb
    User Name: sa
    Password: Leave it blank, no changes needed.

## A written explanation of any compromises or trade-offs made due to time constraints
Em português:
Demorei em torno de 6 horas para concluir o teste.
Atualmente trabalho de domingo a domingo em um projeto emergencial da Drogaria e Drogasil, como tenho um tempo muito limitado, fiz um projeto que pudesse demonstrar que posso estar agregando a todos no dia a dia, de forma bem simples, mas funcional, inclusive não testei 100% das classes fiz testes no controller e no service para servidor como contexto do teste.

----------------------

In english:
I took around 6 hours to complete the test.
Currently, I work from Sunday to Sunday on an emergency project for Drogaria and Drogasil. Since my time is very limited, I created a project that demonstrates how I can contribute to the team on a daily basis in a simple yet functional way. I did not test 100% of the classes, but I wrote tests for the controller and service to provide context for the test environment.