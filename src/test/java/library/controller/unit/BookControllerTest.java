package library.controller.unit;

import java.util.List;
import javax.annotation.Resource;
import library.data.BookTestData;
import static library.data.BookTestData.*;
import library.domain.Book;
import library.domain.dto.BookDTO;
import library.exception.DuplicateException;
import library.exception.BookNotFoundException;
import library.exception.UnknownAuthorException;
import library.service.BookService;
import library.util.TestUtil;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import static org.mockito.Matchers.any;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * Модульные тесты контроллера книг.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:unit-test-context.xml",
    "file:src/main/webapp/WEB-INF/spring/libraryServlet/servlet-context.xml"
})
@WebAppConfiguration
public class BookControllerTest {

    private MockMvc mockMvc;
    
    @Autowired
    private BookService bookServiceMock;
    
    @Resource
    private WebApplicationContext wac;
    
    @Before
    public void setUp() {
        Mockito.reset(bookServiceMock);
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    /**
     * Поиск всех книг.
     * Успех: получены статус 200 и перечень книг.
     * @throws Exception
     */
    @Test
    public void getAll() throws Exception {
        List<Book> list = BookTestData.getBooksList();

        when(bookServiceMock.getAll()).thenReturn(list);

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(list.size())))
                .andExpect(jsonPath("$[0].id", is(BOOK_ID)))
                .andExpect(jsonPath("$[0].author", is(AUTHOR_NAME)))
                .andExpect(jsonPath("$[0].title", is(BOOK_TITLE)))
                .andExpect(jsonPath("$[1].id", is(BOOK_ID)))
                .andExpect(jsonPath("$[1].author", is(AUTHOR_NAME)))
                .andExpect(jsonPath("$[1].title", is(BOOK_TITLE)));

