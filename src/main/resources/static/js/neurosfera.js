/**
 * NEUROSFERA - API CLIENT
 * Funções para comunicação com o backend Spring Boot
 */

const API_BASE_URL = 'http://localhost:8080/api';

// ============================================
// API HELPER
// ============================================
const api = {
    /**
     * Função genérica para fazer requisições
     */
    async request(url, options = {}) {
        try {
            const response = await fetch(`${API_BASE_URL}${url}`, {
                headers: {
                    'Content-Type': 'application/json',
                    ...options.headers
                },
                ...options
            });

            if (!response.ok) {
                const errorData = await response.json().catch(() => ({}));
                throw new Error(errorData.erro || errorData.message || `Erro HTTP: ${response.status}`);
            }

            // Se for DELETE, pode não ter conteúdo
            if (response.status === 204 || options.method === 'DELETE') {
                return null;
            }

            return await response.json();
        } catch (error) {
            console.error('Erro na requisição:', error);
            throw error;
        }
    },

    // ============================================
    // USUÁRIOS
    // ============================================

    /**
     * Listar todos os usuários
     */
    async getUsuarios() {
        return await this.request('/usuarios');
    },

    /**
     * Buscar usuário por ID
     */
    async getUsuario(id) {
        return await this.request(`/usuarios/${id}`);
    },

    /**
     * Criar novo usuário
     */
    async createUsuario(usuario) {
        return await this.request('/usuarios', {
            method: 'POST',
            body: JSON.stringify(usuario)
        });
    },

    /**
     * Atualizar usuário
     */
    async updateUsuario(id, usuario) {
        return await this.request(`/usuarios/${id}`, {
            method: 'PUT',
            body: JSON.stringify(usuario)
        });
    },

    /**
     * Deletar usuário
     */
    async deleteUsuario(id) {
        return await this.request(`/usuarios/${id}`, {
            method: 'DELETE'
        });
    },

    /**
     * Buscar usuários por tipo
     */
    async getUsuariosPorTipo(tipo) {
        return await this.request(`/usuarios/tipo/${tipo}`);
    },

    /**
     * Buscar usuários ativos
     */
    async getUsuariosAtivos() {
        return await this.request('/usuarios/ativos');
    },

    /**
     * Alterar status do usuário
     */
    async alterarStatusUsuario(id, novoStatus) {
        return await this.request(`/usuarios/${id}/status?novoStatus=${novoStatus}`, {
            method: 'PATCH'
        });
    },

    /**
     * Alterar idioma do usuário
     */
    async alterarIdiomaUsuario(id, novoIdioma) {
        return await this.request(`/usuarios/${id}/idioma?novoIdioma=${novoIdioma}`, {
            method: 'PATCH'
        });
    },

    /**
     * Inscrever usuário em distrito
     */
    async inscreverUsuarioNoDistrito(usuarioId, distritoId) {
        return await this.request(`/usuarios/${usuarioId}/distritos/${distritoId}`, {
            method: 'POST'
        });
    },

    /**
     * Desinscrever usuário de distrito
     */
    async desinscreverUsuarioDoDistrito(usuarioId, distritoId) {
        return await this.request(`/usuarios/${usuarioId}/distritos/${distritoId}`, {
            method: 'DELETE'
        });
    },

    /**
     * Listar distritos do usuário
     */
    async getDistritosDoUsuario(usuarioId) {
        return await this.request(`/usuarios/${usuarioId}/distritos`);
    },

    // ============================================
    // DISTRITOS
    // ============================================

    /**
     * Listar todos os distritos
     */
    async getDistritos() {
        return await this.request('/distritos');
    },

    /**
     * Buscar distrito por ID
     */
    async getDistrito(id) {
        return await this.request(`/distritos/${id}`);
    },

    /**
     * Criar novo distrito
     */
    async createDistrito(distrito) {
        return await this.request('/distritos', {
            method: 'POST',
            body: JSON.stringify(distrito)
        });
    },

    /**
     * Atualizar distrito
     */
    async updateDistrito(id, distrito) {
        return await this.request(`/distritos/${id}`, {
            method: 'PUT',
            body: JSON.stringify(distrito)
        });
    },

    /**
     * Deletar distrito
     */
    async deleteDistrito(id) {
        return await this.request(`/distritos/${id}`, {
            method: 'DELETE'
        });
    },

    /**
     * Buscar distritos por área
     */
    async getDistritosPorArea(area) {
        return await this.request(`/distritos/area/${area}`);
    },

    /**
     * Buscar distritos ativos
     */
    async getDistritosAtivos() {
        return await this.request('/distritos/ativos');
    }
};

// ============================================
// FUNÇÕES AUXILIARES
// ============================================

/**
 * Formatar data para padrão brasileiro
 */
function formatDate(dateString) {
    if (!dateString) return '-';
    const date = new Date(dateString);
    return date.toLocaleDateString('pt-BR');
}

/**
 * Formatar data e hora
 */
function formatDateTime(dateString) {
    if (!dateString) return '-';
    const date = new Date(dateString);
    return date.toLocaleString('pt-BR');
}

/**
 * Formatar tipo de usuário
 */
function formatTipo(tipo) {
    const tipos = {
        'APRENDIZ': 'Aprendiz',
        'INSTRUTOR': 'Instrutor',
        'CRIADOR': 'Criador',
        'EMPRESA': 'Empresa',
        'ADMINISTRADOR': 'Administrador'
    };
    return tipos[tipo] || tipo;
}

/**
 * Mostrar mensagem de feedback
 */
function showMessage(elementId, message, type = 'success') {
    const element = document.getElementById(elementId);
    if (!element) return;

    element.innerHTML = `<div class="${type}">${message}</div>`;

    // Remover mensagem após 3 segundos
    setTimeout(() => {
        element.innerHTML = '';
    }, 3000);
}

/**
 * Confirmar ação
 */
function confirmAction(message) {
    return confirm(message);
}

/**
 * Validar email
 */
function isValidEmail(email) {
    const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return regex.test(email);
}

/**
 * Validar senha
 */
function isValidPassword(password) {
    return password && password.length >= 6;
}

/**
 * Debounce para inputs de busca
 */
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

/**
 * Escapar HTML para prevenir XSS
 */
function escapeHtml(text) {
    const map = {
        '&': '&amp;',
        '<': '&lt;',
        '>': '&gt;',
        '"': '&quot;',
        "'": '&#039;'
    };
    return text.replace(/[&<>"']/g, m => map[m]);
}

// ============================================
// INICIALIZAÇÃO
// ============================================

/**
 * Verificar se a API está acessível
 */
async function checkApiHealth() {
    try {
        await fetch(`${API_BASE_URL}/usuarios`);
        return true;
    } catch (error) {
        console.error('API não está acessível:', error);
        return false;
    }
}

/**
 * Inicializar verificações ao carregar a página
 */
document.addEventListener('DOMContentLoaded', async () => {
    const isApiOnline = await checkApiHealth();
    if (!isApiOnline) {
        console.warn('⚠️ API não está respondendo. Verifique se o backend está rodando em http://localhost:8080');
    }
});

// ============================================
// EXPORTAR PARA USO GLOBAL
// ============================================
window.api = api;
window.formatDate = formatDate;
window.formatDateTime = formatDateTime;
window.formatTipo = formatTipo;
window.showMessage = showMessage;
window.confirmAction = confirmAction;
window.isValidEmail = isValidEmail;
window.isValidPassword = isValidPassword;
window.debounce = debounce;
window.escapeHtml = escapeHtml;