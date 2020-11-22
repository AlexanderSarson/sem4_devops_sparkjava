import static org.junit.Assert.*;
import com.despegar.http.client.GetMethod;
import com.despegar.http.client.HttpResponse;
import com.despegar.http.client.PostMethod;
import com.despegar.sparkjava.test.SparkServer;
import com.google.gson.GsonBuilder;
import person.Person;
import spark.servlet.SparkApplication;
import org.junit.*;
import com.google.gson.Gson;
import util.Path;

public class ApplicationTest {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static class AppTestSparkApp implements SparkApplication {
        @Override
        public void init() {
            new Application();
        }
    }

    @ClassRule
    public static SparkServer<AppTestSparkApp> testServer = new SparkServer<>(ApplicationTest.AppTestSparkApp.class, 4567);

    @Test
    public void getAllPersons() throws Exception {
        /* The second parameter indicates whether redirects must be followed or not */
        GetMethod get = testServer.get(Path.FETCH_ALL_PERSONS_URL, false);
        HttpResponse httpResponse = testServer.execute(get);
        String response = new String(httpResponse.body());
        Person[] persons = GSON.fromJson(response, Person[].class);

        assertEquals(4, persons.length);
        assertEquals(200, httpResponse.code());
        assertNotNull(testServer.getApplication());
    }

    @Test
    public void getPersonsByName() throws Exception {
        /* The second parameter indicates whether redirects must be followed or not */
        GetMethod get = testServer.get("/api/person/Oscar", false);
        HttpResponse httpResponse = testServer.execute(get);
        String response = new String(httpResponse.body());
        Person[] persons = GSON.fromJson(response, Person[].class);

        assertEquals("Oscar", persons[0].getfName());
        assertEquals(200, httpResponse.code());
        assertNotNull(testServer.getApplication());
    }

    @Test
    public void createPerson() throws Exception {
        /* The second parameter indicates whether redirects must be followed or not */
        Person person = new Person("test", "test","test");
        PostMethod post = testServer.post("/api/person",GSON.toJson(person) ,false);
        post.addHeader("login", "true");
        HttpResponse httpResponse = testServer.execute(post);
        String response = new String(httpResponse.body());
        Person res = GSON.fromJson(response, Person.class);

        assertEquals("test", res.getfName());
        assertEquals(201, httpResponse.code());
        assertNotNull(testServer.getApplication());
    }

}