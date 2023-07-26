# EXPANSIVE SYSTEM
This project provides the basic functionality that can be customized by subclassing and using pre-built classes

## SCRIPTS DATABASE FILE
1. File scripts create database: [open mssql_base_super.sql](assets/mssql_base_super.sql)<br>
2. File scripts insert database: [open mssql_data_super.sql](assets/mssql_data_super.sql)

## TEST CONTROLLER
<ul>
   <li>
      <h3>execute <a href="assets">two files</a> in mssql(SQL Server)</h3>
      <ul>
         <li><a href="assets/mssql_base_super.sql">create database</a></li>
         <li><a href="assets/mssql_data_super.sql">insert data into database</a></li>
      </ul>
   </li>
   <li>
      <h3>check jdk version and maven version</h3>
      <ul>
         <li>Java: <code>java -version</code></li>
         <li>Maven: <code>mvn -v</code></li>
      </ul>      
   </li>
   <li>
      <h3>start server in</h3>
      <ol>
         <li>Main java: <a href="src/main/java/courage/Application.java#L9">Application.java</a></li>
         <li>Maven run: <code>mvn clean spring-boot:run</code></li>
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
