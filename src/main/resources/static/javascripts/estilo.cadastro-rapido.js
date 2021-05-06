$(function () {
  const modal = $("#modalCadastroRapidoEstilo");
  const btnSalvar = modal.find(".js-modal-cadastro-estilo-salvar-botao");
  const form = modal.find("form");
  const url = form.attr("action");
  const inputNomeEstilo = $("#nomeEstilo");
  const containerMensagemErro = $(".js-mensagem-cadastro-rapido-estilo");

  form.on("submit", (event) => event.preventDefault());
  modal.on("shown.bs.modal", onModalShow);
  modal.on("hide.bs.modal", onModalClose);
  btnSalvar.on("click", onBtnSalvar);

  function onModalShow() {
    inputNomeEstilo.focus();
  }

  function onModalClose() {
    inputNomeEstilo.val("");
    containerMensagemErro.addClass("hidden");
    form.find(".form-group").removeClass("has-error");
  }

  async function onBtnSalvar() {
    const nomeEstilo = inputNomeEstilo.val().trim();

    try {
      const promisseResponse = await fetch(url, {
        method: "POST",
        headers: {
          "Content-Type": "application/json; charset=utf-8",
        },
        body: JSON.stringify({ nome: nomeEstilo }),
      });

      if (promisseResponse.status === 400) {
        const mensagemErro = await promisseResponse.text();
        inputInvalido(mensagemErro);
      } else if (promisseResponse.status === 200) {
        const estilo = await promisseResponse.json();
        inputValido(estilo);
      }
    } catch (erro) {
      console.log(erro);
    }
  }

  function inputInvalido(message) {
    containerMensagemErro.removeClass("hidden");
    containerMensagemErro.html(`<span> ${message} </span>`);
    form.find(".form-group").addClass("has-error");
  }

  function inputValido(estilo) {
    const comboEstilo = $("#estilo");
    comboEstilo.append(
      `<option value=${estilo.codigo}> ${estilo.nome} </option>`
    );
    comboEstilo.val(estilo.codigo);
    modal.modal("hide");
  }
});
