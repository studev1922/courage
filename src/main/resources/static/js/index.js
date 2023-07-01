const { body } = document;
const view = document.querySelector('[view]');
const listActions = document.querySelectorAll('[set-view]');

listActions.forEach(set =>
   set.addEventListener('click', e => {
      let target = e.target, script = document.createElement('script');
      let sourceView = target.getAttribute('set-view');
      let sourceJS = target.getAttribute('set-script');

      rawFile(sourceView, value => {
         view.innerHTML = value; // set html
         body.removeAttribute(document.querySelector('script[script-replace]'));

         if (sourceJS) { //set file script if exist
            script.setAttribute('src', sourceJS);
            script.setAttribute('script-replace', null);
            body.appendChild(script);
         }
      });
   })
);

const rawFile = (source, callback) =>
   fetch(source)
      .then(res => res.text())
      .then(res => callback(res))
      .catch(err => console.error(err));