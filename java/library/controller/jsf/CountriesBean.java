package library.controller.jsf;

import java.util.List;
import library.service.CountryService;
import library.domain.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Контроллер стран.
 */
@Component
public class CountriesBean {
    
    @Autowired
    private CountryService countryService;
    
    public List<Country> getCountriesList() {
        return countryService.getAll();
    }
}
