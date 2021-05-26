class BotoesSubmit {
    constructor() {
        this.submitBtn = $('.js-submit-btn')
        this.formulario = $('.js-formulario-principal');
    }

    iniciar() {
        this.submitBtn.on('click', this.onSubmit.bind(this))
    }

    onSubmit(e) {
        e.preventDefault();

        const botaoClicado = $(e.target);
        const acao = botaoClicado.data('acao');

        const acaoInput = $('<input>');
        acaoInput.attr('name', acao);

        this.formulario.append(acaoInput)
        this.formulario.submit();

    }
}
Brewer.BotoesSubmit = BotoesSubmit;

$(function () {
    const botaoSubmit = new Brewer.BotoesSubmit();
    botaoSubmit.iniciar();
})