<%-- 
    Document   : test
    Created on : 10/08/2012, 12:41:33
    Author     : aoliveir
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s3cache" uri="http://dynamicflow.com.br/aws/s3-cache.tld"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Example 2</title>
    </head>
    <body>
        <h1>Example 2</h1>
        <s3cache:img src="Images/../Thumbnails/1.jpg"/>
        <img src="<s3cache:get src="Images/../Thumbnails/2.jpg"/>"/>
    </body>
</html>
