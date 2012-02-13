                $(document).ready(function() {
                $('#addtodo').submit(function() {return false;});
                $('#saveTaskButton').click(function() {
                    //var content = JSON.stringify({name: encodeURIComponent($("#name_txt").val())}, null, 2);
                    var content = JSON.stringify({name: $("#name_txt").val()}, null, 2);
                    $.ajax({
                       type: 'POST',
                       contentType: 'application/json',
                       url: 'addtodo',
                       data: content,
                       success: function(data) { $('#todolist').prepend('<li>'+data.task+'</li>');},
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
