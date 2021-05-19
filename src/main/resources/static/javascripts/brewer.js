var Brewer = Brewer || {};

class MaskMoney {
    constructor() {
        this.decimal = $(".js-decimal");
        this.plain = $(".js-plain");
    }

    enable() {
        this.decimal.maskMoney({thousands: '.', decimal: ','});
        this.plain.maskMoney({precision: 0, thousands: '.', decimal: ','});
    }
}

class MaskPhoneNumber {
    constructor() {
        this.inputPhoneNumber = $('.js-phone-number');
    }

    enable() {
        const maskBehavior = function (val) {
            return val.replace(/\D/g, '').length === 11 ? '(00) 00000-0000' : '(00) 0000-00009'
        }

        const options = {
            oneKeyPress: function (val, e, field, options) {
                field.mask(maskBehavior.apply({}, arguments), options);
            }
        }

        this.inputPhoneNumber.mask(maskBehavior, options);
    }

}

class MaskDate {
    constructor() {
        this.inputDate = $('.js-date');
    }

    enable() {
        this.inputDate.mask('00/00/0000')
        this.inputDate.datepicker({
            orientation: "bottom",
            language: "pt-BR",
            autoclose: "true",
            endDate: "0d",
        })
    }
}

Brewer.MaskMoney = MaskMoney;
Brewer.MaskPhoneNumber = MaskPhoneNumber;
Brewer.MaskDate = MaskDate;

$(function () {
    const maskMoney = new Brewer.MaskMoney();
    const maskPhoneNumber = new Brewer.MaskPhoneNumber();
    const maskDate = new Brewer.MaskDate();

    maskMoney.enable();
    maskPhoneNumber.enable();
    maskDate.enable();
})