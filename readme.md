# What is this Project ?

This application is created for extrat data from any jdbc database to Excel file with formated values.

Use query in connection JDBC data and extract data with Excel file using POI. 

This Excel file have Collumn site ajusted and Header with Bold font.

Por Sheet information, user *-instructions.properties file and create with custon text.  

    Input :  
        * Query
        * Connection Database
        * Custon Text
        * Fields information

    Output :
        * Formated Excel File
        * Custom sheet data.

## Dependencies for build project

  * Kotlin - Version 1.6
  * Java   - Version 11
  * Maven  - Version 3.8.1

## Dependency for run project

  * Java   - Version 11

## Process Build

### command line
    mvn clean kotlin:compile package

### After build.

copy target/ExtractDataToExcel.jar and /target/libs to your tool Folder.
copy conf-example to contigure query and connection

### Whats is ./conf/* files ?

   + ./conf/connection.properties -> Conection database details
   + ./conf/queries/defaul.sql    -> Query
   + ./conf/queries/defaul-columns.csv    -> CSV to configure fields in Excel.
   + ./conf/queries/defaul-instructions.properties    -> Create Sheet with instructions

You can use many queries in the same configuration. 
For this, change the prefix file "default"

## Run parameter and examples. 

java  -jar ExtractDataToExcel.jar --configdir ~/config-mysql --xlsfile ./out.xlsx

   - sqlfile when is not present, the value is "default" 

java -Duser.timezone=America/Sao_Paulo -jar ExtractDataToExcel.jar --sqlfile info --configdir ~/config-mysql --xlsfile ./out.xlsx

   - When sqlfile is "info" it will use files : "info.sql", "info-columns.csv", "info-instructions.properties"

## Defalts

| Variable      | Value             | Details                                                                                   | 
|---------------|-------------------|-------------------------------------------------------------------------------------------|
| user.timezone | America/Sao_Paulo | Time Zone Used        (example -Duser.timezone\="America/Sao_Paulo")                      |
| sql           | default           | \<sql value\>.sql, \<sql value\>-columns.csv, \<sql value\>-instructions.properties files |
| xls           | output.xlsx       | Excel Output file created                                                                 |                                                               
| configdir     | Current Dir       | Dir of configuration files (connection and queries )                                      |

## Others Functions

### Show Coluns info

    java -jar ExtractDataToExcel.jar --createcolsfile --sqlfile info --configdir ~/config-mysql

### Create Columns file, using SQL file

    java -jar ExtractDataToExcel.jar --createcolsfile --sqlfile info --configdir ~/config-mysql

### Show TimeZone Supported

    java -jar ExtractDataToExcel.jar --showtimezone

### Show Help

```
java -jar ExtractDataToExcel.jar -help

usage:
 -ccf,--createcolsfile   Suggest a configuration column file
 -cd,--configdir <arg>   Directory with condif files.
 -help                   Show help
 -sc,--showcols          List Columns of SQL File
 -sql,--sqlfile <arg>    File with Sql File query. Do not use extension
                         ".sql". Example --sqlfile report1
 -stz,--showtimezone     List all avaliable timezone in enviroment
 -v,--verbose            Show Details Processing
 -xls,--xlsfile <arg>    Output file excel format. Example --xlsfile
                         output.xlsx
```                           

### Others Details

### 1) I don't need Sheet instructions
   When < sql >-instructions.properties file is ausent, the sheet instruction will be not created.

### 2) Databases supported are:
    - Oracle
    - MySQl
    - PostgreSQL

### 3) Database suport 
    Others databases are supported, but add jdbc file in project (pom.xml)

### 4) If you don't want register user and password of database in conection file, put "***" then it is requered in prompt.
