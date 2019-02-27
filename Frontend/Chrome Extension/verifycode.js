document.getElementById("submit_button").addEventListener('click', function(){
	email = document.getElementById("email").value;
	pass = document.getElementById("password").value;
	pass_repeat = document.getElementById("password_confirm").value;

	username = document.getElementById("username").value;

	hash = sha256(pass);

	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
	    if (this.readyState == 4 && this.status == 200) {
	       //This is where the code for the verification form will
	       //be injected.
	       alert("Valid entered");
	    }
	};
	xhttp.open("POST", "http://129.21.113.174:8000/Register/", true);
	xhttp.send(
		"email="+email+"&"+
		"username="+username+"&"+
		"hash="+hash);
});