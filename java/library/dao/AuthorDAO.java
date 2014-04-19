package library.dao;

import java.util.List;
import library.domain.Author;

/**
 * ДАО автора.
 */
public interface AuthorDAO {

    void insert(Author author);
    
    void remove(Author author);

    Author getById(long authorId);

    List<Author> getAll();
}
