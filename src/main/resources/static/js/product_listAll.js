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

tag_mattress_el.addEventListener("click", function() {
	this.classList.toggle("active");
	updateFilter();
});

tag_frame_el.addEventListener("click", function() {
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

//排程器局部更新卡片
$(document).ready(function() {
	console.log("偵測腳本已啟動...");

	function checkStatus() {
		$.get('/prod/api/allProdStatus', function(newList) {
			console.log("收到資料筆數: " + newList.length);

			let updatedCount = 0; // 這次檢查有多少筆變動

			newList.forEach(item => {
				const $span = $('#status-text-' + item.id);

				if ($span.length > 0) {
					const isUp = (item.status == 1 || item.status === true);
					const newText = isUp ? '● 上架中' : '● 已下架';
					const currentText = $span.text().trim();

					if (currentText !== newText) {
						console.log(`--- 偵測到變更: [${item.id}] ---`);
						updatedCount++; // 增加計數

						// 1. 更新文字與顏色
						if (isUp) {
							$span.text('● 上架中').attr('class', 'text-success fw-bold');
							$span.closest('.card').css('opacity', '1').removeClass('opacity-50');
						} else {
							$span.text('● 已下架').attr('class', 'text-danger fw-bold');
							$span.closest('.card').css('opacity', '0.5').addClass('opacity-50');
						}

						// 2. 視覺特效
						$span.hide().fadeIn(1000);
					}
				}
			});

			// 3. 所有商品比對完後，如果有變動，才跳一次吐司
			if (updatedCount > 0) {
				showStatusToast(`系統通知：已有 ${updatedCount} 件商品狀態發生變動。`);
			}
		});
	}

	// 吐司顯示函式 (放在內部或外部皆可，建議放在這裡)
	function showStatusToast(msg) {
		if ($('#toast-wrapper').length === 0) {
			$('body').append('<div id="toast-wrapper" class="toast-container position-fixed bottom-0 end-0 p-3" style="z-index: 1080;"></div>');
		}
		const toastHTML = `
		<div class="toast align-items-center text-white border-0 shadow-lg" style="background-color: #0d6efd;"  
		         role="alert" aria-live="assertive" aria-atomic="true">
                 <div class="d-flex">
                    <div class="toast-body"><i class="bi bi-robot me-2 text-info"></i>${msg}</div>
                    <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>
                </div>
            </div>`;
		const $toastElement = $(toastHTML).appendTo('#toast-wrapper');
		const bsToast = new bootstrap.Toast($toastElement[0], { delay: 4000 });
		bsToast.show();
		$toastElement.on('hidden.bs.toast', function() { $(this).remove(); });
	}

	// 啟動排程
	checkStatus();
	setInterval(checkStatus, 10000);
});
//登入時儲存當前頁面
function loginWithLocation(element) {
    const loginUrl = element.getAttribute('data-login-url') || '/emp/login';
    const currentPath = window.location.pathname;
	let searchParams = window.location.search;    
	let redirectPath = currentPath + searchParams;    
	if (currentPath.includes('getOne_For_Update') || currentPath.includes('addProd')) {
	        if (searchParams) {
	            redirectPath = '/prod/getOne_For_Display' + searchParams;
	        } else {
	            redirectPath = '/prod/listAllProd';
	        }
	    }
    
    window.location.href = loginUrl + '?location=' + encodeURIComponent(redirectPath);
}