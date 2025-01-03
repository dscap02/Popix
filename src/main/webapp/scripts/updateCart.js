document.addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('.quantity-box').forEach(box => {
        box.addEventListener('change', (e) => {
            const productId = e.target.getAttribute('data-id');
            const newQty = e.target.value;

            if (newQty > 0) {
                fetch(`${contextPath}/UpdateCartServlet`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({ productId, qty: newQty }),
                }).then(response => response.json())
                    .then(data => {
                        if (data.success) {
                            location.reload();
                        } else {
                            Swal.fire('Errore', 'Non è stato possibile aggiornare il carrello.', 'error');
                        }
                    });
            } else {
                Swal.fire('Errore', 'La quantità deve essere almeno 1.', 'error');
                e.target.value = 1;
            }
        });
    });
});
