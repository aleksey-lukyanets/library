package library.controller.integration;

import javax.annotation.Resource;
import library.util.TestUtil;
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
 * Интеграционные тесты книг: чтение.
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
public class BookRequestTest {

    private MockMvc mockMvc;
    
    @Resource
    private WebApplicationContext wac;
    
    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    /**
     * Поиск всех книг.
     * Успех: получен перечень книг в алфавитном порядке.
     * @throws Exception
     */
    @Test
    public void findAll() throws Exception {
        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(10)))
                .andExpect(jsonPath("$[0].author", is("Lea Doug")))
                .andExpect(jsonPath("$[0].title", is("Concurrent programming in Java")))
                .andExpect(jsonPath("$[1].author", is("Hoeg Peter")))
                .andExpect(jsonPath("$[1].title", is("Den stille pige")))
                .andExpect(jsonPath("$[2].author", is("Johann Goethe")))
                .andExpect(jsonPath("$[2].title", is("Faust: der tragödie")))
                .andExpect(jsonPath("$[3].author", is("Hoeg Peter")))
                .andExpect(jsonPath("$[3].title", is("Froken Smillas fornemmelse for sne")))
                .andExpect(jsonPath("$[4].author", is("Christian Bauer")))
                .andExpect(jsonPath("$[4].title", is("Hibernate in action")))
                .andExpect(jsonPath("$[5].author", is("Christian Bauer")))
                .andExpect(jsonPath("$[5].title", is("Java persistence with Hibernate")))
                .andExpect(jsonPath("$[6].author", is("Craig Walls")))
                .andExpect(jsonPath("$[6].title", is("Spring in action, third edition")))
                .andExpect(jsonPath("$[7].author", is("Фаулер Мартин")))
                .andExpect(jsonPath("$[7].title", is("UML. Основы")))
                .andExpect(jsonPath("$[8].author", is("Гамма Эрих")))
                .andExpect(jsonPath("$[8].title", is("Приёмы объектно-ориентированного проектирования")))
                .andExpect(jsonPath("$[9].author", is("Фаулер Мартин")))
                .andExpect(jsonPath("$[9].title", is("Рефакторинг. Улучшение существующего кода")));
    }
    
    /**
     * Поиск одной книги. Неизвестная книга.
     * Успех: получен статус 404.
     * @throws Exception
     */
    @Test
    public void findById_NotFound() throws Exception {
        mockMvc.perform(get("/books/{id}", 100L))
                .andExpect(status().isNotFound());
    }
    
    /**
     * Поиск одной книги. Успешно.
     * Успех: получена запрошенная книга.
     * @throws Exception
     */
    @Test
    public void findById() throws Exception {
        mockMvc.perform(get("/books/{id}", 7L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$id", is(7)))
                .andExpect(jsonPath("$author", is("Craig Walls")))
                .andExpect(jsonPath("$title", is("Spring in action, third edition")));
    }
}
