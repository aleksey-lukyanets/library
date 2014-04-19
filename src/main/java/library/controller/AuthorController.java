package library.controller;

import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import library.domain.Author;
import library.domain.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import library.service.AuthorService;
import library.domain.dto.AuthorDTO;
import library.domain.dto.BookDTO;
import library.exception.DuplicateException;
import library.exception.AuthorNotFoundException;
import library.exception.UnknownCountryException;
import library.service.BookService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Контроллер авторов.
 */
@Controller
@RequestMapping(value = "/authors")
public class AuthorController {

    private final AuthorService authorService;
    private final BookService bookService;
    
    @Autowired
    public AuthorController(AuthorService authorService, BookService bookService) {
        this.authorService = authorService;
        this.bookService = bookService;
    }

    /**
     * Перечень всех авторов.
     * 
     * @return перечень авторов
     */
    @RequestMapping(method = RequestMethod.GET,
            produces = MediaTypesUtf8.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public List<AuthorDTO> getAuthors() {
        return createAuthorDtoList(authorService.getAll());
    }
    
    private List<AuthorDTO> createAuthorDtoList(List<Author> authors) {
        List<AuthorDTO> dtos = new ArrayList<>();
        for (Author author : authors) {
            dtos.add(new AuthorDTO(author));
        }
        return dtos;
    }

    /**
     * Один автор.
     * 
     * @param authorId идентификатор автора
     * @return автор с указанным id
     * @throws AuthorNotFoundException если автора с таким id не существует
     */
    @RequestMapping(value = "/{authorId}",
            method = RequestMethod.GET,
            produces = MediaTypesUtf8.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public AuthorDTO getAuthor(@PathVariable long authorId) throws AuthorNotFoundException {
        Author author = authorService.getById(authorId);
        return new AuthorDTO(author);
    }

    /**
     * Перечень книг указанного автора.
     * 
     * @param authorId идентификатор автора
     * @return книги автора с указанным id
     * @throws AuthorNotFoundException если автора с таким id не существует
     */
    @RequestMapping(value = "/{authorId}/books",
            method = RequestMethod.GET,
            produces = MediaTypesUtf8.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public List<BookDTO> getBooksByAuthor(@PathVariable long authorId) throws AuthorNotFoundException {
        List<Book> list = bookService.getByAuthor(authorId);
        return createBookDtoList(list);
    }

    private List<BookDTO> createBookDtoList(List<Book> books) {
        List<BookDTO> dtos = new ArrayList<>();
        for (Book book : books) {
            dtos.add(new BookDTO(book));
        }
        return dtos;
    }

    /**
     * Добавление автора.
     * 
     * @param authorToAdd новый автор
     * @param builder сборщик URI
     * @return созданный автор
     * @throws DuplicateException если автор с таким именем уже существует
     * @throws UnknownCountryException если указанной страны не существует
     */
    @RequestMapping(method = RequestMethod.POST,
            consumes = MediaTypesUtf8.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaTypesUtf8.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AuthorDTO> addAuthor(@Valid @RequestBody AuthorDTO authorToAdd, UriComponentsBuilder builder) throws DuplicateException, UnknownCountryException {
        Author author = authorService.insert(authorToAdd);
        AuthorDTO dto =  new AuthorDTO(author);
        
        UriComponents uriComponents = builder.path("/authors/{id}").buildAndExpand(dto.getId());
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(uriComponents.toUri());
        return new ResponseEntity<>(dto, headers, HttpStatus.CREATED);
    }

    /**
     * Удаление автора.
     * 
     * @param authorId идентификатор автора
     * @return удалённый автор
     * @throws AuthorNotFoundException если автора с таким id не существует
     */
    @RequestMapping(value = "/{authorId}",
            method = RequestMethod.DELETE,
            produces = MediaTypesUtf8.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public AuthorDTO removeAuthor(@PathVariable long authorId) throws AuthorNotFoundException {
        Author author = authorService.remove(authorId);
        return new AuthorDTO(author);
    }
}
