var Brewer = Brewer || {};

class MaskMoney {
  constructor() {
    this.decimal = $(".js-decimal");
    this.plain = $(".js-plain");
  }

  enable(){
    this.decimal.maskMoney({ thousands: '.', decimal: ','});
    this.plain.maskMoney({ precision: 0, thousands: '.', decimal: ','});
  }
}

Brewer.MaskMoney = MaskMoney;

$(function() {
  const maskMoney = new Brewer.MaskMoney();

  maskMoney.enable();
})