import static spark.Spark.*;
import spark.*;
import person.PersonController;
import util.Path;

public class Application {
    public Application() {
        port(4567);

        get(Path.FETCH_ALL_PERSONS_URL, PersonController.fetchAllPersons);
        get("/test", (Request req, Response res) -> "hello!!");
        get(Path.FETCH_PERSON_BY_NAME_URL, PersonController.fetchPersonsByName);
        put(Path.UPDATE_PERSON_URL, PersonController.updatePerson);
        post(Path.CREATE_PERSON_URL, PersonController.createPerson);
        delete(Path.DELETE_PERSON_URL, PersonController.deletePerson);
    }
}