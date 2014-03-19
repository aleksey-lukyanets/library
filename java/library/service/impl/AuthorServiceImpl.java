package library.service.impl;

import java.util.Formatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import library.dao.AuthorDAO;
import library.domain.Author;
import library.service.AuthorService;

@Service
public class AuthorServiceImpl implements AuthorService {

    @Autowired
    private AuthorDAO authorDAO;

    @Override
    @Transactional
    public void insertAuthor(Author author) {
        authorDAO.insert(author);
    }

    @Override
    @Transactional
    public void removeAuthor(Author author) {
        if (author != null) {
            authorDAO.remove(author);
        }
    }

    @Override
    @Transactional
    public Author getById(long authorId) {
        return authorDAO.getById(authorId);
    }

    @Override
    @Transactional
    public List<Author> getAllAuthors() {
        return authorDAO.getAll();
    }

    @Override
    @Transactional
    public String getAuthorsAsJson(List<Author> authors) {
        String jsonResponse = "[ ";
        if (authors != null) {
            for (int i = 0; i < authors.size(); i++) {
                Author author = authors.get(i);
                jsonResponse += getAuthorAsJson(author);
                if (i < (authors.size() - 1)) {
                    jsonResponse += ", ";
                }
            }
        }
        jsonResponse += " ]";
        return jsonResponse;
    }
    
    @Override
    public String getAuthorAsJson(Author author) {
        StringBuilder json = new StringBuilder();
        if (author != null) {
            Formatter fmt = new Formatter();
            fmt.format("{\"id\":%d,\"name\":\"%s\",\"country\":\"%s\"}",
                    author.getId(),
                    author.getName(),
                    author.getCountry().getTitle());
            json.append(fmt);
        }
        return json.toString();
    }

}
