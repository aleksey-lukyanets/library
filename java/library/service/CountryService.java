package library.service;

import java.util.List;
import library.domain.Country;

/**
 * Сервис управления странами.
 */
public interface CountryService {

    void insert(Country country);

    Country getById(long countryId);

    Country getByTitle(String title);

    List<Country> getAll();

    String getCountriesAsJson(List<Country> country);
    
    String getCountryAsJson(Country country);
}
