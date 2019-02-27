
var username = "";

chrome.storage.sync.get(['username'], function(result) {
		console.log(result.username);
      username = result.username;
});

var link = "";

chrome.tabs.query({'active': true, 'lastFocusedWindow': true, 'currentWindow': true}, function (tabs) {
    var url = tabs[0].url;
    console.log(url);
    link = url.replace("=").replace("&");
});

function gatherData(){
	var xhttp2 = new XMLHttpRequest();
	xhttp2.onreadystatechange = function() {
	    if (this.readyState == 4 && this.status == 200) {
	       //This is where the code for the verification form will
	       //be injected.
	       if (xhttp2.responseText != "invalid"){
	       		document.getElementById("nocomment").innerHTML = xhttp2.responseText;
	       		
	       }
	    }
	};
	xhttp2.open("POST", "http://129.21.113.174:8000/GetComment/", true);
	xhttp2.send("link="+link);
}

document.getElementById("submit_button").addEventListener('click', function(){

    var comment = document.getElementById("chat").value;
    comment = comment.replace(/[^a-zA-Z ]/g, "");

	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
	    if (this.readyState == 4 && this.status == 200) {
	       //This is where the code for the verification form will
	       //be injected.
	       if (xhttp.responseText == "inserted"){
	       		alert("Comment Inserted");
	       }
	    }
	};
	xhttp.open("POST", "http://129.21.113.174:8000/SubmitComment/", true);
	xhttp.send(
		"link="+link+"&"+
		"username="+username+"&"+
		"data="+comment);
});

gatherData();