document.addEventListener('DOMContentLoaded', function() {
    var form = document.getElementById('login-form');
    form.addEventListener('submit', function(event) {
        event.preventDefault(); // Impedisce il comportamento predefinito del form (ricaricamento della pagina)
        checkCredentials(); // Chiamata alla funzione che gestisce l'autenticazione tramite AJAX
    });

    function checkCredentials() {
        var xhr = new XMLHttpRequest();
        var url = contextPath + '/login';
        xhr.open('POST', url, true);
        xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        xhr.onreadystatechange = function() {
            if (xhr.readyState === 4) {
                if (xhr.status === 200) {
                    var response = xhr.responseText;
                    if (response === "Nome utente o password sbagliata.") {
                        Swal.fire({
                            icon: 'error',
                            title: 'Oops...',
                            text: 'Nome utente o password sbagliata.'
                        });
                    } else {
                        // SweetAlert di conferma e reindirizzamento
                        Swal.fire({
                            icon: 'success',
                            title: 'Accesso riuscito!',
                            text: 'Benvenuto!',
                            showConfirmButton: false,
                            timer: 1500
                        }).then(() => {
                            // Reindirizza l'utente alla homepage
                            window.location.href = contextPath + '/jsp/HomePage.jsp';
                        });
                    }
                } else {
                    Swal.fire({
                        icon: 'error',
                        title: 'Errore',
                        text: 'Errore durante la richiesta al server. Codice: ' + xhr.status
                    });
                }
            }
        };
        xhr.send('&email=' + encodeURIComponent(document.getElementById('email').value) +
            '&password=' + encodeURIComponent(document.getElementById('password').value));
    }
});
