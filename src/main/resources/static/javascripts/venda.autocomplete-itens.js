Brewer = Brewer || {};

class AutoComplete {
    constructor() {
        this.skuOuNomeInput = $('.js-sku-nome-cerveja-input');
        const htmlTemplateAutoComplete = $('#template-autocomplete-cerveja').html();
        this.template = Handlebars.compile(htmlTemplateAutoComplete);
        this.emitter = $({})
        this.on = this.emitter.on.bind(this.emitter)
    }

    iniciar(){
        const options = {
            url: function(skuOuNome){
                return `${this.skuOuNomeInput.data('url')}?skuOuNome=${skuOuNome}`
            }.bind(this),
            getValue: 'nome',
            minCharNumber: 3,
            requestDelay: 300,
            ajaxSettings: {
                contentType: 'application/json'
            },
            template: {
                type: 'custom',
                method: this.templateItens.bind(this)
            },
            list: {
                onChooseEvent: this.onItemSelecionado.bind(this)
            }
        }

        this.skuOuNomeInput.easyAutocomplete(options);
    }

    onItemSelecionado() {
        this.emitter.trigger('item-selecionado', this.skuOuNomeInput.getSelectedItemData());
        this.skuOuNomeInput.val('');
        this.skuOuNomeInput.focus();
    }

    templateItens(nome, cerveja) {
        cerveja.valorFormatado = Brewer.formatarMoeda(cerveja.valor);
        return this.template(cerveja);
    }

}
Brewer.AutoComplete = AutoComplete;