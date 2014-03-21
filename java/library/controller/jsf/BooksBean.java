package library.controller.jsf;

import java.util.List;
import library.service.AuthorService;
import library.service.BookService;
import library.domain.Author;
import library.domain.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Контроллер книг.
 */
@Component
public class BooksBean {
    
    @Autowired
    private BookService bookService;
    @Autowired
    private AuthorService authorService;
    @Autowired
    private AuthorsBean authorsBean;
    
    private long selectedBookId = 0;
    
    private long bookAuthorId = 0;
    private String newBookTitle = "";
    
    //---------------------------------------------- Методы добавления/удаления
    
    public List<Book> getBooksList() {
        long selectedAuthorId = authorsBean.getSelectedAuthorId();
        if (selectedAuthorId != 0) {
            return bookService.getByAuthor(selectedAuthorId);
        }
        return bookService.getAll();
    }
    
    public void addBook() {
        Author author = authorService.getById(bookAuthorId);
        Book book = new Book(author, newBookTitle);
        bookService.insert(book);
        clearBookSelected();
    }
    
    public void removeBook() {
        if (selectedBookId != 0) {
            bookService.remove(selectedBookId);
            clearBookSelected();
        }
    }
    
    public void clearBookSelected() {
        selectedBookId = 0;
    }
    
    //-------------------------------------------- Аксессоры/мутаторы для полей

    public long getSelectedBookId() {
        return selectedBookId;
    }

    public void setSelectedBookId(long selectedBookId) {
        this.selectedBookId = selectedBookId;
    }

    public long getBookAuthorId() {
        return bookAuthorId;
    }

    public void setBookAuthorId(long bookAuthorId) {
        this.bookAuthorId = bookAuthorId;
    }

    public String getNewBookTitle() {
        return newBookTitle;
    }

    public void setNewBookTitle(String newBookTitle) {
        this.newBookTitle = newBookTitle;
    }
}
