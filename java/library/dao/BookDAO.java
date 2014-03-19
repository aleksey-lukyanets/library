package library.dao;

import java.util.List;
import library.domain.Author;
import library.domain.Book;

/**
 * ДАО книги.
 */
public interface BookDAO {

    void insert(Book book);

    void remove(Book book);

    Book getById(long bookId);

    Book getByTitle(String title);
    
    List<Book> getByAuthor(Author author);

    List<Book> getAll();
}
