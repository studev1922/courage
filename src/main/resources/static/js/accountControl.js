
aMap = new Map(); // access
rMap = new Map(); // roles
pMap = new Map(); // platform


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

function sumitAll() {
   // convert to array for use each asynch
   [].slice.call(metaForm.children).forEach(async form => {
      formNamed = form.querySelector('h2').innerText;
      let url = 'http://localhost:8080/api/accounts';

      let res = await fetch(url, {
         method: "POST",
         body: new FormData(form)
      }); // #do not set headers, browser will auto set boundary
      let data = await res.json();

      if (res.status == 200) {
         mesWin += `\n${formNamed} execute sucessfully.`;
         metaForm.removeChild(form);
         ++countWin;
         console.log(data);
      } else {
         form.style.background = '#ff888880'
         mesLos += `\n${formNamed} execute failed`;
         ++countLos;
         console.error(data);
      }
   });
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

async function cacheData(path, map = new Map(), k = 'id') {
   console.log(`GET data from ${path} to cache.`);

   let res = await fetch(path);
   let data = await res.json();

   if (res.status == 200)
      for (let e of data) map.set(e[k], e);
   else console.error(path, data);
}

async function getData() {
   let res = await fetch('http://localhost:8080/api/accounts');
   let data = await res.json();

   if (res.status == 200) {
      let thead = document.querySelector('#collapseTwo thead');
      let tbody = document.querySelector('#collapseTwo tbody');

      while(thead.lastChild) thead.removeChild(thead.firstChild);
      while(tbody.lastChild) tbody.removeChild(tbody.firstChild);

      if (data) {
         // set sobjects on the header
         let keys = Object.keys(data[0]);
         let row = document.createElement('tr');
         row.setAttribute('class', 'text-center');
         for (let k of keys) row.innerHTML += `<th>${k}</th>`;
         thead.appendChild(row)

         // set view all data
         for (let e of data) {
            let tr = document.createElement('tr');
            for (let k of keys) {
               let e2 = e[k];
               let cellText = new String();
               if(k === 'access') cellText = aMap.get(e2.access)?.uaName;
               else if (Array.isArray(e2)) {
                  switch (k) {
                     case 'roles': e2.forEach(id => cellText += `${rMap.get(id)?.role}<br>`); break;
                     case 'platforms': e2.forEach(id => cellText += `${pMap.get(id)?.upName}<br>`); break;
                     default: cellText = e2.toString().replaceAll(',', '<br>'); break;
                  }
               } else cellText = e2;
               tr.innerHTML += `<td>${cellText}</td>`;
            }
            tbody.appendChild(tr);
         }
      }
   } else {
      console.error(data);
   }
}


(async () => {
   await cacheData('http://localhost:8080/api/accesses', aMap, 'uaid');
   await cacheData('http://localhost:8080/api/roles', rMap, 'urid');
   await cacheData('http://localhost:8080/api/platforms', pMap, 'upid');
   await getData();
})()