Brewer = Brewer || {};

class AutoComplete {
    constructor() {
        this.skuOuNomeInput = $('.js-sku-nome-cerveja-input');
        const htmlTemplateAutoComplete = $('#template-autocomplete-cerveja').html();
        this.template = Handlebars.compile(htmlTemplateAutoComplete);
    }

    iniciar(){
        const options = {
            url: function(skuOuNome){
                return `/brewer/cervejas?skuOuNome=${skuOuNome}`
            },
            getValue: 'nome',
            minCharNumber: 3,
            requestDelay: 300,
            ajaxSettings: {
                contentType: 'application/json'
            },
            template: {
                type: 'custom',
                method: function(nome, cerveja) {
                    cerveja.valorFormatado = Brewer.formatarMoeda(cerveja.valor);
                    return this.template(cerveja);
                }.bind(this)
            }
        }

        this.skuOuNomeInput.easyAutocomplete(options);
    }
}
Brewer.AutoComplete = AutoComplete;

$(function () {
    const autoComplete = new Brewer.AutoComplete();
    autoComplete.iniciar();
})