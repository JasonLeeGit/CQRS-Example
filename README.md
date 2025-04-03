# CQRS-Example
Command Query Responsibility Segregation Example with Kafka event sourcing

CQRS allows you to separate the load from reads and writes allowing you to scale each independently. 
If your application sees a big disparity between reads and writes this is very useful.

To Run

Open a command prompt/terminal and cd to artist-command-service\src\main\resources and run docker-compose up -d

Start the artist-command-service running in eclipse 

Start the artist-query-service running in eclipse 

Open postman and send the below POST to http://localhost:9191/artist which will use the command service to write the data to the 
artist_command table and also send the kafka event
```
{
"type": "CreateArtist",
	"artist" : {
		"artistName" : "Oasis",
		"albumName":  "Be Here Now",
		"albumReleaseYear" : "1997",
		"price" : 9.99
	}
}
```
This event will be consumed by kafka and write the data to the artist_query table which is where will we do all reads from.

Now in postman send a GET request request to http://localhost:9292/artists which should return the data from the artist_query table

To amend an existing record in postman send a PUT request to http://localhost:9191/artist/1 where 1 is the id of the record to be updated
this will update the record in the artist_command table and also sync the artist_query table via a kafka event and listener

To check the MySQL Database container running in docker,

Open a command prompt

Enter docker ps and copy the mysql container id

Now in the same command prompt enter the below,

docker exec -it [your copied container id] mysql -u root -p
				
enter password

use cqrs;

View the Write Table,
select * from artist_command; 

View the Read Table,
select * from artist_query;
