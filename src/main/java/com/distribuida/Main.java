package com.distribuida;

import com.distribuida.db.Book;
import com.distribuida.services.BookService;
import io.helidon.webserver.WebServer;
import jakarta.enterprise.inject.spi.CDI;
import org.apache.webbeans.config.WebBeansContext;
import org.apache.webbeans.spi.ContainerLifecycle;
import com.google.gson.Gson;
import java.math.BigDecimal;
import java.util.List;


public class Main {
    private static ContainerLifecycle lifecycle = null;

    public static void main(String[] args)  {

        lifecycle = WebBeansContext.currentInstance().getService(ContainerLifecycle.class);
        lifecycle.startApplication(null);

        BookService bookService = CDI.current().select(BookService.class).get();

        Book b = new Book();
        b.setId(1);
        b.setAuthor("Marques");
        b.setIsbn("01");
        b.setPrice(new BigDecimal(5));
        b.setTitle("libro");

        bookService.guardar(b);


        WebServer server = WebServer.builder()
                .port(8080)
                .routing(builder -> builder
                        .post("/books", (req, res) -> {
                            Gson gson=new Gson();
                            String body = req.content().as(String.class);
                            Book book = gson.fromJson(body, Book.class);
                            bookService.guardar(book);
                            res.send("Insertado");

                        })
                        .get("/books/{id}", (req, res) -> {
                            int id = Integer.parseInt(req.path().pathParameters().get("id"));
                            Book book = bookService.buscarPorId(id);
                            res.send(new Gson().toJson(book));

                        })
                        .get("/books", (req, res) -> {
                            List<Book> books = bookService.buscarTodos();
                            res.send(new Gson().toJson(books));
                        })

                        .put("/books/{id}",(req, res) -> {
                            Gson gson=new Gson();
                            String body = req.content().as(String.class);
                            Book book = gson.fromJson(body, Book.class);
                            Integer id = Integer.valueOf(req.path().pathParameters().get("id"));
                            book.setId(id);
                            bookService.actualizar(book);
                            res.send("Actualizado");
                        })

                        .delete("/books/{id}", (req, res) -> {
                            int id = Integer.parseInt(req.path().pathParameters().get("id"));
                            bookService.eliminar(id);
                            res.send("Eliminado");

                        }))
                .build();

        server.start();


    }
    public static void shutdown() {
        lifecycle.stopApplication(null);
    }
}