package library.dao;

import java.util.List;
import library.domain.Author;
import library.domain.Book;

/**
 * ДАО книги.
 */
public interface BookDAO {

    /**
     * Поиск всех книг.
     * @return перечень всех книг
     */
    List<Book> getAll();

    /**
     * Поиск книг отдельного автора.
     * @param author автор
     * @return перечень книг автора
     */
    List<Book> getByAuthor(Author author);
    
    /**
     * Поиск книги с указанным id.
     * @param bookId идентификатор книги
     * @return книга, null если не существует
     */
    Book getById(long bookId);

    /**
     * Поиск книги по названию.
     * @param title название книги
     * @return книга, null если не существует
     */
    Book getByTitle(String title);

    /**
     * Добавление книги.
     * @param book новая книга
     * @return созданная книга
     */
    Book insert(Book book);

    /**
     * Удаление книги.
     * @param book удаляемая книга
     * @return удалённая книга
     */
    Book remove(Book book);
}
