package org.nla.mockito;

import org.nla.model.Book;

import java.util.List;

public interface BookDAL {

    String addBook(Book book);

    List<Book> getAllBooks();

    Book getBook(String isbn);

    String updateBook(Book book);
}