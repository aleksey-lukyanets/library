package library.controller.jsf;

import java.io.IOException;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import library.service.AuthorService;
import library.service.BookService;
import library.service.CountryService;
import library.domain.Author;
import library.domain.Book;
import library.domain.Country;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 */
@ManagedBean
@SessionScoped
public class AuthorController {
    
    @Autowired
    AuthorService authorService;
    @Autowired
    BookService bookService;
    @Autowired
    CountryService countryService;
    
    private long selectedAuthorId = 0;
    private long selectedBookId = 0;
    
    private long authorCountryId = 0;
    private String newAuthorName = "";
    
    private long bookAuthorId = 0;
    private String newBookTitle = "";

    /**
     * Creates a new instance of AuthorController
     */
    public AuthorController() {
    }
    
    //---------------------------------------------- Методы добавления/удаления

    public void addAuthor() {
        Country country = countryService.getCountryById(authorCountryId);
        Author author = new Author(country, newAuthorName);
        authorService.insertAuthor(author);
        redirectToIndex();
    }
    
    public void removeAuthor() {
        Author author = authorService.getById(selectedAuthorId);
        authorService.removeAuthor(author);
        redirectToIndex();
    }
    
    public void addBook() {
        Author author = authorService.getById(bookAuthorId);
        Book book = new Book(author, newBookTitle);
        bookService.insertBook(book);
        redirectToIndex();
    }
    
    public void removeBook() {
        if (selectedBookId != 0) {
            Book book = bookService.getById(selectedBookId);
            bookService.removeBook(book);
        }
        redirectToIndex();
    }
    
    private void redirectToIndex() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
        } catch (IOException ex) {
        } finally {
            FacesContext.getCurrentInstance().responseComplete();
        }
    }
    
    //------------------------------------------------------- Аксессоры списков

    public List<Author> getAuthorsList() {
        return authorService.getAllAuthors();
    }
    
    public List<Book> getBooksList() {
        if (selectedAuthorId == 0) {
            return bookService.getAllBooks();
        }
        Author author = authorService.getById(selectedAuthorId);
        return bookService.getAuthoredBooks(author);
    }
    
    public List<Country> getCountriesList() {
        return countryService.getAllCountries();
    }
    
    //-------------------------------------------- Аксессоры/мутаторы для полей

    /**
     * @return the selectedAuthorId
     */
    public long getSelectedAuthorId() {
        return selectedAuthorId;
    }

    /**
     * @param selectedAuthorId the selectedAuthorId to set
     */
    public void setSelectedAuthorId(long selectedAuthorId) {
        this.selectedAuthorId = selectedAuthorId;
    }

    /**
     * @return the selectedBookId
     */
    public long getSelectedBookId() {
        return selectedBookId;
    }

    /**
     * @param selectedBookId the selectedBookId to set
     */
    public void setSelectedBookId(long selectedBookId) {
        this.selectedBookId = selectedBookId;
    }

    /**
     * @return the authorCountryId
     */
    public long getAuthorCountryId() {
        return authorCountryId;
    }

    /**
     * @param authorCountryId the authorCountryId to set
     */
    public void setAuthorCountryId(long authorCountryId) {
        this.authorCountryId = authorCountryId;
    }

    /**
     * @return the bookAuthorId
     */
    public long getBookAuthorId() {
        return bookAuthorId;
    }

    /**
     * @param bookAuthorId the bookAuthorId to set
     */
    public void setBookAuthorId(long bookAuthorId) {
        this.bookAuthorId = bookAuthorId;
    }

    /**
     * @return the newAuthorName
     */
    public String getNewAuthorName() {
        return newAuthorName;
    }

    /**
     * @param newAuthorName the newAuthorName to set
     */
    public void setNewAuthorName(String newAuthorName) {
        this.newAuthorName = newAuthorName;
    }

    /**
     * @return the newBookTitle
     */
    public String getNewBookTitle() {
        return newBookTitle;
    }

    /**
     * @param newBookTitle the newBookTitle to set
     */
    public void setNewBookTitle(String newBookTitle) {
        this.newBookTitle = newBookTitle;
    }

}
