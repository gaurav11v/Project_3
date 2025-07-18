package in.co.rays.project_3.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.EmployeeDTO;
import in.co.rays.project_3.dto.StockPurchaseDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.model.EmployeeModelInt;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.model.StockPurchaseModelInt;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.DataValidator;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

@WebServlet(name = "EmployeeCtl", urlPatterns = { "/ctl/EmployeeCtl" })
public class EmployeeCtl extends BaseCtl {

	private static Logger log = Logger.getLogger(EmployeeCtl.class);

	protected boolean validate(HttpServletRequest request) {
		boolean pass = true;
		if (DataValidator.isNull(request.getParameter("fullName"))) {
			request.setAttribute("fullName", PropertyReader.getValue("error.require", "Full Name"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("userName"))) {
			request.setAttribute("userName", PropertyReader.getValue("error.require", "User Name"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("password"))) {
			request.setAttribute("password", PropertyReader.getValue("error.require", "Password"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("birthDate"))) {
			request.setAttribute("birthDate", PropertyReader.getValue("error.require", "Birth Date"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("contactNumber"))) {
			request.setAttribute("contactNumber", PropertyReader.getValue("error.require", "Contact Number"));
			pass = false;
		}
		return pass;
	}

	protected BaseDTO populateDTO(HttpServletRequest request) {
		EmployeeDTO dto = new EmployeeDTO();

		dto.setFullName(DataUtility.getString(request.getParameter("fullName")));
		dto.setUserName(DataUtility.getString(request.getParameter("userName")));
		dto.setPassword(DataUtility.getString(request.getParameter("password")));
		dto.setBirthDate(DataUtility.getDate(request.getParameter("birthDate")));
		dto.setContactNumber(DataUtility.getString(request.getParameter("contactNumber")));

		System.out.println("syso===>" + request.getParameter("fullName"));
		System.out.println("syso===>" + request.getParameter("userName"));
		System.out.println("syso===>" + request.getParameter("password"));
		System.out.println("syso===>" + request.getParameter("birthDate"));
		System.out.println("syso===>" + request.getParameter("contactNumber"));

		populateBean(dto, request);
		return dto;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String op = request.getParameter("operation");
		long id = DataUtility.getLong(request.getParameter("id"));
		EmployeeModelInt model = ModelFactory.getInstance().getEmployeeModel();
		if (id > 0 || op != null) {
			EmployeeDTO dto;
			try {
				dto = model.findByPK(id);
				ServletUtility.setDto(dto, request);

			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}

		}
		ServletUtility.forward(getView(), request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		String op = request.getParameter("operation");
		long id = DataUtility.getLong(request.getParameter("id"));

		EmployeeModelInt model = ModelFactory.getInstance().getEmployeeModel();

		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {

			EmployeeDTO dto = (EmployeeDTO) populateDTO(request);

			try {
				if (id > 0) {
					dto.setId(id);
					model.update(dto);
					ServletUtility.setDto(dto, request);

					ServletUtility.setSuccessMessage("Record Successfully Updated", request);

				} else {
					System.out.println("StockPurchase add" + dto + "id...." + id);
					// long pk
					model.add(dto);
					ServletUtility.setSuccessMessage("Record Successfully Saved", request);
				}
				ServletUtility.setDto(dto, request);
			} catch (ApplicationException e) {
				e.printStackTrace();
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			} catch (DuplicateRecordException e) {
				ServletUtility.setDto(dto, request);
				ServletUtility.setErrorMessage("User Name Already Exists", request);
			}
		} else if (OP_RESET.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.EMPLOYEE_CTL, request, response);
			return;
		} else if (OP_CANCEL.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.EMPLOYEE_LIST_CTL, request, response);
			return;

		}
		ServletUtility.forward(getView(), request, response);
	}

	@Override
	protected String getView() {
		// TODO Auto-generated method stub
		return ORSView.EMPLOYEE_VIEW;
	}

}
