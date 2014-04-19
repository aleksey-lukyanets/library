package library.controller.integration;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Resource;
import library.data.BookTestData;
import static library.data.BookTestData.AUTHOR_NAME;
import static library.data.BookTestData.BOOK_TITLE;
import static library.data.BookTestData.UNKNOWN_AUTHOR_NAME;
import library.domain.dto.BookDTO;
import library.util.TestUtil;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * Интеграционные тесты книг: управление.
 * 
 * Тесты выполняются последовательно для добавления и удаления известной книги.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:int-test-context.xml",
    "file:src/main/webapp/WEB-INF/spring/libraryServlet/servlet-context.xml"})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class})
@WebAppConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BookCommandTest {

    private MockMvc mockMvc;
    
    private static long addedBookId;
    
    @Resource
    private WebApplicationContext wac;
    
    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }
    
    //-------------------------------------------------------------- Добавление
    
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
    }
    
    /**
     * Добавление книги. Неизвестный автор.
     * Успех: получены статус 422 и сообщение о неизвестном авторе.
     * @throws Exception
     */
    @Test
    public void addBook1_UnknownAuthor() throws Exception {
        BookDTO wrongBook = new BookDTO(0, UNKNOWN_AUTHOR_NAME, AUTHOR_NAME);
        
        mockMvc.perform(post("/books")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(wrongBook))
        )
                .andExpect(jsonPath("$.fieldErrors[*].field", containsInAnyOrder("author")))
                .andExpect(jsonPath("$.fieldErrors[*].message", containsInAnyOrder("Неизвестный автор.")));
    }
    
    /**
     * Добавление книги. Успешное добавление.
     * Успех: получены статус 201 и добавленная книга.
     * @throws Exception
     */
    @Test
    public void addBook2() throws Exception {
        BookDTO bookToAdd = new BookDTO(BookTestData.getSingleBook());
        
        MvcResult mvcResult = mockMvc.perform(post("/books")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bookToAdd))
        )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(header().string("Location", containsString("/books/")))
                .andExpect(jsonPath("$.author", is(AUTHOR_NAME)))
                .andExpect(jsonPath("$.title", is(BOOK_TITLE)))
                .andReturn();
        
        String returnedObject = mvcResult.getResponse().getContentAsString();
        Pattern pattern = Pattern.compile("(\\d+)");
        Matcher matcher = pattern.matcher(returnedObject);
        matcher.find();
        addedBookId = Long.parseLong(matcher.group(), 10);
    }
    
    /**
     * Добавление книги. Добавление существующей.
     * Успех: получен статус 406.
     * @throws Exception
     */
    @Test
    public void addBook3_DuplicateBook() throws Exception {
        BookDTO bookToAdd = new BookDTO(BookTestData.getSingleBook());
        
        mockMvc.perform(post("/books")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bookToAdd))
        )
                .andExpect(status().isUnprocessableEntity());
    }

    //---------------------------------------------------------------- Удаление
    
    /**
     * Удаление книги. Успешное удаление.
     * Успех: получены статус 200 и удалённая книга.
     * @throws Exception
     */
    @Test
    public void removeBook1() throws Exception {
        mockMvc.perform(delete("/books/{bookId}", addedBookId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is((int)addedBookId)))
                .andExpect(jsonPath("$.author", is(AUTHOR_NAME)))
                .andExpect(jsonPath("$.title", is(BOOK_TITLE)));
    }

    /**
     * Удаление книги. Удаление несуществующей.
     * Успех: получен статус 404.
     * @throws Exception
     */
    @Test
    public void removeBook2_NotFound() throws Exception {
        mockMvc.perform(delete("/books/{bookId}", addedBookId))
                .andExpect(status().isNotFound());
    }
}
