inp = document.getElementById('inputpathapi');
form = document.getElementById('fileapicontroler');
var prepareImg = document.getElementById('showprepareimg');

console.log(inp.value);

inp?.addEventListener('keypress', e => {
   if (e.key == 'Enter') readData();
})

async function readData() {
   try {
      let path = inp.value;
      let res = await fetch(path);
      let isFile = path.indexOf('.') > -1; // type file blob
      let data = await (isFile ? res.blob() : res.json(path));

      document.getElementById('showresult')
         .innerHTML = isFile
            ? `<img class="m-auto mt-1 mb-1" src="${URL.createObjectURL(data)}"></img>`
            : JSON.stringify(data, null, 4);

   } catch (err) {
      console.error(err);
   }
}

// file input on change
form.querySelector('input[type="file"]')
   ?.addEventListener('change', e => {
      let img;
      
      while (prepareImg.firstChild)
         prepareImg.removeChild(prepareImg.firstChild);

      for (let f of e.target.files) {
         img = document.createElement('img');
         img.src = URL.createObjectURL(f);
         img.alt = img.title = f.name;
         img.style.objectFit = 'cover'
         img.setAttribute('class', 'col-xxl-4 col-md-3 col-sm-2 border');
         prepareImg.appendChild(img);
      }
   })


// form submit
form.addEventListener('submit', async evt => {
   evt.preventDefault(); // prevent redirect

   // fetch api
   let res = await fetch(inp.value, {
      method: "POST",
      body: new FormData(evt.target),
      headers: { 'Content-Type': 'multipart/form-data;boundary=---WebKitFormBoundaryc3R1ZGV2' }
   });

   let data = await res.json(); // response
   console.log(`response status code: ${res.status}`);

   if (data) document.getElementById('showresult')
      .innerHTML = JSON.stringify(data, null, 4);
})

