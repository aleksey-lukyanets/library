package library.dao;

import java.util.List;
import library.domain.Country;

/**
 * ДАО страны.
 */
public interface CountryDAO {

    /**
     * Поиск всех стран.
     * @return перечень всех стран
     */
    List<Country> getAll();

    /**
     * Поиск страны с указанным id.
     * @param countryId идентификатор страны
     * @return страна, null если не существует
     */
    Country getById(long countryId);

    /**
     * Поиск страны по названию.
     * @param title название страны
     * @return страна, null если не существует
     */
    Country getByTitle(String title);
}
