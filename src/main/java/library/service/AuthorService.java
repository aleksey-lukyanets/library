package library.service;

import java.util.List;
import library.domain.Author;
import library.domain.dto.AuthorDTO;
import library.exception.DuplicateException;
import library.exception.AuthorNotFoundException;
import library.exception.UnknownCountryException;

/**
 * Сервис управления авторами.
 */
public interface AuthorService {

    /**
     * Поиск всех авторов.
     * @return перечень всех авторов
     */
    List<Author> getAll();

    /**
     * Поиск автора с указанным id.
     * @param authorId идентификатор автора
     * @return автор
     * @throws library.exception.AuthorNotFoundException если автора с таким id не существует
     */
    Author getById(long authorId) throws AuthorNotFoundException;
    
    /**
     * Поиск автора по имени.
     * @param name имя автора
     * @return автор
     * @throws library.exception.AuthorNotFoundException если автора с таким именем не существует
     */
    Author getByName(String name) throws AuthorNotFoundException;
    
    /**
     * Добавление автора.
     * @param dto новый автор
     * @return созданный автор
     * @throws library.exception.DuplicateException если автор с таким именем уже существует
     * @throws library.exception.UnknownCountryException если указанной страны не существует
     */
    Author insert(AuthorDTO dto) throws DuplicateException, UnknownCountryException;

    /**
     * Удаление автора.
     * @param authorId идентиикатор удаляемого автора
     * @return удалённый автор
     * @throws library.exception.AuthorNotFoundException если автора с таким id не существует
     */
    Author remove(long authorId) throws AuthorNotFoundException;
}
