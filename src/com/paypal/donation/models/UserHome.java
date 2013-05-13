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
 * Home object for domain model class User.
 * @see com.paypal.donation.models.User
 * @author Hibernate Tools
 */
final public class UserHome {

	private static final Log LOG = LogFactory.getLog(UserHome.class);

	private final SessionFactory sessionFactory = getSessionFactory();
	
	private static UserHome singleton = new UserHome();

	public static UserHome getInstance() {
		return singleton;
	}

	private UserHome() {
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

	public void persist(User transientInstance) {
		LOG.debug("persisting User instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			LOG.debug("persist successful");
		} catch (RuntimeException re) {
			LOG.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(User instance) {
		LOG.debug("attaching dirty User instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			LOG.debug("attach successful");
		} catch (RuntimeException re) {
			LOG.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(User instance) {
		LOG.debug("attaching clean User instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			LOG.debug("attach successful");
		} catch (RuntimeException re) {
			LOG.error("attach failed", re);
			throw re;
		}
	}

	public void delete(User persistentInstance) {
		LOG.debug("deleting User instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			LOG.debug("delete successful");
		} catch (RuntimeException re) {
			LOG.error("delete failed", re);
			throw re;
		}
	}

	public User merge(User user) {
		LOG.debug("merging User instance");
		try {
			sessionFactory.getCurrentSession().beginTransaction();
			sessionFactory.getCurrentSession().get(User.class, user.getId());			
			User result = (User) sessionFactory.getCurrentSession().merge(user);
			sessionFactory.getCurrentSession().getTransaction().commit();
			LOG.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			LOG.error("merge failed", re);
			sessionFactory.getCurrentSession().getTransaction().rollback();
			throw re;
		}
	}

	public User findById(java.lang.Integer id) {
		LOG.debug("getting User instance with id: " + id);
		try {
			User instance = (User) sessionFactory.getCurrentSession().get(
					"com.paypal.donation.models.User", id);
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
	public List<User> findByExample(User instance) {
		LOG.debug("finding User instance by example");
		try {
			List<User> results = (List<User>) sessionFactory
					.getCurrentSession()
					.createCriteria("com.paypal.donation.models.User")
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
