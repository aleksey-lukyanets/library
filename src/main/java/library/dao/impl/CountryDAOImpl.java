package library.dao.impl;

import java.util.List;
import library.dao.CountryDAO;
import library.domain.Country;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Реализация ДАО страны.
 */
@Repository
public class CountryDAOImpl implements CountryDAO {

    @Autowired
    private SessionFactory sessionFactory;
    
    @Override
    @SuppressWarnings("unchecked")
    public List<Country> getAll() {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Country.class);
        return criteria.list();
    }

    @Override
    public Country getById(long countryId) {
        return (Country)sessionFactory.getCurrentSession().get(Country.class, countryId);
    }

    @Override
    public Country getByTitle(String title) {
        Query query = sessionFactory.getCurrentSession().createQuery("from Country where title = :title");
        query.setParameter("title", title);
        return (Country)query.uniqueResult();
    }
}
