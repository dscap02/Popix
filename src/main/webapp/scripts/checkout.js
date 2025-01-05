$(document).ready(function () {
    $('#checkoutForm').submit(function (e) {
        e.preventDefault(); // Per evitare il normale invio del modulo

        if (validateForm()) {
            $.ajax({
                url: contextPath + '/CheckoutServlet',
                method: 'POST',
                data: $(this).serialize(),
                success: function (response) {
                    if (response === "Ordine completato con successo!") {
                        Swal.fire({
                            icon: 'success',
                            title: 'Successo!',
                            text: response,
                            showConfirmButton: false,
                            timer: 1500
                        }).then(() => {
                            window.location.href = contextPath + '/jsp/HomePage.jsp';
                        });
                    } else {
                        Swal.fire({
                            icon: 'error',
                            title: 'Errore!',
                            text: response,
                            showConfirmButton: true
                        });
                    }
                },
                error: function (xhr, status, error) {
                    Swal.fire({
                        icon: 'error',
                        title: 'Errore!',
                        text: 'Si è verificato un problema. Riprova.',
                        showConfirmButton: true
                    });
                }
            });
        }
    });
});
