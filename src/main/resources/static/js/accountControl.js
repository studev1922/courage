i = 0;
function appendForm(temp) {
   let newForm = temp.cloneNode(true);
   let formId = `form${++i}`;
   let btn = newForm.getElementsByTagName('button');
   if (btn?.length) {
      btn[0].setAttribute('onclick', `randomFields(${formId})`); // btn random
      btn[1].setAttribute('onclick', `clearFields(${formId})`) // btn clear
      btn = btn[2]; // btn appen -> remove
   }

   newForm.getElementsByTagName('h2')[0].innerText = formId;
   newForm.setAttribute('id', formId);
   btn.setAttribute('onclick', `metaForm.removeChild(${formId})`);
   btn.setAttribute('class', 'btn-outline-danger')
   btn.innerText = '❌';
   metaForm.appendChild(newForm);
}

async function sumitAll()  {
   let mesWin = new String(), mesLos = new String();
   let countWin = 0, countLos = 0;
   let formNamed;

   for (let form of metaForm.children) {
      formNamed = form.querySelector('h2').innerText;
      let url = 'http://localhost:8080/api/accounts';

      http.post(url, new FormData(form))
         .then(res => {
            mesWin += `\n${formNamed} execute sucessfully.`;
            metaForm.removeChild(form);
            ++countWin;
         })
         .catch(err => {
            form.style.background = '#ff888880'
            mesLos += `\n${formNamed} execute failed: ${err}`;
            ++countLos;
         });
   }

   if (countWin) alert(`EXECUTE DATA SUCCESS: ${countWin} form. ${mesWin}`)
   if (countLos) alert(`EXECUTE DATA FAILED: ${countLos} form!!! ${mesLos}`)
}

function clearFields(temp) {
   let inputs = temp.querySelectorAll('input');
   for (let input of inputs) {
      switch (input.type) {
         case 'radio':
         case 'checkbox':
            input.checked = false;
            break;
         default:
            input.value = null;
            break;
      }
   }
}

function randomFields(temp) {
   // fields
   let username = temp.querySelector("div.col-4 > div:nth-child(2) > input"); // username
   let email = temp.querySelector("div.col-4 > div:nth-child(3) > input"); // email
   let password = temp.querySelector("div.col-4 > div:nth-child(4) > input"); // password
   let name = temp.querySelector("div.col-4 > div:nth-child(5) > input"); // name

   username.value = rValue();
   email.value = rValue(36, '@gmail.com');
   password.value = '123';
   name.value = rValue();

   // relationships
   let autoCheck = (data) => {
      if (data?.length) {
         for (let i = 0; i < data.length; i++)
            data[i].checked = rNumber(1, 2) % 2;
      }
   }
   let accesses = temp.querySelectorAll("div.col-8 > div:nth-child(1) > div input") // list access
   let roles = temp.querySelectorAll("div.col-8 > div:nth-child(2) > div input") // list role
   let platforms = temp.querySelectorAll("div.col-8 > div:nth-child(3) > div input") // list platfrom

   accesses[rNumber(0, accesses?.length - 1)].checked = true;
   autoCheck(roles); autoCheck(platforms);
}

rValue = (x = 36, last = '') => (Math.random() + 1).toString(x).substring(7) + last;
rNumber = (min, max) => Math.floor(Math.random() * (max - min + 1)) + min;