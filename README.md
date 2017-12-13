#WalletHub Code Challenge

##QuickStart
From within the root directory of this project

1. build project using `$ ./gradlew build`
2. `$ java -cp build/libs/parser-1.0.jar Main --accesslog src/main/resources/access.log --startDate=2017-01-01.13:00:00 --duration=hourly --threshold=100`

I have a copy of the access log included in this project for ease of access

##Deliverables
1. Java progam found in `build/libs/parser-1.0` after running `$ ./gradlew build` or building the project in some other way
2. Source code included in this repo
3. MySQL Schema can be recreated using the following Queries
 	- ```CREATE TABLE `records` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `date` datetime NOT NULL,
  `ipAddress` varchar(50) NOT NULL DEFAULT '',
  `httpMethod` varchar(50) NOT NULL DEFAULT '',
  `responseStatus` int(11) NOT NULL,
  `userAgent` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=116485 DEFAULT CHARSET=utf8;```
	- ```CREATE TABLE `blockedIPs` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `address` varchar(50) NOT NULL DEFAULT '',
  `comment` text,
  PRIMARY KEY (`id`),
  UNIQUE KEY `address` (`address`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8;```
4. SQL Queries
	1. ```"SELECT ipAddress, COUNT(*) as count FROM records WHERE id IN (\n" +
            "\tSELECT id FROM records WHERE (records.`date` > ? AND records.`date` < ?)\n" +
            ") GROUP BY ipAddress\n" +
            "HAVING count > ?;";```
   2. ```SELECT * FROM records WHERE (ipAddress = "192.168.234.82")```

##Extra

###Database
Default database connection configuration can be found and changed in `ServiceProveder.java`. The defaults are:

- URL: `jdbc:mysql://localhost:3306/wallethub`
- user: `root`
- password: `null`

###Bonus Features
- You build up the records database by running the tool with the `--accesslog` parameter. After this is done once, you can use the query feature as often as you want without having iterate over all of those entries anymore
- Running multiple threshold violation queries will not introduce duplicate IP entries. Instead, any duplicates will be overwritten with the last queried violation being found in the comment section

###Next Steps
The next features I would want to develop for this tool would be

- Generalizable database, so we don't have to change the hard-coded database information but instead can read it from some kind of configuration file
- Record hashing if (and only if) we want to support multiple `.log` files being added to the database. Current implementation deletes the old data in the table to avoid duplication, but a more sophisticated version would create some hash out of each request/record and use that as a unique key to prevent duplication.