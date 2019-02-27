document.addEventListener('DOMContentLoaded', function () {

  	document.getElementById("globe").addEventListener('click', function(event){
		var thing = document.getElementById("container");
		thing.style.left = "0%";
	});
	document.getElementById("profile").addEventListener('click', function(event){
		var thing = document.getElementById("container");
		thing.style.left = "-100%";
	});
	document.getElementById("group").addEventListener('click', function(event){
		var thing = document.getElementById("container");
		thing.style.left = "-200%";
	});
});