package library.controller;

import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import library.service.BookService;
import library.domain.Book;
import library.domain.dto.BookDTO;
import library.exception.AuthorNotFoundException;
import library.exception.BookNotFoundException;
import library.exception.DuplicateException;
import library.exception.UnknownAuthorException;
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
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Контроллер книг.
 */
@Controller
@RequestMapping(value = "/books")
public class BookController {

    private final BookService bookService;
    
    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * Перечень всех книг.
     * 
     * @return перечень книг
     */
    @RequestMapping(method = RequestMethod.GET,
            produces = MediaTypesUtf8.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public List<BookDTO> getBooks() {
        return createBookDtoList(bookService.getAll());
    }

    private List<BookDTO> createBookDtoList(List<Book> books) {
        List<BookDTO> dtos = new ArrayList<>();
        for (Book book : books) {
            dtos.add(new BookDTO(book));
        }
        return dtos;
    }

    /**
     * Одна книга.
     * 
     * @param bookId идентификатор книги
     * @return книга с указанным id
     * @throws BookNotFoundException если книга с таким id не существует
     */
    @RequestMapping(value = "/{bookId}",
            method = RequestMethod.GET,
            produces = MediaTypesUtf8.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BookDTO getBook(@PathVariable long bookId) throws BookNotFoundException {
        Book book = bookService.getById(bookId);
        return new BookDTO(book);
    }

    /**
     * Добавление книги.
     * 
     * @param bookToAdd новая книга
     * @param builder сборщик URI
     * @return созданная книга
     * @throws DuplicateException если книга с таким названием у автора уже существует
     * @throws UnknownAuthorException если указанного автора не существует
     */
    @RequestMapping(method = RequestMethod.POST,
            consumes = MediaTypesUtf8.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaTypesUtf8.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<BookDTO> addBook(@Valid @RequestBody BookDTO bookToAdd, UriComponentsBuilder builder) throws DuplicateException, UnknownAuthorException {
        Book book = bookService.insert(bookToAdd);
        BookDTO dto = new BookDTO(book);
        
        UriComponents uriComponents = builder.path("/books/{id}").buildAndExpand(dto.getId());
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(uriComponents.toUri());
        
        return new ResponseEntity<>(dto, headers, HttpStatus.CREATED);
    }

    /**
     * Удаление книги.
     * 
     * @param bookId идентификатор книги
     * @return удалённая книга
     * @throws BookNotFoundException если книга с таким id не существует
     */
    @RequestMapping(method = RequestMethod.DELETE,
            value = "/{bookId}",
            produces = MediaTypesUtf8.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BookDTO removeBook(@PathVariable long bookId) throws BookNotFoundException {
        Book book = bookService.remove(bookId);
        return new BookDTO(book);
    }
}
