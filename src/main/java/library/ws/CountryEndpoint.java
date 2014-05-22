package library.ws;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import library.domain.Country;
import library.service.CountryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;

import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.xpath.XPath;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.mycompany.hr.schemas.*;
import java.util.ArrayList;
import javax.xml.bind.JAXBElement;

@Endpoint
public class CountryEndpoint {

    private static final String NAMESPACE_URI = "http://mycompany.com/hr/schemas";

    private XPath startDateExpression;

    private XPath endDateExpression;

    private XPath nameExpression;

    private HumanResourceService humanResourceService;
    
    @Autowired
    private CountryService countryService;

    @Autowired
    public CountryEndpoint(HumanResourceService humanResourceService) throws JDOMException {
        this.humanResourceService = humanResourceService;

        Namespace namespace = Namespace.getNamespace("hr", NAMESPACE_URI);

        startDateExpression = XPath.newInstance("//hr:StartDate");
        startDateExpression.addNamespace(namespace);

        endDateExpression = XPath.newInstance("//hr:EndDate");
        endDateExpression.addNamespace(namespace);

        nameExpression = XPath.newInstance("concat(//hr:FirstName,' ',//hr:LastName)");
        nameExpression.addNamespace(namespace);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "HolidayRequest")
    @ResponsePayload
    public Element handleHolidayRequest(@RequestPayload Element holidayRequest) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = dateFormat.parse(startDateExpression.valueOf(holidayRequest));
        Date endDate = dateFormat.parse(endDateExpression.valueOf(holidayRequest));
        String name = nameExpression.valueOf(holidayRequest);

        humanResourceService.bookHoliday(startDate, endDate, name);
        
        return holidayRequest;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "CountriesRequest")
    @ResponsePayload
    public JAXBElement<CountriesResponseType> getAllCountries(@RequestPayload Element request) throws Exception {
        List<Country> countries = countryService.getAll();
        ObjectFactory factory = new ObjectFactory();
        CountriesResponseType response = factory.createCountriesResponseType();
        List<CountryType> countryTypes = new ArrayList<>();
        for (Country country : countries) {
            CountryType countryType = factory.createCountryType();
            countryType.setId(country.getId());
            countryType.setTitle(country.getTitle());
            countryTypes.add(countryType);
        }
        response.getCountry().addAll(countryTypes);
        return factory.createCountriesResponse(response);
    }
}
