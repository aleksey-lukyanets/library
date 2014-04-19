package library.dao;

import java.util.List;
import library.domain.Author;

/**
 * ДАО автора.
 */
public interface AuthorDAO {

    /**
     * Поиск всех авторов.
     * @return перечень всех авторов
     */
    List<Author> getAll();

    /**
     * Поиск автора с указанным id.
     * @param authorId идентификатор автора
     * @return автор, null если не существует
     */
    Author getById(long authorId);
    
    /**
     * Поиск автора по имени.
     * @param name имя автора
     * @return автор, null если не существует
     */
    Author getByName(String name);

    /**
     * Добавление автора.
     * @param author новый автор
     * @return созданный автор
     */
    Author insert(Author author);
    
    /**
     * Удаление автора.
     * @param author удаляемый автор
     * @return удалённый автор
     */
    Author remove(Author author);
}
