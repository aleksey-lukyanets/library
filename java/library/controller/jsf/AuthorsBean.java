package library.controller.jsf;

import java.util.List;
import library.service.AuthorService;
import library.service.CountryService;
import library.domain.Author;
import library.domain.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Контроллер авторов.
 */
@Component
public class AuthorsBean {
    
    @Autowired
    private AuthorService authorService;
    @Autowired
    private CountryService countryService;
    @Autowired
    private BooksBean booksBean;
    
    private long selectedAuthorId = 0;
    
    private long authorCountryId = 0;
    private String newAuthorName = "";
    
    //---------------------------------------------- Методы добавления/удаления

    public List<Author> getAuthorsList() {
        return authorService.getAll();
    }

    public void addAuthor() {
        Country country = countryService.getById(authorCountryId);
        Author author = new Author(country, newAuthorName);
        authorService.insert(author);
        clearAuthorSelected();
    }
    
    public void removeAuthor() {
        if (selectedAuthorId != 0) {
            authorService.remove(selectedAuthorId);
            clearAuthorSelected();
        }
    }
    
    public void clearAuthorSelected() {
        selectedAuthorId = 0;
        booksBean.clearBookSelected();
    }
    
    //-------------------------------------------- Аксессоры/мутаторы для полей

    public long getSelectedAuthorId() {
        return selectedAuthorId;
    }

    public void setSelectedAuthorId(long selectedAuthorId) {
        this.selectedAuthorId = selectedAuthorId;
    }

    public long getAuthorCountryId() {
        return authorCountryId;
    }

    public void setAuthorCountryId(long authorCountryId) {
        this.authorCountryId = authorCountryId;
    }

    public String getNewAuthorName() {
        return newAuthorName;
    }

    public void setNewAuthorName(String newAuthorName) {
        this.newAuthorName = newAuthorName;
    }
}
