const regexM=/^[6-9][0-9]{9}$/;
const regexP=/^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,}$/;

    (() => {
        'use strict'

        // Fetch all the forms we want to apply custom Bootstrap validation styles to
        const forms = document.querySelectorAll('.needs-validation')



        // Loop over them and prevent submission
        Array.from(forms).forEach(form => {
            form.addEventListener('submit', event => {
                let username=document.querySelector("input[name=username]");
                let password=document.querySelector("input[name=password]");
                password.classList.remove("is-invalid");
                username.classList.remove("is-invalid");
                if (!regexM.test(username.value)) {
                    event.preventDefault()
                    event.stopPropagation()
                    username.classList.add("is-invalid")
                }


                if (!regexP.test(password.value)) {
                    event.preventDefault()
                    event.stopPropagation()
                    password.classList.add("is-invalid")
                }


            }, false)
        })
    })()

