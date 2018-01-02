package com.ac.example.dataAnalysis;

import com.ac.example.dataAnalysis.model.Genre;
import com.ac.example.dataAnalysis.model.Movie;
import com.ac.example.parallelStreams.ParallelStreams;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Alex Carvalho
 */
public class DataAnalysis {

    /**
     * Realiza um agrupamento do total de filmes do ano e exibe o ano com mais filmes
     */
    public static void yearWithMoreMovies(Set<Movie> movies) {
        movies.stream()
                .collect(
                        Collectors.groupingBy(
                                movie -> movie.getReleaseDate().getYear(),
                                Collectors.counting()
                        )
                )
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .ifPresent(System.out::println);
    }

    /**
     * Exibe o filme com a maior média dos votos que possuem mais de 5000 votos
     */
    public static void movieWithBiggerVoteAverage(Set<Movie> movies) {
        movies.stream()
                .filter(movie -> movie.getVoteCount() > 5000)
                .max(Comparator.comparingInt(Movie::getVoteAverage))
                .ifPresent(System.out::println);
    }

    /**
     * Exibe o ano com mais filmes de ficção científica usando o collector summingLong
     */
    public static void yearWithMoreMoviesOfScienceFiction(Set<Movie> movies) {
        movies.stream()
                .collect(
                        Collectors.groupingBy(
                                movie -> movie.getReleaseDate().getYear(),
                                Collectors.summingLong(value -> value.getGenres().stream()
                                        .filter(genre -> "Science Fiction".equals(genre.getName()))
                                        .count())))
                .entrySet().stream()
                .max(
                        Map.Entry.comparingByValue()
                )
                .ifPresent(System.out::println);
    }

    /**
     * Exibe o ano com mais filmes de ficção científica usando um collector customizado
     */
    public static void yearWithMoreMoviesOfScienceFictionCustomCollector(Set<Movie> movies) {
        movies.stream()
                .collect(
                        Collectors.groupingBy(
                                movie -> movie.getReleaseDate().getYear(),
                                Collector.of(LongAdder::new,
                                        (adder, movie) -> movie.getGenres().stream()
                                                .filter(genre -> "Science Fiction".equals(genre.getName()))
                                                .findAny()
                                                .ifPresent(e -> adder.increment()),
                                        (a, b) -> {
                                            a.add(b.longValue());
                                            return a;
                                        })))
                .entrySet().stream()
                .max(
                        Map.Entry.comparingByValue(
                                Comparator.comparingLong(LongAdder::longValue)
                        )
                )
                .ifPresent(System.out::println);
    }

    /**
     * Exibe o numero de vezes que cada caracter aparece no arquivo
     * a - 1 vez
     * b - 10 vezes
     */
    public static void countHowManyTimesEachCharacterAppears(Stream<String> lines) {
        Pattern p = Pattern.compile("[^a-zA-Z0-9]");

        lines.parallel()
                .flatMap(letter -> Pattern.compile("").splitAsStream(letter))
                .filter(letter -> !letter.isEmpty() && !p.matcher(letter).find())
                .collect(
                        Collectors.groupingBy(
                                Function.identity(), Collectors.counting())
                )
                .entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .forEach(System.out::println);
    }

    public static void main(String[] args) {
        try {
            Stream<String> lines = readLines();

            Set<Movie> movies = parseMovies(lines);

            System.out.println("\nyearWithMoreMovies");
            yearWithMoreMovies(movies);
            System.out.println("\nmovieWithBiggerVoteAverage");
            movieWithBiggerVoteAverage(movies);
            System.out.println("\nyearWithMoreMoviesOfScienceFiction");
            yearWithMoreMoviesOfScienceFiction(movies);
            System.out.println("\nyearWithMoreMoviesOfScienceFictionCustomCollector");
            yearWithMoreMoviesOfScienceFictionCustomCollector(movies);

            lines = readLines();

            countHowManyTimesEachCharacterAppears(lines);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Retorna o stream das linhas do arquivo de dados
     */
    private static Stream<String> readLines() {
        String fileName = "files/5000_movies.csv";
        ClassLoader classLoader = ParallelStreams.class.getClassLoader();

        InputStream resource = classLoader.getResourceAsStream(fileName);
        InputStreamReader inputStreamReader = new InputStreamReader(resource, StandardCharsets.UTF_8);

        return new BufferedReader(inputStreamReader).lines();
    }

    /**
     * Realiza o parse das linhas para os objetos utilizando a lib jackson para converter o json com os generos
     */
    private static Set<Movie> parseMovies(Stream<String> lines) {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeFactory typeFactory = objectMapper.getTypeFactory();

        return lines.skip(1)
                .parallel()
                .map(line -> {
                    String[] elements = line.split("\t");

                    Set<Genre> genres = Collections.emptySet();
                    try {
                        genres = objectMapper.readValue(elements[1], typeFactory.constructCollectionType(Set.class, Genre.class));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    return new Movie(elements[17],
                            LocalDate.parse(elements[11]),
                            Integer.valueOf(elements[18]),
                            Integer.valueOf(elements[19]),
                            genres
                    );

                }).collect(Collectors.toSet());
    }
}
