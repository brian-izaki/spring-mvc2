Brewer = Brewer || {};

class PesquisaRapidaCliente {
    constructor() {
        this.pesquisaRapidaClientesModal = $('#pesquisaRapidaClientes');
        this.nomeCliente = $('#nomeClienteModal');
        this.pesquisaRapidaBtn = $('.js-pesquisa-rapida-clientes-btn')
        this.containerTabelaPesquisa = $('#containerTabelaPesquisaRapidaClientes');
        this.htmlTabelaPesquisa = $('#tabela-pesquisa-rapida-clientes').html()
        this.template = Handlebars.compile(this.htmlTabelaPesquisa);
        this.mensagemErro = $('.js-mensagem-erro');
    }

    iniciar() {
        this.pesquisaRapidaBtn.on('click', this.onPesquisaRapidaClicado.bind(this))
    }

    onPesquisaRapidaClicado(e) {
        e.preventDefault();

        const nomeCliente = this.nomeCliente.val().trim();
        const url = this.pesquisaRapidaClientesModal.find('form').attr('action');
        fetch(`${url}?nome=${nomeCliente}`, {
            method: 'GET',
            headers: {
                "Content-Type": "application/json",
                [Brewer.security.header]: Brewer.security.token,
            },
        })
            .then(response => response.json())
            .then(json => this.onPesquisaConcluida.call(this, json))
            .catch(err => {
                this.onErroPesquisa();
            })
    }

    onPesquisaConcluida(clientesLista) {
        const html = this.template(clientesLista)
        this.containerTabelaPesquisa.html(html);
        this.mensagemErro.addClass('hidden')
    }

    onErroPesquisa() {
        this.mensagemErro.removeClass('hidden')
    }
}
Brewer.PesquisaRapidaCliente = PesquisaRapidaCliente;

$(function() {
    const pesquisaRapidaCliente = new Brewer.PesquisaRapidaCliente();

    pesquisaRapidaCliente.iniciar()
})