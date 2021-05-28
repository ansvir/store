$(document).ready(function () {
    let $productsTrs = $('[id^="product-"]');
    let productTrsIds = [];
    let csrfToken = $("input[name='_csrf']").val();

    let $addToCartTrs;
    let $removeFromCartTrs;

    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader('X-CSRF-Token', csrfToken);
    });

    for (let i = 0; i < $productsTrs.length; i++) {
        productTrsIds.push($productsTrs[i].id.substring(8));
    }
    $.when(
        $.ajax({
            type: 'GET',
            url: contextPath + '/cart/products',
            success: function (cart) {
                let inCart = false;
                let products = cart.products;
                for (let j = 0; j < productTrsIds.length; j++) {
                    for (let i = 0; i < products.length; i++) {
                        if (productTrsIds[j] == products[i].id) {
                            inCart = true;
                            break;
                        }
                    }
                    if (inCart) {
                        $($productsTrs[j]).append(`
                        <td id="removeFromCart-${productTrsIds[j]}">
                            <span id="removeFromCartLabel-${productTrsIds[j]}" class="in-cart productAction disable-select">
                                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-dash-lg" viewBox="0 0 16 16">
                                  <path d="M0 8a1 1 0 0 1 1-1h14a1 1 0 1 1 0 2H1a1 1 0 0 1-1-1z"/>
                                </svg>
                            </span>
                        </td>
                    `);
                    } else {
                        $($productsTrs[j]).append(`
                        <td id="addToCart-${productTrsIds[j]}">
                            <span id="addToCartLabel-${productTrsIds[j]}" class="add-to-cart productAction disable-select">+</span>
                        </td>
                    `);
                    }
                    inCart = false;
                }

                $addToCartTrs = $('[id^="addToCart-"]');
                $removeFromCartTrs = $('[id^="removeFromCart-"]');
            }
        })).done(function (ajax) {
        $(document).on('click', `.productAction`, function () {
            toggleProductAction($(this));
        });
    });

    /**
     *
     * @param $currentAction jQuery <tr> with the <span> inside, which id ends with '...Label-${productId}'
     *
     */
    function toggleProductAction($currentAction) {
        let actionId = $currentAction[0].id;
        let productId;
        if (actionId.startsWith("addToCartLabel-")) {
            productId = actionId.substring(15);
            $(`#addToCart-${productId}`).remove();
            $(`#product-${productId}`).append(`
                <td id="removeFromCart-${productId}">
                    <span id="removeFromCartLabel-${productId}" class="in-cart productAction disable-select">
                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-dash-lg" viewBox="0 0 16 16">
                          <path d="M0 8a1 1 0 0 1 1-1h14a1 1 0 1 1 0 2H1a1 1 0 0 1-1-1z"/>
                        </svg>
                    </span>
                </td>
            `);
            sendAjaxAddToCart(productId);
        } else if (actionId.startsWith("removeFromCartLabel-")) {
            productId = actionId.substring(20);
            $(`#removeFromCart-${productId}`).remove();
            $(`#product-${productId}`).append(`
                <td id="addToCart-${productId}">
                    <span id="addToCartLabel-${productId}" class="add-to-cart productAction disable-select">+</span>
                </td>
            `);
            sendAjaxRemoveFromCart(productId);
        }
    }

    let filters = {
        description: '',
        tags: ''
    };

    let descriptionsTags = [];
    let tableTrs = $('#productsTable').find('tr');
    for (let i = 0; i < tableTrs.length; i++) {
        descriptionsTags.push({description: tableTrs[1], tags: tableTrs[2]});
    }

    $(document).on('change', '#searchTags', function () {
        filters.tags = $(this).val().toUpperCase();
        runSearch(filters);
    })

    $(document).on('input', '#searchProducts', function () {
        filters.description = $('#searchProducts').val().toUpperCase();
        runSearch(filters);
    });

    function runSearch(filters) {
        let trs = $('#productsTable').find('tr');

        let restTrs = $.grep(trs, tr => {
            return $($(tr).find('td')[1]).text().toUpperCase().indexOf(filters.description) > -1 &&
                $($(tr).find('td')[2]).text().toUpperCase().indexOf(filters.tags) > -1 ||
                filters.description === '' && filters.tags === '-';
        });

        $(trs).hide();
        $(restTrs).show();
    }

    function sendAjaxGetQuantity() {
        return $.ajax({
            type: 'GET',
            url: contextPath + '/cart/quantity',
            success: function (response) {
                $('#cartNumber').text(response);
            }
        });
    }

    function sendAjaxAddToCart(productId) {
        return $.ajax({
            type: 'POST',
            url: contextPath + '/cart/add',
            data:
                JSON.stringify({
                    'productId': `${productId}`
                }),
            contentType: "application/json",
            success: function () {
                sendAjaxGetQuantity();
            },
            error: function (xhr, status, error) {
                let errorMessage = xhr.status + ': ' + xhr.statusText
                console.log('Error - ' + errorMessage);
            }
        });
    }

    function sendAjaxRemoveFromCart(productId) {
        return $.ajax({
            type: 'POST',
            url: contextPath + '/cart/remove',
            data:
                JSON.stringify({
                    'productId': `${productId}`
                }),
            contentType: "application/json",
            success: function () {
                sendAjaxGetQuantity();
            },
            error: function (xhr, status, error) {
                let errorMessage = xhr.status + ': ' + xhr.statusText
                console.log('Error - ' + errorMessage);
            }
        });
    }
});