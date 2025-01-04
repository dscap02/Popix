document.addEventListener('DOMContentLoaded', function () {
    const form = document.querySelector('#productForm'); // Assicurati che il form abbia l'id 'productForm'

    form.addEventListener('submit', function (event) {
        event.preventDefault(); // Blocca il comportamento predefinito del form

        const id = document.getElementById('idProduct').value.trim();
        const name = document.getElementById('name').value.trim();
        const description = document.getElementById('description').value.trim();
        const cost = document.getElementById('price').value.trim();
        const piecesInStock = document.getElementById('qty').value.trim();
        const brand = document.getElementById('brand').value.trim();
        const figure = document.getElementById('figure').value.trim();
        const imgFile = document.getElementById('img_src').files[0];

        // Preparazione dei dati per l'invio
        const formData = new FormData(form);

        // Invio dei dati alla servlet
        fetch(form.action, {
            method: 'POST',
            body: formData
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    Swal.fire({
                        icon: 'success',
                        title: 'Successo!',
                        text: data.message,
                        showConfirmButton: false,
                        timer: 1500
                    }).then(() => location.reload());
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
                console.error('Error:', error);
                Swal.fire({
                    icon: 'error',
                    title: 'Errore di sistema',
                    text: 'Si è verificato un errore durante l\'invio dei dati.',
                    showConfirmButton: true
                });
            });
    });
});
