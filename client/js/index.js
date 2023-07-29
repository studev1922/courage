// show passowd
function togglePassword(block, img) {
   let input = block.querySelector('input[type]');
   let isPass = input.getAttribute('type') == 'password';
   if (isPass) {
      input.setAttribute('type', 'text');
      img.setAttribute('src', 'assets/imgIcon/hide.png')
   } else {
      input.setAttribute('type', 'password');
      img.setAttribute('src', 'assets/imgIcon/view.png')
   }
}

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
   },
   hideModel: (target) => {
      let close = document.querySelector(`${target} [data-bs-dismiss="modal"]`);
      close?.click();
   },
   showImageInput: (formControl, showInputImages) => {
      if (!formControl) return;
      let input = formControl.querySelector('input[type="file"]');
      let flex = showInputImages.querySelector('.d-flex');
      if (!input || !flex) return; // break

      input.addEventListener('change', _ => {
         let childs = [], src;
         // remove all old images
         while (flex.lastChild) flex.removeChild(flex.firstChild);
         // add new images
         if (input.files?.length)
            for (let f of input.files) {
               src = URL.createObjectURL(f);
               childs.push(`<div class="custom-img"><span>size: ${f.size}</span><img src="${src}"alt="${f.name}"><h5>${f.name}</h5></div>`);
            }
         else childs.push(`<h4 style="background: var(--bgr-linear1);">No photos selected!</h4>`);
         flex.innerHTML = childs.join('');
      });
   }
}

// localStorage
let local = {
   /**
    * @param {String} key 
    * @returns {Object} parse as json
    */
   get: (key) => JSON.parse(localStorage.getItem(key)),

   /**
    * @param {String} key 
    * @param {Object} value 
    * @returns {void} undefined
    */
   put: (key, value) => localStorage.setItem(key, JSON.stringify(value)),


   /**
    * @param {String} key 
    * @returns {void} undefined
    */
   move: (key) => localStorage.removeItem(key)
}

// util
const util = {

   /**
    * @param {Object} obj 
    * @param {Array} files 
    * @returns {FormData}
    */
   getFormData: (obj, files) => {
      if (!obj) throw new Error('input object must be not null!');
      // Create a new FormData object
      var fd = new FormData();
      // Loop through the object keys and append them to the form data
      for (let k in obj) fd.append(k, obj[k]);
      if (files) for (let f of files) fd.append('files', f);
      return fd;
   },

   /**
    * @param {Array} arr find in array
    * @param {Array} values all condition values
    * @param {String} key named
    * @returns {Array} array filter by[key] includes values
    */
   has: (arr, values, key) => arr?.filter(x => values?.includes(x[key])),

   /**
    * @param {Array} _ array data to sort
    * @param {String} col is condition to sort
    * @param {Boolean} isDesc descending or ascending sort
    * @returns {Array} _ array sorted
    */
   sort: (_, col, isDesc) => eval(`_?.sort((${isDesc ? 'o1,o2' : 'o2,o1'}) => o1.${col}.localeCompare(o2.${col}))`),

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

const handleDate = {
   // GROUP BY
   at: { year: 1, month: 12, week: 52, day: 365 },
   
   getDateInfo: function (regTime) {
      let date = new Date(regTime);
      let year = date.getFullYear(); // get year of the date
      let month = date.getMonth() + 1; // (from 1 to 12)
      let week = Math.ceil((Date.now() - date) / (1000 * 60 * 60 * 24 * 7)); // (from 1 to 53)

      // Return an object with these values
      return { year, month, week };
   },

   // Define a helper function that returns a string in the format of 'yyyy-MM-dd'
   formatDate: function (date) {
      // Get the year, month, and day of the date
      let year = date.getFullYear();
      let month = date.getMonth() + 1;
      let day = date.getDate();

      // Pad the month and day with leading zeros if needed
      month = month.toString().padStart(2, "0");
      day = day.toString().padStart(2, "0");

      // Return the formatted string
      return `${year}-${month}-${day}`;
   },

   groupBy: function (data = [], group = this.at.month) {
      // Initialize an empty result object
      let result = {};

      // Loop through the input array
      for (let i = 0; i < data.length; i++) {
         let regTime = data[i].regTime;
         let { year, month, week } = this.getDateInfo(regTime);
         let key;

         // Check which group parameter is passed to the function
         switch (group) {
            case this.at.year: key = year; break; // year
            case this.at.month: key = `${year}-${month}`; break; // year and month
            case this.at.week: key = `${year}-${week}`; break; // year and week
            default:
               key = this.formatDate(new Date(regTime))
               break; // year, month, and day
         }

         // Check if the key already exists in the result object
         if (result[key]) result[key].push(data[i]);
         else result[key] = [data[i]];
      }

      return result;
   }
}