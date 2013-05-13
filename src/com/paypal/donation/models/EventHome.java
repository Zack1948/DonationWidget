package com.paypal.donation.models;
// Generated Jan 3, 2012 11:34:57 AM by Hibernate Tools 3.4.0.CR1

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.paypal.donation.utils.HibernateUtil;

/**
 * Home object for domain model class Event.
 * @see .Event
 * @author Hibernate Tools
 */
final public class EventHome {

	private static final Log LOG = LogFactory.getLog(EventHome.class);

	private final SessionFactory sessionFactory = getSessionFactory();

	protected SessionFactory getSessionFactory() {
		try {
			return HibernateUtil.getSessionFactory();
		} catch (Exception e) {
			LOG.error("Could not locate SessionFactory in JNDI", e);
			throw new IllegalStateException(
					"Could not locate SessionFactory in JNDI");
		}
	}
	private static EventHome singleton = new EventHome();

	public static EventHome getInstance() {
		return singleton;
	}

	private EventHome(){
		
	}
	public void persist(Event transientInstance) {
		LOG.debug("persisting Event instance");
		try {
			sessionFactory.getCurrentSession().beginTransaction();
			sessionFactory.getCurrentSession().persist(transientInstance);
			sessionFactory.getCurrentSession().getTransaction().commit();
			LOG.debug("persist successful");
		} catch (RuntimeException re) {
			LOG.error("persist failed", re);
			sessionFactory.getCurrentSession().getTransaction().rollback();
			throw re;
		} finally {
			
		}
	}

	public void attachDirty(Event instance) {
		LOG.debug("attaching dirty Event instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			LOG.debug("attach successful");
		} catch (RuntimeException re) {
			LOG.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Event instance) {
		LOG.debug("attaching clean Event instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			LOG.debug("attach successful");
		} catch (RuntimeException re) {
			LOG.error("attach failed", re);
			throw re;
		}
	}

	public void delete(Event persistentInstance) {
		LOG.debug("deleting Event instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			LOG.debug("delete successful");
		} catch (RuntimeException re) {
			LOG.error("delete failed", re);
			throw re;
		}
	}

	public Event merge(Event detachedInstance) {
		LOG.debug("merging Event instance");
		try {
			Event result = (Event) sessionFactory.getCurrentSession().merge(
					detachedInstance);
			LOG.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			LOG.error("merge failed", re);
			throw re;
		}
	}

	public Event findById(java.lang.Integer id) {
		LOG.debug("getting Event instance with id: " + id);
		try {
			Event instance = (Event) sessionFactory.getCurrentSession().get(
					"Event", id);
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

	@SuppressWarnings("rawtypes")
	public List findByExample(Event instance) {
		LOG.debug("finding Event instance by example");
		try {
			List results = sessionFactory.getCurrentSession()
					.createCriteria("Event").add(Example.create(instance))
					.list();
			LOG.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			LOG.error("find by example failed", re);
			throw re;
		}
	}
}
