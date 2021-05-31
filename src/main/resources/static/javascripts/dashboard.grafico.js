var Brewer = Brewer || {};

class GraficoVendaPorMes {
    constructor() {
        this.ctx = $('#graficoVendasPorMes')[0].getContext('2d');
    }

    iniciar() {
        const graficoVendasPorMes = new Chart(this.ctx, {
            type: 'line',
            data: {
                labels: ['Jan', 'Fev', 'Mar', 'Abr', 'Jun'],
                datasets: [{
                    label: 'Vendas por mês',
                    backgroundColor: "rgba(26, 179, 148, 0.5)",
                    pointBorderColor: "rgba(26, 179, 148, 1)",
                    pointBackgroundColor: "#fff",
                    data: [10, 5, 6, 2, 6]
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