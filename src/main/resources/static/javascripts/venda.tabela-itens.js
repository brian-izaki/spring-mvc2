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

        response.done(this.onItemAtualizadoServidor.bind(this))
    }

    onItemAtualizadoServidor(htmlResponse) {
        this.tabelaItensContainer.html(htmlResponse)
        $('.js-tabela-cerveja-quantidade-item').on('change', this.onQuantidadeItemAlterado.bind(this))
        $('.js-tabela-item').on('dblclick', this.onDoubleClick)
        $('.js-exclusao-item-btn').on('click', this.onExclusaoItemClick.bind(this))
    }

    onQuantidadeItemAlterado(event) {
        const input = $(event.target);
        const quantidade = input.val();
        const codigoCerveja = input.data('codigo-cerveja');

        const response = $.ajax({
            url: `item/${codigoCerveja}`,
            method: 'PUT',
            headers: {
                [Brewer.security.header]: Brewer.security.token,
            },
            data: {
                quantidade
            }
        })

        response.done(this.onItemAtualizadoServidor.bind(this));

    }

    onDoubleClick(event) {
        const item = $(event.currentTarget);
        item.toggleClass('solicitando-exclusao');
    }

    onExclusaoItemClick(event) {
        const codigoCerveja = $(event.target).data('codigo-cerveja');
        const resposta = $.ajax({
            url: `item/${codigoCerveja}`,
            headers: {
                [Brewer.security.header]: Brewer.security.token,
            },
            method: 'DELETE'
        })

        resposta.done(this.onItemAtualizadoServidor.bind(this))
    }
}
Brewer.TabelaItens = TabelaItens;


$(function () {
    const autoComplete = new Brewer.AutoComplete();
    autoComplete.iniciar();

    const tabelaItens = new Brewer.TabelaItens(autoComplete);
    tabelaItens.iniciar();
})
