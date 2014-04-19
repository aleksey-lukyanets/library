package library.dao.impl;

import java.util.List;
import library.dao.BookDAO;
import library.domain.Author;
import library.domain.Book;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Реализация ДАО книги.
 */
@Repository
public class BookDAOImpl implements BookDAO {

    @Autowired
    private SessionFactory sessionFactory;

    //------------------------------------------------------------------ Чтение

    @Override
    public List<Book> getAll() {
        Criteria criteria = sessionFactory.getCurrentSession()
                .createCriteria(Book.class)
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                .addOrder(Property.forName("title").asc());
        return criteria.list();
    }
    
    @Override
    public List<Book> getByAuthor(Author author) {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Book.class)
                .add(Restrictions.eq("author", author))
                .addOrder(Property.forName("title").asc());
        return criteria.list();
    }

    @Override
    public Book getById(long bookId) {
        return (Book)sessionFactory.getCurrentSession().get(Book.class, bookId);
    }
    
    @Override
    public Book getByTitle(String title) {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Book.class)
                .add(Restrictions.eq("title", title));
        return (Book)criteria.uniqueResult();
    }

    //--------------------------------------------------------------- Изменение
    
    @Override
    public Book insert(Book book) {
        sessionFactory.getCurrentSession().save(book);
        return book;
    }

    @Override
    public Book remove(Book book) {
        sessionFactory.getCurrentSession().delete(book);
        return book;
    }
}
