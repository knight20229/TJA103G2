$(document).ready(function() {

    const $sizeSelect = $('select[name="size"]');
    const $qtyInput = $('.qty-input');

    // --------------------------
    // 初始化尺寸選項狀態
    // --------------------------
    let firstAvailable = null;
    $sizeSelect.find('option').each(function() {
        const stock = parseInt($(this).data('stock')) || 0;

        if (stock <= 0) {
            $(this).attr('disabled', true);
        } else {
            $(this).removeAttr('disabled');
            if (!firstAvailable) firstAvailable = $(this);
        }
    });

    if (firstAvailable) {
        firstAvailable.prop('selected', true);
        // 將數量最大值設為該尺寸庫存
        $qtyInput.attr('max', firstAvailable.data('stock'));
        $qtyInput.val(1); // 預設數量 1
    }

    if ($sizeSelect.hasClass('nice-select')) {
        $sizeSelect.niceSelect('update');
    }

    // --------------------------
    // 數量按鈕
    // --------------------------
    $('.qty-btn').on('click', function() {
        const $input = $(this).siblings('.qty-input');
        const min = parseInt($input.attr('min')) || 1;
        const max = parseInt($input.attr('max')) || 1; // 最大值現在是庫存
        let value = parseInt($input.val()) || min;

        if ($(this).hasClass('plus')) {
            value = Math.min(max, value + 1);
        } else if ($(this).hasClass('minus')) {
            value = Math.max(min, value - 1);
        }

        $input.val(value);
        updateTotalPrice();
    });

    // --------------------------
    // 尺寸選擇改變價格
    // --------------------------
    $sizeSelect.on('change', function() {
        const selectedStock = parseInt($sizeSelect.find('option:selected').data('stock')) || 0;

        // 更新數量最大值為庫存
        $qtyInput.attr('max', selectedStock);
        // 如果當前數量大於庫存，改為庫存
        if (parseInt($qtyInput.val()) > selectedStock) {
            $qtyInput.val(selectedStock);
        }

        updateTotalPrice();
    });

    // --------------------------
    // 更新總價
    // --------------------------
    function updateTotalPrice() {
        const selectedSize = $sizeSelect.find('option:selected');
        const unitPrice = parseInt(selectedSize.val()) || 0;
        const qty = parseInt($qtyInput.val()) || 1;

        $('.discounted-price').text('$' + (unitPrice * qty).toLocaleString());
    }

    // 初始化價格
    updateTotalPrice();

});
