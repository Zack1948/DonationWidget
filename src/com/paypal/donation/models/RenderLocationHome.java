package com.paypal.donation.models;
// Generated Feb 23, 2012 2:26:05 PM by Hibernate Tools 3.4.0.CR1

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.paypal.donation.utils.HibernateUtil;

/**
 * Home object for domain model class RenderLocation.
 * @see .RenderLocation
 * @author Hibernate Tools
 */
final public class RenderLocationHome {

	private static final Log LOG = LogFactory.getLog(RenderLocationHome.class);
	private final SessionFactory sessionFactory = getSessionFactory();

	static RenderLocationHome singleton = new RenderLocationHome();
	
	private RenderLocationHome() { }

	public static RenderLocationHome getInstance() {
		return singleton;
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

	public void persist(RenderLocation transientInstance) {
		LOG.debug("persisting RenderLocation instance");
		try {
			sessionFactory.getCurrentSession().beginTransaction();
			sessionFactory.getCurrentSession().persist(transientInstance);
			sessionFactory.getCurrentSession().getTransaction().commit();
			LOG.debug("persist successful");
		} catch (RuntimeException re) {
			LOG.error("render location persist failed", re);
			LOG.error("Is donation data - "+ transientInstance.getDonateflag());
			LOG.error("Render Location info - "+ transientInstance.getIpaddress() + transientInstance.getCountry());
		}
	}

	public void attachDirty(RenderLocation instance) {
		LOG.debug("attaching dirty RenderLocation instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			LOG.debug("attach successful");
		} catch (RuntimeException re) {
			LOG.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(RenderLocation instance) {
		LOG.debug("attaching clean RenderLocation instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			LOG.debug("attach successful");
		} catch (RuntimeException re) {
			LOG.error("attach failed", re);
			throw re;
		}
	}

	public void delete(RenderLocation persistentInstance) {
		LOG.debug("deleting RenderLocation instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			LOG.debug("delete successful");
		} catch (RuntimeException re) {
			LOG.error("delete failed", re);
			throw re;
		}
	}

	public RenderLocation merge(RenderLocation detachedInstance) {
		LOG.debug("merging RenderLocation instance");
		try {
			RenderLocation result = (RenderLocation) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			LOG.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			LOG.error("merge failed", re);
			throw re;
		}
	}

	public RenderLocation findById(java.lang.Integer id) {
		LOG.debug("getting RenderLocation instance with id: " + id);
		try {
			sessionFactory.getCurrentSession().beginTransaction();
			RenderLocation instance = (RenderLocation) sessionFactory
					.getCurrentSession().get("RenderLocation", id);
			if (instance == null) {
				LOG.debug("get successful, no instance found");
			} else {
				LOG.debug("get successful, instance found");
			}
			sessionFactory.getCurrentSession().getTransaction().commit();
			return instance;
		} catch (RuntimeException re) {
			LOG.error("get failed", re);
			sessionFactory.getCurrentSession().getTransaction().rollback();
			throw re;
		}
	}

	public List<RenderLocation> findByExample(RenderLocation instance) {
		LOG.debug("finding RenderLocation instance by example");
		try {
			@SuppressWarnings("unchecked")
			List<RenderLocation> results = sessionFactory.getCurrentSession()
					.createCriteria("RenderLocation")
					.add(Example.create(instance)).list();
			LOG.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			LOG.error("find by example failed", re);
			throw re;
		}
	}
}
