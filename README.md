# Java 8 Advanced

## Streams

**Um Stream é dividido em duas partes:**

- Um objeto para acessar os dados: [Spliterator](src/main/java/com/ac/example/spliterator/Spliterator.md)
- Um objeto para manusear o processamento dos dados: ReferencedPipeline

**Streams podem ser executados em [paralelo.](src/main/java/com/ac/example/parallelStreams/ParallelStreams.md)**

## Collector
Um collector tem como objetivo reduzir os dados em um recipiente, a jdk provem um conjunto com os principais 
redutores que podemos precisar, eles estão na factory class Collectors.

Um colletor é poder ser criado apartir dos seguintes elementos:

- supplier - fornecedor
- accumulator - acumulador
- combiner - combinador

Não é preciso implementar a interface Collector, a própria interface possui o método estático _of_ 
que retorna uma nova instância. 

Esta é a implementação utilizada pelo Collectors.toList()

```
Collector collector = Collector.of( 
    () -> new ArrayList(),                                      // supplier
    (person, list) -> list.add(person),                         // accumulator
    (list1, list2) -> { list1.addAll(list2) ; return list1 ; }  // combiner
);
```

Exemplo de análise de volume de dados utilizando dados ficticios com uma lista de filmes do IMDb: [Código](src/main/java/com/ac/example/dataAnalysis/DataAnalysis.java)
