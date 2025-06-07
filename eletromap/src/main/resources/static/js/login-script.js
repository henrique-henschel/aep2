// Garante que o script só execute após o carregamento completo do HTML
document.addEventListener('DOMContentLoaded', () => {

    // Seleciona os elementos do formulário uma única vez para melhor performance
    const loginForm = document.getElementById('loginForm');
    const credencialInput = document.getElementById('credencial');
    const passwordInput = document.getElementById('password');
    const errorMessageElement = document.getElementById('errorMessage');

    // Adiciona o evento de 'submit' ao formulário
    loginForm.addEventListener('submit', (event) => {
        // Previne o recarregamento padrão da página em TODOS os casos
        event.preventDefault();

        // Obtém e limpa os valores dos campos
        const credencial = credencialInput.value.trim();
        const password = passwordInput.value.trim();

        // Limpa mensagens de erro antigas
        errorMessageElement.textContent = '';

        // 1. Validação de campos vazios
        if (credencial === '' || password === '') {
            errorMessageElement.textContent = 'Por favor, preencha todos os campos.';
            return; // Interrompe a execução da função aqui
        }

        // 2. Validação das credenciais de administrador
        // (Considerando 'admin' e 'admin123' como as credenciais corretas para o admin)
        if (credencial === 'admin' && password === 'admin123') {
            window.location.href = 'admin.html'; // Redireciona para a página de admin
            return;
        }
        
        // 3. Redirecionamento para prototipagem (qualquer outra credencial válida)
        window.location.href = 'mapa.html';
    });
});