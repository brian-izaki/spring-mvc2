var Brewer = Brewer || {};

class GraficoVendaPorMes {
    constructor() {
        this.ctx = $('#graficoVendasPorMes')[0].getContext('2d');
    }

    iniciar() {
        fetch('vendas/totalPorMes', {
            method: "GET",
        })
            .then(success => success.json())
            .then(json => this.renderizar(json))
    }

    renderizar(vendaMes) {
        console.log(vendaMes)
        const meses = vendaMes.map((obj) => obj.mes).reverse();
        const valores = vendaMes.map((obj) => obj.total).reverse();

        const graficoVendasPorMes = new Chart(this.ctx, {
            type: 'line',
            data: {
                labels: meses,
                datasets: [{
                    label: 'Vendas por mês',
                    backgroundColor: "rgba(26, 179, 148, 0.5)",
                    pointBorderColor: "rgba(26, 179, 148, 1)",
                    pointBackgroundColor: "#fff",
                    data: valores
                }]
            },
        });
    }
}
Brewer.GraficoVendaPorMes = GraficoVendaPorMes;


class PorOrigem {
    constructor() {
        this.ctx = $('#graficoPorOrigem')[0].getContext('2d');
    }

    iniciar() {
        fetch('vendas/vendaPorOrigem', {
            method: "GET",
        })
            .then(success => success.json())
            .then(json => this.renderizar(json))
    }

    renderizar(vendaMes) {
        console.log(vendaMes)
        const meses = vendaMes.map((obj) => obj.mes).reverse();
        const nacional = vendaMes.map((obj) => obj.totalNacional).reverse();
        const internacional = vendaMes.map((obj) => obj.totalInternacional).reverse();

        const graficoPorOrigem = new Chart(this.ctx, {
            type: 'bar',
            data: {
                labels: meses,
                datasets: [
                    {
                        label: 'Nacional',
                        backgroundColor: "rgba(26, 170, 50, 0.5)",
                        data: nacional
                    },
                    {
                        label: 'Internacional',
                        backgroundColor: "rgba(26, 179, 148, 0.5)",
                        data: internacional
                    },
                ]
            },
        });
    }
}
Brewer.PorOrigem = PorOrigem;

$(function () {
    const graficoVendaPorMes = new Brewer.GraficoVendaPorMes();
    const porOrigem = new Brewer.PorOrigem();

    graficoVendaPorMes.iniciar();
    porOrigem.iniciar()
})