# What is this Project ?

This application is created for extrat data from any jdbc database to Excel file with formated values.

Use query in connection JDBC data and extract data with Excel file using POI.

This Excel file have Collumn site ajusted and Header with Bold font.

There are sheet information, for this use < sql >-instructions.properties file and create with custon text.

    Input :  
        * Queries
        * Connection Database
        * Custon Text
        * Fields information

    Output :
        * Formated Excel Files
        * Custom sheet data.

## Dependencies for build project

* Kotlin - Version 1.6
* Java   - Version 18
* Maven  - Version 3.8.1

## Dependency for run project

* Java   - Version 18

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

### Create Columns file (./config/queries/*-columns.csv) from SQL file

    java -jar ExtractDataToExcel.jar --createcolsfile --sqlfile info --configdir ~/config-mysql

### Show TimeZone Supported

    java -jar ExtractDataToExcel.jar --showtimezone

### Show Help

```
java -jar ExtractDataToExcel.jar -help

usage:
 -ccf,--createcolsfile    Create a configuration column file
 -cd,--configdir <arg>    Directory with config files.
 -help                    Show help
 -sc,--showcols           Show Columns of SQL File
 -sft,--showfieldtypes    Show all field types and your use
 -sql,--sqlfile <arg>     File with Sql File(s) query. Do not use
                          extension ".sql". Example --sqlfile
                          report1,report2,report3. The number of queries
                          have the same number of excel files.
 -sqlp,--sqlparam <arg>   add param to SQL file. Format -sqlp "KEY=VALUE".
                          You can repeat this options many times.In .sql
                          use %{KEY} for replace value. Example: in param
                          use -sqlp "LIMIT=10" and in query file have ->
                          select c1, c2 from table limit %{LIMIT}
 -stz,--showtimezone      Show all avaliable timezone in enviroment
 -v,--verbose             Turn verbose mode on. Show details while
                          processing
 -xls,--xlsfile <arg>     Output file(s) excel format. Example --xlsfile
                          output1.xlsx,output2.xlsx,output3.xlsx
                       
```                           

### There are show variatios on Used fields for this use: java -jar ExtractDataToExcel.jar -sft

```
Show all field types :
Field: str_upper                                - String field, after read data convert data to Uppercase.
Field: str_lower                                - String field, after read data convert data to Lowercase.
Field: str_lower_without_accentuation           - String field, after read data convert data to lowercase, remove accentuation (é->e, à->a...).
Field: str_upper_without_accentuation           - String field, after read data convert data to uppercase, remove accentuation (É->e, Á->a...).
Field: str_lower_without_accentuation_only_letters - String field, after read data convert data to lowercase, remove accentuation and remove no letters (-01ÉeÁx->eeax).
Field: str_upper_without_accentuation_only_letters - String field, after read data convert data to uppercase, remove accentuation and remove no letters (-01ÉeÁx->EEAX).
Field: str_only_letters_without_accentuation    - String field, after read data convert remove accentuation and remove no letters("-01Ée.Áx"->EeAx).
Field: str_only_letters                         - String field, after read data convert remove no letters("-01Ée.Áx"->ÉaÁx).
Field: str_without_accentuation                 - String field, after read data remove accentuation("-01Ée.Áx"->"-01Ea.Ax").
Field: str                                      - Field String with no transformations
Field: int                                      - Integer field
Field: long                                     - Long number field
Field: float                                    - Float decimal field
Field: double                                   - Double decimal field
Field: day                                      - Day information field. dd/mm/yyyy
Field: time                                     - Time information field. HH:MM:SS
Field: datetime                                 - Timestamp field Data and Time field. 
```

### Format in Colluns file : 
	-- NameExcel,NameDB,Type,SizeExcel,SizeDB[,AddToExcel]
	* NameExcel is name used in Collun Excel file.
	* NameDB is field name in database
	* Type is type of field in list -sft help
	* AddToExcel is false or true. If true add collumn in Excel File, false dont write this field in Excel file.

### Why field is not added in Excel file?
 - You can use this field to Sort data
 - This field is not used.

### Using Sorted Fields
    In *-columns.csv add last lines fields database then you need sorted.
    Use this format but the "+" char is optional:
```
    --SORTED+:<first database field>
    --SORTED+:<second database field>
    --SORTED+:...
```
    or with minus for reverse order.
```
    --SORTED-:<first database field>
    --SORTED-:<second database field>
    --SORTED-:...
```
    Only one this (+ or -) has used. 

    Use this option when is very slow ORDER BY CLAUSE in SQL Query. 

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
    Example :
        * user=***Use Keyboard***
        * pass=***Use Keyboard***

### 5) Download packages ready to use:
    https://1drv.ms/f/s!Asl9Qoe_F0_Vp6se7kHBum5fsfyxeg

### 6) Last changes

#### 09/07/2022    (Version 1.1.0)
        * Use java 18
        * Execute many queries in the same process.
        * Accept Parameters to query in command line.
        * Order Asc/Desc in Any collumn after execute query.
	
#### 21/07/2022    (Version 1.1.1)
        * Added field types with string process. 
	* Declared fields can be dont writed in Excel file.
	
### 7) TODO List
        * Add create password (excel or zip) on the fly and log it on terminal.
        * Parameter file Multiline.
        * Add rules for apply colors in rows at Excel file. (Font and Backcolor)
        * Add output types (zip, csv, ...)
        * Add options in Coluns Files for Add Line Title. (Use title, Number os Coluns, Size of font, Color font and Color Background.
	* Sorting remove special chars and replace á by a ... Ignoring Case. [Done]
