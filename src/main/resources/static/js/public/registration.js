const regexM = /^[6-9][0-9]{9}$/;
const regexP = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,}$/;
const regexE = /^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$/;


window.addEventListener("load", function () {

    let mobileInput = document.querySelector("input[name=mobileNumber]");
    let passwordInput = document.querySelector("input[name=password]")
    let emailInput = document.querySelector('input[name=email]');
    let confirmPassword = document.querySelector("input[name=confirmPassword]")
    let firstNameInput = document.querySelector("input[name=firstName]")
    let lastNameInput = document.querySelector("input[name=lastName]")


    let form = document.querySelector("form");
    form.addEventListener("submit", function (event) {
        mobileInput.classList.remove("is-invalid");
        mobileInput.classList.add("was-validated");
        firstNameInput.classList.remove("is-invalid");
        firstNameInput.classList.add("was-validated");
        lastNameInput.classList.remove("is-invalid");
        lastNameInput.classList.add("was-validated");
        passwordInput.classList.remove("is-invalid");
        passwordInput.classList.add("was-validated");
        emailInput.classList.remove("is-invalid");
        emailInput.classList.add("was-validated");
        confirmPassword.classList.remove("is-invalid");
        confirmPassword.classList.add("was-validated");

        if (firstNameInput.value.length < 3) {
            firstNameInput.classList.add("is-invalid");
            event.preventDefault();
        }

        if (lastNameInput.value.length < 1) {
            lastNameInput.classList.add("is-invalid");
            event.preventDefault();
        }

        if (!regexE.test(emailInput.value)) {
            emailInput.classList.add("is-invalid");
            event.preventDefault();
        }

        if (!regexM.test(mobileInput.value)) {
            mobileInput.classList.add("is-invalid");
            event.preventDefault();
        }

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


// let mobileNumber=document.getElementById("mobileNumber");
// let password=document.getElementById("password");
//
// function validate(){
//     let validation=true;
//     mobileNumber.classList.remove("is-invalid");
//     mobileNumber.classList.add("is-valid");
//     password.classList.add("is-valid");
//
//     if(!(regexM.test(mobileNumber.value))) {
//         console.log("called mobile")
//         mobileNumber.classList.add("is-invalid");
//         validation=false;
//     }
//     if(!regexP.test(password.value)){
//         console.log("called password")
//         password.classList.add("is-invalid");
//         document.getElementsByClassName("invalid-feedback").innerText="Enter valid paSS word";
//         validation=false;
//     }
//
//     return validation;
// }
