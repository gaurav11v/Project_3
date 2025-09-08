package in.co.rays.project_3.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import in.co.rays.project_3.dto.ProductDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DatabaseException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.util.JDBCDataSource;

/**
 * JDBC implements of Subject model
 * @author Gaurav Verma
 *
 */
public class ProductModelJDBCImpl implements ProductModelInt {
	private static Logger log = Logger.getLogger(ProductModelJDBCImpl.class);

	public long nextPK() throws DatabaseException {
		log.debug("product pk start");
		Connection con = null;
		long pk = 0;
		try {
			con = JDBCDataSource.getConnection();
			PreparedStatement ps = con.prepareStatement("select max(id) from ST_PRODUCT");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				pk = rs.getLong(1);
				System.out.println(pk);
			}
		} catch (Exception e) {
			log.error(e);
			throw new DatabaseException("Database Exception" + e);

		} finally {
			JDBCDataSource.closeConnection(con);
		}
		log.debug("product pk is end");
		return pk + 1;

	}

	
	@Override
	public long add(ProductDTO dto) throws ApplicationException, DuplicateRecordException {
		System.out.println("hellllo");
		log.debug("user add is started");
		Connection con = null;
		long pk = 0;
	
		try {
			con = JDBCDataSource.getConnection();
			con.setAutoCommit(false);
			pk = nextPK();
			System.out.println("insert data");
			PreparedStatement ps = con
					.prepareStatement("insert into ST_PRODUCT values(?,?,?,?,?)");
			ps.setLong(1, pk);
			ps.setString(2, dto.getProductName());
			ps.setDate(3, new java.sql.Date(dto.getPurchaseDate().getTime()));
			ps.setString(4, dto.getProductAmmount());
			ps.setString(5, dto.getProductCategory());
			
			ps.executeUpdate();
			System.out.println("data insert successfully" + pk);
			con.commit();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				con.rollback();

			} catch (Exception e2) {
				log.error(e);
				e2.printStackTrace();
				e.printStackTrace();
				throw new ApplicationException("exception: add rollback exception:" + e2.getMessage());

			}
			throw new ApplicationException("Exception : Exception in add User");
		} finally {
			JDBCDataSource.closeConnection(con);
		}

		log.debug("product model add is ended");
		return pk;
	}

	@Override
	public void delete(ProductDTO dto) throws ApplicationException {
		Connection con = null;
		try {
			con = JDBCDataSource.getConnection();
			con.setAutoCommit(false);
			PreparedStatement ps = con.prepareStatement("delete from ST_PRODUCT where id=?");
			ps.setLong(1, dto.getId());
			ps.executeUpdate();
			con.commit();
			ps.close();

		} catch (Exception e) {
			log.error("Database Exception..", e);
			try {
				con.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Exception : Delete rollback exception " + ex.getMessage());
			}
			throw new ApplicationException("Exception : Exception in delete User");
		} finally {
			JDBCDataSource.closeConnection(con);
		}
		log.debug("Model delete Started");
	}

	@Override
	public void update(ProductDTO dto) throws ApplicationException, DuplicateRecordException {
		Connection con = null;
		PreparedStatement ps = null;
		
		try {
			con = JDBCDataSource.getConnection();
			ps = con.prepareStatement(
					"update ST_PRODUCT set PRODUCTNAME=?, PURCHASEDATE=?, PRODUCTAMMOUNT=?, PRODUCTCATEGORY=? WHERE ID=?");

			ps.setString(1, dto.getProductName());
			ps.setDate(2, new java.sql.Date(dto.getPurchaseDate().getTime()));
			ps.setString(3, dto.getProductAmmount());
			ps.setString(4, dto.getProductCategory());
			ps.setLong(5, dto.getId());
		
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	@Override
	public List list() throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List list(int pageNo, int pageSize) throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List search(ProductDTO dto) throws ApplicationException {
		return null;
	}

	@Override
	public List search(ProductDTO dto, int pageNo, int pageSize) throws ApplicationException {
		Connection con = null;
		PreparedStatement ps = null;
		ArrayList array = null;
		StringBuffer sql = new StringBuffer("select * from ST_PRODUCT where 1=1");
		if (dto != null) {
			if (dto.getId() > 0) {
				sql.append(" AND ID = " + dto.getId());
			}
			if (dto.getProductName() != null && dto.getProductName().length() > 0) {
				sql.append(" AND PRODUCTNAME like '" + dto.getProductName() + "%'");
			}
			
		}
		if (pageSize > 0) {
			pageNo = (pageNo - 1) * pageSize;
			sql.append("limit" + pageNo + "," + pageSize);
		}
		array = new ArrayList();
		try {
			con = JDBCDataSource.getConnection();
			ps = con.prepareStatement(sql.toString());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				dto = new ProductDTO();
				dto.setId(rs.getLong(1));
				dto.setProductName(rs.getString(2));
				dto.setPurchaseDate(rs.getDate(3));
				dto.setProductAmmount(rs.getString(4));
				dto.setProductCategory(rs.getString(5));
				
				array.add(dto);
			}
			rs.close();

		} catch (Exception e) {
			log.error("Database Exception..", e);
			throw new ApplicationException("Exception : Exception in search product");
		} finally {
			JDBCDataSource.closeConnection(con);
		}

		log.debug("Model search End");

		return array;
	}

	@Override
	public ProductDTO findByPK(long pk) throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProductDTO fingByName(String name) throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	}

