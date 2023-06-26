# source
Java Spring-boot source system management

## JAVA MAIN CLASS
Run file [open Application.java](src/main/java/courage/Application.java#L9)

## SCRIPTS DATABASE FILE
- File scripts create database: [open mssql_base_super.sql](assets/mssql_base_super.sql)<br>
- File scripts insert database: [open mssql_data_super.sql](assets/mssql_data_super.sql)

## TEST ACCOUNT CONTROLL
<table border>
   <thead>
      <tr>
         <th>function</th>
         <th>method</th>
         <th>path</th>
         <th>example</th>
      </tr>
   </thead>
   <tbody>
      <tr>
         <td rowspan="2">login with jwt or username and password</td>
         <th rowspan="2">POST</th>
         <td colspan="2" style="text-align: center">
            <h3>[authorization:'token ...'] has higher priority than [username & password]</h3>
         </td>
      </tr>
      <tr>
         <td>http://localhost:8080/api/accounts/login</td>
         <td>
            <ul>
               <li>USERNAME & PASSWORD: ?username=admin&password=123</li>
               <li>JSON WEB TOKEN: header['authorization'] = '[token...]'</li>
            </ul>
         </td>
      </tr>
      <tr>
         <td>read account's image file on divice</td>
         <th>GET</th>
         <td>[project's location...]\courage\src\main\webapp\uploads\account\{fileName}</td>
         <td>[project's location...]\courage\src\main\webapp\uploads\account\default.png</td>
      </tr>
      <tr>
         <td>read account's image file on server</td>
         <td>GET</td>
         <td>http://localhost:8080/uploads/account/{fileName}</td>
         <td>http://localhost:8080/uploads/account/default.png</td>
      </tr>
      <tr>
         <td>read all or read by list id</td>
         <td>GET</td>
         <td>
            <ul>
               <li>all: http://localhost:8080/api/accounts</li>
               <li>by ids: http://localhost:8080/api/accounts?id={ids}</li>
            </ul>
         </td>
         <td>
            <ul>
               <li>all: http://localhost:8080/api/accounts</li>
               <li>by ids: http://localhost:8080/api/accounts?id=1001,1002</li>
            </ul>
         </td>
      </tr>
      <tr>
         <td>read one by id</td>
         <td>GET</td>
         <td>http://localhost:8080/api/accounts/{id}</td>
         <td>http://localhost:8080/api/accounts/1001</td>
      </tr>
      <tr>
         <td>save with image</td>
         <td>POST, PUT</td>
         <td>
            <ol>
               <li>http://localhost:8080/api/accounts</li>
               <li>http://localhost:8080/api/accounts/one</li>
            </ol>
         </td>
         <td>
            <code>form action="http://localhost:8080/api/accounts/one" enctype="multipart/form-data"</code> 
         </td>
      </tr>
      <tr>
         <td>save all by json body</td>
         <td>POST, PUT</td>
         <td>http://localhost:8080/api/accounts/all</td>
         <td>
            request.body = [{}, {}, ...]
         </td>
      </tr>
      <tr>
         <td>delete by id</td>
         <td>DELETE</td>
         <td>http://localhost:8080/api/accounts/{id}</td>
         <td>http://localhost:8080/api/accounts/{1001}</td>
      </tr>
   </tbody>
</table>