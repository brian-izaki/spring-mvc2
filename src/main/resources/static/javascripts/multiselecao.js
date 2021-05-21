Brewer = Brewer || {};

class Multiselecao {
    constructor() {
        this.statusBtn = $('.js-status-btn');
        this.selecaoCheckbox = $('.js-selecao');
    }

    iniciar() {
        this.statusBtn.on('click', this.onStatusBtnClicado.bind(this))
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
            const request = fetch('/brewer/usuarios/status', {
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
}
Brewer.Multiselecao = Multiselecao;

$(function() {
    const multiselecao = new Brewer.Multiselecao();

    multiselecao.iniciar();
})