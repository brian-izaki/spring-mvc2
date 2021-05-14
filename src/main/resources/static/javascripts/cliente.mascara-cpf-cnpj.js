var Brewer = Brewer || {};

class MascaraCpfCnpj {
    constructor() {
        this.radioTipoPessoa = $('.js-radio-tipo-pessoa');
        this.labelCpfCnpj = $('[for=cpfOuCnpj]')
        this.inputCpfCnpj = $('#cpfOuCnpj');
    }

    iniciar() {
        this.radioTipoPessoa.on('change', this.onTipoPessoaAlterado.bind(this));
    }

    onTipoPessoaAlterado(event) {
        const tipoPessoaSelecionada = $(event.currentTarget)
        this.labelCpfCnpj.text(tipoPessoaSelecionada.data('documento'))
        this.inputCpfCnpj.mask(tipoPessoaSelecionada.data('mascara'))
        this.inputCpfCnpj.val('');
        this.inputCpfCnpj.removeAttr('disabled')
    }

}
Brewer.MascaraCpfCnpj = MascaraCpfCnpj;

$(function() {
    const mascaraCpfCnpj = new MascaraCpfCnpj();

    mascaraCpfCnpj.iniciar();
})