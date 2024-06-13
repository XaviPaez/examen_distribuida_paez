package com.distribuida.services;

import com.distribuida.db.Book;

import java.util.List;

public interface BookService {

    void guardar(Book b);
    Book buscarPorId(Integer id);
    List<Book> buscarTodos();
    void actualizar(Book b);
    void eliminar(Integer id);

}
