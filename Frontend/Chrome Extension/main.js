//find a way to load this shit when the popup is cliked.

//This function allows for the querying of data and insertion of 
//data from the server
var xhr = new XMLHttpRequest();
xhr.open("POST", "http://129.21.143.119:8000/", true);
xhr.setRequestHeader('Content-Type', 'application/json');
xhr.onreadystatechange = function() {
  if (xhr.readyState == 4) {
  	document.getElementById("resp").innerHTML = xhr.responseText;
  }
}
//Send post data to server aka parameters for request.
xhr.send('');