<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<style >
.error{
	color:red;
	font-weight:bold;
}
</style>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

		<h1>Withdraw Amount</h1>
<div>
		
			<!-- depositAmount method is called -->
			<form action="withdrawAmount" method="post">
				<table cellpadding="6" cellspacing="2" align="center">
				<tr>
					<td>Mobile Number</td>
					<td><input type="text" name="mobileNo" size="30" /></td>
					
				</tr>

				<tr>
					<td>Amount</td>
					<td><input type="text" name="wallet.balance" size="30" /></td>
					
				</tr>

				<tr>
					<td><input type="submit" name="submit" value="withdraw" />
					<td><input type="reset" name="submit" value="Clear Form" />
				</tr>
				</table>
			</form>
			
			</div>
			<div>
				<font color="red"> <c:if test="${not empty errorMessage}">
					${errorMessage}
				</c:if>
				</font>
			</div>
	
	
</body>
</html>