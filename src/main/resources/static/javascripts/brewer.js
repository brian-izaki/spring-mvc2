$(function () {
  const decimal = $(".js-decimal");
  decimal.maskMoney();

  const plain = $(".js-plain");
  plain.maskMoney({ precision: 0, thousands: '.'});
});
