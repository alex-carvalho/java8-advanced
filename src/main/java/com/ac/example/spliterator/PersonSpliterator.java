package com.ac.example.spliterator;

import com.ac.example.spliterator.model.Person;

import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @author Alex Cavalho
 */
public class PersonSpliterator implements Spliterator<Person> {
    private final Spliterator<String> lineSpliterator;
    private String name;
    private int age;
    private String city;

    PersonSpliterator(Spliterator<String> lineSpliterator) {
        this.lineSpliterator = lineSpliterator;
    }

    public static Stream<Person> toPeopleStream(Stream<String> lines) {
        Spliterator<String> lineSpliterator = lines.spliterator();

        Spliterator<Person> peopleSpliterator = new PersonSpliterator(lineSpliterator);

        return StreamSupport.stream(peopleSpliterator, false);
    }

    @Override
    public boolean tryAdvance(Consumer<? super Person> action) {
        Person person = new Person();
        if (this.lineSpliterator.tryAdvance(person::setName)
                && this.lineSpliterator.tryAdvance(person::setAgeAndParse)
                && this.lineSpliterator.tryAdvance(person::setCity)) {

            action.accept(person);
            return true;
        }
        return false;
    }

    @Override
    public Spliterator<Person> trySplit() {
        return null;
    }

    @Override
    public long estimateSize() {
        return lineSpliterator.estimateSize() / 3;
    }

    @Override
    public int characteristics() {
        return lineSpliterator.characteristics();
    }
}
