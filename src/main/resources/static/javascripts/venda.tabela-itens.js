class TabelaItens{
    constructor(autoComplete) {
        this.autoComplete = autoComplete;
        this.tabelaItensContainer = $('.js-tabela-cervejas-container');
    }

    iniciar() {
        this.autoComplete.on('item-selecionado', this.onItemSelecionado.bind(this))
    }

    onItemSelecionado(eve, item) {
        const response = $.ajax({
            url: 'item',
            method: 'POST',
            headers: {
                [Brewer.security.header]: Brewer.security.token,
            },
            data: {
                codigoCerveja: item.codigo,
            }
        })

        response.done(this.onItemAdicionadoServidor.bind(this))
    }

    onItemAdicionadoServidor(htmlResponse) {
        this.tabelaItensContainer.html(htmlResponse)
    }
}
Brewer.TabelaItens = TabelaItens;


$(function () {
    const autoComplete = new Brewer.AutoComplete();
    autoComplete.iniciar();

    const tabelaItens = new Brewer.TabelaItens(autoComplete);
    tabelaItens.iniciar();
})
