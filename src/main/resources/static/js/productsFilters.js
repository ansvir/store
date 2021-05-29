$(document).ready(function() {

    let filters = {
        description: '',
        tags: ''
    };

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
});