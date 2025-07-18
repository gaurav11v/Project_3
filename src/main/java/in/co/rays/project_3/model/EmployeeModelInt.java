package in.co.rays.project_3.model;

import java.util.List;

import in.co.rays.project_3.dto.EmployeeDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.exception.RecordNotFoundException;

public interface EmployeeModelInt {

	public long add(EmployeeDTO dto) throws ApplicationException, DuplicateRecordException;

	public void delete(EmployeeDTO dto) throws ApplicationException;

	public void update(EmployeeDTO dto) throws ApplicationException, DuplicateRecordException;

	public EmployeeDTO findByPK(long pk) throws ApplicationException;

	public EmployeeDTO findByUserName(String userName) throws ApplicationException;

	public List list() throws ApplicationException;

	public List list(int pageNo, int pageSize) throws ApplicationException;

	public List search(EmployeeDTO dto, int pageNo, int pageSize) throws ApplicationException;

	public List search(EmployeeDTO dto) throws ApplicationException;

}
