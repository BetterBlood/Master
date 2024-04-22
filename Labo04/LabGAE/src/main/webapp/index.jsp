<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="ch.heigvd.cld.lab.HelloAppEngine" %>
<html>
<head>
    <link href='//fonts.googleapis.com/css?family=Marmelad' rel='stylesheet' type='text/css'>
    <title>Hello App Engine Standard Java 8</title>
</head>
<body>
<h1>Hello App Engine -- Java 8!</h1>

<p>This is <%= HelloAppEngine.getInfo() %>.</p>
<table>
    <tr>
        <td colspan="2" style="font-weight:bold;">Available Servlets:</td>
    </tr>
    <tr>
        <td><a href='/hello'>Hello App Engine</a></td>
    </tr>
    <tr>
        <td><a href='/datastorewrite'>Datastore Write Servlet</a></td>
    </tr> <!-- Add this line to include a link to your Datastore Write Servlet -->
</table>
<h2>Create Entity:</h2>
<form id="datastoreWriteForm" action='/datastorewrite' method='get' onsubmit="return validateForm()">
    <label for='_kind'>kind (required) :</label>
    <input type='text' id='_kind' name='_kind' required><br>
    <label for='_key'>key (optional) :</label>
    <input type='text' id='_key' name='_key'><br>
    <label for='author'>author (optional) :</label>
    <input type='text' id='author' name='author'><br>
    <label for='title'>title (optional) :</label>
    <input type='text' id='title' name='title'><br>
    <input type='submit' value='Submit'>
</form>

<script>
    function validateForm() {
        let kind = document.getElementById('_kind').value;
        if (kind.trim() === "") {
            alert("_kind is required");
            return false;
        }
        return true;
    }
</script>

</body>
</html>
