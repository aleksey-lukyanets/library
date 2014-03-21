package library.service;

import java.util.List;
import library.domain.Book;

/**
 * Сервис управления книгами.
 */
public interface BookService {

    void insert(Book book);

    void remove(long bookId);

    Book getById(long bookId);
    
    Book getByTitle(String title);
    
    List<Book> getByAuthor(long authorId);

    List<Book> getAll();

    String getBooksAsJson(List<Book> books);
    
    String getBookAsJson(Book book);
}
