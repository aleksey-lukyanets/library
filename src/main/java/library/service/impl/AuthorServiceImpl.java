package library.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import library.dao.AuthorDAO;
import library.domain.Author;
import library.domain.Country;
import library.domain.dto.AuthorDTO;
import library.exception.DuplicateException;
import library.exception.AuthorNotFoundException;
import library.exception.UnknownCountryException;
import library.service.AuthorService;
import library.service.CountryService;

/**
 * Реализация сервиса авторов.
 */
@Service
public class AuthorServiceImpl implements AuthorService {

    @Autowired
    private AuthorDAO authorDAO;
    
    @Autowired
    private CountryService countryService;

    //------------------------------------------------------------------ Чтение
    
    @Transactional(readOnly = true)
    @Override
    public List<Author> getAll() {
        return authorDAO.getAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Author getById(long authorId) throws AuthorNotFoundException {
        Author author = authorDAO.getById(authorId);
        if (author == null) {
            throw new AuthorNotFoundException("No author found with id: " + authorId);
        }
        return author;
    }

    @Transactional(readOnly = true)
    @Override
    public Author getByName(String name) throws AuthorNotFoundException {
        Author author = authorDAO.getByName(name);
        if (author == null) {
            throw new AuthorNotFoundException("No author found with name: " + name);
        }
        return author;
    }

    //--------------------------------------------------------------- Изменение
    
    @Transactional(rollbackFor = {DuplicateException.class, UnknownCountryException.class})
    @Override
    public Author insert(AuthorDTO dto) throws DuplicateException, UnknownCountryException {
        Country country = countryService.getByTitle(dto.getCountry());
        String name = dto.getName();
        checkDuplicated(name);
        Author author = new Author(country, name);
        return authorDAO.insert(author);
    }
    
    private void checkDuplicated(String name) throws DuplicateException {
        if (authorDAO.getByName(name) != null) {
            throw new DuplicateException("Duplicated author with name: " + name);
        }
    }

    @Transactional(rollbackFor = {AuthorNotFoundException.class})
    @Override
    public Author remove(long authorId) throws AuthorNotFoundException {
        Author author = getById(authorId);
        return authorDAO.remove(author);
    }
}
