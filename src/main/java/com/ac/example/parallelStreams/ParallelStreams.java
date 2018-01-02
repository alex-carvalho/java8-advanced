package com.ac.example.parallelStreams;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Alex Carvalho
 */
public class ParallelStreams {

    /**
     * Como os Streams utilizão o framework fork/join é possivel alterar o numero do poll
     * de Threads utilizando a seguinte propriedade:
     */
    public static void changeNumberThreadsWithProperty() {
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "6");

        IntStream.range(0, 1_000_000)
                .parallel()
                .boxed()
                .map(s -> Thread.currentThread().getName())
                .distinct()
                .forEach(s -> System.out.println("Processed in the thread: " + s));
    }

    /**
     * Outra forma de alterar o numero do Threads é criando um ForkJoinPool
     */
    public static void changeNumberThreadsWithNewPool() throws ExecutionException, InterruptedException {
        ForkJoinPool forkJoinPool = new ForkJoinPool(5);

        forkJoinPool.submit(() ->

                IntStream.range(0, 1_000_000)
                        .parallel()
                        .boxed()
                        .map(s -> Thread.currentThread().getName())
                        .distinct()
                        .forEach(s -> System.out.println("Processed in the thread: " + s))
        ).get();
    }

    /**
     * Como o ArrayList não é thread-safe no metodo abaixo pode ocorrer dois problemas
     * um ArrayIndexOutOfBoundsException ou o total de itens incorreto
     */
    public static void arrayListNotThreadSafe() {
        List<Integer> integers = new ArrayList<>();

        IntStream.range(0, 100_000)
                .parallel()
                .forEach(integers::add);

        System.out.println("Size: " + integers.size());
    }

    /**
     * Como ArrayList não é thread-safe foi criado uma nova implementação no pacote de concorrência CopyOnWriteArrayList,
     * mas está não é a melhor forma de retornar uma lista, no próximo exemplo demostro a melhor forma
     */
    public static void arrayListThreadSafe() {
        List<Integer> integers = new CopyOnWriteArrayList<>();

        IntStream.range(0, 100_000)
                .parallel()
                .forEach(integers::add);

        System.out.println("Size: " + integers.size());
    }

    /**
     * Em comparação com o exemplo anterior a execução é extremamente mais performatica
     */
    public static void bestListConcatenationUsingCollector() {
        List<Integer> integers = IntStream.range(0, 100_000)
                .parallel()
                .boxed()
                .collect(Collectors.toList());

        System.out.println("Size: " + integers.size());
    }

    /**
     * Exemplo de operação stateless, pois cada Thread pode executar o filtro na sua parte dos dados sem depender
     * do resultado de uma outra execução
     */
    public static void statelessOperation() {
        IntStream.range(0, 100_000)
                .parallel()
                .filter(number -> number % 2 == 0)
                .close();
    }

    /**
     * Exemplo de operação stateful, para pular 2 e manter somente 5 itens é preciso manter um AtomicLong
     * visivel entre as threads em execução
     */
    public static void statefulOperation() {
        IntStream.range(0, 100_000)
                .parallel()
                .skip(1)
                .limit(100)
                .close();
    }
}