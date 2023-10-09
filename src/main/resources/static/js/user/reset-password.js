const regexP = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,}$/;

window.addEventListener("load", function () {
    console.log("called");
    let passwordInput = document.querySelector("input[name=password]")
    let confirmPassword = document.querySelector("input[name=confirmPassword]")


    let form = document.getElementById("password-form")
    form.addEventListener("submit",
        function (event) {
            console.log("called me");
            passwordInput.classList.remove("is-invalid");
            passwordInput.classList.add("was-validated");
            confirmPassword.classList.remove("is-invalid");
            confirmPassword.classList.add("was-validated");


            if (!regexP.test(passwordInput.value)) {
                passwordInput.classList.add("is-invalid");
                event.preventDefault();
            }


            if (passwordInput.value !== confirmPassword.value) {
                confirmPassword.classList.add("is-invalid");
                event.preventDefault();
            }


        });
});



