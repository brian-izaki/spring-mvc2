var Brewer = Brewer || {};

class MascaraCep {
    constructor() {
        this.inputCep = $('.js-input-cep')
    }

    iniciar() {
        const maskBehavior = this.inputCep.data('mascara');

        const options = {
            oneKeyPress: function(val, e, field, options) {
                field.mask(maskBehavior.apply({}, arguments), options);
            }
        }

        this.inputCep.mask(maskBehavior, options);
    }

}

Brewer.MascaraCep = MascaraCep;

$(function () {
    const mascaraCep = new Brewer.MascaraCep();

    mascaraCep.iniciar();
})