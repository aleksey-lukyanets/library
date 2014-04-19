package library.controller.integration;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Resource;
import library.data.AuthorTestData;
import static library.data.AuthorTestData.AUTHOR_NAME;
import static library.data.AuthorTestData.COUNTRY_TITLE;
import static library.data.AuthorTestData.UNKNOWN_COUNTRY_TITLE;
import library.domain.dto.AuthorDTO;
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
 * Интеграционные тесты авторов: управление.
 * 
 * Тесты выполняются последовательно для добавления и удаления известного автора.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:int-test-context.xml",
    "file:src/main/webapp/WEB-INF/spring/libraryServlet/servlet-context.xml"})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class})
@WebAppConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AuthorCommandTest {

    private MockMvc mockMvc;
    
    private static long addedAuthorId;
    
    @Resource
    private WebApplicationContext wac;
    
    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }
    
    //-------------------------------------------------------------- Добавление
    
    /**
     * Добавление автора. Ошибки валидации строк названий.
     * Успех: получены статус 422 и список нарушенных ограничений.
     * @throws Exception
     */
    @Test
    public void addAuthor_ValidationErrors() throws Exception {
        AuthorDTO wrongAuthor = new AuthorDTO(0, "", "");
        
        mockMvc.perform(post("/authors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(wrongAuthor))
        )
                .andExpect(status().isNotAcceptable())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.fieldErrors", hasSize(2)))
                .andExpect(jsonPath("$.fieldErrors[*].field", containsInAnyOrder("name", "country")))
                .andExpect(jsonPath("$.fieldErrors[*].message", containsInAnyOrder(
                        "Имя автора не может быть пустым.",
                        "Название страны не может быть пустым."
                )));
    }
    
    /**
     * Добавление автора. Неизвестная страна.
     * Успех: получены статус 422 и сообщение о неизвестной стране.
     * @throws Exception
     */
    @Test
    public void addAuthor1_UnknownCountry() throws Exception {
        AuthorDTO wrongAuthor = new AuthorDTO(0, UNKNOWN_COUNTRY_TITLE, AUTHOR_NAME);
        
        mockMvc.perform(post("/authors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(wrongAuthor))
        )
                .andExpect(status().isNotAcceptable())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.fieldErrors", hasSize(1)))
                .andExpect(jsonPath("$.fieldErrors[*].field", containsInAnyOrder("country")))
                .andExpect(jsonPath("$.fieldErrors[*].message", containsInAnyOrder("Неизвестная страна.")));
    }
    
    /**
     * Добавление автора. Успешное добавление.
     * Успех: получены статус 201 и добавленный автор.
     * @throws Exception
     */
    @Test
    public void addAuthor2() throws Exception {
        AuthorDTO authorToAdd = new AuthorDTO(AuthorTestData.getSingleAuthor());
        
        MvcResult mvcResult = mockMvc.perform(post("/authors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(authorToAdd))
        )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(header().string("Location", containsString("/authors/")))
                .andExpect(jsonPath("$.name", is(AUTHOR_NAME)))
                .andExpect(jsonPath("$.country", is(COUNTRY_TITLE)))
                .andReturn();
        
        String returnedObject = mvcResult.getResponse().getContentAsString();
        Pattern pattern = Pattern.compile("(\\d+)");
        Matcher matcher = pattern.matcher(returnedObject);
        matcher.find();
        addedAuthorId = Long.parseLong(matcher.group(), 10);
    }
    
    /**
     * Добавление автора. Добавление существующего.
     * Успех: получен статус 406.
     * @throws Exception
     */
    @Test
    public void addAuthor3_Duplicate() throws Exception {
        AuthorDTO authorToAdd = new AuthorDTO(AuthorTestData.getSingleAuthor());
        
        mockMvc.perform(post("/authors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(authorToAdd))
        )
                .andExpect(status().isUnprocessableEntity());
    }

    //---------------------------------------------------------------- Удаление
    
    /**
     * Удаление автора. Успешное удаление.
     * Успех: получены статус 200 и удалённый автор.
     * @throws Exception
     */
    @Test
    public void removeAuthor1() throws Exception {
        mockMvc.perform(delete("/authors/{authorId}", addedAuthorId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is((int)addedAuthorId)))
                .andExpect(jsonPath("$.name", is(AUTHOR_NAME)))
                .andExpect(jsonPath("$.country", is(COUNTRY_TITLE)));
    }

    /**
     * Удаление автора. Удаление несуществующего.
     * Успех: получен статус 404.
     * @throws Exception
     */
    @Test
    public void removeAuthor2_NotFound() throws Exception {
        mockMvc.perform(delete("/authors/{authorId}", addedAuthorId))
                .andExpect(status().isNotFound());
    }
}
