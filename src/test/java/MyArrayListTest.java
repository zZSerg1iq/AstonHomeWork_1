import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.Objects;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;


class MyArrayListTest {

    private Random random = new Random();
    private String testStr = "Безумно, можно быть первым Безумно, можно через стены Я шерстяной волчара, боже, как я хорош, как мощны мои лапищи Редко когда думаю, а еще реже думаю АУФ";


    private static class Person implements Comparable<Person>{

        @Override
        public int compareTo(@NotNull Person o) {
            int nameCompare = this.getName().compareTo(o.getName());
            if (nameCompare == 0){
                return Integer.compare(this.getAge(), o.getAge());
            }
            return nameCompare;
        }

        private final String name;
        private final int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }
    }

    @Test
    void add() {
        MyList<String> myStringList = new MyArrayList<>();
        assertEquals(0, myStringList.size());
        for (int i = 0; i < 10; i++) {
            myStringList.add(getRandomString());
        }
        assertEquals(10, myStringList.size());
    }

    @Test
    void addByIndex() {
        MyList<String> myStringList = new MyArrayList<>();
        assertEquals(0, myStringList.size());
        for (int i = 0; i < 10; i++) {
            myStringList.add(getRandomString());
        }
        assertEquals(10, myStringList.size());

        for (int i = 0; i < 10; i++) {
            myStringList.add(5, "index 5");
        }
        assertEquals(20, myStringList.size());
        for (int i = 5; i < 15; i++) {
            assertEquals(myStringList.get(i), "index 5");
        }
    }

    @Test
    void get() {
        MyList<String> myStringList = new MyArrayList<>();
        assertEquals(0, myStringList.size());
        for (int i = 0; i < 10; i++) {
            myStringList.add(getRandomString());
        }
        assertEquals(10, myStringList.size());

        for (int i = 0; i < myStringList.size(); i++) {
            assertNotNull(myStringList.get(i));
            assertTrue(myStringList.get(i).length() > 0);
        }
    }

    @Test
    void removes() {
        MyList<Person> personList = new MyArrayList<>();
        Person boy = new Person("Школоло", 12);
        Person oldMan = new Person("Старец", 100);
        Person dunkan = new Person("Дункан", 564);

        for (int i = 0; i < 10; i++) {
            personList.add(new Person(getRandomString(), random.nextInt(100)));
        }
        assertEquals(10, personList.size());

        personList.add(boy);
        personList.add(5, oldMan);
        personList.add(7, dunkan);

        assertTrue(personList.contains(boy));
        assertTrue(personList.contains(oldMan));
        assertTrue(personList.contains(dunkan));

        personList.remove(personList.size()-1);
        personList.remove(7);
        personList.remove(5);

        assertFalse(personList.contains(boy));
        assertFalse(personList.contains(oldMan));
        assertFalse(personList.contains(dunkan));


        personList.add(2, boy);
        personList.add(6, oldMan);
        personList.add(8, dunkan);

        assertTrue(personList.contains(boy));
        assertTrue(personList.contains(oldMan));
        assertTrue(personList.contains(dunkan));

        personList.remove(oldMan);
        personList.remove(boy);
        personList.remove(dunkan);

        assertFalse(personList.contains(boy));
        assertFalse(personList.contains(oldMan));
        assertFalse(personList.contains(dunkan));
    }

    @Test
    void replace(){
        MyList<Person> personList = new MyArrayList<>();
        Person boy = new Person("Школоло", 12);
        Person oldMan = new Person("Старец", 100);
        Person dunkan = new Person("Дункан", 564);

        personList.add(boy);
        personList.add(oldMan);
        personList.add(dunkan);

        assertTrue(personList.contains(boy));
        assertTrue(personList.contains(oldMan));
        assertTrue(personList.contains(dunkan));

        var randomPerson = new Person(getRandomString(), random.nextInt(100));
        personList.replace(1, randomPerson);
        assertFalse(personList.contains(oldMan));
        assertTrue(personList.contains(randomPerson));
    }

    @Test
    void clear() {
        MyList<Integer> integerMyList = new MyArrayList<>();
        for (int i = 0; i < 100; i++) {
            integerMyList.add(i);
        }

        for (int i = 0; i < 50; i++) {
            integerMyList.remove(i);
        }
        assertEquals(50, integerMyList.size());

        integerMyList.clear();
        assertEquals(0, integerMyList.size());
    }

    @Test
    void sort() {
        //integer sort
        MyList<Integer> integerMyList = new MyArrayList<>();
        integerMyList.add(10);
        integerMyList.add(6);
        integerMyList.add(1);
        integerMyList.add(8);
        integerMyList.add(3);
        integerMyList.add(2);
        integerMyList.add(5);
        integerMyList.add(0);
        integerMyList.add(7);
        integerMyList.add(9);
        integerMyList.add(4);

        integerMyList.sort();

        for (int i = 0; i < integerMyList.size(); i++) {
            assertEquals(i, integerMyList.get(i));
        }


        //classes comparator sort
        MyList<Person> persons = new MyArrayList<>(Comparator.comparing(Person::getName));
        persons.add(new Person("r", 1));
        persons.add(new Person("f", 1));
        persons.add(new Person("b", 1));
        persons.add(new Person("c", 1));
        persons.add(new Person("a", 1));
        persons.add(new Person("e", 1));
        persons.add(new Person("g", 1));
        persons.add(new Person("d", 1));
        persons.sort();

        assertEquals("a", persons.get(0).name);
        assertEquals("b", persons.get(1).name);
        assertEquals("c", persons.get(2).name);
        assertEquals("d", persons.get(3).name);
        assertEquals("e", persons.get(4).name);
        assertEquals("f", persons.get(5).name);
        assertEquals("g", persons.get(6).name);
        assertEquals("r", persons.get(7).name);





        //classes hash sort
        MyList<Person> personsHash = new MyArrayList<>();
        personsHash.add(new Person("r", 1));
        personsHash.add(new Person("f", 1));
        personsHash.add(new Person("b", 1));
        personsHash.add(new Person("c", 1));
        personsHash.add(new Person("a", 1));
        personsHash.add(new Person("e", 1));
        personsHash.add(new Person("g", 1));
        personsHash.add(new Person("d", 1));

        persons.sort();

        assertEquals("a", persons.get(0).name);
        assertEquals("b", persons.get(1).name);
        assertEquals("c", persons.get(2).name);
        assertEquals("d", persons.get(3).name);
        assertEquals("e", persons.get(4).name);
        assertEquals("f", persons.get(5).name);
        assertEquals("g", persons.get(6).name);
        assertEquals("r", persons.get(7).name);
    }


    private String getRandomString(){
        int start = random.nextInt(testStr.length());
        int end = random.nextInt(testStr.length());

        if (start >= end){
            return getRandomString();
        }

        return testStr.substring(start, end);
    }
}