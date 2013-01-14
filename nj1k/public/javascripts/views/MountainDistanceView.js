var MountainDistanceView = Backbone.View.extend({
	el: '<tr></tr>',
	render: function() {
		
		var href = this.model.get("m2").get("id");
		var name = this.model.get("m2").get("name");
		var distance = this.model.get("distanceAsMiles");
		
		this.$el.append('<td><a href="' + href + '">' + name + '</a></td>');
		this.$el.append('<td>' + distance + '</td>');
		return this;
	}
});