$(document).ready(function() {
    let csrfToken = $("input[name='_csrf']").val();

    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader('X-CSRF-Token', csrfToken);
    });
})