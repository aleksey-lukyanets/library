package library.service.impl;

import java.util.Formatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import library.dao.CountryDAO;
import library.domain.Country;
import library.service.CountryService;

@Service
public class CountryServiceImpl implements CountryService {

    @Autowired
    private CountryDAO countryDAO;

    @Override
    @Transactional
    public void insertCountry(Country country) {
        countryDAO.insert(country);
    }

    @Override
    @Transactional
    public Country getCountryById(long countryId) {
        return countryDAO.getById(countryId);
    }

    @Override
    @Transactional
    public Country getCountry(String countryname) {
        return countryDAO.getByTitle(countryname);
    }

    @Override
    @Transactional
    public List<Country> getAllCountries() {
        return countryDAO.getAll();
    }

    @Override
    @Transactional
    public String getCountriesAsJson(List<Country> countries) {
        String jsonResponse = "[ ";
        for (int i = 0; i < countries.size(); i++) {
            Country country = countries.get(i);
            jsonResponse += getCountryAsJson(country);
            if (i < (countries.size() - 1)) {
                jsonResponse += ", ";
            }
        }
        jsonResponse += " ]";
        return jsonResponse;
    }
    
    @Override
    public String getCountryAsJson(Country country) {
        StringBuilder json = new StringBuilder();
        Formatter fmt = new Formatter();
        fmt.format("{\"id\":%d,\"title\":\"%s\"}",
                country.getId(),
                country.getTitle());
        json.append(fmt);
        return json.toString();
    }

}
