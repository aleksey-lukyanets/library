package library.service;

import java.util.List;
import library.domain.Country;
import library.exception.UnknownCountryException;

/**
 * Сервис управления странами.
 */
public interface CountryService {

    /**
     * Поиск всех стран.
     * @return перечень всех стран
     */
    List<Country> getAll();

    /**
     * Поиск страны с указанным id.
     * @param countryId идентификатор страны
     * @return страна
     * @throws library.exception.UnknownCountryException если страны с таким id не существует
     */
    Country getById(long countryId) throws UnknownCountryException;

    /**
     * Поиск страны по названию.
     * @param title название страны
     * @return страна
     * @throws library.exception.UnknownCountryException если страны с таким названием не существует
     */
    Country getByTitle(String title) throws UnknownCountryException;
}
