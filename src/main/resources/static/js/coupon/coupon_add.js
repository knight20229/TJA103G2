$("button#cancel").on("click", function(){
    let c = confirm("確認取消編輯優惠券?");
    if (c){
        window.history.back();
    }
	
});

//登入時儲存當前頁面

function loginWithLocation(element) {
    const loginUrl = element.getAttribute('data-login-url') || '/emp/login';
    const currentPath = window.location.pathname;
    let searchParams = window.location.search;    
    let redirectPath = currentPath + searchParams;    

    // 1. 如果網址列沒 ID，從隱藏欄位抓 couponId
    if (!searchParams) {
        const idInput = document.querySelector('input[name="couponId"]');
        if (idInput && idInput.value) {
            searchParams = "?couponId=" + idInput.value;
        }
    }

    // 2. 判斷跳轉邏輯
    if (currentPath.includes('getOne_For_Update') || currentPath.includes('add')) {
        if (searchParams) {
            // 登入後回「單一查詢頁」 (GET)
            redirectPath = '/coupon/getOne_For_Display' + searchParams;
        } else {
            // 新增頁 -> 登入後回「列表頁」
            redirectPath = '/coupon/getAllCoupon';
        }
    } else {
        // 如果是「單一查詢頁」或「列表頁」點登入
        redirectPath = currentPath + searchParams;
    }
    
    window.location.href = loginUrl + '?location=' + encodeURIComponent(redirectPath);
}