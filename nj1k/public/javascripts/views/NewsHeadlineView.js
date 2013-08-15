var NewsHeadlineView = Backbone.View.extend({
	el: '<tr></tr>',
	render: function() {
		
		var href = this.model.get("id");
		var name = this.model.get("title");
		var date = this.model.get("news_date");
		
		this.$el.append('<td>' + date + '</td>')
		this.$el.append('<td><a href="news/' + href + '">' + name + '</a></td>');
		return this;
	}
});