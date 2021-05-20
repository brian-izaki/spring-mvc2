var Brewer = Brewer || {};

class ComboEstado {
    constructor() {
        this.combo = $('#estado');
        this.emitter = $({});
        this.on = this.emitter.on.bind(this.emitter);
    }

    iniciar() {
        this.combo.on('change', this.onEstadoAlterado.bind(this))
    }

    onEstadoAlterado() {
        this.emitter.trigger('alterado', this.combo.val());
    }
}

Brewer.ComboEstado = ComboEstado;

class ComboCidade {
    constructor(comboEstado) {
        this.comboEstado = comboEstado;
        this.combo = $('#cidade');
        this.imgLoading = $('.js-img-loading')
        this.inputHiddenCidadeSelecionada = $('#inputHiddenCidadeSelecionada');
    }

    iniciar() {
        this.comboEstado.on('alterado', this.onEstadoAlterado.bind(this))
        const codigoEstado = this.comboEstado.combo.val();
        this.inicializarCidades.call(this, codigoEstado);
    }

    onEstadoAlterado(evento, codigoEstado) {
        this.inputHiddenCidadeSelecionada.val('');
        this.inicializarCidades.call(this, codigoEstado);
    }

    inicializarCidades(codigoEstado){
        if (!codigoEstado) {
            this.reset();
            return;
        }

        const urlRequest = this.combo.data('url');
        const optionsRequest = {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                [Brewer.security.header]: Brewer.security.token,
            }
        }
        this.iniciarReq();
        fetch(`${urlRequest}?estado=${codigoEstado}`, optionsRequest)
            .then(responseCidades => responseCidades.json())
            .then(cidades => this.onBuscarCidadesFinalizado(cidades))
            .catch(err => console.log("erro ao buscar cidades: ", err))
            .finally(() => this.finalizarReq())
    }

    onBuscarCidadesFinalizado(cidades) {
        const optionElements = cidades.map(cidade => {
            return `
                <option value="${cidade.codigo}">
                    ${cidade.nome}
                </option>
            `
        });

        this.combo.html(optionElements.join(''));
        this.combo.removeAttr("disabled")

        const codigoCidadeSelecionada = this.inputHiddenCidadeSelecionada.val();
        if (codigoCidadeSelecionada) {
            this.combo.val(codigoCidadeSelecionada)
        }

    }

    reset() {
        this.combo.html('<option value="">Deve selecionar um estado</option>');
        this.combo.val('');
        this.combo.attr('disabled', 'disabled');
    }

    iniciarReq() {
        this.reset();
        this.imgLoading.show();
    }

    finalizarReq() {
        this.imgLoading.hide();
    }

}

Brewer.ComboCidade = ComboCidade;

$(function () {
    const comboEstado = new Brewer.ComboEstado();
    const comboCidade = new Brewer.ComboCidade(comboEstado);

    comboEstado.iniciar();
    comboCidade.iniciar();
})