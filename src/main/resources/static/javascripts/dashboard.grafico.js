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

$(function() {
    const graficoVendaPorMes = new Brewer.GraficoVendaPorMes();

    graficoVendaPorMes.iniciar();
})