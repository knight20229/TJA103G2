$(document).ready(function() {

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

    // 綁定事件
    selects.forEach(function($select) {
        $select.on('change', updateTotal);
    });

    bedFrameSelect.on('change', updateTotal);
    qtyInput.on('input', updateTotal);

    // 初始化小計
    updateTotal();

});
