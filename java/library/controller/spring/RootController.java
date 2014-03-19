package library.controller.spring;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import library.service.CountryService;
import library.domain.Country;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;

/**
 * Контроллер пользовательского интерфейса.
 */
@Controller
public class RootController {

    @Value("${serverUrl}")
    private String serverUrl;
    @Autowired
    CountryService countryService;

    @RequestMapping(value = "/index",
            method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("serverUrl", serverUrl);
        return "hello";
    }
    
    @RequestMapping(value = "/countries",
            method = RequestMethod.GET,
            produces = "application/json; charset=utf-8")
    public @ResponseBody List<Country> getCountries() {
        return countryService.getAllCountries();
    }
}
