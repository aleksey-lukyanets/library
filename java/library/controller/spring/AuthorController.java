package library.controller.spring;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import library.service.AuthorService;
import library.service.CountryService;
import library.domain.Author;
import library.domain.Country;
import library.domain.adapter.AuthorAdapter;

/**
 * Контроллер управления авторами.
 */
@Controller
@RequestMapping(value = "/authors")
public class AuthorController {
    
    @Autowired
    AuthorService authorService;
    @Autowired
    CountryService countryService;

    @RequestMapping(method = RequestMethod.GET,
            produces = "application/json; charset=utf-8")
    public @ResponseBody String getAuthors() {
        List<Author> authors = authorService.getAllAuthors();
        return authorService.getAuthorsAsJson(authors);
    }
    
    @RequestMapping(method = RequestMethod.POST,
            consumes = "application/json",
            produces = "application/json; charset=utf-8")
    public @ResponseBody String addAuthor(@RequestBody AuthorAdapter adapter) {
        String jsonResponse = "{\"status\": ";
        try {
            Country country = countryService.getCountryById(adapter.getCountryId());
            Author author = new Author(country, adapter.getName());
            authorService.insertAuthor(author);
            jsonResponse += "\"ok\", \"response\":" + authorService.getAuthorAsJson(author);
        } catch (Exception ex) {
            jsonResponse += "\"error\"";
        }
        jsonResponse += "}";
        return jsonResponse;
    }

    @RequestMapping(method = RequestMethod.DELETE,
            value = "/{authorId}",
            consumes = "application/json")
    public ResponseEntity<String> removeAuthor(@PathVariable long authorId) {
        Author author = authorService.getById(authorId);
        authorService.removeAuthor(author);
        return new ResponseEntity<>(new HttpHeaders(), HttpStatus.OK);
    }

}
