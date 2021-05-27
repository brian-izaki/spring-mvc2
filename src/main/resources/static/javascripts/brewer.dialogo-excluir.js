Brewer = Brewer || {};

class DialogoExcluir {
    constructor() {
        this.exclusaoBtn = $('.js-exclusao-btn')
    }

    iniciar(){
        this.exclusaoBtn.on('click', this.onExcluirClicado.bind(this))

        if (window.location.search.indexOf('excluido') > -1) {
            swal('Pronto!', 'Excluído com sucesso!', 'success')
        }
    }

    onExcluirClicado(event) {
        event.preventDefault();
        const botaoClicado =  $(event.currentTarget);
        const url = botaoClicado.data('url');
        const objeto = botaoClicado.data('objeto');

        swal({
           title: "Tem certeza?",
            text: `Excluir ${objeto}? Você não poderá recuperar depois.`,
            showCancelButton: true,
            confirmButtonColor: '#DD6B55',
            confirmButtonText: 'Sim, exclua agora!',
            closeOnConfirm: false,
        }, this.onExcluirConfirmado.bind(this, url))
    }

    onExcluirConfirmado(url) {
        const options = {
            method: "DELETE",
            headers: {
                [Brewer.security.header]: [Brewer.security.token]
            }
        };

        fetch(url, options)
            .then(async response => {
                if (response.status === 200)
                    this.onExcluidoSucesso()
                else if (response.status === 400) {
                    this.onErroAoExcluir(await response.text());
                }
            })
            .catch(err => console.log("Erro ao tentar fazer requisição", err))
    }

    onErroAoExcluir(textoErro) {
        swal("Oops!", textoErro, "error")
    }

    onExcluidoSucesso() {
        const urlAtual = window.location.href;
        const separador = urlAtual.indexOf('?') > -1 ? '&' : '?';
        const novaUrl = urlAtual.indexOf('excluido') > -1 ? urlAtual : `${urlAtual}${separador}excluido`;
        window.location = novaUrl;
    }
}
Brewer.DialogoExcluir = DialogoExcluir;

$(function() {
    const dialogoExclusao = new Brewer.DialogoExcluir();
    dialogoExclusao.iniciar();
})