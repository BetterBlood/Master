package ch.heigvd.cld.lab;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


@WebServlet(name = "DatastoreWrite", value = "/datastorewrite")
public class DatastoreWrite extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/plain");
        PrintWriter pw = resp.getWriter();

        // Extracting field-value pairs from the query part of the URI
        String kind = req.getParameter("_kind");
        if (kind == null || kind.isEmpty()) {
            pw.println("Error: _kind parameter is missing.");
            return;
        }

        String keyName = req.getParameter("_key");

        // Creating a new entity with the specified kind and key
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Entity entity;
        if (keyName != null && !keyName.isEmpty()) {
            entity = new Entity(kind, keyName);
        } else {
            // will create a new entity with a generated key
            entity = new Entity(kind);
        }

        // Setting properties based on specific field-value pairs
        String author = req.getParameter("author");
        String title = req.getParameter("title");
        if (author != null && !author.isEmpty()) {
            entity.setProperty("author", author);
        }
        if (title != null && !title.isEmpty()) {
            entity.setProperty("title", title);
        }

        // Writing the entity to the datastore
        datastore.put(entity);

        pw.println("Entity written to datastore successfully.");

    }
}
