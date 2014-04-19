package library.service.impl;

import java.util.List;
import library.dao.AuthorDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import library.dao.BookDAO;
import library.domain.Author;
import library.domain.Book;
import library.domain.dto.BookDTO;
import library.exception.AuthorNotFoundException;
import library.exception.BookNotFoundException;
import library.exception.DuplicateException;
import library.exception.UnknownAuthorException;
import library.service.AuthorService;
import library.service.BookService;

/**
 * Реализация сервиса книг.
 */
@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookDAO bookDAO;
    
    @Autowired
    private AuthorDAO authorDAO;
    
    @Autowired
    private AuthorService authorService;

    //------------------------------------------------------------------ Чтение

    @Transactional(readOnly = true)
    @Override
    public List<Book> getAll() {
        return bookDAO.getAll();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Book> getByAuthor(long authorId) throws AuthorNotFoundException {
        Author author = authorService.getById(authorId);
        return bookDAO.getByAuthor(author);
    }

    @Transactional(readOnly = true)
    @Override
    public Book getById(long bookId) throws BookNotFoundException {
        Book book = bookDAO.getById(bookId);
        if (book == null) {
            throw new BookNotFoundException("No book found with id: " + bookId);
        }
        return book;
    }
  
    @Transactional(readOnly = true)
    @Override
    public Book getByTitle(String title) throws BookNotFoundException {
        Book book = bookDAO.getByTitle(title);
        if (book == null) {
            throw new BookNotFoundException("No book found with title: " + title);
        }
        return book;
    }
    
    //--------------------------------------------------------------- Изменение
    
    @Transactional(rollbackFor = {DuplicateException.class, UnknownAuthorException.class, AuthorNotFoundException.class})
    @Override
    public Book insert(BookDTO dto) throws DuplicateException, UnknownAuthorException {
        Author author = getAuthorIfExists(dto.getAuthor());
        checkIfBookDuplicates(dto.getTitle(), author);
        Book book = new Book(author, dto.getTitle());
        return bookDAO.insert(book);
    }

    private Author getAuthorIfExists(String name) throws UnknownAuthorException {
        try {
            return authorService.getByName(name);
        } catch (AuthorNotFoundException ex) {
            throw new UnknownAuthorException(ex.getMessage());
        }
    }
    
    private void checkIfBookDuplicates(String checkedTitle, Author author) throws DuplicateException {
        List<Book> authoredBooks = bookDAO.getByAuthor(author);
        for (Book book : authoredBooks) {
            if (book.getTitle().equals(checkedTitle)) {
                throw new DuplicateException("Duplicated book by author " + author.getName() + " with title: " + checkedTitle);
            }
        }
    }

    @Transactional(rollbackFor = {BookNotFoundException.class})
    @Override
    public Book remove(long bookId) throws BookNotFoundException {
        Book book = getById(bookId);
        return bookDAO.remove(book);
    }
}
