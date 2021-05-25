class TabelaItens{
    constructor(autoComplete) {
        this.autoComplete = autoComplete;
        this.tabelaItensContainer = $('.js-tabela-cervejas-container');
        this.uuid = $('#uuid').val();

        this.emitter = $({})
        this.on = this.emitter.on.bind(this.emitter);
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
                uuid: this.uuid,
            }
        })

        response.done(this.onItemAtualizadoServidor.bind(this))
    }

    onItemAtualizadoServidor(htmlResponse) {
        this.tabelaItensContainer.html(htmlResponse)

        const quantidadeItemInput = $('.js-tabela-cerveja-quantidade-item');
        quantidadeItemInput.on('change', this.onQuantidadeItemAlterado.bind(this))
        quantidadeItemInput.maskMoney({precision: 0, thousands: ''});

        const tabelaItem = $('.js-tabela-item');
        tabelaItem.on('dblclick', this.onDoubleClick)
        $('.js-exclusao-item-btn').on('click', this.onExclusaoItemClick.bind(this))

        this.emitter.trigger('tabela-itens-atualizada', tabelaItem.data('valorTotal'));
    }

    onQuantidadeItemAlterado(event) {
        const input = $(event.target);
        let quantidade = input.val();

        if (quantidade <= 0) {
            input.val(1);
            quantidade = 1;
        }

        const codigoCerveja = input.data('codigo-cerveja');

        const response = $.ajax({
            url: `item/${codigoCerveja}`,
            method: 'PUT',
            headers: {
                [Brewer.security.header]: Brewer.security.token,
            },
            data: {
                quantidade,
                uuid: this.uuid,
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
            url: `item/${this.uuid}/${codigoCerveja}`,
            headers: {
                [Brewer.security.header]: Brewer.security.token,
            },
            method: 'DELETE'
        })

        resposta.done(this.onItemAtualizadoServidor.bind(this))
    }
}
Brewer.TabelaItens = TabelaItens;
