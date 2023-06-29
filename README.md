# source
Java Spring-boot source system management

## SCRIPTS DATABASE FILE
- File scripts create database: [open mssql_base_super.sql](assets/mssql_base_super.sql)<br>
- File scripts insert database: [open mssql_data_super.sql](assets/mssql_data_super.sql)

## JAVA MAIN CLASS
Run file [open Application.java](src/main/java/courage/Application.java#L9)

## ABSTRACT API CONTROLLER
- [AbstractAPI_Read.java](src/main/java/courage/controller/rest/AbstractAPI_Read.java)
  + [read all or by list id](src/main/java/courage/controller/rest/AbstractAPI_Read.java#L29)
  + [read one by id](src/main/java/courage/controller/rest/AbstractAPI_Read.java#L40)
- [AbstractRESTful.java](src/main/java/courage/controller/rest/AbstractRESTful.java)
  + [save one data with files](src/main/java/courage/controller/rest/AbstractRESTful.java#L50)
  + [delete data and files by path id](src/main/java/courage/controller/rest/AbstractRESTful.java#L62)
- [AbstractRestAPI.java](src/main/java/courage/controller/rest/AbstractRestAPI.java)
  + [save one data](src/main/java/courage/controller/rest/AbstractRestAPI.java#L28)
  + [save all by array json](src/main/java/courage/controller/rest/AbstractRestAPI.java#L37)
  + [delete one by id](src/main/java/courage/controller/rest/AbstractRestAPI.java#L47)
- [AbstractFileAPI.java](src/main/java/courage/controller/rest/AbstractFileAPI.java)
  + `In process`
- [RestFileControl.java](src/main/java/courage/controller/rest/RestFileControl.java)
  + `In process extends from AbstractFileAPI.java`

## TEST ACCOUNT API CONTROLLER
TEST ACCOUNT FORM DATA
[OPEN ACCOUNT FORM CONTROL](client/index.html)

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
         <td>update only password</td>
         <th>PUT, PATCH</th>
         <td>http://localhost:8080/api/accounts/update-passowrd</td>
         <td>
            <h3>unique is email or username</h3>
            http://localhost:8080/api/accounts/update-passowrd?unique=...&password=...
         </td>
      </tr>
      <tr>
         <td>log-out account</td>
         <th>ALL</th>
         <td colspan="2" style="text-align: center">
            http://localhost:8080/api/accounts/logout
         </td>
      </tr>
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
