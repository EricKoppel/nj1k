require(['jquery'], function() {
	
	if ($(window).width() < 768) {
		return;
	}
	
	jsRoutes.controllers.ExternalNewsController.getNewsFromNYNJTC().ajax({
	  success: function(data) {
		  $.each(data, function(i, item) {
			  var r = jsRoutes.controllers.ExternalNewsController.getNewsArticleFromNYNJTC(item.link);
			  var mediaBody = $('<div class="media-body">');
			  $(mediaBody).append('<h4 class="media-heading"><strong><em><a target="_blank" href="' + item.link + '">' + item.title + '</a></em></strong></h4>');
			
			  $('<div class="media" style="border-bottom: 1px solid #ddd">').append(mediaBody).appendTo('#externalNews');
			  
			  $.ajax({
				  url: r.url,
				  cache: true,
				  success: function(data) {
    				  $(mediaBody).append('<p><strong>' + item.date + ' - </strong>' + data + ' <p><a target="_blank" href="' + item.link + '"><em>Read More...</em></a></p>');
				  }
			  });
		  });
		}
	});
});