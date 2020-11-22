package person;

import spark.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;

public class PersonController {
    private static ArrayList<Person> persons = new ArrayList<>(Arrays.asList(new Person[] {new Person("Oscar", "Larsen", "5484848"), new Person("Sune", "Sørensen", "8548488"), new Person("Jesper", "Bertelsen", "8481255"), new Person("Thomas", "Nielsen", "58547456")}));
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static Route fetchAllPersons = (Request req, Response res) -> GSON.toJson(persons);

    public static Route fetchPersonsByName = (Request req, Response res) -> {
        String name = req.params(":name");
        Person[] foundPerson = persons.stream().filter(person -> person.getfName().equalsIgnoreCase(name)).toArray(Person[]::new);
        return foundPerson.length > 0 ? GSON.toJson(foundPerson) : "Person not found";
    };
    public static Route updatePerson = (Request req, Response res) -> {
        Person personToUpdate = GSON.fromJson(req.body(), Person.class);
        persons = new ArrayList<>(Arrays.asList(persons.stream().map(person -> person.getfName().equalsIgnoreCase(personToUpdate.getfName()) ? personToUpdate : person).toArray(Person[]::new)));
        return GSON.toJson(personToUpdate);
    };
    public static Route createPerson = (Request req, Response res) -> {
        Person person = GSON.fromJson(req.body(), Person.class);
        String header = req.headers("login");
        if(header.equalsIgnoreCase("true")){
            persons.add(person);
            res.status(201);
            return GSON.toJson(person);
        } else {
            return "you need to login";
        }
    };
    public static Route deletePerson = (Request req, Response res) -> {
        Person personToDelete = GSON.fromJson(req.body(), Person.class);
        Person[] foundPerson = persons.stream().filter(person -> person.getfName().equalsIgnoreCase(personToDelete.getfName())).toArray(Person[]::new);
        Arrays.stream(foundPerson).forEach(person -> persons.remove(person));
        return GSON.toJson(personToDelete);
    };


}
