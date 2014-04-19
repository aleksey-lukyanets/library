package library.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import library.dao.CountryDAO;
import library.domain.Country;

/**
 * Реализация ДАО страны.
 */
@Service
public class CountryDAOImpl implements CountryDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void insert(Country country) {
        sessionFactory.getCurrentSession().save(country);
    }

    @Override
    public Country getById(long countryId) {
        return (Country) sessionFactory.getCurrentSession().get(Country.class, countryId);
    }

    @Override
    public Country getByTitle(String title) {
        Query query = sessionFactory.getCurrentSession().createQuery("from Country where title = :title");
        query.setParameter("title", title);
        return (Country) query.list().get(0);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Country> getAll() {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Country.class);
        return criteria.list();
    }

}
