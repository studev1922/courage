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
         <li><code>http://localhost:8080/index</code></li>
         <li><code>http://localhost:8080/server</code></li>
      </ol>
   </li>
</ul>
