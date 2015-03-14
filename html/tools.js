/**
 * 截取小数位数。
 * @param value
 * @param precision
 * @returns
 */
function toFixed(value, precision) {
	var power = Math.pow(10, precision || 0);
	return Math.round(value * power) / power;
}