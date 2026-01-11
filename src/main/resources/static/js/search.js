// --------------------------
// 搜尋商品
// --------------------------
const searchForm = $('#searchForm');
const searchInput = $('#searchInput');

searchForm.on('submit', function(e) {
	const value = searchInput.val().trim();
	if (value === "") {
		e.preventDefault();
		alert("請輸入商品名稱");
	} else if (!/^[\u4e00-\u9fa5]+$/.test(value)) {
		e.preventDefault();
		alert("只可搜尋商品名稱（中文）");
	}
});


// Enter 鍵觸發搜尋
searchInput.on('keypress', function(e) {
	if (e.which === 13) {
		e.preventDefault();
		searchBtn.click();
	}
});