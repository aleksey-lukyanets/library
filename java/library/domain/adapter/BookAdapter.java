package library.domain.adapter;

/**
 * Адаптер книги для чтения объекта JSON.
 */
public class BookAdapter {
    
    private long authorId;
    private String title;

    public long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(long authorId) {
        this.authorId = authorId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
