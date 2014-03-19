package library.service;

import java.util.List;

import library.domain.Author;

public interface AuthorService {

    void insertAuthor(Author author);

    void removeAuthor(Author author);

    Author getById(long authorId);

    List<Author> getAllAuthors();

    String getAuthorsAsJson(List<Author> authors);
    
    String getAuthorAsJson(Author author);
}
