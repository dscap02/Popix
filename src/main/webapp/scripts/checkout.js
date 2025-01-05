$(document).ready(function () {
    $('#checkoutForm').submit(function (e) {
        e.preventDefault(); // Per evitare il normale invio del modulo

        if (validateForm()) {
            $.ajax({
                url: contextPath + '/CheckoutServlet',
                method: 'POST',
                data: $(this).serialize(),
                success: function (response) {
                    var data = JSON.parse(response);
                    Swal.fire('Successo', data.message, 'success').then(() => {
                        window.location.href = contextPath + '/jsp/HomePage.jsp';
                    });
                },
                error: function (xhr, status, error) {
                    var data = JSON.parse(xhr.responseText);
                    Swal.fire('Errore', data.error, 'error');
                }
            });
        }
    });
});
