// $(document).ready(
//     function() {
//
//         // SUBMIT FORM
//         $("#user-post").submit(function(event) {
//             // Prevent the form from submitting via the browser.
//             event.preventDefault();
//             ajaxPost();
//         });
//
//         function ajaxPost() {
//
//             // PREPARE FORM DATA
//             var formData = {
//                 bookId : $("#user-id").val()
//
//             }
//
//             // DO DELETE
//             $.ajax({
//                 type : "DELETE",
//                 contentType : "application/json",
//                 url : "admin/users/41",
//                 data : JSON.stringify(formData),
//                 dataType : 'json',
//                 success : function(result) {
//                     if (result.status == "success") {
//                         $("#postResultDiv").html(
//                             "" + result.data.bookName
//                             + "Post Successfully! <br>"
//                             + "---> Congrats !!" + "</p>");
//                     } else {
//                         $("#postResultDiv").html("<strong>Error</strong>");
//                     }
//                     console.log(result);
//                 },
//                 error : function(e) {
//                     alert("Error!")
//                     console.log("ERROR: ", e);
//                 }
//             });
//
//         }
//
//     })