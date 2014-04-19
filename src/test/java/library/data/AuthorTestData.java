package library.data;

import java.util.Arrays;
import java.util.List;
import library.domain.Author;
import library.domain.Country;

/**
 * 
 */
public class AuthorTestData {
    
    public static final int AUTHOR_ID = 11;
    public static final String AUTHOR_NAME = "Лев Толстой";
    public static final String COUNTRY_TITLE = "Россия";
    
    public static final String UNKNOWN_COUNTRY_TITLE = "неизвестное государство";

    public static Author getSingleAuthor() {
        Country country = new Country(COUNTRY_TITLE);
        return new Author(AUTHOR_ID, country, AUTHOR_NAME);
    }

    public static List<Author> getAuthorsList() {
        return Arrays.asList(
                getSingleAuthor(),
                getSingleAuthor());
    }
}
