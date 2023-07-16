// clear tooltip
function removeElements(selector = '.fade') {
   if (!selector) return;
   let elements = document.querySelectorAll(selector);
   for (let e of elements) document.body.removeChild(e);
}


// seting user-inteface
function setting(customize = {}) {
   let { colortrip, bgr } = customize;
   let { body } = document;

   if (colortrip) {
      let e = document.createElement('div');
      e.setAttribute('class', 'bg-animation');
      body.appendChild(e);
   } else {
      body.querySelector('.bg-animation')?.remove();
   }

   if (bgr.on) {
      body.style.backgroundImage = colortrip ? `var(--bgr-linear1), url('${bgr.link}')` : `url('${bgr.link}')`;
   } else {
      body.style.backgroundImage = 'var(--bgr-linear)';
   }
}

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

// localStorage
let local = {
   /**
    * @param {String} key 
    * @returns {Object} parse as json
    */
   read: (key) => JSON.parse(localStorage.getItem(key)),

   /**
    * @param {String} key 
    * @param {Object} value 
    * @returns {void} undefined
    */
   write: (key, value) => localStorage.setItem(key, JSON.stringify(value)),


   /**
    * @param {String} key 
    * @returns {void} undefined
    */
   delete: (key) => localStorage.removeItem(key)
}

// util
const util = {

   /**
    * 
    * @param {Array} arr find in array
    * @param {Array} values all condition values
    * @param {String} key named
    * @returns 
    */
   has: (arr, values, key) => arr?.filter(x => values?.includes(x[key])),

   /**
    * 
    * @param {Array} _ array data to sort
    * @param {String} col is condition to sort
    * @param {Boolean} desc descending or ascending sort
    * @returns {Array} _ array sorted
    */
   sort: (_, col, desc) => eval(`_?.sort((${desc ? 'o1,o2' : 'o2,o1'}) => o1.${col}.localeCompare(o2.${col}))`),

   /**
    * @param {Array} arr sum of array
    * @returns {Number} total
    */
   total: (arr) => {
      let total = 0;
      for (x of arr) total += x;
      return total;
   }
}