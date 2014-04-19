package library.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import library.dao.BookDAO;
import library.domain.Author;
import library.domain.Book;

/**
 * Реализация ДАО книги.
 */
@Service
public class BookDAOImpl implements BookDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void insert(Book book) {
        sessionFactory.getCurrentSession().save(book);
    }

    @Override
    public void remove(Book book) {
        sessionFactory.getCurrentSession().delete(book);
    }

    @Override
    public Book getById(long bookId) {
        return (Book) sessionFactory.getCurrentSession().get(Book.class, bookId);
    }
    
    @Override
    public Book getByTitle(String title) {
        Query query = sessionFactory.getCurrentSession().createQuery("from Book where title = :title");
        query.setParameter("title", title);
        return (Book) query.list().get(0);
    }
    
    @Override
    public List<Book> getByAuthor(Author author) {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Book.class);
        criteria.add(Restrictions.eq("author", author));
        return criteria.list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Book> getAll() {
        Criteria criteria = sessionFactory.getCurrentSession()
                .createCriteria(Book.class)
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                .addOrder(Property.forName("title").asc());
        return criteria.list();
    }

}
