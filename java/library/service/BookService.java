package library.service;

import java.util.List;
import library.domain.Author;
import library.domain.Book;

public interface BookService {

    void insertBook(Book book);

    void removeBook(Book book);

    Book getById(long bookId);
    
    Book getBook(String title);
    
    List<Book> getAuthoredBooks(Author author);

    List<Book> getAllBooks();

    String getBooksAsJson(List<Book> books);
    
    String getBookAsJson(Book book);
}
