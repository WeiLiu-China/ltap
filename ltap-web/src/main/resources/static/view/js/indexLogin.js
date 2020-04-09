// JavaScript Document
"use strict";  
function pageLoad(){
	var storage=window.localStorage;
	storage.a=1;
	//alert(storage.a);
	var stuNum=localStorage.getItem("stuNum");
	var userPwd=localStorage.getItem("userPwd");
	//alert(stuNum);
	if(stuNum!=null){
		$("#stuNum").val(stuNum);
	}
	if(userPwd!=null){
		$("#userPwd").val(userPwd);
	}
	
	//console.log(checkbox.value);
}

function check(){
	var checkbox2=$("#checkbox2").prop("checked");
	var checkbox3=$("#checkbox3").prop("checked"); 
}

function btnOnclick(){
	
	//check();
	  
	var checkbox=$("#checkbox1").prop("checked");
	if(checkbox==true){
	var stuNum=$("#user_id").val();
	var userPwd=$("#password").val();
	if(stuNum!=null||userPwd!=null){
		localStorage.setItem("stuNum",stuNum);
		localStorage.setItem("userPwd",userPwd);
	}
	}
	else
		{
		//storage.removeItem("a");
		localStorage.removeItem("userPwd");
		//alert(checkbox);
		}
}