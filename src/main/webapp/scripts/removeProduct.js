document.addEventListener('DOMContentLoaded', function() {
    var deleteButton = document.getElementById('deleteProductBtn');
    if (deleteButton) {
        deleteButton.addEventListener('click', function() {
            var productId = this.getAttribute('data-id');
            fetch(contextPath+'/DeleteProductServlet', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: `id=${productId}`
            })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        Swal.fire({
                            icon: 'success',
                            title: 'Successo',
                            text: data.message
                        }).then(() => {
                            window.location.reload();  // Ricaricare la pagina per rifrescare la dashboard
                        });
                    } else {
                        Swal.fire({
                            icon: 'error',
                            title: 'Errore',
                            text: data.message
                        });
                    }
                })
                .catch(error => console.error('Errore durante la richiesta:', error));
        });
    }
});
