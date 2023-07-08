/**
 * @param {HTMLElement} e 
 * @param {Number} speed 
 */
const textTyping = (e, speed = 10) => {
   let className = e.getAttribute('class');

   if (className.includes('typing')) {
      // console.log('type writing...');
   } else {
      let i = 0;
      let txt = e.innerHTML;
      
      e.innerHTML = null;
      e.setAttribute('class', `${className} typing`);

      let interval = setInterval(() => {
         if(i < txt.length) e.innerHTML += txt.charAt(i++);
         else {
            clearInterval(interval);
            e.setAttribute('class', className);
         }
      }, speed || 15);
   }
}
