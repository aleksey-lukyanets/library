package library.service;

import java.util.List;
import library.domain.Author;

/**
 * Сервис управления авторами.
 */
public interface AuthorService {

    void insert(Author author);

    void remove(long authorId);

    Author getById(long authorId);

    List<Author> getAll();

    String getAuthorsAsJson(List<Author> authors);
    
    String getAuthorAsJson(Author author);
}
