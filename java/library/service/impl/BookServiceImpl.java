package library.service.impl;

import java.util.Formatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import library.dao.BookDAO;
import library.domain.Author;
import library.domain.Book;
import library.service.BookService;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookDAO bookDAO;

    @Override
    @Transactional
    public void insertBook(Book book) {
        bookDAO.insert(book);
    }

    @Override
    @Transactional
    public void removeBook(Book book) {
        bookDAO.remove(book);
        book.getAuthor().getBooks().remove(book);
    }

    @Override
    @Transactional
    public Book getById(long bookId) {
        return bookDAO.getById(bookId);
    }
  
    @Override
    @Transactional
    public Book getBook(String title) {
        return bookDAO.getByTitle(title);
    }

    @Override
    @Transactional
    public List<Book> getAuthoredBooks(Author author) {
        return bookDAO.getByAuthor(author);
    }

    @Override
    @Transactional
    public List<Book> getAllBooks() {
        return bookDAO.getAll();
    }

    @Override
    @Transactional
    public String getBooksAsJson(List<Book> books) {
        String jsonResponse = "[ ";
        for (int i = 0; i < books.size(); i++) {
            Book book = books.get(i);
            jsonResponse += getBookAsJson(book);
            if (i < (books.size() - 1)) {
                jsonResponse += ", ";
            }
        }
        jsonResponse += " ]";
        return jsonResponse;
    }
    
    @Override
    public String getBookAsJson(Book book) {
        StringBuilder json = new StringBuilder();
        Formatter fmt = new Formatter();
        fmt.format("{\"id\":%d,\"title\":\"%s\",\"author\":\"%s\"}",
                book.getId(),
                book.getTitle(),
                book.getAuthor().getName());
        json.append(fmt);
        return json.toString();
    }

}
