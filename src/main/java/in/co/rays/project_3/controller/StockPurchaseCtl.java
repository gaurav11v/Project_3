package in.co.rays.project_3.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.StockPurchaseDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.model.StockPurchaseModelInt;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.DataValidator;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

@WebServlet(urlPatterns = { "/ctl/StockPurchaseCtl" })
public class StockPurchaseCtl extends BaseCtl {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(StockPurchaseCtl.class);

	protected boolean validate(HttpServletRequest request) {
		boolean pass = true;
		if (DataValidator.isNull(request.getParameter("quantity"))) {
			request.setAttribute("quantity", PropertyReader.getValue("error.require", "Quantity"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("purchasePrice"))) {
			request.setAttribute("purchasePrice", PropertyReader.getValue("error.require", "Purchase Price"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("purchaseDate"))) {
			request.setAttribute("purchaseDate", PropertyReader.getValue("error.require", "Purchase Date"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("orderType"))) {
			request.setAttribute("orderType", PropertyReader.getValue("error.require", "orderType"));
			pass = false;
		}
		
		return pass;
	}

	protected BaseDTO populateDTO(HttpServletRequest request) {
		StockPurchaseDTO dto = new StockPurchaseDTO();
		
		
		dto.setQuantity(request.getParameter("quantity"));
		dto.setPurchasePrice(request.getParameter("purchasePrice"));
		dto.setPurchaseDate(DataUtility.getDate(request.getParameter("purchaseDate")));
		dto.setOrderType(request.getParameter("orderType"));
		
		System.out.println("syso===>" + request.getParameter("quantity"));
		System.out.println("syso===>" +request.getParameter("city"));
		System.out.println("syso===>" +request.getParameter("address"));
		System.out.println("syso===>" +request.getParameter("state"));
		System.out.println("syso===>" +request.getParameter("mobileNo"));
		
				
		populateBean(dto,request);
		return dto;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String op = request.getParameter("operation");
		long id = DataUtility.getLong(request.getParameter("id"));
		StockPurchaseModelInt model=ModelFactory.getInstance().getStockPurchaseModel();
		if (id > 0 || op != null) {
			StockPurchaseDTO dto;
			try {
			  dto=model.findByPK(id);
			  ServletUtility.setDto(dto, request);
				
			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}

		}
		ServletUtility.forward(getView(), request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
       
		String op=request.getParameter("operation");
       long id=DataUtility.getLong(request.getParameter("id"));
  
       StockPurchaseModelInt model=ModelFactory.getInstance().getStockPurchaseModel();
       
       if (OP_SAVE.equalsIgnoreCase(op)||OP_UPDATE.equalsIgnoreCase(op)) {
    	   
    	   StockPurchaseDTO dto = (StockPurchaseDTO) populateDTO(request);

    	   try {
				if (id > 0) {
					dto.setId(id);
					model.update(dto);
					ServletUtility.setDto(dto, request);
					
					ServletUtility.setSuccessMessage("Record Successfully Updated", request);

				} else {
					System.out.println("StockPurchase add" + dto + "id...." + id);
					//long pk 
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
				ServletUtility.setErrorMessage("StockPurchaseDTO Already Exists", request);
			} 
		} else if (OP_RESET.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.STOCK_PURCHASE_CTL, request, response);
				return;
		} else if (OP_CANCEL.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.STOCK_PURCHASE_LIST_CTL, request, response);
			return;

		}
		ServletUtility.forward(getView(), request, response);
	}

	@Override
	protected String getView() {
		// TODO Auto-generated method stub
		return ORSView.STOCK_PURCHASE_VIEW;
	}

}
