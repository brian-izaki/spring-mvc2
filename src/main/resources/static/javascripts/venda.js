class Venda {
    constructor(tabelaItens) {
        this.tabelaItens = tabelaItens;
        this.valorTotalBox = $('.js-valor-total-box');
        this.valorFreteInput = $('#valorFrete');
        this.valorDescontoInput = $('#valorDesconto');

        this.valorTotalItens = 0;
        this.valorFrete = 0;
        this.valorDesconto = 0;
    }

    iniciar() {
        this.tabelaItens.on('tabela-itens-atualizada', this.onTabelaItensAtualizada.bind(this))
        this.valorFreteInput.on('keyup', this.onValorFreteAlterado.bind(this))
        this.valorDescontoInput.on('keyup', this.onValorDescontoAlterado.bind(this));

        this.tabelaItens.on('tabela-itens-atualizada', this.onValoresAlterados.bind(this))
        this.valorFreteInput.on('keyup', this.onValoresAlterados.bind(this))
        this.valorDescontoInput.on('keyup', this.onValoresAlterados.bind(this));
    }

    onTabelaItensAtualizada(ev, valorTotalItens) {
        this.valorTotalItens = valorTotalItens ? Number(valorTotalItens) : 0;
    }

    onValorFreteAlterado(ev) {
        this.valorFrete = Brewer.recuperarValorFormatado($(ev.target).val());
    }

    onValorDescontoAlterado(ev) {
        this.valorDesconto = Brewer.recuperarValorFormatado($(ev.target).val());
    }

    onValoresAlterados() {
        const valorTotal = (this.valorTotalItens + this.valorFrete) - this.valorDesconto;

        this.valorTotalBox.html(Brewer.formatarMoeda(valorTotal))

        // pegar o box e deixar vermelho só se estiver com valortotal negativo
        const precoBox = $('.js-preco-venda-box');

        if (valorTotal < 0)
            precoBox.addClass('alert-danger')
        else
            precoBox.removeClass('alert-danger');
    }

}

Brewer.Venda = Venda;

$(function () {
    const autoComplete = new Brewer.AutoComplete();
    autoComplete.iniciar();

    const tabelaItens = new Brewer.TabelaItens(autoComplete);
    tabelaItens.iniciar();

    const venda = new Brewer.Venda(tabelaItens);
    venda.iniciar();
})