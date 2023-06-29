const http = {
   get: (url = "") => http.request(url, "GET"),
   post: (url = "", body = {}) => http.request(url, "POST", body),
   put: (url = "", body = {}) => http.request(url, "PUT", body),
   delete: (url = "", body = {}) => http.request(url, "DELETE", body),
   request: async (
      url = "",
      method = 'GET',
      body = {},
      headers = {
         'Content-Type': 'multipart/form-data;boundary=U1RVREVWLUNPVVJBR0U='
      }
   ) => await fetch(url, { method, headers, body }).then(res => res.json())
}