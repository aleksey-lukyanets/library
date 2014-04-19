package library.service;

import java.util.List;
import library.domain.Book;
import library.domain.dto.BookDTO;
import library.exception.AuthorNotFoundException;
import library.exception.BookNotFoundException;
import library.exception.DuplicateException;
import library.exception.UnknownAuthorException;

/**
 * Сервис управления книгами.
 */
public interface BookService {

    /**
     * Поиск всех книг.
     * @return перечень всех книг
     */
    List<Book> getAll();
    
    /**
     * Поиск книг отдельного автора.
     * @param authorId идентификатор автора
     * @return перечень книг автора
     * @throws library.exception.AuthorNotFoundException если автора с таким id не существует
     */
    List<Book> getByAuthor(long authorId) throws AuthorNotFoundException;

    /**
     * Поиск книги с указанным id.
     * @param bookId идентификатор книги
     * @return книга
     * @throws library.exception.BookNotFoundException если книга с таким id не существует
     */
    Book getById(long bookId) throws BookNotFoundException;
    
    /**
     * Поиск книги по названию.
     * @param title название книги
     * @return книга
     * @throws library.exception.BookNotFoundException если книга с таким названием не существует
     */
    Book getByTitle(String title) throws BookNotFoundException;

    /**
     * Добавление книги.
     * @param dto новая книга
     * @return созданная книга
     * @throws library.exception.DuplicateException если книга с таким названием у автора уже существует
     * @throws library.exception.UnknownAuthorException если указанного автора не существует
     */
    Book insert(BookDTO dto) throws DuplicateException, UnknownAuthorException;

    /**
     * Удаление книги.
     * @param bookId идентификатор удаляемой книги
     * @return удалённая книга
     * @throws library.exception.BookNotFoundException если книга с таким id не существует
     */
    Book remove(long bookId) throws BookNotFoundException;
}
