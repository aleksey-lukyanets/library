package library.data;

import java.util.Arrays;
import java.util.List;
import library.domain.Author;
import library.domain.Book;
import library.domain.Country;

/**
 * 
 */
public class BookTestData {
    
    public static final int BOOK_ID = 11;
    public static final String AUTHOR_NAME = "Hoeg Peter";
    public static final String BOOK_TITLE = "Die Kinder der Elefantenhüter";
    
    public static final String UNKNOWN_AUTHOR_NAME = "неизвестный автор";

    public static Book getSingleBook() {
        Country country = new Country();
        Author author = new Author(country, AUTHOR_NAME);
        return new Book(BOOK_ID, author, BOOK_TITLE);
    }

    public static List<Book> getBooksList() {
        return Arrays.asList(
                getSingleBook(),
                getSingleBook());
    }
}
