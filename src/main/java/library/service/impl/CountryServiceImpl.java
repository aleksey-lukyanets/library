package library.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import library.dao.CountryDAO;
import library.domain.Country;
import library.exception.UnknownCountryException;
import library.service.CountryService;

/**
 * Реализация сервиса стран.
 */
@Service
public class CountryServiceImpl implements CountryService {

    @Autowired
    private CountryDAO countryDAO;

    @Transactional(readOnly = true)
    @Override
    public List<Country> getAll() {
        return countryDAO.getAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Country getById(long countryId) throws UnknownCountryException {
        Country country = countryDAO.getById(countryId);
        if (country == null) {
            throw new UnknownCountryException("No country found with id: " + countryId);
        }
        return country;
    }

    @Transactional(readOnly = true)
    @Override
    public Country getByTitle(String title) throws UnknownCountryException {
        Country country = countryDAO.getByTitle(title);
        if (country == null) {
            throw new UnknownCountryException("No country found with title: " + title);
        }
        return country;
    }
}