        verify(bookServiceMock, times(1)).getAll();
        verifyNoMoreInteractions(bookServiceMock);
    }

    /**
     * Поиск одной книги. Неизвестная книга.
     * Успех: получен статус 404.
     * @throws Exception
     */
    @Test
    public void getById_NotFound() throws Exception {
        when(bookServiceMock.getById(BOOK_ID)).thenThrow(new BookNotFoundException());
        
        mockMvc.perform(get("/books/{bookId}", BOOK_ID))
                .andExpect(status().isNotFound());

        verify(bookServiceMock, times(1)).getById(BOOK_ID);
        verifyNoMoreInteractions(bookServiceMock);
    }

    /**
     * Поиск одной книги. Успешно.
     * Успех: получена запрошенная книга.
     * @throws Exception
     */
    @Test
    public void getById() throws Exception {
        Book book = BookTestData.getSingleBook();

        when(bookServiceMock.getById(BOOK_ID)).thenReturn(book);

        mockMvc.perform(get("/books/{bookId}", BOOK_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(BOOK_ID)))
                .andExpect(jsonPath("$.author", is(AUTHOR_NAME)))
                .andExpect(jsonPath("$.title", is(BOOK_TITLE)));

        verify(bookServiceMock, times(1)).getById(BOOK_ID);
        verifyNoMoreInteractions(bookServiceMock);
    }

    /**
     * Добавление книги. Ошибки валидации строк названий.
     * Успех: получены статус 422 и список нарушенных ограничений.
     * @throws Exception
     */
    @Test
    public void addBook_ValidationErrors() throws Exception {
        BookDTO wrongBook = new BookDTO(0, "", "");
        
        mockMvc.perform(post("/books")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(wrongBook))
        )
                .andExpect(status().isNotAcceptable())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.fieldErrors", hasSize(2)))
                .andExpect(jsonPath("$.fieldErrors[*].field", containsInAnyOrder("author", "title")))
                .andExpect(jsonPath("$.fieldErrors[*].message", containsInAnyOrder(
                        "Имя автора не может быть пустым.",
                        "Название книги не может быть пустым."
                )));

        verifyZeroInteractions(bookServiceMock);
    }
    
    /**
     * Добавление книги. Неизвестный автор.
     * Успех: получены статус 422 и сообщение о неизвестном авторе.
     * @throws Exception
     */
    @Test
    public void addBook_UnknownAuthor() throws Exception {
        BookDTO wrongBook = new BookDTO(0, UNKNOWN_AUTHOR_NAME, AUTHOR_NAME);
        
        when(bookServiceMock.insert(any(BookDTO.class))).thenThrow(new UnknownAuthorException());
        
        mockMvc.perform(post("/books")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(wrongBook))
        )
                .andExpect(status().isNotAcceptable())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.fieldErrors", hasSize(1)))
                .andExpect(jsonPath("$.fieldErrors[*].field", containsInAnyOrder("author")))
                .andExpect(jsonPath("$.fieldErrors[*].message", containsInAnyOrder("Неизвестный автор.")));

        ArgumentCaptor<BookDTO> bookCaptor = ArgumentCaptor.forClass(BookDTO.class);
        verify(bookServiceMock, times(1)).insert(bookCaptor.capture());
        verifyZeroInteractions(bookServiceMock);
    }
    
    /**
     * Добавление книги. Добавление существующей.
     * Успех: получен статус 406.
     * @throws Exception
     */
    @Test
    public void addBook_Duplicate() throws Exception {
        BookDTO bookToAdd = new BookDTO(BookTestData.getSingleBook());
        
        when(bookServiceMock.insert(any(BookDTO.class))).thenThrow(new DuplicateException());
        
        mockMvc.perform(post("/books")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bookToAdd))
        )
                .andExpect(status().isUnprocessableEntity());

        ArgumentCaptor<BookDTO> bookCaptor = ArgumentCaptor.forClass(BookDTO.class);
        verify(bookServiceMock, times(1)).insert(bookCaptor.capture());
        verifyZeroInteractions(bookServiceMock);
    }
    
    /**
     * Добавление книги. Успешное добавление.
     * Успех: получены статус 201 и добавленная книга.
     * @throws Exception
     */
    @Test
    public void addBook() throws Exception {
        Book book = BookTestData.getSingleBook();
        BookDTO bookToAdd = new BookDTO(book);

        when(bookServiceMock.insert(any(BookDTO.class))).thenReturn(book);

        mockMvc.perform(post("/books")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bookToAdd))
        )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(BOOK_ID)))
                .andExpect(jsonPath("$.author", is(AUTHOR_NAME)))
                .andExpect(jsonPath("$.title", is(BOOK_TITLE)));

        ArgumentCaptor<BookDTO> bookCaptor = ArgumentCaptor.forClass(BookDTO.class);
        verify(bookServiceMock, times(1)).insert(bookCaptor.capture());
        verifyNoMoreInteractions(bookServiceMock);

        BookDTO dtoArgument = bookCaptor.getValue();
        assertThat(dtoArgument.getAuthor(), is(AUTHOR_NAME));
    }

    /**
     * Удаление книги. Удаление несуществующей.
     * Успех: получен статус 404.
     * @throws Exception
     */
    @Test
    public void removeBook_NotFound() throws Exception {
        when(bookServiceMock.remove(BOOK_ID)).thenThrow(new BookNotFoundException());
        
        mockMvc.perform(delete("/books/{bookId}", BOOK_ID))
                .andExpect(status().isNotFound());

        verify(bookServiceMock, times(1)).remove(BOOK_ID);
        verifyNoMoreInteractions(bookServiceMock);
    }

    /**
     * Удаление книги. Успешное удаление.
     * Успех: получены статус 200 и удалённая книга.
     * @throws Exception
     */
    @Test
    public void removeBook() throws Exception {
        Book book = BookTestData.getSingleBook();

        when(bookServiceMock.remove(BOOK_ID)).thenReturn(book);

        mockMvc.perform(delete("/books/{bookId}", BOOK_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(BOOK_ID)))
                .andExpect(jsonPath("$.author", is(AUTHOR_NAME)))
                .andExpect(jsonPath("$.title", is(BOOK_TITLE)));

        verify(bookServiceMock, times(1)).remove(BOOK_ID);
        verifyNoMoreInteractions(bookServiceMock);
    }
}
