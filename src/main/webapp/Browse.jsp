<html>
<body>
<h1>Browse</h1>

	<form action="upload.html" method="post" enctype="multipart/form-data">  
ClientName: &nbsp; &nbsp; <input type="text" name="clientname" id="clientname" /> <br> 
Email ID:   &nbsp;&nbsp;&nbsp;<input type="text" name="email" id="email" /> <br> 

	<input type="button" name="submit" value="submit"  onclick="validateClient();" /> 
          <table style="margin-left: 200px;width: 800px;height: 350px; display:none;" id="uploadingFile">
                       <!--<input type="hidden" value="<%= request.getParameter("clientname") %>" name="clientname" />
		               <input type="hidden" value="<%= request.getParameter("email") %>" name="email" />-->
                            <tr><td>Select Your files:</td><td>:</td><td><input type="file" name="f1"></td>
                            <td></td><td><input type="submit" value="submit"></td></tr>
                            
                        </table> 
	</form>           

 <script>
function validateClient()
{ 
  var xhttp = new XMLHttpRequest();
  xhttp.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200) {
    if(this.responseText.trim()=="valid") {
     	document.getElementById("uploadingFile").style.display = "table";
     }
    }
  };
  xhttp.open("POST", "validating.html", true);
  xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
  xhttp.send("clientname="+document.getElementById("clientname").value+"&email="+document.getElementById("email").value);
}
</script>

                        </body>
                        </html>