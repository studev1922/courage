# README
Java Spring-boot source system management

## SCRIPTS DATABASE FILE
1. File scripts create database: [open mssql_base_super.sql](assets/mssql_base_super.sql)<br>
2. File scripts insert database: [open mssql_data_super.sql](assets/mssql_data_super.sql)

## JAVA MAIN CLASS
Run file [open Application.java](src/main/java/courage/Application.java#L9)

<hr>

## ABSTRACT API CONTROLLER
- [AbstractAPI_Read.java](src/main/java/courage/controller/rest/AbstractAPI_Read.java)
  + [read all or by list id](src/main/java/courage/controller/rest/AbstractAPI_Read.java#L31)
  + [read by page](src/main/java/courage/controller/rest/AbstractAPI_Read.java#L49)
  + [read one by id](src/main/java/courage/controller/rest/AbstractAPI_Read.java#L63)
- [AbstractRESTful.java](src/main/java/courage/controller/rest/AbstractRESTful.java)
  + [save one data with files](src/main/java/courage/controller/rest/AbstractRESTful.java#L50)
  + [delete data and files by path id](src/main/java/courage/controller/rest/AbstractRESTful.java#L62)
- [AbstractRestAPI.java](src/main/java/courage/controller/rest/AbstractRestAPI.java)
  + [save one data](src/main/java/courage/controller/rest/AbstractRestAPI.java#L28)
  + [save all by array json](src/main/java/courage/controller/rest/AbstractRestAPI.java#L37)
  + [delete one by id](src/main/java/courage/controller/rest/AbstractRestAPI.java#L47)
- [AbstractFileAPI.java](src/main/java/courage/controller/rest/AbstractFileAPI.java)
  + [getFiles](src/main/java/courage/controller/rest/AbstractFileAPI.java#L57)
  + [saveFile](src/main/java/courage/controller/rest/AbstractFileAPI.java#L74)
  + [deleteFile](src/main/java/courage/controller/rest/AbstractFileAPI.java#L87)
- [RestFileControl.java](src/main/java/courage/controller/rest/RestFileControl.java)
  + [RestFileControl$UAccessApi](src/main/java/courage/controller/rest/RestFileControl.java#L19)
<hr>

## TEST CONTROLLER
<ul>
   <li>
      <h3>execute <a href="assets">two files</a> in mssql(SQL Server 2019)</h3>
      <ul>
         <li><a href="assets/mssql_base_super.sql">create database</a></li>
         <li><a href="assets/mssql_data_super.sql">insert data into database</a></li>
      </ul>
   </li>
   <li>
      <h3>start server in</h3>
      <ol>
         <li>Main java: <a href="src/main/java/courage/Application.java#L9">Application.java</a></li>
         <li>Maven run: <code>mvn spring-boot:run</code></li>
      </ol>
   </li>
   <li>
      <h3>open app on</h3>
      <ol>
         <li><code>http://localhost:8080</code></li>
         <li><code>http://localhost:8080/server</code></li>
      </ol>
   </li>
</ul>
<hr>

## TEST FILE API CONTROLLER
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
         <td>read static file</td>
         <th>GET</th>
         <td>http://localhost:8080/uploads/account<code>/{path}</code> </td>
         <td>http://localhost:8080/uploads/account<code>/default.png</code></td>
      </tr>
      <tr>
         <td>read file api</td>
         <th>GET</th>
         <td>http://localhost:8080/api/uploads/account</td>
         <td>
            <ul>
               <li>
                  <h3>option file api</h3>
                  http://localhost:8080/api/uploads/account
               </li>
               <li>
                  <h3>read byte[] as file</h3>
                  http://localhost:8080/api/uploads/account<code>/default.png?is=true</code>
               </li>
            </ul>
         </td>
      </tr>
      <tr>
         <td>save file</td>
         <th>POST</th>
         <td>http://localhost:8080/api/uploads/account</td>
         <td>
            <code>
               form action="http://localhost:8080/api/uploads/account" enctype="multipart/form-data"
               <br>
               input type="file" name="files" multiple
            </code>
         </td>
      </tr>
      <tr>
         <td>delete file</td>
         <th>DELETE</th>
         <td>http://localhost:8080/api/uploads/account<code>/{path}</code></td>
         <td>
            <ol>
               <li>
                  <h3>delete folder: "test"</h3>
                  http://localhost:8080/api/uploads/account<code>/test</code>
               </li>
               <li>
                  <h3>delete files in folder: "test"</h3>
                  http://localhost:8080/api/uploads/account<code>/test?files=file1.png,file2.jpg,file3.gif</code>
               </li>
            </ol>
         </td>
      </tr>
   </tbody>
</table>

<hr>

## TEST ACCOUNT API CONTROLLER
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
            http://localhost:8080/api/accounts/update-passowrd<code>?unique=...&password=...</code>
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
               <li>USERNAME & PASSWORD: <code>?username=admin&password=123</code></li>
               <li>JSON WEB TOKEN: header['authorization'] = '[token...]'</li>
            </ul>
         </td>
      </tr>
      <tr>
         <td>read account's image file on divice</td>
         <th>GET</th>
         <td>[project's location...]\courage\src\main\webapp\uploads\account<code>\{fileName}</code></td>
         <td>[project's location...]\courage\src\main\webapp\uploads\account<code>\default.png</code></td>
      </tr>
      <tr>
         <td>read account's image file on server</td>
         <td>GET</td>
         <td>http://localhost:8080/uploads/account<code>/{fileName}</code></td>
         <td>http://localhost:8080/uploads/account<code>/default.png</code></td>
      </tr>
      <tr>
         <td>read all or read by list id</td>
         <td>GET</td>
         <td>
            <ul>
               <li>all: http://localhost:8080/api/accounts</li>
               <li>by ids: http://localhost:8080/api/accounts<code>?id={ids}</code></li>
            </ul>
         </td>
         <td>
            <ul>
               <li>all: http://localhost:8080/api/accounts</li>
               <li>by ids: http://localhost:8080/api/accounts<code>?id=1001,1002</code></li>
            </ul>
         </td>
      </tr>
      <tr>
         <td>read one by id</td>
         <td>GET</td>
         <td>http://localhost:8080/api/accounts<code>/{id}</code></td>
         <td>http://localhost:8080/api/accounts<code>/1001</code></td>
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
         <td>delete by id</td>
         <td>DELETE</td>
         <td>http://localhost:8080/api/accounts<code>/{id}</code></td>
         <td>http://localhost:8080/api/accounts<code>/{1001}</code></td>
      </tr>
   </tbody>
</table>
