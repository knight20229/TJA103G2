$("button#cancel").on("click", function(){
    let c = confirm("確認取消編輯優惠券?");
    if (c){
        window.history.back();
    }
	
});

function loginWithLocation(element) {
    const loginUrl = element.getAttribute('data-login-url') || '/emp/login';
    const currentPath = window.location.pathname;
    let searchParams = window.location.search;    
    
    // 1. 確保抓到正確的 ID 
    if (!searchParams) {
        const idInput = document.querySelector('input[name="promotionsId"]'); // <-- 修改這裡
        if (idInput && idInput.value) {
            searchParams = "?promotionsId=" + idInput.value; // <-- 修改這裡
        }
    }

    let redirectPath;

    // 2. 判斷頁面類型決定去向
    if (currentPath.includes('getOne_For_Update')) {
        // 編輯頁 -> 降級回該 ID 的單一查詢頁
        redirectPath = '/promotions/getOne_For_Display' + searchParams; // <-- 修改路徑
    } else if (currentPath.includes('add')) {
        // 新增頁 -> 回列表
        redirectPath = '/promotions/getAllPromotions'; // <-- 修改路徑
    } else {
        // 列表頁或單一查詢頁 -> 原路徑回跳
        redirectPath = currentPath + searchParams;
    }
    
    window.location.href = loginUrl + '?location=' + encodeURIComponent(redirectPath);
}