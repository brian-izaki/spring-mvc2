var Brewer = Brewer || {};

class EstiloCadastroRapido {
  constructor() {
    this.modal = $("#modalCadastroRapidoEstilo");
    this.btnSalvar = this.modal.find(".js-modal-cadastro-estilo-salvar-botao");
    this.form = this.modal.find("form");
    this.url = this.form.attr("action");
    this.inputNomeEstilo = $("#nomeEstilo");
    this.containerMensagemErro = $(".js-mensagem-cadastro-rapido-estilo");
  }

  iniciar() {
    this.form.on("submit", (event) => event.preventDefault());
    this.modal.on("shown.bs.modal", this.onModalShow.bind(this));
    this.modal.on("hide.bs.modal", this.onModalClose.bind(this));
    this.btnSalvar.on("click", this.onBtnSalvar.bind(this));
  }

  onModalShow() {
    this.inputNomeEstilo.focus();
  }

  onModalClose() {
    this.inputNomeEstilo.val("");
    this.containerMensagemErro.addClass("hidden");
    this.form.find(".form-group").removeClass("has-error");
  }

  async onBtnSalvar() {
    const nomeEstilo = this.inputNomeEstilo.val().trim();

    try {
      const promisseResponse = await fetch(this.url, {
        method: "POST",
        headers: {
          [window.Brewer.security.header]: window.Brewer.security.token,
          "Content-Type": "application/json; charset=utf-8",
        },
        body: JSON.stringify({ nome: nomeEstilo }),
      });

      if (promisseResponse.status === 400) {
        const mensagemErro = await promisseResponse.text();
        this.inputInvalido(mensagemErro);
      } else if (promisseResponse.status === 200) {
        const estilo = await promisseResponse.json();
        this.inputValido(estilo);
      }
    } catch (erro) {
      console.log(erro);
    }
  }

  inputInvalido(message) {
    this.containerMensagemErro.removeClass("hidden");
    this.containerMensagemErro.html(`<span> ${message} </span>`);
    this.form.find(".form-group").addClass("has-error");
  }

  inputValido(estilo) {
    const comboEstilo = $("#estilo");
    comboEstilo.append(
      `<option value=${estilo.codigo}> ${estilo.nome} </option>`
    );
    comboEstilo.val(estilo.codigo);
    this.modal.modal("hide");
  }
}

Brewer.EstiloCadastroRapido = EstiloCadastroRapido;

$(function () {
  const estiloCadastroRapido = new Brewer.EstiloCadastroRapido();
  estiloCadastroRapido.iniciar();
});
