/**
 * These file contains all the validations that are required for the widget
 */
function validateForm(){
	
	// Description textarea max length validation 
	if($('#description').val().length > 1000){
		$('#aboutvalidation').show();
		$('#description').focus();
		return false;
	}else{
		$('#aboutvalidation').hide();
	}
	
	// title validation
	if(!$('#widgetTitle').val()){
		$('#titleMandatoryVal').show();
		$('#widgetTitle').focus();
		return false;
	}else{
		$('#titleMandatoryVal').hide();
	}
	
	// xss validation
	if($('#widgetTitle').val().match(titleValidation) == $('#widgetTitle').val()) {
		$('#titleTextVal').hide();
	} else {
		$('#titleTextVal').show();
		$('#widgetTitle').focus();
		return false;
	}
	
	// validation for goals
	if($('#widgetGoal').val() !== ""){
		if(isNaN($('#widgetGoal').val()) || $('#widgetGoal').val() <= 0){
			$('#goalValidation').show();
			$('#widgetGoal').focus();
			return false;
		}else{
			$('#goalValidation').hide();
		}
	}else{
		$('#goalValidation').hide();
	}

	//validation for EIN
	var val = /^\d{2}-\d{7}$/;
	if($.trim($('#ein').val()) != '' &&  $('#ein').val().search(val)==-1){
		$('#einVal').show();
		$('#ein').focus();
		return false;
	}else{
		$('#einVal').hide();
	}
	
	//validation for org-name
	if($('#orgName').val().match(titleValidation) == $('#orgName').val()) {
		$('#orgNameTextVal').hide();
	} else {
		$('#orgNameTextVal').show();
		$('#orgName').focus();
		return false;
	}
	return true;
}
