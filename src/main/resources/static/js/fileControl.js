inp = document.getElementById('inputpathapi');
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