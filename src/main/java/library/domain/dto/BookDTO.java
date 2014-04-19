package library.domain.dto;

import library.domain.Book;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Адаптер книги.
 */
public class BookDTO {
    
    private long id;
    
    @NotEmpty
    @Length(max = 100)
    private String author;
    
    @NotEmpty
    @Length(max = 100)
    private String title;

    public BookDTO() {
    }
    
    public BookDTO(long id, String author, String title) {
        this.id = id;
        this.author = author;
        this.title = title;
    }
    
    public BookDTO(Book book) {
        this.id = book.getId();
        this.author = book.getAuthor().getName();
        this.title = book.getTitle();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
