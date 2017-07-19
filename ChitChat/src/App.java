import org.apache.http.client.fluent.Request;

import org.apache.http.client.utils.URIBuilder;

import java.io.IOException;
import java.net.URI;

/**
 * Hello ChitChat!
 */
public class App {
    public static void main(String[] args) {
        try {
            String users = Request.Get("http://chitchat.andrej.com")
                                  .execute()
                                  .returnContent().asString();
            System.out.println(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
   
}