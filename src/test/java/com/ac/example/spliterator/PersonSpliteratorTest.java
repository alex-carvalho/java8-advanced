package com.ac.example.spliterator;

import com.ac.example.spliterator.model.Person;
import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

/**
 * @author Alex Carvalho
 */
public class PersonSpliteratorTest {


    @Test
    public void testTotalSixPeople() {
        Stream<String> lines = readLines();

        Stream<Person> people = PersonSpliterator.toPeopleStream(lines);

        Assert.assertEquals(6, people.count());

        lines.close();
        people.close();
    }

    @Test
    public void testNameFirstPeople() {
        Stream<String> lines = readLines();

        Stream<Person> people = PersonSpliterator.toPeopleStream(lines);

        Assert.assertEquals("Alice", people.findFirst().get().getName());

        lines.close();
        people.close();
    }


    private Stream<String> readLines() {
        String fileName = "files/people.txt";
        ClassLoader classLoader = PersonSpliteratorTest.class.getClassLoader();

        InputStream resource = classLoader.getResourceAsStream(fileName);
        InputStreamReader inputStreamReader = new InputStreamReader(resource, StandardCharsets.UTF_8);

        return new BufferedReader(inputStreamReader).lines();
    }
}
