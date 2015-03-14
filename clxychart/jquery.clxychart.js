/*!
 * ClxyChart JavaScript Library v1.0.0
 * http://code.google.com/p/clxy-studio/
 *
 * Copyright 2011, CLXY
 * Released under the Apache License 2.0.
 */
(function($, document) {

	$.ClxyChart = function(s) {

		var canvas = $(s);
		if (canvas.length != 1) {
			log('Can\'t find canvas:' + s);
			return;
		}

		this.params = {
			axisOn : true,
			axisLineColor : '#317de4',
			gridLineOn : true,
			gridLineColor : 'rgba(100, 100, 100, 0.2)',
			gridRate : 9,
			borderColor : '#fff', // Color of Bar or Pie chart's border.
			borderWidth : 1, // Line width of Bar or Pie chart's border.
			padding : 0.1, // Chart's padding.
			piePadding : 5,
			barPadding : 20,
			lineWidth : 2, // Line width for line chart.
			endPointR : 0.2, // Radius of end points for line chart.
			// Random colors.
			colors : [ '#006cff', '#ff6600', '#93bbf4', '#34a038', 'hsl(39, 100%, 50%)' ]
		};

		// Effects will be copied to context.
		this.effects = {
			titleOn : true, // Draw data's title or not.
			textPadding : 2, // Text padding for title above.
			shadowColor : 'rgba(0, 0, 0, 0.5)',
			shadowBlur : 2,
			shadowOffsetX : 3,
			shadowOffsetY : 0,
			font : '16px "Microsoft Yahei",sans-serif',
			textAlign : 'center',
			globalAlpha : 0.8
		};

		var groups = this.groups = {};
		var context = canvas[0].getContext("2d");
		var args = {
			clxyChart : this,
			context : context,
			params : this.params,
			getColor : function() {
				return this.color || this.clxyChart.getRandomColor();
			}
		};

		this.draw = function() {

			if ($.isEmptyObject(groups)) {
				log('No chart.');
				return;
			}

			var width = canvas.width();
			var height = canvas.height();
			// scale context by padding.
			var padding = this.params.padding;
			context.translate(width * padding, height * padding);
			context.scale((1 - padding * 2), (1 - padding * 2));
			$.extend(context, this.effects);

			// Draw all axis first.
			$.each(groups, function(key, group) {
				group.drawAxis(width, height);
			});
			$.each(groups, function(key, group) {
				group.draw(width, height);
			});
		};

		/**
		 * Add chart. <br>
		 * And group chart by 'group' property (no group = 'undefined').
		 */
		this.add = function(chart) {

			var c = createChart(chart, args);
			if (!c || !c.datas || c.datas.length == 0) {
				log('Can\'t create chart:' + chart.type);
				return;
			}

			var name = c.group || 'undefined';
			groups[name] = new Group(name, c).merge(groups[name]);
			return this;
		};

		var currentColors = $.merge([], this.params.colors);

		this.getRandomColor = function() {

			var index = Math.floor(Math.random() * currentColors.length);
			var result = currentColors.splice(index, 1)[0];
			if (currentColors.length == 0) {
				currentColors = $.grep(this.params.colors, function(value) {
					return value != result;
				});
			}
			return result;
		};

		return this;
	};

	/**
	 * Line chart and point chart.
	 */
	var LineChart = function(chart, args) {

		$.extend(this, chart, args);

		// Adjust data. scale both x and y.
		this.transData = function(width, height, props) {
			var propX = props['x'];
			var propY = props['y'];
			$.each(this.datas, function(i, p) {
				p.title = p.title || (p.x + ', ' + p.y);
				p.x = propX.scale(p.x, width, height);
				p.y = propY.scale(p.y, width, height);
			});
		};

		this.draw = function(width, height) {

			var points = this.datas;
			// sort by x value.
			points.sort(function(p1, p2) {
				return (p1.x < p2.x) ? -1 : (p1.x == p2.x ? 0 : 1);
			});

			var ctx = this.context;
			var color = this.getColor();
			var isPoint = this.isPoint;
			ctx.lineWidth = this.lineWidth || this.params.lineWidth;
			var endPointR = ctx.lineWidth + this.params.endPointR;
			ctx.textPadding += endPointR;

			$.each(points, function(i, p1) {

				ctx.strokeStyle = p1.color || color;
				ctx.fillStyle = ctx.strokeStyle; // for line endpoint.
				fillArc(ctx, p1, endPointR); // endpoint.

				var p2 = points[i + 1];
				var top = p2 && (p1.y > p2.y);
				var hAlign = (i == 0) ? 'left' : 'center';
				fillText(ctx, p1.title, p1.x, p1.y, top, hAlign);

				if (isPoint || !p2)
					return;
				ctx.beginPath();
				line(ctx, p1.x, p1.y, p2.x, p2.y);
				ctx.stroke();
			});
		};

		return this;
	};

	var BarChart = function(chart, args) {

		$.extend(this, chart, args);

		var map = this.map = {};
		var datas = this.datas = $.makeArray(this.datas);
		$.each(datas, function(i, data) {
			var key = data.x || i;
			map[key] = $.merge($.makeArray(map[key]), [ data ]);
		});

		// Adjust data. scale y.
		this.transData = function(width, height, props) {
			var bc = this;
			var propY = props['y'];
			$.each(datas, function(i, data) {
				data.color = data.color || bc.getColor();
				data.title = data.title || data.x || data.y;
				data.x = data.x || i;
				data.y = propY.scale(data.y, width, height);
			});
			this.refer.y = propY.scale(this.refer.y || 0, width, height);
		};

		this.merge = function(chart) {
			this.refer = chart.refer || this.refer;
			$.merge(datas, chart.datas);
			$.each(chart.map, function(key, value) {
				map[key] = $.merge($.makeArray(map[key]), value);
			});
			return this;
		};

		this.draw = function(width, height) {

			// calc width of bar.
			var count = Object.keys(map).length;
			var padding = this.params.barPadding;
			var barWidth = (width - (count + 1) * padding) / count;

			var ctx = this.context;
			var base = this.refer.y;
			ctx.strokeStyle = this.params.borderColor;
			ctx.lineWidth = this.params.borderWidth;

			var i = 0;
			$.each(map, function(key, ds) {
				var x = i * (padding + barWidth) + padding;
				var w = barWidth / ds.length;
				$.each(ds, function(j, d) {
					x = x + j * w;
					ctx.fillStyle = d.color;
					ctx.fillRect(x, d.y, w, (base - d.y));
					ctx.strokeRect(x, d.y, w, (base - d.y));
					fillText(ctx, d.title, x + w / 2, d.y, (d.y > base));
				});
				i++;
			});
		};
	};

	var PieChart = function(chart, args) {

		$.extend(this, chart, args);

		// Adjust data.
		this.transData = function(width, height, props) {
			$.each(this.datas, function(i, data) {
				data.title = data.title || (data.value * 100 + '%');
			});
		};

		this.draw = function(width, height) {

			var refer = $.extend({
				x : width / 2,
				y : height / 2
			}, this.refer);
			var padding = this.padding || this.params.piePadding;
			var r = this.radius || Math.min(refer.x - padding, refer.y - padding);

			var pc = this;
			var ctx = this.context;
			ctx.translate(refer.x, refer.y);
			ctx.strokeStyle = this.params.borderColor;
			ctx.lineWidth = this.params.borderWidth;

			var angle = -1 * Math.PI / 2;
			$.each(this.datas, function(i, data) {
				ctx.save();

				var plusAngle = 2 * Math.PI * data.value;
				var middle = angle + plusAngle / 2;
				// draw slice.
				ctx.rotate(middle);
				ctx.translate(padding, 0);
				ctx.rotate(-plusAngle / 2);
				ctx.beginPath();
				ctx.moveTo(0, 0);
				ctx.arc(0, 0, r, 0, plusAngle, false);
				ctx.lineTo(0, 0);
				ctx.fillStyle = data.color || pc.getColor();
				ctx.fill();
				// draw border.
				ctx.stroke();
				// draw title.
				var top = middle > 0 && middle < Math.PI;
				ctx.rotate(plusAngle / 2);
				ctx.translate(r + ctx.textPadding, 0);
				ctx.rotate((top ? 3 : 1) * Math.PI / 2);
				fillText(ctx, data.title, 0, 0, top);

				angle += plusAngle;
				ctx.restore();
			});
		};

		return this;
	};

	var Group = function(name, chart) {

		this.name = name;
		var params = chart.params;
		var context = this.context = chart.context;
		var props = {
			x : new PropX(),
			y : new PropY()
		};
		var axis = this.axis = {
			on : true,
			origin : undefined
		};

		// All bar data will merge to on chart.
		this.barChart = undefined;
		this.charts = [];
		if (chart instanceof BarChart) {
			this.barChart = chart;
		} else {
			this.charts.push(chart);
		}

		this.merge = function(g2) {
			if (!g2)
				return this;
			$.merge(this.charts, g2.charts);
			this.barChart = mergeBarChart(this.barChart, g2.barChart);
			return this;
		};

		this.drawAxis = function(width, height) {

			// Barchart will draw together too.
			if (this.barChart)
				this.charts.push(this.barChart);

			// Collection all data will be scaled.
			var list = [];
			$.each(this.charts, function(i, chart) {
				// pie chart don't calc rate.
				if (chart instanceof PieChart)
					return;

				$.merge(list, chart.datas);
				$.merge(list, $.makeArray(chart.refer));
			});

			// calc the min and max by all data in same group.
			$.each(props, function(k, p) {
				var values = $.map(list, function(data, i) {
					return data[p.name];
				});
				p.max = Math.max.apply(Math, values);
				p.min = Math.min.apply(Math, values);
				p.calcRate(width, height);
			});

			$.each(this.charts, function(i, chart) {
				chart.transData(width, height, props);
			});

			// draw axis.
			if (!params.axisOn || !axis.on)
				return;

			context.save();
			context.strokeStyle = params.axisLineColor;
			context.lineWidth = 0.5;
			clearShadow(context); // axis have no shadow.
			var gridLineOn = params.gridLineOn;
			var gridRate = params.gridRate;
			var gridLineColor = params.gridLineColor;
			var padding = params.padding * width / 3;
			var o = this.getAxisOrigin(height);
			context.beginPath();
			if (o.x !== undefined)
				line(context, o.x, -padding, o.x, height + padding);
			if (o.y !== undefined)
				line(context, -padding, o.y, width + padding, o.y);
			context.stroke();

			// draw grid line.
			if (!gridLineOn) {
				return;
			}
			context.beginPath();
			context.strokeStyle = gridLineColor;
			var step = height / gridRate;
			var start = (o.y || height) % step;
			context.beginPath();
			for ( var y = start; y < height; y += step) {
				line(context, 0, y, width, y);
			}
			context.stroke();
			context.restore();
		};

		this.draw = function(width, height) {
			$.each(this.charts, function(i, chart) {
				context.save();
				chart.draw(width, height, props);
				context.restore();
			});
		};

		this.getAxisOrigin = function(height) {

			var x = 0;
			var y = height;

			if (this.barChart) {
				y = this.barChart.refer.y;
			}

			return {
				x : x,
				y : y
			};
		};

		return this;
	};

	/**
	 * X on coordinate.<br>
	 * Scale by canvas' width.
	 */
	var PropX = function() {
		this.name = 'x';
		this.scale = function(value, width, height) {
			return (value - this.min) * this.rate;
		};
		this.calcRate = function(width, height) {
			this.rate = calcRate(this.min, this.max, width);
		}
	};

	/**
	 * Y on coordinate.<br>
	 * Scale by canvas' height.
	 */
	var PropY = function() {
		this.name = 'y';
		this.scale = function(value, width, height) {
			return height - (value - this.min) * this.rate;
		};
		this.calcRate = function(width, height) {
			this.rate = calcRate(this.min, this.max, height);
		}
	};

	/**
	 * ==---------------------------------------- Util method.
	 */
	var createChart = function(chart, args) {

		switch (chart.type) {
		case 'point':
			return new LineChart(chart, $.extend({
				isPoint : true
			}, args));
		case 'line':
			return new LineChart(chart, args);
		case 'pie':
			return new PieChart(chart, args);
		case 'bar':
			return new BarChart(chart, args);
		}
	};

	/**
	 * Draw text.No shadow.
	 */
	var fillText = function(context, text, x, y, top, hAlign) {

		if (!context.titleOn) {
			return;
		}

		var padding = context.textPadding || 0 + context.lineWidth || 0;
		context.save();
		clearShadow(context);
		y = top ? (y + padding) : (y - padding);
		context.textBaseline = top ? 'top' : 'alphabetic';
		context.textAlign = hAlign || 'center';
		context.fillText(text, x, y);
		context.restore();
	};

	/**
	 * Fill arc for abbreviated.
	 */
	var fillArc = function(context, point, r) {
		context.beginPath();
		context.arc(point.x, point.y, r, 0, Math.PI * 2, true);
		context.closePath();
		context.fill();
	};

	/**
	 * Draw line for abbreviated.
	 */
	var line = function(context, x1, y1, x2, y2) {
		context.moveTo(x1, y1);
		context.lineTo(x2, y2);
	};

	var clearShadow = function(context) {
		$.extend(context, {
			shadowBlur : 0,
			shadowOffsetX : 0,
			shadowOffsetY : 0,
			globalAlpha : 1
		});
	};

	var mergeBarChart = function(bc1, bc2) {

		if ((!bc1 && !bc2))
			return;

		if (!bc1 || !bc2 || (bc1 == bc2))
			return bc1 || bc2;

		return bc1.merge(bc2);
	};

	var calcRate = function(min, max, refer) {

		if (max == min) {
			return (max == 0) ? 1 : refer / max;
		} else {
			return refer / (max - min);
		}
	};

	var log = function(obj) {
		console.log(obj);
	};

})(jQuery, document);
