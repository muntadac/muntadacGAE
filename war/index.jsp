<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
	"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=IUTF-8">
<title>Muntadac</title>
</head>
<body>
	<form action="muntadacgae" method="POST">
		<div style="color: #FF0000;">${errorMessage}</div>
		<table cellspacing="10">
			<tr>
				<td><INPUT TYPE="radio" name="type_date" value="gregorian"
					checked="checked" />Gregorian</td>
				<td colspan="2"><INPUT TYPE="radio" NAME="type_date"
					VALUE="hijri" />Hijri</td>
			</tr>

			<tr>
				<td><INPUT TYPE="radio" name="gender" value="male"
					checked="checked" />Male</td>
				<td colspan="2"><INPUT TYPE="radio" name="gender"
					VALUE="female" />Female</td>
			</tr>

			<tr>
				<td><label for="day">Day</label> <INPUT TYPE="text" name="day"
					id="day" value="${param.day}"/></td>
				<td><label for="month">Month</label><INPUT TYPE="text"
					name="month" id="month" value="${param.month}"/></td>
				<td><label for="year">Year</label><INPUT TYPE="text"
					name="year" id="year" value="${param.year}"/></td>
			</tr>

			<tr>
				<td><INPUT type=submit value="OK" /></td>
			</tr>

			<tr>
				<td><textarea rows="4" cols="30">${text1}</textarea></td>
			</tr>

			<tr>
				<td><textarea rows="4" cols="30">${text2}</textarea></td>
			</tr>

		</table>


	</form>
</body>
</html>
