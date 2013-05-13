package com.paypal.donation.models;

// Generated Dec 9, 2011 10:53:44 AM by Hibernate Tools 3.4.0.CR1

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.LockMode;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;

import com.paypal.donation.services.ApplicationProperties;
import com.paypal.donation.utils.HibernateUtil;

/**
 * Home object for domain model class Widgetui.
 * @see com.paypal.donation.models.Widgetui
 * @author Hibernate Tools
 */
@SuppressWarnings("unchecked")
final public class WidgetuiHome {

	private static final Log LOG = LogFactory.getLog(WidgetuiHome.class);

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
	
	static WidgetuiHome singleton = new WidgetuiHome();

	public static WidgetuiHome getInstance() {
		return singleton;
	}
	
	private WidgetuiHome(){
		
	}

	public void persist(Widgetui widgetui) {
		LOG.debug("persisting Widgetui instance");
		try {
			sessionFactory.getCurrentSession().beginTransaction();
			sessionFactory.getCurrentSession().persist(widgetui);
			sessionFactory.getCurrentSession().getTransaction().commit();
		} catch (RuntimeException re) {
			sessionFactory.getCurrentSession().getTransaction().rollback();
			LOG.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(Widgetui instance) {
		LOG.debug("attaching dirty Widgetui instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			LOG.debug("attach successful");
		} catch (RuntimeException re) {
			LOG.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Widgetui instance) {
		LOG.debug("attaching clean Widgetui instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			LOG.debug("attach successful");
		} catch (RuntimeException re) {
			LOG.error("attach failed", re);
			throw re;
		}
	}

	public void delete(Widgetui persistentInstance) {
		LOG.debug("deleting Widgetui instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			LOG.debug("delete successful");
		} catch (RuntimeException re) {
			LOG.error("delete failed", re);
			throw re;
		}
	}

	public Widgetui merge(Widgetui widgetUIMerge) {
		LOG.debug("merging Widgetui instance");
		try {
			sessionFactory.getCurrentSession().beginTransaction();
			sessionFactory.getCurrentSession().get(Widgetui.class, widgetUIMerge.getId());
			Widgetui result = (Widgetui) sessionFactory.getCurrentSession().merge(widgetUIMerge);
			sessionFactory.getCurrentSession().getTransaction().commit();
			LOG.debug("merge Widgetui instance successful");
			return result;
		} catch (RuntimeException re) {
			sessionFactory.getCurrentSession().getTransaction().rollback();
			LOG.error("merge Widgetui instance failed", re);
			throw re;
		}
	}

	public Widgetui findById(java.lang.Integer id) {
		LOG.debug("getting Widgetui instance with id: " + id);
		try {
			Widgetui instance = (Widgetui) sessionFactory.getCurrentSession()
					.get("com.paypal.donation.models.Widgetui", id);
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
	
	public Widgetui findByWidgetId(long widgetId) {
		LOG.debug("finding widgetui instances by widget id");
		try {
			sessionFactory.getCurrentSession().beginTransaction();
			List<Widgetui> results = sessionFactory.getCurrentSession().createQuery(
							"from com.paypal.donation.models.Widgetui wui where wui.widgetid=?").setLong(0, widgetId).list();
			if (!results.isEmpty()) {
				LOG.debug("found widgetui instance");
			} else {
				LOG.debug("no widgetui instance found");
			}
			sessionFactory.getCurrentSession().getTransaction().commit();
			return results.get(0);
		} catch (RuntimeException re) {
			LOG.error("find widgetui instances by widget id failed");
			sessionFactory.getCurrentSession().getTransaction().rollback();
			throw re;
		}
	}
	
	public List<Widgetui> findWidgetsByPayerId(String payerId){
		LOG.debug("finding widgetui instances by payer id");
		List<Widgetui> results = null;
		try {
			sessionFactory.getCurrentSession().beginTransaction();
			results = sessionFactory.getCurrentSession().createQuery(
							" select wui from com.paypal.donation.models.Widgetui wui, com.paypal.donation.models.Widget wi where wui.widgetid = wi.id and wi.status != 'D' and wui.payerid=?").setString(0, payerId).list();
			if (!results.isEmpty()) {
				LOG.debug("found widgetui instances");
			} else {
				LOG.debug("no widgetui instances found");
			}
			sessionFactory.getCurrentSession().getTransaction().commit();
			return results;
		} catch (RuntimeException re) {
			LOG.error("find widgetui instances by payer id failed");
			sessionFactory.getCurrentSession().getTransaction().rollback();
			throw re;
		} 
	}
	
	public List<Object[]> findWidgetDetailsByPayerId(String payerId) {
		try {
			sessionFactory.getCurrentSession().beginTransaction();
			SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(ApplicationProperties.getProperty("GET_WIDGETS_SUMMARY_QUERY"))
					.addScalar("widgetexternalid", Hibernate.STRING)
					.addScalar("title", Hibernate.STRING)
					.addScalar("goal", Hibernate.DOUBLE)
					.addScalar("amount", Hibernate.DOUBLE);
			List<Object[]> results = (List<Object[]>)(query
					.setParameter(0, payerId,Hibernate.STRING)
					.setParameter(1, payerId,Hibernate.STRING))
					.list();
			sessionFactory.getCurrentSession().getTransaction().commit();
			return results;
		} catch (RuntimeException re) {
			LOG.error("find widget details by payer id failed");
			sessionFactory.getCurrentSession().getTransaction().rollback();
			throw re;

		}
	}
}
