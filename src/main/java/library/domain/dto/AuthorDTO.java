package library.domain.dto;

import library.domain.Author;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Адаптер автора.
 */
public class AuthorDTO {
    
    private long id;

    @NotEmpty
    @Length(max = 32)
    private String country;
    
    @NotEmpty
    @Length(max = 100)
    private String name;

    public AuthorDTO() {
    }
    
    public AuthorDTO(long id, String country, String name) {
        this.id = id;
        this.country = country;
        this.name = name;
    }
    
    public AuthorDTO(Author author) {
        this.id = author.getId();
        this.country = author.getCountry().getTitle();
        this.name = author.getName();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
