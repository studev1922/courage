# source
Java Spring-boot source system management

## JAVA MAIN CLASS
Run file [open Application.java](src/main/java/courage/Application.java#L9)

## SCRIPTS DATABASE FILE
- File scripts create database: [open mssql_base_super.sql](assets/mssql_base_super.sql)<br>
- File scripts insert database: [open mssql_data_super.sql](assets/mssql_data_super.sql)

## TEST ACCOUNT CONTROLL
<table border="1">
<thead>
   <tr>
      <th rowspan="2" colspan="2">function</th>
      <th colspan="4" style="text-align: center;">methods</th>
      <th rowspan="2" style="text-align: center;">EX</th>
   </tr>
   <tr>
      <th>GET</th>
      <th>POST</th>
      <th>PUT</th>
      <th>DELETE</th>
   </tr>
</thead>
<tbody>
   <tr>
      <th rowspan="5" style="writing-mode: tb-rl; text-align: center;">rest-api</th>
   </tr>
   <tr>
      <td>READ</td>
      <td>✔</td>
      <td>❌</td>
      <td>❌</td>
      <td>❌</td>
      <td>
         <ul>
            <li>ALL: http://localhost:8080/api/accounts</li>
            <li>BYID: http://localhost:8080/api/accounts/1001</li>
         </ul>
      </td>
   </tr>
   <tr>
      <td>CREATE</td>
      <td>❌</td>
      <td>✔</td>
      <td>✔</td>
      <td>❌</td>
      <td>http://localhost:8080/api/accounts</td>
   </tr>
   <tr>
      <td>UPDATE</td>
      <td>❌</td>
      <td>✔</td>
      <td>✔</td>
      <td>❌</td>
      <td>http://localhost:8080/api/accounts</td>
   </tr>
   <tr>
      <td>DELETE</td>
      <td>❌</td>
      <td>❌</td>
      <td>❌</td>
      <td>✔</td>
      <td>http://localhost:8080/api/accounts/1001</td>
   </tr>
   <tr><td colspan="7"><hr></td></tr>
   <tr>
      <th rowspan="5" style="writing-mode: tb-rl; text-align: center;">multipart/form-data</th>
   </tr>
   <tr>
      <td>READ</td>
      <td>✔</td>
      <td>❌</td>
      <td>❌</td>
      <td>❌</td>
      <td>
         <ul>
            <li>ALL: _________ in process</li>
            <li>BYID: http://localhost:8080/thong-tin-tai-khoan/1001</li>
         </ul>
      </td>
   </tr>
   <tr>
      <td>CREATE</td>
      <td>❌</td>
      <td>✔</td>
      <td>❌</td>
      <td>❌</td>
      <td>_________ in process</td>
   </tr>
   <tr>
      <td>UPDATE</td>
      <td>❌</td>
      <td>✔</td>
      <td>❌</td>
      <td>❌</td>
      <td>_________ in process</td>
   </tr>
   <tr>
      <td>DELETE</td>
      <td>✔</td>
      <td>❌</td>
      <td>❌</td>
      <td>❌</td>
      <td>_________ in process</td>
   </tr>
</tbody>
</table>
