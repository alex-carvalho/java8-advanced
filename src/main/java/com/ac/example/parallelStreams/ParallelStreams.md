#### Execução em paralelo

Streams em paralelos é uma abstração que foi construida para utilizar o framework fork/join introduzido na versão 7 do java,
com esta abstração ficou facil executar código em paralelo, mas é preciso tomar alguns cuidados com classes que não são thread-safe. 
Alguns exemplos de operações em paralelo.


##### Operações Stateful vs Stateless
- Stateful: é preciso o compartilhamnto de um estado entre as Threads que executão a operação 
- Stateless: operação que não mantem estado

Para saber se a operação que deseja executar é Stateful ou Stateless pode-se consultar a documentação.

Nem sempre executar uma operação em paralelo pode trazer benefícios, o custo de separar os dados e unir os resultados 
pode ser muito alto dependendo do volume dedados e o tipo de operação a ser relizada.


Exemplos:  [ParallelStreams](src/main/java/com/ac/example/parallelStreams/ParallelStreams.java)