package library.service;

import java.util.List;

import library.domain.Country;

public interface CountryService {

    void insertCountry(Country country);

    Country getCountryById(long countryId);

    Country getCountry(String title);

    List<Country> getAllCountries();

    String getCountriesAsJson(List<Country> country);
    
    String getCountryAsJson(Country country);
}
