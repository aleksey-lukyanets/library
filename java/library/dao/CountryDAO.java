package library.dao;

import java.util.List;
import library.domain.Country;

/**
 * ДАО страны.
 */
public interface CountryDAO {

    void insert(Country country);

    Country getById(long countryId);

    Country getByTitle(String title);

    List<Country> getAll();
}
