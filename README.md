# EXPANSIVE SYSTEM
This project provides the basic functionality that can be customized by subclassing and using pre-built classes

## PLAYLIST OF THIS PROJECT
[![expansive system](https://img.youtube.com/vi/vx_LY4PD-qs/maxresdefault.jpg)](https://youtu.be/vx_LY4PD-qs?si=yBHz1T81bD_CcCmQ)

## FUNCTIONAL
- CRUD data through _RESTful_
- Read, write **multiple file** as _json api_
- Other: _email_, _jwt_, _customize-display_, _etc_...
  + Send _email_, management _random code_ same as _cookies_ (**key**:**value**, age).
  + _Authentication_ and _decentralization_ between **client** and **server** through json web token (**jwt**).
  + Statistic account with **Chart JS** (_regTime_, _quantity_, _relationships_...).
  + Customize _display_ on web-app (navbar > setting)
  + ACCESS data: **AWAIT**, **LOCK** (_soft-delete_), **PRIVATE**, **PROTECTED**, **PUBLIC**
  + Connect to the **rdbms** (_SQL Server_) and **CRUD** through **jdbc**.

## TECHNOLOGIES USED
- RDBMS (Sql Server 2014+)
- Java (v17+), Spring-boot(v3.0.x)
  + Libraries [SEE](./pom.xml#L20): jpa, web, jdbc, lombok, tomcat, thymeleaf, security, oauth2-client, oauth2-resource-server, email, devtool.
  + Source server configuration: [application.yml](src/main/resources/application.yml)
    + mssql: [username](src/main/resources/application.yml#L5) : [password](src/main/resources/application.yml#L6) `default "sa" : "your mssql server's password"`
    + email: [username](src/main/resources/application.yml#L19) : [password](src/main/resources/application.yml#L20)
- Javascript (ES6)
  + Frameworks: AngularJS(v1.8), Bootstrap(v5.1)
  + Libraries: AngularJS(router, cookies), ChartJS(v4.3.2)
- FRONTEND: HTML5 - CSS3
  + font-awesome-v6.4.0.all.min.css
  + bootstrap-v5.1.3.min.css

## ACCOUNTS
<table>
    <thead>
        <tr>
            <th>USERNAME</th>
            <th>PASSWORD</th>
            <th>ROLES</th>
            <th>IS ACTIVE</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>admin</td>
            <td>123</td>
            <td>ADMIN, STAFF, USER</td>
            <td>✔</td>
        </tr>
        <tr>
            <td>staff</td>
            <td>123</td>
            <td>STAFF</td>
            <td>✔</td>
        </tr>
        <tr>
            <td>user1</td>
            <td>123</td>
            <td>USER</td>
            <td>✔</td>
        </tr>
        <tr>
            <td>user2</td>
            <td>123</td>
            <td>default:[USER]</td>
            <td>❌</td>
        </tr>
        <tr>
            <td>partner</td>
            <td>123</td>
            <td>PARTNER</td>
            <td>✔</td>
        </tr>
    </tbody>
</table>

## SCRIPTS DATABASE FILE
1. File scripts create database: [open mssql_base_super.sql](assets/mssql_base_super.sql)<br>
2. File scripts insert database: [open mssql_data_super.sql](assets/mssql_data_super.sql)

## DEPLOYMENT
- Requirements
   - SQL Server 2014 or later `sqlcmd -?`
   - JDK 17 or later `java -version`
   - Maven 3.x.x `mvn -v`
   - **_If you use Git_ `git -v`**
- Guide
   1. `git clone https://github.com/studev1922/courage.git` or download [courage-main.zip](https://github.com/studev1922/courage/archive/refs/heads/main.zip) and extract file
   2. Point to the project `cd [...path to the project]/courage`
   3. Execute database
      - Create database `sqlcmd -f 65001 -i assets/mssql_base_super.sql`
      - Insert database `sqlcmd -f 65001 -i assets/mssql_data_super.sql`
   4. Start server with maven `mvn clean spring-boot:run -X`
   5. Start **courage/client/index.html** with Live Server or _copy_ **all files in courage/client** and _paste_ to **courage/src/main/resources/static** : `robocopy client src/main/resources/static /e` or **courage/src/main/webapp** : `robocopy client src/main/webapp /e`
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
