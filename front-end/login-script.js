document.getElementById('loginForm').addEventListener('submit', (event) => {
    const credencial = document.getElementById('credencial').value.trim();
    const password = document.getElementById('password').value.trim();

    if (credencial === '' || password === '') {
        event.preventDefault();
        alert('Por favor, preencha todos os campos do formulário.');
    }
});
