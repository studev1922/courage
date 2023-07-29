# EXPANSIVE SYSTEM
This project provides the basic functionality that can be customized by subclassing and using pre-built classes

## SCRIPTS DATABASE FILE
1. File scripts create database: [open mssql_base_super.sql](assets/mssql_base_super.sql)<br>
2. File scripts insert database: [open mssql_data_super.sql](assets/mssql_data_super.sql)

## ACCOUNTS
<table>
    <thead>
        <tr>
            <th>USERNAME</th>
            <th>PASSWORD</th>
            <th>ROLES</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>admin</td>
            <td>123</td>
            <td>ADMIN, STAFF, USER</td>
        </tr>
        <tr>
            <td>staff</td>
            <td>123</td>
            <td>STAFF</td>
        </tr>
        <tr>
            <td>user1</td>
            <td>123</td>
            <td>USER</td>
        </tr>
        <tr>
            <td>user2</td>
            <td>123</td>
            <td>_</td>
        </tr>
        <tr>
            <td>partner</td>
            <td>123</td>
            <td>PARTNER</td>
        </tr>
    </tbody>
</table>

## DEPLOYMENT
- Requirements
   - SQL Server 2014 or later `sqlcmd -?`
   - JDK 17 or later `java -version`
   - Maven 3.x.x `mvn -v`
   - **_If you use Git_ `git -v`**
- Guide
   1. `git clone https://github.com/studev1922/courage.git`or download [courage-main.zip](https://github.com/studev1922/courage/archive/refs/heads/main.zip) and extract file
   2. Point to the project `cd [...path to the project]/courage`
   3. Execute database
      - Create database `sqlcmd -f 65001 -i assets/mssql_base_super.sql`
      - Insert database `sqlcmd -f 65001 -i assets/mssql_data_super.sql`
   4. Start server with maven `mvn clean spring-boot:run -X`
   5. Start **courage/client** with Live Server or _copy_ **all file in courage/client** and _paste_ to **courage/src/main/resources/static** or **courage/src/main/webapp**
   6. Open **client** and **server**
      - Open link **With Live Server** [http://127.0.0.1:5500/client/index.html](http://127.0.0.1:5500/client/index.html)
      - Open link **With static or webapp** [http://localhost:8080/index.html](http://localhost:8080/index.html)
      - localhost
        - `http://127.0.0.1:5500` or `http://localhost:5500`
        - `http://localhost:8080` or `http://127.0.0.1:8080`
        - **path** _/server_ or _/client_ or _/index.html_
## ABBREVIATE
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
         <li>mvn: <code>http://localhost:8080/index</code></li>
         <li>mvn: <code>http://localhost:8080/server</code></li>
         <li>run as Live Server or any... <code>http://127.0.0.1:5500/client/index.html</code></li>
      </ol>
   </li>
</ul>
