package com.paypal.donation.models;

// Generated Dec 9, 2011 10:53:44 AM by Hibernate Tools 3.4.0.CR1

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;

import com.paypal.donation.services.ApplicationProperties;
import com.paypal.donation.utils.Constants;
import com.paypal.donation.utils.HibernateUtil;
import com.paypal.donation.utils.SHA1Hash;

/**
 * Home object for domain model class Widget.
 * @see com.paypal.donation.models.Widget
 * @author Hibernate Tools
 */
@SuppressWarnings("unchecked")
final public class WidgetHome {

	private static final Log LOG = LogFactory.getLog(WidgetHome.class);

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
	
	private static WidgetHome singleton = new WidgetHome();

	public static WidgetHome getInstance() {
		return singleton;
	}
	
	private WidgetHome(){
		
	}

	public void persist(Widget widget) {
		LOG.debug("persisting Widget instance");
		try {
			sessionFactory.getCurrentSession().beginTransaction();
			sessionFactory.getCurrentSession().persist(widget);
			int random = (int)(Math.random() * 1000000);
			widget.setWidgetexternalid(SHA1Hash.SHA1_32("Donate"+random+":"+widget.getId()));
			sessionFactory.getCurrentSession().merge(widget);
			sessionFactory.getCurrentSession().getTransaction().commit();
		} catch (RuntimeException re) {
			sessionFactory.getCurrentSession().getTransaction().rollback();
			LOG.error("persist failed", re);
			throw re;
		}
		finally {
			
		}
	}

	public void attachDirty(Widget instance) {
		LOG.debug("attaching dirty Widget instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			LOG.debug("attach successful");
		} catch (RuntimeException re) {
			LOG.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Widget instance) {
		LOG.debug("attaching clean Widget instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			LOG.debug("attach successful");
		} catch (RuntimeException re) {
			LOG.error("attach failed", re);
			throw re;
		}
	}

	public void delete(Widget persistentInstance) {
		LOG.debug("deleting Widget instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			LOG.debug("delete successful");
		} catch (RuntimeException re) {
			LOG.error("delete failed", re);
			throw re;
		}
	}
	
	public Widget merge(Widget widgetMerge) {
		LOG.debug("merging Widget instance");
		try {
			sessionFactory.getCurrentSession().beginTransaction();
			sessionFactory.getCurrentSession().get(Widget.class, widgetMerge.getId());
			Widget result = (Widget) sessionFactory.getCurrentSession().merge(widgetMerge);
			sessionFactory.getCurrentSession().getTransaction().commit();
			LOG.debug("merge Widget instance successful");
			return result;
		} catch (RuntimeException re) {
			sessionFactory.getCurrentSession().getTransaction().rollback();
			LOG.error("merge Widget instance failed", re);
			throw re;
		}
	}

	public Widget findById(long id) {
		LOG.debug("getting Widget instance with id: " + id);
		try {
			sessionFactory.getCurrentSession().beginTransaction();
			Widget instance = (Widget) sessionFactory.getCurrentSession().get("com.paypal.donation.models.Widget", id);
			sessionFactory.getCurrentSession().getTransaction().commit();
			if (instance == null) {
				LOG.debug("get widget findById successful, no instance found");
			} else {
				LOG.debug("get widget findById successful, instance found");
			}
			return instance;
		} catch (RuntimeException re) {
			LOG.error("get widget findById failed", re);
			sessionFactory.getCurrentSession().getTransaction().rollback();
			throw re;
		}
	}

	public Widget findByWidgetExternalId(String widgetExternalId) {
		LOG.debug("finding widget instance by widget external id");
		try {
			sessionFactory.getCurrentSession().beginTransaction();
			List<Widget> results = sessionFactory.getCurrentSession().createQuery(
							"from com.paypal.donation.models.Widget wg where wg.widgetexternalid=?").setString(0, widgetExternalId).list();
			if (results.size() > 0) {
				LOG.debug("found widget instance by external id:"+widgetExternalId);
				return results.get(0);
			} else {
				LOG.debug("no widget instance found by external id:"+widgetExternalId);
			}
			sessionFactory.getCurrentSession().getTransaction().commit();
		} catch (RuntimeException re) {
			LOG.error("find widget instance by widget external id failed for:"+widgetExternalId);
			sessionFactory.getCurrentSession().getTransaction().rollback();
			throw re;
		}
		return null;
	}
	
	public Widget findByVettingRequestId(String vettingRequestId) {
		LOG.debug("finding widget instance by vetting request id");
		try {
			sessionFactory.getCurrentSession().beginTransaction();
			List<Widget> results = sessionFactory.getCurrentSession().createQuery(
							"from com.paypal.donation.models.Widget wg where wg.vettingRequestId=?").setString(0, vettingRequestId).list();
			if (results.size() > 0) {
				LOG.debug("found widget instance by vetting request id:"+vettingRequestId);
				return results.get(0);
			} else {
				LOG.debug("no widget instance found by vetting request id:"+vettingRequestId);
			}
			sessionFactory.getCurrentSession().getTransaction().commit();
		} catch (RuntimeException re) {
			LOG.error("find widget instance by vetting request id failed for:"+vettingRequestId);
			sessionFactory.getCurrentSession().getTransaction().rollback();
			throw re;
		}
		return null;
	}
	
/*	public List<Widget> findByExample(Widget instance) {
		log.debug("finding Widget instance by example");
		try {
			List<Widget> results = (List<Widget>) sessionFactory
					.getCurrentSession()
					.createCriteria("com.paypal.models.Widget")
					.add(create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}*/
	
	public void updateEinStatus(String requestStatus, Widget widget){
		char statusValue = 'N';
		if("1".equals(requestStatus)){
			statusValue = ApplicationProperties.getProperty(Constants.VETTING_INPROCESS_CHAR).charAt(0);
		}else{
			statusValue = ApplicationProperties.getProperty(Constants.VETTING_NOT_INITIATED_CHAR).charAt(0);
		}
		widget.setEinVerified(statusValue);
		merge(widget);
	}
}
