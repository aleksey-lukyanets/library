package library.controller.unit;

import java.util.List;
import javax.annotation.Resource;
import library.data.AuthorTestData;
import static library.data.AuthorTestData.*;
import library.domain.Author;
import library.domain.dto.AuthorDTO;
import library.exception.DuplicateException;
import library.exception.AuthorNotFoundException;
import library.exception.UnknownCountryException;
import library.service.AuthorService;
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
 * Модульные тесты контроллера авторов.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:unit-test-context.xml",
    "file:src/main/webapp/WEB-INF/spring/libraryServlet/servlet-context.xml"
})
@WebAppConfiguration
public class AuthorControllerTest {

    private MockMvc mockMvc;
    
    @Autowired
    private AuthorService authorServiceMock;
    
    @Resource
    private WebApplicationContext wac;
    
    @Before
    public void setUp() {
        Mockito.reset(authorServiceMock);
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    /**
     * Поиск всех авторов.
     * Успех: получены статус 200 и перечень авторов.
     * @throws Exception
     */
    @Test
    public void getAll_AuthorsFound() throws Exception {
        List<Author> list = AuthorTestData.getAuthorsList();

        when(authorServiceMock.getAll()).thenReturn(list);

        mockMvc.perform(get("/authors"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(list.size())))
                .andExpect(jsonPath("$[0].id", is(AUTHOR_ID)))
                .andExpect(jsonPath("$[0].name", is(AUTHOR_NAME)))
                .andExpect(jsonPath("$[0].country", is(COUNTRY_TITLE)))
                .andExpect(jsonPath("$[1].id", is(AUTHOR_ID)))
                .andExpect(jsonPath("$[1].name", is(AUTHOR_NAME)))
                .andExpect(jsonPath("$[1].country", is(COUNTRY_TITLE)));

        verify(authorServiceMock, times(1)).getAll();
        verifyNoMoreInteractions(authorServiceMock);
    }

    /**
     * Поиск одного автора. Неизвестный автор.
     * Успех: получен статус 404.
     * @throws Exception
     */
    @Test
    public void getById_NotFound() throws Exception {
        when(authorServiceMock.getById(AUTHOR_ID)).thenThrow(new AuthorNotFoundException());
        
        mockMvc.perform(get("/authors/{authorId}", AUTHOR_ID))
                .andExpect(status().isNotFound());

        verify(authorServiceMock, times(1)).getById(AUTHOR_ID);
        verifyNoMoreInteractions(authorServiceMock);
    }

    /**
     * Поиск одного автора. Успешно.
     * Успех: получен запрошенный автор.
     * @throws Exception
     */
    @Test
    public void getById() throws Exception {
        Author author = AuthorTestData.getSingleAuthor();

        when(authorServiceMock.getById(AUTHOR_ID)).thenReturn(author);

        mockMvc.perform(get("/authors/{authorId}", AUTHOR_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(AUTHOR_ID)))
                .andExpect(jsonPath("$.name", is(AUTHOR_NAME)))
                .andExpect(jsonPath("$.country", is(COUNTRY_TITLE)));

        verify(authorServiceMock, times(1)).getById(AUTHOR_ID);
        verifyNoMoreInteractions(authorServiceMock);
    }

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

        verifyZeroInteractions(authorServiceMock);
    }
    
