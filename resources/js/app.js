                $(document).ready(function() {
                
		$('#name_txt').focus();
		$('#name_txt').keydown(function(e) { /* 13 = return key */if( e.keyCode === 13) { $("#saveTaskButton").trigger('click'); }});

		$('#addtodo').submit(function() {return false;});
                
	        $(".deleteicon").click(function() {
			var content = JSON.stringify({id: this.id}, null, 2);
			//alert("sending: "+content);
			$.ajax({ type: 'POST', contentType: 'application/json', url: 'removetodo', data: content, processData: false, dataType: 'json',
			       success: function(data) { if(data == true) location.reload(true); else alert("returns: "+data);}});
		});	 
		
		$(".doneicon").click(function() {
                        var content = JSON.stringify({id: this.id}, null, 2);
                        //alert("sending: "+content);
                        $.ajax({ type: 'POST', contentType: 'application/json', url: 'donetodo', data: content, processData: false, dataType: 'json',
                               success: function(data) { if(data == true) location.reload(true); else alert("returns: "+data);}});
                });

		$('#saveTaskButton').click(function() {
                    //var content = JSON.stringify({name: encodeURIComponent($("#name_txt").val())}, null, 2);
                    var content = JSON.stringify({name: $("#name_txt").val()}, null, 2);
                    $.ajax({
                       type: 'POST',
                       contentType: 'application/json',
                       url: 'addtodo',
                       data: content,
                       success: function(data) { location.reload(true); },
                       error: ajaxFailed,
                       processData: false,
                       dataType: 'json'
                    });
                    });

                function ajaxFailed(result) {
                  if (result.status == 200 && result.statusText == "OK") {
                      //this is here only because chrome breaks on this method only for no reason whatsoever.
                      //chrome sees the request as failed, but everything happens fine...
                      //branchDetailsSuccess(result.responseText);
                       alert("google chrome isue?"); }
                           else {
                                alert("FAILED : " + result.status + ' ' + result.statusText);
                                  }
                }

                })
