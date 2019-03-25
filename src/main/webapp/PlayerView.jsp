<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Assisted Board Games : ChessGame</title>
</head>
<body>
	<div>

		<%
			String attribut = (String) request.getAttribute("board");
			out.println(attribut);
		%>

		<%-- <c:out value="${(String) request.getAttribute(board)}" escapeXml="false" /> --%>
	</div>
	<form action="PlayerServlet" method="post">
		Jouer un coup :
		Position de départ :<input type="text" id="start" placeholder="ex : A2" />
		<input type="submit" value="ok" /> <br/>
		
		Position d'arrivée :<input type="text" id="end" placeholder="ex : A4" />
		<input type="submit" value="ok" /> <br/>
	</form>
</body>
</html>