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
import com.paypal.donation.utils.Constants;
import com.paypal.donation.utils.HibernateUtil;

import static org.hibernate.criterion.Example.create;

/**
 * Home object for domain model class Transaction.
 * @see com.paypal.donation.models.Transaction
 * @author Hibernate Tools
 */
final public class TransactionHome {

	private static final Log LOG = LogFactory.getLog(TransactionHome.class);

	private final SessionFactory sessionFactory = getSessionFactory();
	
	static TransactionHome singleton = new TransactionHome();

	public static TransactionHome getInstance() {
		return singleton;
	}

	private TransactionHome() {
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

	public void persist(Transaction transientInstance) throws Exception {
		LOG.debug("persisting Transaction instance");
		try {
			sessionFactory.getCurrentSession().beginTransaction();
			sessionFactory.getCurrentSession().persist(transientInstance);
			sessionFactory.getCurrentSession().getTransaction().commit();
			LOG.debug("persist successful");			
		}  catch (Exception e) {
			sessionFactory.getCurrentSession().getTransaction().rollback();
			throw e;
			
		} finally {
			
		}
	}

	public void attachDirty(Transaction instance) {
		LOG.debug("attaching dirty Transaction instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			LOG.debug("attach successful");
		} catch (RuntimeException re) {
			LOG.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Transaction instance) {
		LOG.debug("attaching clean Transaction instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			LOG.debug("attach successful");
		} catch (RuntimeException re) {
			LOG.error("attach failed", re);
			throw re;
		}
	}

	public void delete(Transaction persistentInstance) {
		LOG.debug("deleting Transaction instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			LOG.debug("delete successful");
		} catch (RuntimeException re) {
			LOG.error("delete failed", re);
			throw re;
		}
	}

	public Transaction merge(Transaction detachedInstance) {
		LOG.debug("merging Transaction instance");
		try {
			sessionFactory.getCurrentSession().beginTransaction();
			sessionFactory.getCurrentSession().get(Transaction.class, detachedInstance.getId());
			Transaction result = (Transaction) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			LOG.debug("merge successful");
			sessionFactory.getCurrentSession().getTransaction().commit();
			return result;
		} catch (RuntimeException re) {
			LOG.error("merge failed", re);
			sessionFactory.getCurrentSession().getTransaction().rollback();
			throw re;
		} finally {
			
		}
	}

	public Transaction findById(java.lang.Integer id) {
		LOG.debug("getting Transaction instance with id: " + id);
		try {
			Transaction instance = (Transaction) sessionFactory
					.getCurrentSession().get("com.paypal.donation.models.Transaction",
							id);
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
	public List<Transaction> findByExample(Transaction instance) {
		LOG.debug("finding Transaction instance by example");
		try {
			List<Transaction> results = (List<Transaction>) sessionFactory
					.getCurrentSession()
					.createCriteria("com.paypal.donation.models.Transaction")
					.add(create(instance)).list();
			LOG.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			LOG.error("find by example failed", re);
			throw re;
		}
	}
	@SuppressWarnings("unchecked")
	public Transaction findByTransactionId(String transactionId) throws Exception {
		LOG.debug("getting Transaction instance with transaction id: " + transactionId);
		try {
			Transaction transaction = null;
			sessionFactory.getCurrentSession().beginTransaction();
			List<Transaction> instance = sessionFactory
					.getCurrentSession().createQuery(
							"from com.paypal.donation.models.Transaction t where t.paypaltransid=?")
							.setString(0, transactionId)
							.list();
			if (instance == null || instance.size() == 0) {
				LOG.debug("get successful, no instance found");
			} else {
				LOG.debug("get successful, instance found");
				transaction = instance.get(0);
			}
			sessionFactory.getCurrentSession().getTransaction().commit();
			return transaction;
		} catch (Exception e) {
			sessionFactory.getCurrentSession().getTransaction().rollback();
			throw e;
			
		} finally {
			
		}
	}
	
	@SuppressWarnings("unchecked")
	public Double findTotalGoalAmountCollected(String widgetId) throws Exception{
		LOG.debug("getting Transaction instance with transaction id: " + widgetId);
		try {
			Double amount = null;
			sessionFactory.getCurrentSession().beginTransaction();
			String sql = ApplicationProperties.getProperty(Constants.TRANSACTION_SUM_QUERY);
			SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(sql);
			query.setParameter(0, widgetId, Hibernate.STRING);
			List<Double> instance = (List<Double>)query.list();
			if (instance == null || instance.size() == 0) {
				LOG.debug("get successful, no instance found");
				amount = 0.0;
			} else {
				LOG.debug("get successful, instance found");
				amount = (Double) instance.get(0);
			}
			sessionFactory.getCurrentSession().getTransaction().commit();
			return amount;
		} catch (Exception e) {
			sessionFactory.getCurrentSession().getTransaction().rollback();
			throw e;			
		} finally {
			
		}
		
	}	
}
