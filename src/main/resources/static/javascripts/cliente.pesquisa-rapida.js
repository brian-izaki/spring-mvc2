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
        this.pesquisaRapidaClientesModal.on('shown.bs.modal', this.onModalShow.bind(this))
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
        this.mensagemErro.addClass('hidden')

        const html = this.template(clientesLista)
        this.containerTabelaPesquisa.html(html);

        const tabelaClientePesquisa = new Brewer.TabelaPesquisaRapida(this.pesquisaRapidaClientesModal);
        tabelaClientePesquisa.inciar();
    }

    onErroPesquisa() {
        this.mensagemErro.removeClass('hidden')
    }

    onModalShow() {
        this.nomeCliente.focus();
    }
}
Brewer.PesquisaRapidaCliente = PesquisaRapidaCliente;

class TabelaClientePesquisa {
    constructor(modal) {
        this.cliente = $('.js-cliente-pesquisa-rapida')
        this.modalCliente = modal;
    }

    inciar() {
        this.cliente.on('click', this.onClienteSelecionado.bind(this))
    }

    onClienteSelecionado(e) {
        this.modalCliente.modal('hide');

        const clienteSelecionado = $(e.currentTarget);
        $('#nomeCliente').val(clienteSelecionado.data('nome'))
        $('#codigoCliente').val(clienteSelecionado.data('codigo'))
    }
}
Brewer.TabelaPesquisaRapida = TabelaClientePesquisa;

$(function() {
    const pesquisaRapidaCliente = new Brewer.PesquisaRapidaCliente();

    pesquisaRapidaCliente.iniciar()
})