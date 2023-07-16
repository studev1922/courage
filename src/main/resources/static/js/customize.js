/**
 * @param {HTMLElement} e 
 * @param {Number} interval 
 */
const textTyping = (e, interval = 10) => {
   let className = e.getAttribute('class');

   if (className.includes('typing')) {
      // console.log('type writing...');
   } else {
      let i = 0;
      let txt = e.innerHTML;

      e.innerHTML = null;
      e.setAttribute('class', `${className} typing`);

      let timeInterval = setInterval(() => {
         if (i < txt.length) {
            e.innerHTML += txt.charAt(i++);
         } else {
            clearInterval(timeInterval);
            e.setAttribute('class', className);
         }
      }, interval || 15);
   }
}

// bootstrap framework
const bsfw = {
   /**
    * create all popover from attribute's target
    */
   loadPopovers: () => {
      let append = (element) => {
         let target = element.getAttribute('target');
         bsfw.showPopover(element, target);
      };
      document.querySelectorAll('[data-bs-toggle="popover"]').forEach(append);
   },
   showPopover: (element, target) => {
      let content = document.querySelector(target);
      new bootstrap.Popover(element, { html: true, content });
   }
}