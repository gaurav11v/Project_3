package in.co.rays.project_3.model;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import in.co.rays.project_3.dto.StockPurchaseDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.util.HibDataSource;

public class StockPurchaseModelHibImp implements StockPurchaseModelInt {

	@Override
	public long add(StockPurchaseDTO dto) throws ApplicationException, DuplicateRecordException {
		Session session = null;
		Transaction tx = null;
		StockPurchaseDTO duplicatePurchasePrice = fingByPurchasePrice(dto.getPurchasePrice());
		if (duplicatePurchasePrice != null) {
			throw new DuplicateRecordException("Purchase Price already exist");
		}
		try {
			session = HibDataSource.getSession();
			tx = session.beginTransaction();
			session.save(dto);
			tx.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();

			}
			throw new ApplicationException("Exception in Stock Purchase Add " + e.getMessage());
		} finally {
			session.close();
		}
		return dto.getId();
	}

	@Override
	public void delete(StockPurchaseDTO dto) throws ApplicationException {
		Session session = null;
		Transaction tx = null;
		try {
			session = HibDataSource.getSession();
			tx = session.beginTransaction();
			session.delete(dto);
			tx.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();

			}
			throw new ApplicationException("Exception in Stock Purchase Delete " + e.getMessage());
		} finally {
			session.close();
		}
	}

	@Override
	public void update(StockPurchaseDTO dto) throws ApplicationException, DuplicateRecordException {
		Session session = null;
		Transaction tx = null;
		StockPurchaseDTO dtoExist = fingByPurchasePrice(dto.getPurchasePrice());

		// Check if updated College already exist
		/*
		 * if (dtoExist != null && dtoExist.getId() != dto.getId()) { throw new
		 * DuplicateRecordException("College is already exist"); }
		 */
		try {
			session = HibDataSource.getSession();
			tx = session.beginTransaction();
			System.out.println("before update");

			session.saveOrUpdate(dto);
			System.out.println("after update");
			tx.commit();

		} catch (HibernateException e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ApplicationException("Exception in Stock Purchase update" + e.getMessage());
		} finally {
			session.close();
		}

	}

	public List list() throws ApplicationException {
		return list(0, 0);
	}

	public List list(int pageNo, int pageSize) throws ApplicationException {
		Session session = null;
		List list = null;
		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(StockPurchaseDTO.class);
			if (pageSize > 0) {
				pageNo = ((pageNo - 1) * pageSize) + 1;
				criteria.setFirstResult(pageNo);
				criteria.setMaxResults(pageSize);
			}
			list = criteria.list();

		} catch (HibernateException e) {

			throw new ApplicationException("Exception : Exception in  Stock Purchase list");
		} finally {
			session.close();
		}

		return list;
	}

	public List search(StockPurchaseDTO dto) throws ApplicationException {
		return search(dto, 0, 0);
	}

	public List search(StockPurchaseDTO dto, int pageNo, int pageSize) throws ApplicationException {
		Session session = null;
		List list = null;
		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(StockPurchaseDTO.class);
			if (dto != null) {
				if (dto.getId() > 0) {
					criteria.add(Restrictions.eq("id", dto.getId()));

				}
				if (dto.getQuantity() != null && dto.getQuantity().length() > 0) {
					criteria.add(Restrictions.like("quantity", dto.getQuantity() + "%"));
				}
				if (dto.getPurchasePrice() != null && dto.getPurchasePrice().length() > 0) {
					criteria.add(Restrictions.like("purchasePrice", dto.getPurchasePrice() + "%"));
				}
				
			}
			/*
			 * if(dto.getPurchasePrice()!=null&&dto.getPurchasePrice().length()>0){
			 * criteria.add(Restrictions.like("purchase_price",
			 * dto.getPurchasePrice()+"%")); }
			 * if(dto.getState()!=null&&dto.getState().length()>0){
			 * criteria.add(Restrictions.like("state", dto.getState()+"%")); }
			 * if(dto.getCity()!=null&&dto.getCity().length()>0){
			 * criteria.add(Restrictions.like("city", dto.getCity()+"%")); }
			 */
			if (pageSize > 0) {
				criteria.setFirstResult((pageNo - 1) * pageSize);
				criteria.setMaxResults(pageSize);

			}
			list = criteria.list();
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new ApplicationException("Exception in Stock Purchase search");
		} finally {
			session.close();
		}
		return list;
	}

	public StockPurchaseDTO findByPK(long pk) throws ApplicationException {
		System.out.println("======" + pk + "----------------------------------");
		Session session = null;
		StockPurchaseDTO dto = null;
		try {
			session = HibDataSource.getSession();

			dto = (StockPurchaseDTO) session.get(StockPurchaseDTO.class, pk);
			System.out.println(dto);
		} catch (HibernateException e) {

			throw new ApplicationException("Exception : Exception in getting course by pk");
		} finally {
			session.close();
		}
		System.out.println("++++" + dto);
		return dto;
	}

	public StockPurchaseDTO fingByPurchasePrice(String purchasePrice) throws ApplicationException {
		Session session = null;
		StockPurchaseDTO dto = null;
		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(StockPurchaseDTO.class);
			criteria.add(Restrictions.eq("purchasePrice", purchasePrice));
			List list = criteria.list();
			if (list.size() == 1) {
				dto = (StockPurchaseDTO) list.get(0);
			}
		} catch (HibernateException e) {

			throw new ApplicationException("Exception in getting User by Login " + e.getMessage());

		} finally {
			session.close();
		}
		return dto;
	}

}