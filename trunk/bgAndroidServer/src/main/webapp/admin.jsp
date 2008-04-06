<%@ page language="java" import="bg.android.web.*" %>
<jsp:useBean id="bean" scope="request" class="bg.android.web.BeanService" />
<html>
<body>
<h2>Admin Android</h2>
<hr/>

<ul>
	<li>
		<a href="http://dev.java-consultant.com/bgAndroid/carpool_show_mobiles.html">carpool_show_mobiles</a> : display on a map, on a web page, the positions of mobiles
	</li><li>
		<a href="http://dev.java-consultant.com/bgAndroid/carpool_set_my_position.html">carpool_set_my_position</a> : allow to set my position on a map.
	</li><li>
		<a href="http://dev.java-consultant.com/bgAndroid/carpool_simulator.html">simulator</a> : To simulate easily car and people every where in the world.
	</li>
</ul>
<hr/>


<table border="1">

<tr> <td> <a href="admin?action=simuElements"> admin?action=simuElements</a> </td> <td> Read a file of cars and pedestrians. It is usefull for debugging.
 </td> </tr> <tr> <td> 
<a href="admin?action=check"> admin?action=check</a> </td> <td> Display all the mobiles of the database
 </td> </tr> <tr> <td> 
<a href="admin?action=checkSimple"> admin?action=checkSimple</a> </td> <td> Display the number of mobiles inside the database
 </td> </tr> <tr> <td> 
<a href="admin?action=reInitBuffe"> admin?action=reInitBuffe</a> </td> <td> Delete all mobiles in buffer 
 </td> </tr> <tr> <td> 
<a href="http://bertrand.guiral.free.fr/android/carpool_simulator.html">simulator : </a> </td> <td> To simulate mobile with googlemap
 </td> </tr> <tr> <td> 
<a href="http://bertrand.guiral.free.fr/android/carpool_set_my_position.html">set_my_position.html : </a> </td> <td> To set my position by the web
 </td> </tr> <tr> <td> 
<a href="http://bertrand.guiral.free.fr/android/carpool_show_mobiles.html">show_mobiles.html: </a> </td> <td> Show the mobile with googlemap
 </td> </tr>  
</table>
<p/>
<h2> get the list of mobile for a position </h2>
<form action="/bgAndroid/service" method="GET">
<input type="hidden"  name="action" value="getCars"/>
<input type="hidden"  name="idAndroid" value="Dev000"/>
<input type="hidden"  name="type" value="0"/>
<input type="hidden"  name="ua" value="Android.bg.1"/>
<table border="1">
<tr> <td> latitude</td> <td> <input type="text"  name="latitudeE6" value="37422384"/> </td> </tr>
<tr> <td> longitude</td> <td><input type="text"  name="longitudeE6" value="-122096533"/></td> </tr>
<tr> <td> latitude span </td> <td><input type="text"  name="latitudeSpan" value="0"/></td> </tr>
<tr> <td> longitude span </td> <td><input type="text"  name="longitudeSpan" value="0"/></td> </tr>
<tr> <td> </td> <td><input type="submit"  name="submit" value="submit"/></td> </tr>

</table>

</form>



<hr/>
<%= bean.getComment() %>
<hr/>


<a href="init">log</a>
</body>
</html>
