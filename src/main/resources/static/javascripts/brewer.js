$(function () {
  const decimal = $(".js-decimal");
  decimal.maskMoney({ thousands: '.', decimal: ','});

  const plain = $(".js-plain");
  plain.maskMoney({ precision: 0, thousands: '.', decimal: ','});
});
