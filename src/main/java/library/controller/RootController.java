package library.controller;

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

    @Value("${ServerUrl}")
    private String serverUrl;

    @Value("${EjbVersion}")
    private String ejbVersion;
    
    @Autowired
    private CountryService countryService;

    /**
     * Главная страница.
     * 
     * @param model
     * @return
     */
    @RequestMapping(value = "/index",
            method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("serverUrl", serverUrl);
        model.addAttribute("ejbVersion", ejbVersion);
        return "index";
    }
    
    /**
     * @return перечень всех стран
     */
    @RequestMapping(value = "/countries",
            method = RequestMethod.GET,
            produces = MediaTypesUtf8.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public List<Country> getCountries() {
        return countryService.getAll();
    }
}
