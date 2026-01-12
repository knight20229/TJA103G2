document.addEventListener("DOMContentLoaded", function () {
  
  //  Summernote
  if ($("#summernote").length > 0) {
        $("#summernote").summernote({
            placeholder: "請輸入商品介紹...",
            tabsize: 2,
            height: 200,
        });
    }

	//  圖片預覽
	const uploadImg = document.getElementById("uploadImg");
	const previewImg = document.getElementById("previewImg");
	const base64Input = document.getElementById("base64Image"); 

	if (uploadImg && previewImg) {
	    uploadImg.addEventListener("change", function () {
	        const file = this.files[0];
	        if (file) {
	            const reader = new FileReader();
	            
	            reader.onload = function (e) {
	                const base64String = e.target.result;
	                
	                previewImg.src = base64String;
	                previewImg.style.display = "block";
	                
	                if (base64Input) {
	                    base64Input.value = base64String;
	                }
	            };
	            
	            reader.readAsDataURL(file); 
	        }
	    });
	}
	  
	  // --- 初始化圖片顯示狀態 ---
	  if (previewImg) {
	      const currentSrc = previewImg.src;
	      if (currentSrc && !currentSrc.endsWith('/') && !currentSrc.endsWith('undefined')) {
	          previewImg.style.display = "block";
	      } else {
	          previewImg.style.display = "none";
	      }
	  }

  // --- 商品類型切換 ---
    const btn_mattress_el = document.getElementById("btn_mattress");
    const btn_frame_el = document.getElementById("btn_frame");
    const product_type = document.getElementById("product_type");
    const mattress_material_div = document.getElementById("mattress_material");
    const frame_material_div = document.getElementById("frame_material");
    
    // 只有當按鈕跟類型欄位都存在時，才執行切換邏輯
    if (btn_mattress_el && btn_frame_el && product_type) {
        const mattress_functions_items = document.querySelectorAll(".mattress_functions");
        const frame_functions_items = document.querySelectorAll(".frame_functions");

        let isInitialLoad = true;

        function updateUI(isFrame) {
            // 這些元素也在 updateUI 內部進行安全檢查
            const mattressSelect = mattress_material_div ? mattress_material_div.querySelector("select") : null;
            const frameSelect = frame_material_div ? frame_material_div.querySelector("select") : null;
            const mattressGroup = document.getElementById("mattress_func_group");
            const frameGroup = document.getElementById("frame_func_group");

            if (isFrame) {
                btn_frame_el.classList.add("active");
                btn_mattress_el.classList.remove("active");
                if(frame_material_div) frame_material_div.classList.remove("d-none");
                if(mattress_material_div) mattress_material_div.classList.add("d-none");
                if(frameSelect) frameSelect.disabled = false;
                if(mattressSelect) mattressSelect.disabled = true;

                if(mattressGroup) mattressGroup.classList.add("d-none");
                if(frameGroup) frameGroup.classList.remove("d-none");
                
                mattress_functions_items.forEach(el => {
                    let input = el.querySelector('input');
                    if(input) {
                        input.disabled = true;
                        if (!isInitialLoad) input.checked = false;
                    }
                });
                frame_functions_items.forEach(el => {
                    let input = el.querySelector('input');
                    if(input) input.disabled = false;
                });
            } else {
                btn_mattress_el.classList.add("active");
                btn_frame_el.classList.remove("active");
                if(mattress_material_div) mattress_material_div.classList.remove("d-none");
                if(frame_material_div) frame_material_div.classList.add("d-none");
                if(mattressSelect) mattressSelect.disabled = false;
                if(frameSelect) frameSelect.disabled = true;

                if(mattressGroup) mattressGroup.classList.remove("d-none");
                if(frameGroup) frameGroup.classList.add("d-none");
                
                frame_functions_items.forEach(el => {
                    let input = el.querySelector('input');
                    if(input) {
                        input.disabled = true;
                        if (!isInitialLoad) input.checked = false;
                    }
                });
                mattress_functions_items.forEach(el => {
                    let input = el.querySelector('input');
                    if(input) input.disabled = false;
                });
            }
            isInitialLoad = false;
        }

        // 初始化
        let currentVal = product_type.value;
        if (currentVal === "" || currentVal === null) {
            currentVal = "false";
            product_type.value = "false"; 
        }
        updateUI(String(currentVal) === "true");

        btn_mattress_el.addEventListener("click", function () {
            product_type.value = "false";
            updateUI(false);
        });

        btn_frame_el.addEventListener("click", function () {
            product_type.value = "true";
            updateUI(true);
        });
    }

    // 上下架狀態
	let btn_on = document.getElementById("btn_on");
	let btn_off = document.getElementById("btn_off");
	let product_status = document.getElementById("product_status");

    // 只有當這三個元素都存在時才綁定事件
	if (product_status && btn_on && btn_off) {
	    const isStatusOn = (String(product_status.value) === "true");
	    btn_on.classList.toggle("active", isStatusOn);
	    btn_off.classList.toggle("active", !isStatusOn);

        btn_on.addEventListener("click", function () {
            product_status.value = "true";
            btn_on.classList.add("active");
            btn_off.classList.remove("active");
        });

        btn_off.addEventListener("click", function () {
            product_status.value = "false";
            btn_off.classList.add("active");
            btn_on.classList.remove("active");
        });
	}

	// 庫存管理-尺寸
	$('#width_selector').on('change', function() {
	    const selectedWidth = $(this).val();
	    console.log("當前選擇的寬度是: " + selectedWidth);

	    $('.size_group').hide(); 
	    
	    if (selectedWidth) {
	        const targetId = '#width_group_' + selectedWidth;
	        console.log("準備顯示的 ID 是: " + targetId);
	        
	        const $targetGroup = $(targetId);
	        if ($targetGroup.length > 0) {
	            $targetGroup.show(); 
	            $('#no_width_msg').hide();
	        } else {
	            console.log("找不到對應的規格區塊: " + targetId);
	            $('#no_width_msg').show();
	        }
	    } else {
	        $('#no_width_msg').show();
	    }
	});

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