const tag_mattress_el = document.getElementById("tag_mattress");
const tag_frame_el = document.getElementById("tag_frame");

function updateFilter() {
    // 取得目前選中狀態
    let isMattressActive = tag_mattress_el.classList.contains("active");
    let isFrameActive = tag_frame_el.classList.contains("active");

    // 若兩者都不選，則自動全選
    if (!isMattressActive && !isFrameActive) {
        tag_mattress_el.classList.add("active");
        tag_frame_el.classList.add("active");
        isMattressActive = true;
        isFrameActive = true;
    }

    // 執行顯示/隱藏
    const productItems = document.querySelectorAll(".product-item");
    productItems.forEach(item => {
        const type = item.getAttribute("data-type");

        if (isMattressActive && isFrameActive) {
            item.style.display = "block";
        } else if (isMattressActive) {
            item.style.display = (type === "false" || type === "0") ? "block" : "none";
        } else if (isFrameActive) {
            item.style.display = (type === "true" || type === "1") ? "block" : "none";
        }
    });
}

tag_mattress_el.addEventListener("click", function () {
    this.classList.toggle("active");
    updateFilter();
});

tag_frame_el.addEventListener("click", function () {
    this.classList.toggle("active");
    updateFilter();
});


// 下拉選單跳轉功能
function scrollToProduct(element) {
    const productId = element.getAttribute('data-id');
    
    document.getElementById('searchInput').value = element.textContent;
    document.getElementById('searchInput').dispatchEvent(new Event('input'));
}

const searchInput = document.getElementById('searchInput');
const clearSearchBtn = document.getElementById('clearSearch');

// 監聽輸入事件
searchInput.addEventListener('input', function() {
    const keyword = this.value.toLowerCase().trim();
    const productItems = document.querySelectorAll('.product-item');

    // 控制「X」按鈕的顯示與隱藏
    if (this.value.length > 0) {
        clearSearchBtn.style.display = 'block';
    } else {
        clearSearchBtn.style.display = 'none';
    }

    // 執行模糊查詢
    productItems.forEach(item => {
        const nameSpan = item.querySelector('.card-title span');
        const productName = nameSpan ? nameSpan.textContent.toLowerCase() : "";
        item.style.display = productName.includes(keyword) ? "block" : "none";
    });
});

// 點擊「X」按鈕清空搜尋
clearSearchBtn.addEventListener('click', function() {
    searchInput.value = ''; 
    this.style.display = 'none'; 
    searchInput.focus(); 
    
    // 觸發一次 input 事件來恢復所有卡片顯示
    searchInput.dispatchEvent(new Event('input'));
});