Brewer = Brewer || {};

class Multiselecao {
    constructor() {
        this.statusBtn = $('.js-status-btn');
        this.selecaoCheckbox = $('.js-selecao');
        this.selecaoTodosCheckbox = $('.js-selecao-todos')
    }

    iniciar() {
        this.statusBtn.on('click', this.onStatusBtnClicado.bind(this))
        this.selecaoTodosCheckbox.on('click', this.onSelecaoTodosClicados.bind(this));
        this.selecaoCheckbox.on('click', this.onSelecaoClicado.bind(this))
    }

    onStatusBtnClicado(event) {
        const botaoClicado = $(event.currentTarget);
        const status = botaoClicado.data('status');

        const checkBoxSelecionados = this.selecaoCheckbox.filter(':checked');
        const codigos = $.map(checkBoxSelecionados, function (c) {
            return $(c).data('codigo')
        })

        if (codigos.length > 0) {
            const data = new FormData();
            data.append("codigos", codigos);
            data.append("status", status);
            fetch(botaoClicado.data('url'), {
                method: "PUT",
                headers: {
                    [Brewer.security.header]: Brewer.security.token
                },
                body: data,
            })
                .then(response => response.text())
                .then(text => window.location.reload())
                .catch(err => console.log(err))
        }

    }

    onSelecaoTodosClicados() {
        const status = this.selecaoTodosCheckbox.prop('checked');
        this.selecaoCheckbox.prop('checked', status);
        this.statusBotaoAcao.call(this, status)
    }

    onSelecaoClicado() {
        const selecaoCheckboxChecados = this.selecaoCheckbox.filter(':checked');
        this.selecaoTodosCheckbox.prop('checked', selecaoCheckboxChecados.length >= this.selecaoCheckbox.length);
        this.statusBotaoAcao.call(this, selecaoCheckboxChecados.length)
    }

    statusBotaoAcao(ativar) {
        ativar ? this.statusBtn.removeClass('disabled') : this.statusBtn.addClass('disabled');
    }
}

Brewer.Multiselecao = Multiselecao;

$(function () {
    const multiselecao = new Brewer.Multiselecao();

    multiselecao.iniciar();
})