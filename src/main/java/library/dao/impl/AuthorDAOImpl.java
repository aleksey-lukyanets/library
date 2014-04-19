package library.dao.impl;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import library.dao.AuthorDAO;
import library.domain.Author;
import org.hibernate.criterion.Restrictions;

/**
 * Реализация ДАО автора.
 */
@Repository
public class AuthorDAOImpl implements AuthorDAO {

    @Autowired
    private SessionFactory sessionFactory;
    
    //------------------------------------------------------------------ Чтение
    
    @Override
    public List<Author> getAll() {
        Criteria criteria = sessionFactory.getCurrentSession()
                .createCriteria(Author.class)
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                .addOrder(Property.forName("name").asc());
        return criteria.list();
    }

    @Override
    public Author getById(long authorId) {
        return (Author)sessionFactory.getCurrentSession().get(Author.class, authorId);
    }
    
    @Override
    public Author getByName(String name) {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Author.class);
        criteria.add(Restrictions.eq("name", name));
        return (Author)criteria.uniqueResult();
    }

    //--------------------------------------------------------------- Изменение
    
    @Override
    public Author insert(Author author) {
        sessionFactory.getCurrentSession().save(author);
        return author;
    }

    @Override
    public Author remove(Author author) {
        sessionFactory.getCurrentSession().delete(author);
        return author;
    }
}
