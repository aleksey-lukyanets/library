package library.domain.adapter;

/**
 * Адаптер автора для чтения объекта JSON.
 */
public class AuthorAdapter {

    private long countryId;
    private String name;

    public long getCountryId() {
        return countryId;
    }

    public void setCountryId(long countryId) {
        this.countryId = countryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
