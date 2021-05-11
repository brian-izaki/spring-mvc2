var Brewer = Brewer || {};

class UploadFoto {
    constructor() {
        this.inputNomeFoto = $('input[name=foto]');
        this.inputContentType = $('input[name=contentType]');

        this.fotoCervejaTemplate = $('#foto-cerveja').html();
        this.template = Handlebars.compile(this.fotoCervejaTemplate);

        this.containerCerveja = $('.js-container-foto-cerveja');

        this.uploadDrop = $('#upload-drop')
    }

    iniciar() {
        const settings = {
            type: 'json',
            filelimit: 1,
            allow: '*.(jpg|jpeg|png)',
            url: this.containerCerveja.data('url-fotos'),
            complete: this.onUploadCompleto.bind(this)
        }

        UIkit.upload($('#upload-drop'), settings);

        if (this.inputNomeFoto.val()) {
            this.onUploadCompleto.call(this, {
                response: {
                    nome: this.inputNomeFoto.val(),
                    contentType: this.inputContentType.val(),
                }
            });
        }
    }

    onUploadCompleto(resposta) {
        this.inputNomeFoto.val(resposta.response.nome)
        this.inputContentType.val(resposta.response.contenType)

        this.uploadDrop.addClass("hidden")
        const htmlFotoCerveja = this.template({nomeFoto: resposta.response.nome})
        this.containerCerveja.append(htmlFotoCerveja);
        console.log(this)
        $('.js-remove-foto').on('click', this.onRemoveFoto.bind(this));
    }

    onRemoveFoto() {
        $('.js-foto-cerveja').remove();

        this.inputNomeFoto.val('');
        this.inputContentType.val('');
        this.uploadDrop.removeClass('hidden');
    }

}

Brewer.UploadFoto = UploadFoto;

$(function () {
    const uploadFoto = new Brewer.UploadFoto();
    uploadFoto.iniciar();
})