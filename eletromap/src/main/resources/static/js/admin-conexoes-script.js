// --- Variáveis Globais ---
let allConexoes = [];
let filteredConexoes = [];
let allUnits = []; // Para popular os selects do modal
let currentDeletingId = null;

const conexoesApiUrl = '/conexoes';
const unidadesApiUrl = '/unidades'; // Precisamos buscar as unidades para o formulário

// --- Elementos do DOM ---
const elements = {
    // Filtros
    searchInput: document.getElementById('search-unit-id'),
    sortBy: document.getElementById('sort-by'),
    
    // Tabela e Contagem
    conexoesTableBody: document.getElementById('conexoes-table-body'),
    conexoesCount: document.getElementById('conexoes-count'),
    
    // Botões Principais
    addConexaoBtn: document.getElementById('btn-add-conexao'),
    
    // Modal de Adicionar Conexão
    conexaoModal: document.getElementById('conexao-modal'),
    conexaoForm: document.getElementById('conexao-form'),
    origemSelect: document.getElementById('conexao-origem'),
    destinoSelect: document.getElementById('conexao-destino'),
    closeModalBtn: document.getElementById('close-modal'),
    cancelModalBtn: document.getElementById('cancel-modal'),
    
    // Modal de Excluir
    deleteModal: document.getElementById('delete-modal'),
    cancelDeleteBtn: document.getElementById('cancel-delete'),
    confirmDeleteBtn: document.getElementById('confirm-delete'),

    // Notificações
    toastContainer: document.getElementById('toast-container')
};

// --- Inicialização ---
document.addEventListener('DOMContentLoaded', initializePage);

async function initializePage() {
    bindEvents();
    // Carrega tanto as conexões quanto as unidades em paralelo para otimizar
    showLoadingState();
    try {
        await Promise.all([
            loadConexoes(),
            loadUnitsForSelects()
        ]);
        showToast('Dados carregados com sucesso!', 'success');
    } catch (error) {
        showErrorState();
        showToast('Erro ao carregar dados iniciais.', 'error');
    }
}

// --- Vinculação de Eventos ---
function bindEvents() {
    elements.searchInput.addEventListener('input', debounce(applyFilters, 300));
    elements.sortBy.addEventListener('change', applyFilters);
    
    elements.addConexaoBtn.addEventListener('click', openAddModal);
    elements.closeModalBtn.addEventListener('click', closeModal);
    elements.cancelModalBtn.addEventListener('click', closeModal);
    elements.conexaoForm.addEventListener('submit', handleFormSubmit);
    
    elements.cancelDeleteBtn.addEventListener('click', closeDeleteModal);
    elements.confirmDeleteBtn.addEventListener('click', confirmDelete);

    document.addEventListener('keydown', (e) => {
        if (e.key === 'Escape') {
            closeModal();
            closeDeleteModal();
        }
    });
}

// --- Funções de API ---
async function loadConexoes() {
    const response = await fetch(conexoesApiUrl);
    if (!response.ok) throw new Error('Falha ao buscar conexões');
    allConexoes = await response.json();
    applyFilters();
}

async function loadUnitsForSelects() {
    const response = await fetch(unidadesApiUrl);
    if (!response.ok) throw new Error('Falha ao buscar unidades para os seletores');
    allUnits = await response.json();
    populateSelects();
}

async function createConexao(origemId, destinoId) {
    const response = await fetch(`${conexoesApiUrl}?origemId=${origemId}&destinoId=${destinoId}`, {
        method: 'POST'
    });
    if (!response.ok) throw new Error('Falha ao criar conexão');
    await loadConexoes(); // Recarrega a lista
}

async function deleteConexao(id) {
    const response = await fetch(`${conexoesApiUrl}/${id}`, {
        method: 'DELETE'
    });
    if (!response.ok) throw new Error('Falha ao excluir conexão');
    await loadConexoes(); // Recarrega a lista
}

// --- Lógica de Filtragem e Ordenação ---
function applyFilters() {
    const searchTerm = elements.searchInput.value.trim();
    const sortBy = elements.sortBy.value;

    filteredConexoes = allConexoes.filter(conexao => {
        if (!searchTerm) return true;
        // Verifica se o ID buscado corresponde à origem OU ao destino
        return conexao.origem.id.toString() === searchTerm || conexao.destino.id.toString() === searchTerm;
    });

    filteredConexoes.sort((a, b) => {
        switch (sortBy) {
            case 'origem': return a.origem.id - b.origem.id;
            case 'destino': return a.destino.id - b.destino.id;
            default: return a.id - b.id; // Ordenar por ID da conexão por padrão
        }
    });

    renderConexoes();
    updateConexoesCount();
}

