package in.co.rays.project_3.dto;

import java.util.Date;

public class StockPurchaseDTO extends BaseDTO {

	private String quantity;
	private String purchasePrice;
	private Date purchaseDate;
	private String orderType;

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(String purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public Date getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return id + " ";
	}

	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return purchasePrice;
	}

}
