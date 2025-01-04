function validateForm() {
    let valid = true;
    let errors = [];

    const name = document.getElementById('name').value.trim();
    const surname = document.getElementById('surname').value.trim();
    const country = document.getElementById('country').value.trim();
    const city = document.getElementById('city').value.trim();
    const address = document.getElementById('address').value.trim();
    const cap = document.getElementById('cap').value.trim();
    const birthdayDate = document.getElementById('birthday_date').value.trim();
    const cellulare = document.getElementById('cellulare').value.trim();
    const clienteEmail = document.getElementById('cliente_email').value.trim();

    const cardNumber = document.getElementById('card_number').value.trim();
    const cvc = document.getElementById('cvc').value.trim();
    const owner = document.getElementById('owner').value.trim();
    const expiration = document.getElementById('expiration').value.trim();

    if (!name) {
        valid = false;
        errors.push('Il campo Nome è obbligatorio!');
    }

    if (!surname) {
        valid = false;
        errors.push('Il campo Cognome è obbligatorio!');
    }

    if (!country) {
        valid = false;
        errors.push('Il campo Paese è obbligatorio!');
    }

    if (!city) {
        valid = false;
        errors.push('Il campo Città è obbligatorio!');
    }

    if (!address) {
        valid = false;
        errors.push('Il campo Indirizzo è obbligatorio!');
    }

    if (!cap || !/^\d{5}$/.test(cap)) {
        valid = false;
        errors.push('Inserisci un CAP valido (5 cifre numeriche).');
    }

    if (!birthdayDate) {
        valid = false;
        errors.push('Il campo Data di Nascita è obbligatorio!');
    } else {
        const today = new Date();
        const birthDate = new Date(birthdayDate);
        let age = today.getFullYear() - birthDate.getFullYear();
        if (today.getMonth() < birthDate.getMonth() || (today.getMonth() === birthDate.getMonth() && today.getDate() < birthDate.getDate())) {
            age--;
        }
        if (age < 18) {
            valid = false;
            errors.push('Devi avere almeno 18 anni per procedere.');
        }
    }

    if (!cellulare || !/^\d{10}$/.test(cellulare)) {
        valid = false;
        errors.push('Inserisci un numero di cellulare valido (10 cifre numeriche).');
    }

    if (!clienteEmail) {
        valid = false;
        errors.push('Il campo Email è obbligatorio!');
    }

    if (!cardNumber || !/^\d{13,16}$/.test(cardNumber)) {
        valid = false;
        errors.push('Inserisci un numero di carta valido (tra 13 e 16 cifre numeriche).');
    }

    if (!cvc || !/^\d{3,4}$/.test(cvc)) {
        valid = false;
        errors.push('Inserisci un CVC valido (3-4 cifre numeriche).');
    }

    if (!owner) {
        valid = false;
        errors.push('Il campo Titolare Carta è obbligatorio!');
    }

    if (!expiration) {
        valid = false;
        errors.push('Il campo Data di Scadenza è obbligatorio!');
    } else {
        const today = new Date();
        const expDate = new Date(expiration + '-01');
        if (expDate < today) {
            valid = false;
            errors.push('La carta di credito è scaduta.');
        }
    }

    if (!valid) {
        Swal.fire('Errore', errors.join('<br>'), 'error');
    }

    return valid;
}
