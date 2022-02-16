# What is Project ?

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

## Dependeces

  * Kotlin - Version 1.6
  * Java   - Version 11
  * Maven  - Version 3.8.1

## Process Build

mvn clean kotlin:compile package

copy target/ExtractDataToExcel.jar and /target/libs to your tool Folder.
copy conf-example to contigure query and connection
   + ./conf/connection.properties -> Conection database details
   + ./conf/queries/defaul.sql    -> Query
   + ./conf/queries/defaul-columns.csv    -> CSV to configure fields in Excel.
   + ./conf/queries/defaul-instructions.properties    -> Create Sheet with instructions

You can use many queries in the same configuration. For this change the prefix file "default"

## Run parameter and examples. 

java -jar ExtractDataToExcel.jar --configdir ~/config-mysql --xlsfile ./out.xlsx

  - sqlfile when is not present, the value is "default" 

java -jar ExtractDataToExcel.jar --sqlfile info --configdir ~/config-mysql --xlsfile ./out.xlsx

    - sqlfile is "info" so it find files "info.sql", "info-columns.csv", "info-instructions.properties"

When *-instructions.properties file is ausent, the sheet instruction will be not created.

### Databases supported are:
    - Oracle
    - MySQl
    - PostgreSQL

## Others databases are supported, but add jdbc file in project (pom.xml)