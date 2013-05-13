var jsWidgetExternalId = '';
var serverUrl = '';

$(document).ready(function() {
	//code selects the theme image from the list. Used for outer border.
	$('input[name=themeSelect]').click(function(event) {
		var imageURL = 'assets/images/themes/'+($('input[name=themeSelect]:checked').val()<10?"0"+$('input[name=themeSelect]:checked').val(): $('input[name=themeSelect]:checked').val())+'.jpg';
		$('#wiframe').contents().find('#widgetOuterBorder').css('backgroundImage','url('+imageURL+')');
		
		jQuery.each($('[type="radio"]'), function() {
			if(this.id != 'theme' || this.id != 'themeColor') {
				if(this.id == event.target.id) {
					$('#IMG'+event.target.id).css('background','blue');
				} else {
					$('#IMG'+this.id).css('background','white');
				}
			}
		});
		
		event.stopPropagation();
	});

	//code selects the color image from the list. Used for outer border.
	$('input[name=colorSelect]').click(function(event) {
		var imageURL = 'assets/images/themes/'+(colorThemes[$('input[name=colorSelect]:checked').val()-1])+'.jpg';
		$('#wiframe').contents().find('#widgetOuterBorder').css('backgroundImage','url('+imageURL+')');
		
		jQuery.each($('[type="radio"]'), function() {
			if(this.id != 'theme' || this.id != 'themeColor') {
				if(this.id == event.target.id) {
					$('#IMG'+event.target.id).css('background','blue');
				} else {
					$('#IMG'+this.id).css('background','white');
				}
			}
		});
		
		event.stopPropagation();
	});

	$('#causeColor').colorpicker().on('changeColor', function(ev){
		$('#wiframe').contents().find('#title').css('color', ev.color.toHex());
		$('#causeColor').attr('value', ev.color.toHex());
		});

	$('#progressBarColor').colorpicker().on('changeColor', function(ev){
		$('#wiframe').contents().find('#progressBar').css('background', ev.color.toHex());
		$('#progressBarColor').attr('value', ev.color.toHex());
	});
	
	$("#about").click(function() {
		$('#wiframe').contents().find('#widgetDiv').hide();
		$('#wiframe').contents().find('#progressDiv').hide();
		$('#wiframe').contents().find('#about').show();
	});
	
	$('#description').keyup(function(){
		$('#wiframe').contents().find('#aboutDescription').text($('#description').val());
	});
	
	// adding URL to the home page
	$('#url').keyup(function(){
		if($('input[name=webpageURL]').val() == ""){
			$('#wiframe').contents().find('#imageURLId').hide();
		}else{
			$('#wiframe').contents().find('#imageURLId').show();
			$('#wiframe').contents().find('#imageURLId').attr('href', $('input[name=webpageURL]').val());
		}
	});

	// changes the value of title on widget (keyup)
	$('#widgetTitle').keyup(function(){
		var desc = $('#widgetTitle').val();
		$('#widgetTitle').val(desc.replace(/\n/g, " "));		
		if($('#widgetTitle').val() == '') {
			$('#wiframe').contents().find('#title').text('Title');
		} else {
			$('#wiframe').contents().find('#title').text($('#widgetTitle').val());
		}
	});

	$('#widgetImage').keyup(function(){
		$('#wiframe').contents().find('#coverImage').attr('src',$('input[name=widgetImage]').val());
	});

	// goals currency selected and displayed
	$('#widgetGoal').keyup(function(){
		var goalAmount = $('input[name=widgetGoal]').val();
		if(goalAmount != '') {
			var currencyVal = $('#currency').val();
			var currencyArray = currencyVal.split('^');
			var currencyUnits = currencyArray[0];
			$('#wiframe').contents().find('#goalTarget').html(goalAmount+'&nbsp;'+currencyUnits);
		} else {
			$('#wiframe').contents().find('#goalTarget').html('&nbsp;');
		}
	});

	$('#currency').change(function() {
		var currencyVal = $('#currency').val();
		var currencyArray = currencyVal.split('^');
		var currencyUnits = currencyArray[0];
		var goalAmount = $('input[name=widgetGoal]').val();
		if(goalAmount != '') {
			$('#wiframe').contents().find('#goalTarget').html(goalAmount+'&nbsp;'+currencyUnits);
		} else {
			$('#wiframe').contents().find('#goalTarget').html('&nbsp;');
		}
	});
});
