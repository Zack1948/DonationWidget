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
 * Home object for domain model class Location.
 * @see com.paypal.donation.models.Location
 * @author Hibernate Tools
 */
final public class LocationHome {

	private static final Log LOG = LogFactory.getLog(LocationHome.class);

	private final SessionFactory sessionFactory = getSessionFactory();
	
	private static LocationHome singleton = new LocationHome();

	public static LocationHome getInstance() {
		return singleton;
	}

	private LocationHome() {
	}
	protected SessionFactory getSessionFactory() {
		try {
			return HibernateUtil.getSessionFactory();
		} catch (Exception e) {
			LOG.error("Could not locate SessionFactory in JNDI", e);
			throw new IllegalStateException(
					"Could not locate SessionFactory in JNDI");
		}
	}

	public void persist(Location transientInstance) {
		LOG.debug("persisting Location instance");
		try {
			sessionFactory.getCurrentSession().beginTransaction();
			sessionFactory.getCurrentSession().persist(transientInstance);	
			sessionFactory.getCurrentSession().getTransaction().commit();
			LOG.debug("persist successful");
		} catch (Exception e) {
			sessionFactory.getCurrentSession().getTransaction().rollback();
			LOG.error(e);
		} finally {
			
		}
	}

	public void attachDirty(Location instance) {
		LOG.debug("attaching dirty Location instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			LOG.debug("attach successful");
		} catch (RuntimeException re) {
			LOG.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Location instance) {
		LOG.debug("attaching clean Location instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			LOG.debug("attach successful");
		} catch (RuntimeException re) {
			LOG.error("attach failed", re);
			throw re;
		}
	}

	public void delete(Location persistentInstance) {
		LOG.debug("deleting Location instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			LOG.debug("delete successful");
		} catch (RuntimeException re) {
			LOG.error("delete failed", re);
			throw re;
		}
	}

	public Location merge(Location detachedInstance) {
		LOG.debug("merging Location instance");
		try {
			Location result = (Location) sessionFactory.getCurrentSession()
					.merge(detachedInstance);
			LOG.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			LOG.error("merge failed", re);
			throw re;
		}
	}

	public Location findById(java.lang.Integer id) {
		LOG.debug("getting Location instance with id: " + id);
		try {
			Location instance = (Location) sessionFactory.getCurrentSession()
					.get("com.paypal.donation.models.Location", id);
			if (instance == null) {
				LOG.debug("get successful, no instance found");
			} else {
				LOG.debug("get successful, instance found");
			}
			return instance;
		} catch (RuntimeException re) {
			LOG.error("get failed", re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Location> findByExample(Location instance) {
		LOG.debug("finding Location instance by example");
		try {
			List<Location> results = (List<Location>) sessionFactory
					.getCurrentSession()
					.createCriteria("com.paypal.donation.models.Location")
					.add(create(instance)).list();
			LOG.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			LOG.error("find by example failed", re);
			throw re;
		}
	}
}
