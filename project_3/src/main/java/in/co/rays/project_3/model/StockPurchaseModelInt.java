package in.co.rays.project_3.model;

import java.util.List;

import in.co.rays.project_3.dto.StockPurchaseDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;

public interface StockPurchaseModelInt {

	public long add(StockPurchaseDTO dto) throws ApplicationException, DuplicateRecordException;

	public void delete(StockPurchaseDTO dto) throws ApplicationException;

	public void update(StockPurchaseDTO dto) throws ApplicationException, DuplicateRecordException;

	public List list() throws ApplicationException;

	public List list(int pageNo, int pageSize) throws ApplicationException;

	public List search(StockPurchaseDTO dto) throws ApplicationException;

	public List search(StockPurchaseDTO dto, int pageNo, int pageSize) throws ApplicationException;

	public StockPurchaseDTO findByPK(long pk) throws ApplicationException;

	public StockPurchaseDTO fingByPurchasePrice(String purchasePrice) throws ApplicationException;
}
