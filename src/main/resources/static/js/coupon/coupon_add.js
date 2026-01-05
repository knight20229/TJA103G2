$("button#cancel").on("click", function(){
    let c = confirm("確認取消編輯優惠券?");
    if (c){
        window.history.back();
    }
	
});

