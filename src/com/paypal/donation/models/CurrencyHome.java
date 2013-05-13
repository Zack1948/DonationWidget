package com.paypal.donation.models;

// Generated Dec 9, 2011 10:53:44 AM by Hibernate Tools 3.4.0.CR1

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;

import com.paypal.donation.utils.HibernateUtil;

import static org.hibernate.criterion.Example.create;

/**
 * Home object for domain model class Currency.
 * @see com.paypal.donation.models.Currency
 * @author Hibernate Tools
 */
final public class CurrencyHome {

	private static final Log LOG = LogFactory.getLog(CurrencyHome.class);

	private final SessionFactory sessionFactory = getSessionFactory();
	
	private static CurrencyHome singleton = new CurrencyHome();

	public static CurrencyHome getInstance() {
		return singleton;
	}

	private CurrencyHome() { }
	
	protected SessionFactory getSessionFactory() {
		try {
			return HibernateUtil.getSessionFactory();
	
		} catch (Exception e) {
			LOG.error("Could not locate SessionFactory in JNDI", e);
			throw new IllegalStateException(
					"Could not locate SessionFactory in JNDI");
		}
	}

	public void persist(Currency transientInstance) {
		LOG.debug("persisting Currency instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			LOG.debug("persist successful");
		} catch (RuntimeException re) {
			LOG.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(Currency instance) {
		LOG.debug("attaching dirty Currency instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			LOG.debug("attach successful");
		} catch (RuntimeException re) {
			LOG.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Currency instance) {
		LOG.debug("attaching clean Currency instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			LOG.debug("attach successful");
		} catch (RuntimeException re) {
			LOG.error("attach failed", re);
			throw re;
		}
	}

	public void delete(Currency persistentInstance) {
		LOG.debug("deleting Currency instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			LOG.debug("delete successful");
		} catch (RuntimeException re) {
			LOG.error("delete failed", re);
			throw re;
		}
	}

	public Currency merge(Currency detachedInstance) {
		LOG.debug("merging Currency instance");
		try {
			Currency result = (Currency) sessionFactory.getCurrentSession()
					.merge(detachedInstance);
			LOG.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			LOG.error("merge failed", re);
			throw re;
		}
	}

	public Currency findById(java.lang.Integer id) {
		LOG.debug("getting Currency instance with id: " + id);
		try {
			sessionFactory.getCurrentSession().beginTransaction();
			Currency instance = (Currency) sessionFactory.getCurrentSession()
					.get("com.paypal.donation.models.Currency", id);
			if (instance == null) {
				LOG.debug("get successful, no instance found");
			} else {
				LOG.debug("get successful, instance found");
			}
			return instance;
		} catch (RuntimeException re) {
			sessionFactory.getCurrentSession().getTransaction().rollback();
			LOG.error("get failed", re);
			throw re;
		} finally {
			sessionFactory.getCurrentSession().getTransaction().commit();
		}
	}

	@SuppressWarnings("unchecked")
	public List<Currency> findByExample(Currency instance) {
		LOG.debug("finding Currency instance by example");
		try {
			sessionFactory.getCurrentSession().beginTransaction();
			List<Currency> results = (List<Currency>) sessionFactory
					.getCurrentSession()
					.createCriteria("com.paypal.donation.models.Currency")
					.add(create(instance)).list();
			LOG.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			sessionFactory.getCurrentSession().getTransaction().rollback();
			LOG.error("find by example failed", re);
			throw re;
		} finally {
			sessionFactory.getCurrentSession().getTransaction().commit();
		}
	}
	
	public List<Currency> getAllCurrencies() {
		LOG.debug("getting all Currencies");
		try {
			sessionFactory.getCurrentSession().beginTransaction();
			@SuppressWarnings("unchecked")
			List<Currency> instance = sessionFactory.getCurrentSession().createQuery("from Currency").list();
			//This works too..
			//List<Currency> instance = sessionFactory.getCurrentSession().createSQLQuery("Select * from Currency").addEntity(Currency.class).list();			
			sessionFactory.getCurrentSession().getTransaction().commit();
			return instance;
		} catch (RuntimeException re) {
			sessionFactory.getCurrentSession().getTransaction().rollback();
			LOG.error("get failed", re);
			throw re;
		} finally {
			
		}
	}
	
}