    /**
     * Добавление автора. Неизвестная страна.
     * Успех: получены статус 422 и сообщение о неизвестной стране.
     * @throws Exception
     */
    @Test
    public void addAuthor1_UnknownCountry() throws Exception {
        AuthorDTO wrongAuthor = new AuthorDTO(0, UNKNOWN_COUNTRY_TITLE, AUTHOR_NAME);
        
        when(authorServiceMock.insert(any(AuthorDTO.class))).thenThrow(new UnknownCountryException());
        
        mockMvc.perform(post("/authors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(wrongAuthor))
        )
                .andExpect(status().isNotAcceptable())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.fieldErrors", hasSize(1)))
                .andExpect(jsonPath("$.fieldErrors[*].field", containsInAnyOrder("country")))
                .andExpect(jsonPath("$.fieldErrors[*].message", containsInAnyOrder("Неизвестная страна.")));

        ArgumentCaptor<AuthorDTO> authorCaptor = ArgumentCaptor.forClass(AuthorDTO.class);
        verify(authorServiceMock, times(1)).insert(authorCaptor.capture());
        verifyZeroInteractions(authorServiceMock);
    }
    
    /**
     * Добавление автора. Добавление существующего.
     * Успех: получен статус 406.
     * @throws Exception
     */
    @Test
    public void addAuthor_Duplicate() throws Exception {
        AuthorDTO authorToAdd = new AuthorDTO(AuthorTestData.getSingleAuthor());
        
        when(authorServiceMock.insert(any(AuthorDTO.class))).thenThrow(new DuplicateException());
        
        mockMvc.perform(post("/authors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(authorToAdd))
        )
                .andExpect(status().isUnprocessableEntity());

        ArgumentCaptor<AuthorDTO> authorCaptor = ArgumentCaptor.forClass(AuthorDTO.class);
        verify(authorServiceMock, times(1)).insert(authorCaptor.capture());
        verifyZeroInteractions(authorServiceMock);
    }
    
    /**
     * Добавление автора. Успешное добавление.
     * Успех: получены статус 201 и добавленный автор.
     * @throws Exception
     */
    @Test
    public void addAuthor() throws Exception {
        Author author = AuthorTestData.getSingleAuthor();
        AuthorDTO authorToAdd = new AuthorDTO(author);

        when(authorServiceMock.insert(any(AuthorDTO.class))).thenReturn(author);

        mockMvc.perform(post("/authors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(authorToAdd))
        )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(AUTHOR_ID)))
                .andExpect(jsonPath("$.name", is(AUTHOR_NAME)))
                .andExpect(jsonPath("$.country", is(COUNTRY_TITLE)));

        ArgumentCaptor<AuthorDTO> authorCaptor = ArgumentCaptor.forClass(AuthorDTO.class);
        verify(authorServiceMock, times(1)).insert(authorCaptor.capture());
        verifyNoMoreInteractions(authorServiceMock);

        AuthorDTO dtoArgument = authorCaptor.getValue();
        assertThat(dtoArgument.getName(), is(AUTHOR_NAME));
    }

    /**
     * Удаление автора. Удаление несуществующего.
     * Успех: получен статус 404.
     * @throws Exception
     */
    @Test
    public void removeAuthor_AuthorEntryNotFound_ShouldReturnHttpStatusCode404() throws Exception {
        when(authorServiceMock.remove(AUTHOR_ID)).thenThrow(new AuthorNotFoundException());
        
        mockMvc.perform(delete("/authors/{authorId}", AUTHOR_ID))
                .andExpect(status().isNotFound());

        verify(authorServiceMock, times(1)).remove(AUTHOR_ID);
        verifyNoMoreInteractions(authorServiceMock);
    }

    /**
     * Удаление автора. Успешное удаление.
     * Успех: получены статус 200 и удалённый автор.
     * @throws Exception
     */
    @Test
    public void removeAuthor() throws Exception {
        Author author = AuthorTestData.getSingleAuthor();

        when(authorServiceMock.remove(AUTHOR_ID)).thenReturn(author);

        mockMvc.perform(delete("/authors/{authorId}", AUTHOR_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(AUTHOR_ID)))
                .andExpect(jsonPath("$.name", is(AUTHOR_NAME)))
                .andExpect(jsonPath("$.country", is(COUNTRY_TITLE)));

        verify(authorServiceMock, times(1)).remove(AUTHOR_ID);
        verifyNoMoreInteractions(authorServiceMock);
    }
}
