package library.controller.integration;

import javax.annotation.Resource;
import library.util.TestUtil;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasProperty;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * Интеграционные тесты авторов: чтение.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:int-test-context.xml",
    "file:src/main/webapp/WEB-INF/spring/libraryServlet/servlet-context.xml"
})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class})
@WebAppConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AuthorRequestTest {

    private MockMvc mockMvc;
    
    @Resource
    private WebApplicationContext wac;
    
    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    /**
     * Поиск всех авторов.
     * Успех: получены статус 200 и перечень авторов в алфавитном порядке.
     * @throws Exception
     */
    @Test
    public void findAll() throws Exception {
        mockMvc.perform(get("/authors"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(7)))
                .andExpect(jsonPath("$[0].name", is("Christian Bauer")))
                .andExpect(jsonPath("$[0].country", is("США")))
                .andExpect(jsonPath("$[1].name", is("Craig Walls")))
                .andExpect(jsonPath("$[1].country", is("США")))
                .andExpect(jsonPath("$[2].name", is("Hoeg Peter")))
                .andExpect(jsonPath("$[2].country", is("Дания")))
                .andExpect(jsonPath("$[3].name", is("Johann Goethe")))
                .andExpect(jsonPath("$[3].country", is("Германия")))
                .andExpect(jsonPath("$[4].name", is("Lea Doug")))
                .andExpect(jsonPath("$[4].country", is("США")))
                .andExpect(jsonPath("$[5].name", is("Гамма Эрих")))
                .andExpect(jsonPath("$[5].country", is("США")))
                .andExpect(jsonPath("$[6].name", is("Фаулер Мартин")))
                .andExpect(jsonPath("$[6].country", is("США")));
    }
    
    /**
     * Поиск одного автора. Неизвестный автор.
     * Успех: получен статус 404.
     * @throws Exception
     */
    @Test
    public void findById_NotFound() throws Exception {
        mockMvc.perform(get("/authors/{id}", 100L))
                .andExpect(status().isNotFound());
    }
    
    /**
     * Поиск одного автора. Успешно.
     * Успех: получен запрошенный автор.
     * @throws Exception
     */
    @Test
    public void findById() throws Exception {
        mockMvc.perform(get("/authors/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$id", is(1)))
                .andExpect(jsonPath("$name", is("Гамма Эрих")))
                .andExpect(jsonPath("$country", is("США")));
    }
    
    /**
     * Поиск всех книг одного автора. Неизвестный автор.
     * Успех: получен статус 404.
     * @throws Exception
     */
    @Test
    public void findByAuthor_NotFound() throws Exception {
        mockMvc.perform(get("/authors/{authorId}/books", 100L))
                .andExpect(status().isNotFound());
    }

    /**
     * Поиск всех книг одного автора. Успешно.
     * Успех: получен перечень книг в алфавитном порядке.
     * @throws Exception
     */
    @Test
    public void findByAuthor() throws Exception {
        mockMvc.perform(get("/authors/{authorId}/books", 7L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].author", everyItem(is("Hoeg Peter"))));
    }
}
