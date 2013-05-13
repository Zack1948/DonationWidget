package com.paypal.donation.services;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;

import com.paypal.donation.models.TransactionHome;
import com.paypal.donation.models.Widget;
import com.paypal.donation.models.WidgetHome;
import com.paypal.donation.models.Widgetui;
import com.paypal.donation.models.WidgetuiHome;
import com.paypal.donation.utils.HibernateUtil;
import com.paypal.donation.utils.SHA1Hash;
import com.paypal.donation.utils.Utility;

public final class WidgetService {

	private static final Log LOG = LogFactory.getLog(WidgetService.class);
	private static WidgetService singletonObj = new WidgetService();

	public static WidgetService getInstance() {
		return singletonObj;
	}

	private WidgetService() {
	}

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
	
	public Widgetui findWidgetUIByWidgetId(final long widgetId)
			throws Exception {
		LOG.debug("find widgetui instance by widget id");
		try {
			return WidgetuiHome.getInstance().findByWidgetId(widgetId);
		} catch (Exception exc) {
			LOG.error("find widgetui instance by widget id failed");
			LOG.error(Utility.getStackTrace(exc));
			throw exc;
		}
	}
	
	public Widgetui findWidgetuiByExternalId(final String widgetExternalId)
			throws Exception {
		LOG.debug("find widgetui instances by widget external id");
		try {
			return WidgetuiHome.getInstance().findByWidgetId(
					WidgetHome.getInstance()
							.findByWidgetExternalId(widgetExternalId).getId());
		} catch (Exception exc) {
			LOG.error("find widgetui instances by widget external id failed");
			LOG.error(Utility.getStackTrace(exc));
			throw exc;
		}
	}

	public long findTotalGoalAmountCollected(final String widgetExternalId)
			throws Exception {
		LOG.debug("find total amount collected by widget id");
		try {
			final Double amountCollected = TransactionHome.getInstance()
					.findTotalGoalAmountCollected(widgetExternalId);
			return (amountCollected == null ? 0 : amountCollected.longValue());
		} catch (Exception exc) {
			LOG.error("find total amount collected by widget id failed");
			LOG.error(Utility.getStackTrace(exc));
			throw exc;
		}
	}

	public Widget findWidgetByExternalId(final String widgetExternalId)
			throws Exception {
		LOG.debug("find widget instance by widget external id");
		try {
			return WidgetHome.getInstance().findByWidgetExternalId(widgetExternalId);
		} catch (Exception exc) {
			LOG.error("find widget instance by widget external id failed");
			LOG.error(Utility.getStackTrace(exc));
			throw exc;
		}
	}
	
	public List<Object[]> findWidgets(HttpServletRequest request) throws Exception{
		LOG.debug("Starting WidgetService.findWidgets()");
		HttpSession session = request.getSession(false);
		try{
			return WidgetuiHome.getInstance().
			findWidgetDetailsByPayerId((String)session.getAttribute("PAYER_ID"));
		}catch(Exception exc){
			LOG.error(Utility.getStackTrace(exc));
			throw exc;
		}
	}

	public String saveOrUpdateWidget(String purpose, Widget widget, Widgetui widgetUI)  throws Exception {
		String widgetExternalId = null;
		try {
			sessionFactory.getCurrentSession().beginTransaction();
			if("Save".equals(purpose)) {
				sessionFactory.getCurrentSession().persist(widget);
				int random = (int)(Math.random() * 1000000);
				widget.setWidgetexternalid(SHA1Hash.SHA1_32("Donate"+random+":"+widget.getId()));
				sessionFactory.getCurrentSession().merge(widget);
				widgetUI.setWidgetid(widget.getId());
				sessionFactory.getCurrentSession().persist(widgetUI);
			} else if ("Update".equals(purpose)) {
				widget.setUpdatedDt(new Date());
				sessionFactory.getCurrentSession().merge(widget);
				sessionFactory.getCurrentSession().merge(widgetUI);
			}
			widgetExternalId = widget.getWidgetexternalid();
			sessionFactory.getCurrentSession().getTransaction().commit();
			return widgetExternalId;
		} catch (RuntimeException re) {
			sessionFactory.getCurrentSession().getTransaction().rollback();
			LOG.error("persist failed", re);
			throw re;
		}
	}
	
	public String shortenUrl(String widgetExternalId) {
		LOG.debug("Start function - shortenURL");
		String shortUrl = null;
		try {
			String longUrl = Utility.getServerUrl()+"/r?i="+widgetExternalId;
			shortUrl = Utility.shortURL(longUrl);
		} catch (Exception exc) {
			LOG.error(Utility.getStackTrace(exc));
		}
		LOG.debug("End function - shortenURL");
		return shortUrl;
	}
}
