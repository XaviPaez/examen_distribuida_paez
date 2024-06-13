package com.distribuida.services;

import com.distribuida.db.Book;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import java.util.List;

@ApplicationScoped
public class BookServiceImpl implements BookService{

    @Inject
    EntityManager em;

    @Override
    public void guardar(Book b) {
        em.getTransaction().begin();
        em.persist(b);
        em.getTransaction().commit();
    }


    @Override
    public Book buscarPorId(Integer id) {
        return em.find(Book.class,id);
    }

    @Override
    public List<Book> buscarTodos() {
        return em
                .createQuery("select b from Book b ", Book.class).getResultList();
    }

    @Override
    public void actualizar(Book b) {
        em.getTransaction().begin();
        em.merge(b);
        em.getTransaction().commit();
    }

    @Override
    public void eliminar(Integer id) {
        Book b = this.buscarPorId(id);
        em.getTransaction().begin();
        em.remove(b);
        em.getTransaction().commit();
    }

}
