var MountainDistanceView = Backbone.View.extend({
	el: '<div class="tableRow"></div>',
	render: function() {
		this.$el.append('<div class="tableCell">' + this.model.get("m2").get("name") + '</div>');
		this.$el.append('<div class="tableCell">' + this.model.get("distanceAsMiles") + '</div>');
		return this;
	}
});