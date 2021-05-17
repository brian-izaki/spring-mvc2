var Brewer = Brewer || {};

class MascaraCpfCnpj {
    constructor() {
        this.radioTipoPessoa = $('.js-radio-tipo-pessoa');
        this.labelCpfCnpj = $('[for=cpfOuCnpj]')
        this.inputCpfCnpj = $('#cpfOuCnpj');
    }

    iniciar() {
        this.radioTipoPessoa.on('change', this.onTipoPessoaAlterado.bind(this));
        const tipoPessoaSelecionada = this.radioTipoPessoa.filter(':checked')[0];
        if (tipoPessoaSelecionada) {
            this.aplicarMascara.call(this, $(tipoPessoaSelecionada));
        }

    }

    onTipoPessoaAlterado(event) {
        const tipoPessoaSelecionada = $(event.currentTarget)

        this.aplicarMascara.call(this, tipoPessoaSelecionada);
        this.inputCpfCnpj.val('');
    }

    aplicarMascara(tipoPessoaSelecionada){
        this.labelCpfCnpj.text(tipoPessoaSelecionada.data('documento'))
        this.inputCpfCnpj.mask(tipoPessoaSelecionada.data('mascara'))
        this.inputCpfCnpj.removeAttr('disabled')
    }

}
Brewer.MascaraCpfCnpj = MascaraCpfCnpj;

$(function() {
    const mascaraCpfCnpj = new MascaraCpfCnpj();

    mascaraCpfCnpj.iniciar();
})