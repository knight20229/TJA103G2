$(document).ready(function () {
    const $productContainer = $(".shop-product-wrap");
    const $productCols = $productContainer.find(".col-lg-4, .col-md-6, .col-sm-6, .col-12"); // 商品外層
    const currentCriteria = {
        min: undefined,
        max: undefined,
        hardness: undefined,
        material: undefined,
        type: undefined
    };

    // 篩選商品
    function filterProducts() {
        let visibleCount = 0;
        $productCols.each(function () {
            const $col = $(this);
            const $prod = $col.find(".single-grid-product");

            let show = true;
            const price = parseInt($prod.data("price")) || 0;

            // 價格篩選
            if ((currentCriteria.min !== undefined && price < currentCriteria.min) ||
                (currentCriteria.max !== undefined && price > currentCriteria.max)) {
                show = false;
            }

            // 軟硬度篩選
            if (currentCriteria.hardness && $prod.data("hardness") !== currentCriteria.hardness) {
                show = false;
            }

            // 材質篩選
            if (currentCriteria.material) {
                const prodMaterial = $prod.data("material") || '';
                if (!prodMaterial.includes(currentCriteria.material)) {
                    show = false;
                }
            }

            // 類型篩選
            if (currentCriteria.type && $prod.data("type") !== currentCriteria.type) {
                show = false;
            }

            show ? $col.show() : $col.hide();
            if (show) visibleCount++;
        });

        return visibleCount;
    }

    // 篩選按鈕
	$(document).on('click', '.filter-btn', function () {
	    const $btn = $(this);

	    if ($btn.data("min") !== undefined) {
	        const isActive =
	            currentCriteria.min === $btn.data("min") &&
	            currentCriteria.max === $btn.data("max");

	        $('.filter-btn[data-min]').removeClass('active');

	        if (isActive) {
	            currentCriteria.min = undefined;
	            currentCriteria.max = undefined;
	        } else {
	            currentCriteria.min = $btn.data("min");
	            currentCriteria.max = $btn.data("max");
	            $btn.addClass('active');
	        }
	    }

	    if ($btn.data("hardness") !== undefined) {
	        const isActive = currentCriteria.hardness === $btn.data("hardness");

	        $('.filter-btn[data-hardness]').removeClass('active');

	        if (isActive) {
	            currentCriteria.hardness = undefined;
	        } else {
	            currentCriteria.hardness = $btn.data("hardness");
	            $btn.addClass('active');
	        }
	    }

	    if ($btn.data("material") !== undefined) {
	        const isActive = currentCriteria.material === $btn.data("material");

	        $('.filter-btn[data-material]').removeClass('active');

	        if (isActive) {
	            currentCriteria.material = undefined;
	        } else {
	            currentCriteria.material = $btn.data("material");
	            $btn.addClass('active');
	        }
	    }

	    if ($btn.data("type") !== undefined) {
	        const isActive = currentCriteria.type === $btn.data("type");

	        $('.filter-btn[data-type]').removeClass('active');

	        if (isActive) {
	            currentCriteria.type = undefined;
	        } else {
	            currentCriteria.type = $btn.data("type");
	            $btn.addClass('active');
	        }
	    }

	    filterProducts();
	});


    // 排序
    $("#sort-by").on("change", function () {
        const sortValue = $(this).val();
        const $visibleProducts = $productCols.filter(":visible");

        function getPrice($prod) {
            const discounted = $prod.find(".discounted-price").first();
            const main = $prod.find(".main-price").first();
            if (discounted.length) return parseFloat(discounted.text().replace("$", ""));
            if (main.length) return parseFloat(main.text().replace("$", ""));
            return 0;
        }

        $visibleProducts.sort((a, b) => {
            const $a = $(a).find(".single-grid-product");
            const $b = $(b).find(".single-grid-product");
            return sortValue === "2" ? getPrice($b) - getPrice($a) : getPrice($a) - getPrice($b);
        });

        $visibleProducts.appendTo($productContainer);
    });

    // 商品選項與計價
    const selects = [
        $('#size-select'),
        $('#weight-select'),
        $('#material-select'),
        $('#feature-select'),
        $('#cloth-select')
    ];

    const bedFrameSelect = $('#bed-frame-select');
    const qtyInput = $('.pro-qty input');
    const totalPriceElement = $('#total-price');
    const BED_FRAME_PRICE = 5000;

    function updateTotal() {
        let total = 0;
        selects.forEach(function($select) {
            const price = parseFloat($select.find('option:selected').data('price')) || 0;
            total += price;
        });

        if(bedFrameSelect.val() === '1') {
            total += BED_FRAME_PRICE;
        }

        const qty = parseInt(qtyInput.val()) || 1;
        total = total * qty;
        totalPriceElement.text('$' + total.toLocaleString());
    }

    selects.forEach($select => $select.on('change', updateTotal));
    bedFrameSelect.on('change', updateTotal);
    qtyInput.on('input', updateTotal);
    updateTotal();

    // --------------------------
    // 搜尋商品
    // --------------------------
    const searchInput = $('#searchInput');
    const searchBtn = $('#searchBtn');

    searchBtn.on('click', function(event) {
        event.preventDefault();
        const value = searchInput.val().trim();

        if (value === "") {
            alert("請輸入商品名稱");
            return;
        }

        const chineseRegex = /^[\u4e00-\u9fa5]+$/;
        if (!chineseRegex.test(value)) {
            alert("只可搜尋商品名稱（中文）");
            return;
        }

        let found = false;
        $productCols.each(function() {
            const $col = $(this);
            const name = $col.find(".single-grid-product .title a").text().trim();
            if (name.includes(value)) {
                $col.show();
                found = true;
            } else {
                $col.hide();
            }
        });

        if (!found) {
            alert("查無商品");
            $productCols.show(); // 重新顯示全部商品
        }
    });

    // 按 Enter 鍵觸發搜尋
    searchInput.on('keypress', function(e) {
        if (e.which === 13) {
            e.preventDefault();
            searchBtn.click();
        }
    });
});
