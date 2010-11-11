<%-- 
    Document   : bookRoom
    Created on : 11.11.2010, 12:41:27
    Author     : Jenda
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<%

  String date = request.getParameter("date");
  int group = Integer.valueOf(request.getParameter("group"));
  String startTime = request.getParameter("startTime");
  String endTime = request.getParameter("endTime");

  out.println("<br><br>Selected date:"+date+"<br>Selected group:"+group+"<br>"+startTime+" -> "+endTime);

%>
