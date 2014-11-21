require(['jquery', 'jcrop', 'modal'], function() {
	var jcropApi;
	
	function updateCoords(e) {
		$("#x").val(e.x);
		$("#y").val(e.y);
		$("#x2").val(e.x2);
		$("#y2").val(e.y2);
		$("#w").val(e.w);
		$("#h").val(e.h);	
		
		var containerWidth = parseInt($("#img_container").css("width"), 10);
		var containerHeight = parseInt($("#img_container").css("height"), 10);
		
		var rx = containerWidth / e.w;
		var ry = containerHeight / e.h;
		
		var img = new Image();
		img.src = $("#preview").attr("src");

		var origWidth = img.width;
		var origHeight = img.height;

		$("#profile_img").css({
			width: Math.round(rx * origWidth) + 'px',
			height: Math.round(ry * origHeight) + 'px',
			marginLeft: '-' + Math.round(rx * e.x) + 'px',
			marginTop: '-' + Math.round(ry * e.y) + 'px'
		});
	}
	
	$(document).ready(function() {

		$('#delete_button').click(function() {
			$.ajax({
				url : '@routes.AccountController.deleteProfilePic()',
				type : 'DELETE',
				success : function(result) {
					$('#img_container').fadeOut();
				}
			});
		});
		
		$("#preview_ok").click(function() {
			$("#preview_modal").modal('hide');
			$('#preview').remove();
			$('#thumbnail_preview').prepend('<img alt="preview" id="preview" src="#" />');
			jcropApi.destroy();
		});
		
		
		$("#profile_img_upload").change(function() {
			if (this.files && this.files[0]) {
				var reader = new FileReader();
				
				reader.onload = function(e) {
					var profileImg = $("#profile_img");
					var preview = $("#preview");
					
					preview.attr("src", e.target.result);
					profileImg.attr("src", e.target.result);

					var img = new Image();
					img.src = preview.attr("src");

					var W = img.width;
					var H = img.height;
					var w = W / 2;
					var h = H / 2;
					
					var x = W/2 - w/2
					var y = H/2 - h/2
					var x1 = x + w
					var y1 = y + h
					
					preview.Jcrop({
						addClass: 'jcropCentered',
						aspectRatio: 1,
						boxWidth: 800,
						boxHeight: 800,
						setSelect: [x, y, x1, y1],
						onChange: updateCoords,
						onSelect: updateCoords
					}, function() {
						jcropApi = this;
					});
				}
				
				reader.readAsDataURL(this.files[0]);
			}
			
			$("#preview_modal").modal('show');
		});
	});
});