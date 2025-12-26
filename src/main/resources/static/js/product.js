// 將所有需要抓取 HTML 元素的程式碼都包在這裡面
document.addEventListener("DOMContentLoaded", function () {
  
  // 1. Summernote
  $("#summernote").summernote({
    placeholder: "請輸入商品介紹...",
    tabsize: 2,
    height: 200,
  });

  // 2. 尺寸邏輯 (移到這裡面)
  const dataElem = document.getElementById('allSizesData');
  const widthSelector = document.getElementById('width_selector');

  if (dataElem && widthSelector) {
    const allSizes = JSON.parse(dataElem.value);

    widthSelector.addEventListener('change', function() {
        const selectedWidth = parseInt(this.value);
        console.log("選取寬度:", selectedWidth);

        document.querySelectorAll('.size_row').forEach(row => {
            const rowLength = parseInt(row.getAttribute('data-length'));
            const hiddenIdInput = row.querySelector('.size-id-input');
            
            const found = allSizes.find(s => s.width === selectedWidth && s.length === rowLength);
            
            if (found) {
                hiddenIdInput.value = found.sizeId;
                console.log(`長度 ${rowLength} 對應 ID: ${found.sizeId}`);
            } else {
                hiddenIdInput.value = "";
            }
        });
    });
  }

  // 3. 圖片預覽
  const uploadImg = document.getElementById("uploadImg");
  const previewImg = document.getElementById("previewImg");
  if (uploadImg) {
    uploadImg.addEventListener("change", function () {
      const file = this.files[0];
      if (file) previewImg.src = URL.createObjectURL(file);
    });
  }

  // 商品類型切換
  const btn_mattress_el = document.getElementById("btn_mattress");
  const btn_frame_el = document.getElementById("btn_frame");
  const product_type = document.getElementById("product_type");
  const mattress_functions_el = document.querySelectorAll(
    ".mattress_functions input"
  );
  const frame_functions_el = document.querySelectorAll(".frame_functions input");

  // 點擊床墊按鈕
  btn_mattress_el.addEventListener("click", function () {
    this.classList.add("active");
    btn_frame_el.classList.remove("active");
    document.getElementById("mattress_material").classList.remove("d-none");
    document.getElementById("frame_material").classList.add("d-none");
    product_type.value = "mattress";
    // 啟用床墊
    mattress_functions_el.forEach((mf) => (mf.disabled = false));
    // 停用床架
    frame_functions_el.forEach((ff) => {
      ff.checked = false;
      ff.disabled = true;
    });
  });

  // 點擊床架按鈕
  btn_frame_el.addEventListener("click", function () {
    this.classList.add("active");
    btn_mattress_el.classList.remove("active");
    document.getElementById("frame_material").classList.remove("d-none");
    document.getElementById("mattress_material").classList.add("d-none");
    product_type.value = "frame";
    // 停用床墊
    mattress_functions_el.forEach((mf) => {
      mf.checked = false;
      mf.disabled = true;
    });
    // 啟用床架
    frame_functions_el.forEach((ff) => (ff.disabled = false));
    mattress_functions_el.disabled = true;
    frame_functions_el.disabled = false;
  });

  /* 無尺寸按鈕 */

  document.querySelectorAll(".btn-nosize").forEach((btn) => {
    btn.addEventListener("click", function () {
      const group = btn.closest(".size_group");
      const qty = group.querySelector(".product_stock");
      const price = group.querySelector(".product_price");

      const isDisabled = qty.disabled;

      if (!isDisabled) {
        qty.value = "";
        price.value = "";
        qty.disabled = true;
        price.disabled = true;
        btn.classList.add("btn-primary");
        btn.classList.remove("btn-outline-secondary");
        btn.textContent = "無庫存";
      } else {
        qty.disabled = false;
        price.disabled = false;
        btn.classList.remove("btn-primary");
        btn.classList.add("btn-outline-secondary");
        btn.textContent = "無庫存";
      }
    });
  });

  // 上架 / 下架切換
  let btn_on_el = document.getElementById("btn_on");
  let btn_off_el = document.getElementById("btn_off");

  btn_on_el.addEventListener("click", function () {
    btn_on_el.classList.toggle("active", true);
    btn_off_el.classList.toggle("active", false);
  });
  btn_off_el.addEventListener("click", function () {
    btn_off_el.classList.toggle("active", true);
    btn_on_el.classList.toggle("active", false);
  });
  
  
  
  
});