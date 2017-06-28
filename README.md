gRPC CRUD application

Build project before usage with following maven command

```
mvn verify
```

For starting server and clint, please execute following commands in separated terminals:

- server

    ```
    mvn exec:java -Dexec.mainClass=ua.dp.dmma.crud.server.CrudServer

    ```
- client

    ```
    mvn exec:java -Dexec.mainClass=ua.dp.dmma.crud.client.CrudConsoleClient

    ```


Commands list for client :
- c create Dto, for ex "c  1 string_value true"
- d delete Dto by it's i field value, for ex "d 1"
- u update Dto, for ex "u 2 some_new_value false"
- l return list of saved Dtos
- h return commands list
- e exit from client app