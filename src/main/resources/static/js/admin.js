$(document).ready(function() {

    let $modal = $('#productActionModal');
    let $closeModal = $('#closeModal');
    let $changeProductButtons = $('[id^="changeProduct-"]');
    let $deleteProductButtons = $('[id^="deleteProduct-"]');
    let $forceUpdateProductButtons = $('[id^="forceUpdateProduct-"]');
    let $addProductButton = $('#addProduct');
    let $productLoaders = $('[id^="loaderProduct-"]');

    for (let i = 0; i < $productLoaders.length; i++) {
        $($productLoaders[i]).hide();
    }

    $('#loaderForm').hide();

    let products;

    $.ajax({
        type: 'GET',
        url: contextPath + '/product/get/all',
        success: function(productsResponse) {
            products = productsResponse;
            let currentProductId;
            let currentProduct;
            let currentCommand;

            for (let i = 0; i < $changeProductButtons.length; i++) {
                $(document).on('click', `#${$changeProductButtons[i].id}`, function() {
                    currentCommand = 'change_product';
                    currentProductId = $(this).attr('id').substring(14);
                    for (let i = 0;i < products.length; i++) {
                        if (products[i].id == currentProductId) {
                            currentProduct = products[i];
                        }
                    }
                    $($modal).css("display", "block");
                    $('#modalHeader').text(`Change product #${currentProductId}`);
                    for (let i = 0; i < products.length; i++) {
                        if (products[i].id == currentProductId) {
                            $('#productName').val(products[i].name);
                            $('#productDescription').val(products[i].description);
                            let tags = products[i].tags;
                            let tagsNames = [];
                            for (let j = 0; j < tags.length; j++) {
                                tagsNames.push(tags[j].name);
                            }
                            $("#productTags").val(tagsNames);
                        }
                    }
                });
            }

            $($addProductButton).on('click', function() {
                cleanModal();
                $($modal).css("display", "block");
                $('#modalHeader').text("Create new product");
                currentCommand = 'add_product'
            });

            for (let i = 0; i < $deleteProductButtons.length; i++) {
                $(document).on('click', `#${$deleteProductButtons[i].id}`, function() {
                    currentCommand = 'delete_product';
                    currentProductId = $deleteProductButtons[i].id.substring(14)
                });
            }

            for (let i = 0; i < $forceUpdateProductButtons.length; i++) {
                $(document).on('click', `#${$forceUpdateProductButtons[i].id}`, function() {
                    currentCommand = 'force_update';
                    currentProductId = $forceUpdateProductButtons[i].id.substring(19);
                    for (let i = 0; i < products.length; i++) {
                        if (products[i].id == currentProductId) {
                            currentProduct = products[i];
                        }
                    }

                    $('#productName').val(currentProduct.name);
                    $('#productDescription').val(currentProduct.description);
                    let currentTagsNames = [];
                    for (let i = 0; i < currentProduct.tags.length; i++) {
                        currentTagsNames.push(currentProduct.tags[i].name);
                    }
                    $('#productTags').val(currentTagsNames);
                });
            }

            $($closeModal).on('click', function() {
                $($modal).css("display", "none");
            });

            $('#productsActionsForm').submit(function(e) {
                $('#command').val(currentCommand);
                $("#productId").val(currentProductId);
                $('#loaderForm').show();
                $('#loaderProduct-' + currentProductId).show();
            });

            function cleanModal() {
                $('#productName').val('')
                $('#productDescription').val('');
                $("#productTags").val([]);
            }
        }
    });

    // $(document).on('click', function() {
    //     $($modal).css("display", "none");
    // });
});