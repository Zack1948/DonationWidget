package com.paypal.donation.services;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.paypal.donation.models.User;
import com.paypal.donation.utils.HibernateUtil;

public final class UserService {

	private static final Log LOG = LogFactory.getLog(UserService.class);

	private static UserService usrSvcSingleton = new UserService();

	public static UserService getInstance() {
		return usrSvcSingleton;
	}

	private UserService() {

	}

	@SuppressWarnings({ "unchecked" })
	public User findUserByEmail(final String email) throws Exception {
		LOG.debug("Start function - findUserByEmail");
		User oneUser = null;
		try {
			HibernateUtil.getSessionFactory().getCurrentSession()
					.beginTransaction();
			final List<User> results = HibernateUtil
					.getSessionFactory()
					.getCurrentSession()
					.createQuery(
							"from com.paypal.donation.models.User e where e.emailId=?")
					.setString(0, email).list();
			if (results.isEmpty()) {
				LOG.debug("Users could not be found");
			} else {
				LOG.debug("Users with email already exists");
				oneUser = (User) results.get(0);
			}
			HibernateUtil.getSessionFactory().getCurrentSession()
					.getTransaction().commit();

		} catch (Exception e) {
			HibernateUtil.getSessionFactory().getCurrentSession()
					.getTransaction().rollback();
			throw e;
		}
		LOG.debug("End function - findUserByEmail");
		return oneUser;
	}

	@SuppressWarnings({ "unchecked" })
	public User findUserByPayerId(final String payerId) throws Exception {
		LOG.debug("Start function - findUserByPayerId");
		User oneUser = null;
		try {
			HibernateUtil.getSessionFactory().getCurrentSession()
					.beginTransaction();
			final List<User> results = HibernateUtil
					.getSessionFactory()
					.getCurrentSession()
					.createQuery(
							"from com.paypal.donation.models.User e where e.payerId=?")
					.setString(0, payerId).list();

			if (results.isEmpty()) {
				LOG.debug("Users could not be found");
			} else {
				LOG.debug("Users with payerId already exists");
				oneUser = (User) results.get(0);
			}

			HibernateUtil.getSessionFactory().getCurrentSession()
					.getTransaction().commit();
		} catch (Exception e) {
			HibernateUtil.getSessionFactory().getCurrentSession()
					.getTransaction().rollback();
			// LOGGER.error(Utility.getStackTrace(e));
			throw e;
		}
		LOG.debug("End function - findUserByPayerId");
		return oneUser;
	}

	public User upsertUser(final User user) throws Exception {
		User oneUser = null;
		LOG.debug("Start function - upsertUser");
		try {
			HibernateUtil.getSessionFactory().getCurrentSession()
					.beginTransaction();
			HibernateUtil.getSessionFactory().getCurrentSession().persist(user);
			HibernateUtil.getSessionFactory().getCurrentSession()
					.getTransaction().commit();

		} catch (Exception e) {
			HibernateUtil.getSessionFactory().getCurrentSession()
					.getTransaction().rollback();
			throw e;
		}
		LOG.debug("End function - upsertUser");
		return oneUser;
	}
}