// --- Renderização na Tela ---
function renderConexoes() {
    const tbody = elements.conexoesTableBody;
    if (filteredConexoes.length === 0) {
        showEmptyState();
        return;
    }

    tbody.innerHTML = filteredConexoes.map(conexao => `
        <tr class="hover:bg-gray-50 transition-colors">
            <td class="px-6 py-4 text-sm font-medium text-gray-900">#${conexao.id}</td>
            <td class="px-6 py-4 text-sm text-gray-700">
                <span class="font-bold">ID: ${conexao.origem.id}</span> - ${escapeHtml(conexao.origem.endereco)}
            </td>
            <td class="px-6 py-4 text-sm text-gray-700">
                <span class="font-bold">ID: ${conexao.destino.id}</span> - ${escapeHtml(conexao.destino.endereco)}
            </td>
            <td class="px-6 py-4 text-right">
                <button 
                    onclick="openDeleteModal(${conexao.id})"
                    class="text-red-600 hover:text-red-900 transition-colors p-1"
                    title="Excluir conexão"
                >
                    <i class="fas fa-trash"></i>
                </button>
            </td>
        </tr>
    `).join('');
}

function updateConexoesCount() {
    elements.conexoesCount.textContent = filteredConexoes.length;
}

// --- Estados da Tabela (Carregando, Vazio, Erro) ---
function showLoadingState() {
    elements.conexoesTableBody.innerHTML = `<tr><td colspan="4" class="text-center p-12 text-gray-500">Carregando...</td></tr>`;
}

function showEmptyState() {
    elements.conexoesTableBody.innerHTML = `<tr><td colspan="4" class="text-center p-12 text-gray-500">Nenhuma conexão encontrada.</td></tr>`;
}

function showErrorState() {
    elements.conexoesTableBody.innerHTML = `<tr><td colspan="4" class="text-center p-12 text-red-500">Erro ao carregar os dados.</td></tr>`;
}


// --- Lógica do Modal ---
function populateSelects() {
    elements.origemSelect.innerHTML = '<option value="">Selecione a unidade de origem</option>';
    elements.destinoSelect.innerHTML = '<option value="">Selecione a unidade de destino</option>';
    
    allUnits.forEach(unit => {
        const optionHtml = `<option value="${unit.id}">ID: ${unit.id} - ${escapeHtml(unit.endereco)}</option>`;
        elements.origemSelect.innerHTML += optionHtml;
        elements.destinoSelect.innerHTML += optionHtml;
    });
}

function openAddModal() {
    elements.conexaoForm.reset();
    elements.conexaoModal.classList.remove('hidden');
    document.body.style.overflow = 'hidden';
}

function closeModal() {
    elements.conexaoModal.classList.add('hidden');
    document.body.style.overflow = 'auto';
}

function openDeleteModal(id) {
    currentDeletingId = id;
    elements.deleteModal.classList.remove('hidden');
    document.body.style.overflow = 'hidden';
}

function closeDeleteModal() {
    elements.deleteModal.classList.add('hidden');
    document.body.style.overflow = 'auto';
    currentDeletingId = null;
}

async function handleFormSubmit(e) {
    e.preventDefault();
    const origemId = elements.origemSelect.value;
    const destinoId = elements.destinoSelect.value;

    if (!origemId || !destinoId) {
        showToast('Por favor, selecione a origem e o destino.', 'error');
        return;
    }
    if (origemId === destinoId) {
        showToast('A origem e o destino não podem ser iguais.', 'error');
        return;
    }

    try {
        await createConexao(origemId, destinoId);
        showToast('Conexão criada com sucesso!', 'success');
        closeModal();
    } catch (error) {
        console.error("Erro no formulário:", error);
        showToast('Não foi possível criar a conexão.', 'error');
    }
}

async function confirmDelete() {
    if (!currentDeletingId) return;

    try {
        await deleteConexao(currentDeletingId);
        showToast('Conexão excluída com sucesso!', 'success');
    } catch (error) {
        console.error("Erro ao deletar:", error);
        showToast('Não foi possível excluir a conexão.', 'error');
    } finally {
        closeDeleteModal();
    }
}

// --- Funções Utilitárias ---
function debounce(func, wait) {
    let timeout;
    return function executedFunction(...args) {
        const later = () => {
            clearTimeout(timeout);
            func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
}

function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

function showToast(message, type = 'info') {
    // Implementação do Toast pode ser copiada do admin-script.js original
    const toast = document.createElement('div');
    const colors = { info: 'bg-blue-500', success: 'bg-green-500', error: 'bg-red-500' };
    toast.className = `${colors[type]} text-white px-4 py-3 rounded-lg shadow-lg`;
    toast.textContent = message;
    elements.toastContainer.appendChild(toast);
    setTimeout(() => toast.remove(), 3000);
}

// Disponibiliza funções no escopo global para serem chamadas pelo onclick no HTML
window.openDeleteModal = openDeleteModal;