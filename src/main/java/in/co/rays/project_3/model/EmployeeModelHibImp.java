package in.co.rays.project_3.model;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import in.co.rays.project_3.dto.EmployeeDTO;
import in.co.rays.project_3.dto.StockPurchaseDTO;
import in.co.rays.project_3.dto.UserDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.util.HibDataSource;

public class EmployeeModelHibImp implements EmployeeModelInt {

	@Override
	public long add(EmployeeDTO dto) throws ApplicationException, DuplicateRecordException {
		Session session = null;
		Transaction tx = null;
		EmployeeDTO duplicateUserName = findByUserName(dto.getUserName());
		if (duplicateUserName != null) {
			throw new DuplicateRecordException("User Name already exist");
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
			throw new ApplicationException("Exception in Employee Add " + e.getMessage());
		} finally {
			session.close();
		}
		return dto.getId();

	}

	@Override
	public void delete(EmployeeDTO dto) throws ApplicationException {

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
			throw new ApplicationException("Exception in Employee Delete " + e.getMessage());
		} finally {
			session.close();
		}
	}

	@Override
	public void update(EmployeeDTO dto) throws ApplicationException, DuplicateRecordException {
		Session session = null;
		Transaction tx = null;
		EmployeeDTO existDto = findByUserName(dto.getUserName());
		// Check if updated LoginId already exist
		if (existDto != null && existDto.getId() != dto.getId()) {

			System.out.println("in duplicate reocord  condition model update");
			throw new DuplicateRecordException("User Name is already exist");
		}
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
			throw new ApplicationException("Exception in Employee update" + e.getMessage());
		} finally {
			session.close();
		}
	}

	@Override
	public EmployeeDTO findByPK(long pk) throws ApplicationException {
		System.out.println("======" + pk + "----------------------------------");
		Session session = null;
		EmployeeDTO dto = null;
		try {
			session = HibDataSource.getSession();

			dto = (EmployeeDTO) session.get(EmployeeDTO.class, pk);
			System.out.println(dto);
		} catch (HibernateException e) {

			throw new ApplicationException("Exception : Exception in getting EmployeeDTO by pk");
		} finally {
			session.close();
		}
		System.out.println("++++" + dto);
		return dto;
	}

	@Override
	public EmployeeDTO findByUserName(String userName) throws ApplicationException {
		Session session = null;
		EmployeeDTO dto = null;
		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(EmployeeDTO.class);
			criteria.add(Restrictions.eq("userName", userName));
			List list = criteria.list();
			if (list.size() == 1) {
				dto = (EmployeeDTO) list.get(0);
			}
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new ApplicationException("Exception in getting Employee by User Name " + e.getMessage());

		} finally {
			session.close();
		}

		return dto;
	}

	@Override
	public List list() throws ApplicationException {
		return list(0, 0);
	}

	@Override
	public List list(int pageNo, int pageSize) throws ApplicationException {
		Session session = null;
		List list = null;
		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(EmployeeDTO.class);
			if (pageSize > 0) {
				pageNo = ((pageNo - 1) * pageSize) + 1;
				criteria.setFirstResult(pageNo);
				criteria.setMaxResults(pageSize);
			}
			list = criteria.list();

		} catch (HibernateException e) {

			throw new ApplicationException("Exception : Exception in  Employee list");
		} finally {
			session.close();
		}

		return list;
	}

	@Override
	public List search(EmployeeDTO dto, int pageNo, int pageSize) throws ApplicationException {
		Session session = null;
		List list = null;
		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(EmployeeDTO.class);
			if (dto != null) {
			
				if (dto.getId()!= null && dto.getId() > 0) {
					criteria.add(Restrictions.eq("id", dto.getId()));
				}
				if (dto.getFullName() != null && dto.getFullName().length() > 0) {
					criteria.add(Restrictions.like("fullName", dto.getFullName() + "%"));
				}
				if (dto.getUserName() != null && dto.getUserName().length() > 0) {
					criteria.add(Restrictions.like("userName", dto.getUserName() + "%"));
				}
				if (dto.getPassword() != null && dto.getPassword().length() > 0) {
					criteria.add(Restrictions.like("password", dto.getPassword() + "%"));
				}
				if (dto.getContactNumber() != null && dto.getContactNumber().length() > 0) {
					criteria.add(Restrictions.like("contactNumber", dto.getContactNumber() + "%"));
				}
				if (dto.getBirthDate() != null && dto.getBirthDate().getTime() > 0) {
					criteria.add(Restrictions.eq("birthDate", dto.getBirthDate()));
				}

			}

			if (pageSize > 0) {
				criteria.setFirstResult((pageNo - 1) * pageSize);
				criteria.setMaxResults(pageSize);

			}
			list = criteria.list();
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new ApplicationException("Exception in Employee search");
		} finally {
			session.close();
		}
		return list;
	}

	@Override
	public List search(EmployeeDTO dto) throws ApplicationException {
		return search(dto, 0, 0);
	}

}
