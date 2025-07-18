<%@page import="in.co.rays.project_3.controller.EmployeeListCtl"%>
<%@page import="in.co.rays.project_3.dto.EmployeeDTO"%>
<%@page import="in.co.rays.project_3.util.DataUtility"%>
<%@page import="java.util.Iterator"%>

<%@page import="in.co.rays.project_3.util.ServletUtility"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
<script src="<%=ORSView.APP_CONTEXT%>/js/jquery.min.js"></script>
<script type="text/javascript"
	src="<%=ORSView.APP_CONTEXT%>/js/CheckBox11.js"></script>
<style>
.p1 {
	padding: 8px;
}

.p4 {
	background-image: url('<%=ORSView.APP_CONTEXT%>/img/list2.jpg');
	background-repeat: no-repeat;
	background-attachment: fixed;
	background-size: cover;
	padding-top: 85px;

	/* background-size: 100%; */
}
</style>
</head>
<body class="p4">
	<div>
		<%@include file="Header.jsp"%>
	</div>
	<div>
		<form action="<%=ORSView.EMPLOYEE_LIST_CTL%>" method="post">





			<jsp:useBean id="dto"
				class="in.co.rays.project_3.dto.EmployeeDTO" scope="request"></jsp:useBean>
			<%
				List list1 = (List) request.getAttribute("employeeList");
			%>
			<%
				int pageNo = ServletUtility.getPageNo(request);
				int pageSize = ServletUtility.getPageSize(request);
				int index = ((pageNo - 1) * pageSize) + 1;
				int nextPageSize = DataUtility.getInt(request.getAttribute("nextListSize").toString());
				List list = ServletUtility.getList(request);
				Iterator<EmployeeDTO> it = list.iterator();
				if (list.size() != 0) {
			%>
			<center>
				<h1 class="text-light font-weight-bold pt-3">
					<font color="black"><u>Employee List</u></font>
				</h1>
			</center>
			<div class="row">
				<div class="col-md-4"></div>

				<%
					if (!ServletUtility.getSuccessMessage(request).equals("")) {
				%>

				<div class="col-md-4 alert alert-success alert-dismissible"
					style="background-color: #80ff80">
					<button type="button" class="close" data-dismiss="alert">&times;</button>
					<h4>
						<font color="#008000"><%=ServletUtility.getSuccessMessage(request)%></font>
					</h4>
				</div>
				<%
					}
				%>

				<div class="col-md-4"></div>
			</div>
			<div class="row">
				<div class="col-md-4"></div>

				<%
					if (!ServletUtility.getErrorMessage(request).equals("")) {
				%>
				<div class=" col-md-4 alert alert-danger alert-dismissible">
					<button type="button" class="close" data-dismiss="alert">&times;</button>
					<h4>
						<font color="red"> <%=ServletUtility.getErrorMessage(request)%></font>
					</h4>
				</div>
				<%
					}
				%>
				<div class="col-md-4"></div>
			</div>
			</br>

			<div class="row">

				<div class="col-sm-2"></div>

				<div class="col-sm-2">
					<input class="form-control" type="text" name="fullName"
						placeholder="Enter Full Name" 
						value="<%=ServletUtility.getParameter("fullName", request)%>">
				</div>
&emsp;
				<div class="col-sm-2">
					<input type="text" name="userName" placeholder="Enter User Name"
						class="form-control"
						value="<%=ServletUtility.getParameter("userName", request)%>">
				</div>
&emsp;
				<div class="col-sm-2">
					<input type="submit" class="btn btn-primary btn-md"
						style="font-size: 17px" name="operation"
						value="<%=EmployeeListCtl.OP_SEARCH%>">&emsp; <input
						type="submit" class="btn btn-dark btn-md" style="font-size: 17px"
						name="operation" value="<%=EmployeeListCtl.OP_RESET%>">
				</div>

				<div class="col-sm-1"></div>
			</div>

			</br>
			<div style="margin-bottom: 20px;" class="table-responsive" >
				<table class="table table-dark table-bordered  table-hover">
					<thead>
						<tr style="background-color: #8C8C8C;" align="center">

							<th width="10%"><input type="checkbox" id="select_all"
								name="Select" class="text"> Select All</th>
							<th class="text">S.NO</th>
							<th class="text">Full Name</th>
							<th class="text">User Name</th>
							<th class="text">Password</th>
							<th class="text">Date Of Birth</th>
							<th class="text">Contact Number</th>
							<th class="text">Edit</th>
						</tr>
					</thead>
					<%
						while (it.hasNext()) {
								dto = it.next();
					%>

					<tbody>
						<tr>
							<td align="center"><input type="checkbox" class="checkbox"
								name="ids" value="<%=dto.getId()%>"></td>
							<td align="center"><%=index++%></td>
							<td align="center"><%=dto.getFullName()%></td>
							<td align="center"><%=dto.getUserName()%></td>
							<td align="center"><%=dto.getPassword()%></td>
							<td align="center"><%=dto.getBirthDate()%></td>
							<td align="center"><%=dto.getContactNumber()%></td>
							<td align="center"><a
								href="EmployeeCtl?id=<%=dto.getId()%>">Edit</a></td>
						</tr>
					</tbody>
					<%
						}
					%>
				</table>
			</div>
			<table width="100%">
				<tr>
					<td><input type="submit" name="operation"
						class="btn btn-secondary btn-md" style="font-size: 17px"
						value="<%=EmployeeListCtl.OP_PREVIOUS%>"
						<%=pageNo > 1 ? "" : "disabled"%>></td>
					<td><input type="submit" name="operation"
						class="btn btn-primary btn-md" style="font-size: 17px"
						value="<%=EmployeeListCtl.OP_NEW%>"></td>
					<td><input type="submit" name="operation"
						class="btn btn-danger btn-md" style="font-size: 17px"
						value="<%=EmployeeListCtl.OP_DELETE%>"></td>

					<td align="right"><input type="submit" name="operation"
						class="btn btn-secondary btn-md" style="font-size: 17px"
						style="padding: 5px;" value="<%=EmployeeListCtl.OP_NEXT%>"
						<%=(nextPageSize != 0) ? "" : "disabled"%>></td>
				</tr>
				<tr></tr>
			</table>
			</br>

			<%
				}
				if (list.size() == 0) {
					System.out.println("user list view list.size==0");
			%>
			<center>
				<h1 class="text-primary font-weight-bold pt-3">Employee List</h1>
			</center>
			</br>
			<div class="row">
				<div class="col-md-4"></div>

				<%
					if (!ServletUtility.getErrorMessage(request).equals("")) {
				%>
				<div class=" col-md-4 alert alert-danger alert-dismissible">
					<button type="button" class="close" data-dismiss="alert">&times;</button>
					<h4>
						<font color="red"> <%=ServletUtility.getErrorMessage(request)%></font>
					</h4>
				</div>
				<%
					}
				%>
				<div class="col-md-4"></div>
			</div>
			</br>

			<div style="padding-left: 48%;">
				<input type="submit" name="operation" class="btn btn-primary btn-md"
					style="font-size: 17px" value="<%=EmployeeListCtl.OP_BACK%>">
			</div>
			<%
				}
			%>
			<input type="hidden" name="pageNo" value="<%=pageNo%>"> <input
				type="hidden" name="pageSize" value="<%=pageSize%>">


		</form>

	</div>
	</br>
	</br>

<%@include file="FooterView.jsp"%>
</body>
</html>