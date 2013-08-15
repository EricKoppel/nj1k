var NewsHeadlineModelCollection = Backbone.Collection.extend({
	model: NewsHeadline,
	url: function() {
		return 'http://localhost:9000/news/page/' + this.collectionId;
	},
	initialize: function() {
		console.debug("Initializing NewsHeadlineModelCollection");
	}
});