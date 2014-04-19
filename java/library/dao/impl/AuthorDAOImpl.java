package library.dao.impl;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import library.dao.AuthorDAO;
import library.domain.Author;

/**
 * Реализация ДАО автора.
 */
@Service
public class AuthorDAOImpl implements AuthorDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void insert(Author author) {
        sessionFactory.getCurrentSession().save(author);
    }

    @Override
    public void remove(Author author) {
        sessionFactory.getCurrentSession().delete(author);
    }

    @Override
    public Author getById(long authorId) {
        return (Author) sessionFactory.getCurrentSession().get(Author.class, authorId);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Author> getAll() {
        Criteria criteria = sessionFactory.getCurrentSession()
                .createCriteria(Author.class)
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                .addOrder(Property.forName("name").asc());
        return criteria.list();
    }
}
