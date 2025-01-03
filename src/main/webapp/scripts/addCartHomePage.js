document.addEventListener('DOMContentLoaded', function () {
    const cartButtons = document.querySelectorAll('.cart-btn');

    cartButtons.forEach(button => {
        button.addEventListener('click', function (event) {
            event.preventDefault(); // Previene il comportamento predefinito del link

            const productContainer = this.closest('.box'); // Trova il container del prodotto
            const productId = productContainer.querySelector('img').src.split('id=')[1]; // Estrae l'ID dal link immagine
            const quantity = 1; // Per questa pagina, aggiunge 1 al carrello

            fetch(contextPath + '/addToCart', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: `productId=${productId}&quantity=${quantity}`
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Errore di rete');
                    }
                    return response.json();
                })
                .then(data => {
                    if (data.success) {
                        Swal.fire({
                            icon: 'success',
                            title: 'Successo!',
                            text: data.message,
                            showConfirmButton: false,
                            timer: 1500
                        });
                    } else {
                        Swal.fire({
                            icon: 'error',
                            title: 'Errore!',
                            text: data.message,
                            showConfirmButton: true
                        });
                    }
                })
                .catch(error => {
                    console.error('Errore:', error);
                    Swal.fire('Errore', 'Si è verificato un problema. Riprova.', 'error');
                });
        });
    });
});
