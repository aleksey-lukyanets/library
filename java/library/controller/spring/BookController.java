package library.controller.spring;

import java.util.List;
import library.service.AuthorService;
import library.service.BookService;
import library.service.CountryService;
import library.domain.Author;
import library.domain.Book;
import library.domain.adapter.BookAdapter;
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

/**
 * Контроллер управления книгами.
 */
@Controller
@RequestMapping(value = "/books")
public class BookController {

    @Autowired
    private AuthorService authorService;
    @Autowired
    private BookService bookService;
    @Autowired
    private CountryService countryService;

    @RequestMapping(method = RequestMethod.GET,
            produces = "application/json; charset=utf-8")
    public @ResponseBody String getBooks() {
        List<Book> books = bookService.getAll();
        return bookService.getBooksAsJson(books);
    }

    @RequestMapping(value = "/authored/{authorId}",
            method = RequestMethod.GET,
            produces = "application/json; charset=utf-8")
    public @ResponseBody String getBooksByAuthor(@PathVariable long authorId) {
        String jsonResponse = "{\"status\": ";
        try {
            List<Book> books = bookService.getByAuthor(authorId);
            jsonResponse += "\"ok\", \"response\":" + bookService.getBooksAsJson(books);
        } catch (Exception ex) {
            jsonResponse += "\"error\"";
        }
        jsonResponse += "}";
        return jsonResponse;
    }

    @RequestMapping(method = RequestMethod.POST,
            consumes = "application/json",
            produces = "application/json; charset=utf-8")
    public @ResponseBody String addBook(@RequestBody BookAdapter adapter) {
        String jsonResponse = "{\"status\": ";
        try {
            Author author = authorService.getById(adapter.getAuthorId());
            Book book = new Book(author, adapter.getTitle());
            bookService.insert(book);
            jsonResponse += "\"ok\", \"response\":" + bookService.getBookAsJson(book);
        } catch (Exception ex) {
            jsonResponse += "\"error\"";
        }
        jsonResponse += "}";
        return jsonResponse;
    }

    @RequestMapping(method = RequestMethod.DELETE,
            value = "/{bookId}",
            consumes = "application/json")
    public ResponseEntity<String> removeBook(@PathVariable long bookId) {
        bookService.remove(bookId);
        return new ResponseEntity<>(new HttpHeaders(), HttpStatus.OK);
    }
}
