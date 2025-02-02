To Run

cd to artist-command-service\src\main\resources and run docker_compose up

Start the artist-command-service running in eclipse 


Start the artist-query-service running in eclipse 


Open postman and send the below POST to http://localhost:9191/artist which will use the command service to write the data to the 
artist_command table and also send the kafka event

{
"type": "CreateArtist",
	"artist" : {
		"artistName" : "Oasis",
		"albumName":  "Be Here Now",
		"albumReleaseYear" : "1994",
		"price" : 9.99
	}
}

This event will be consumed by kafka and write the data to the artist_query table which is where will we do all reads from.

Now in postman send a GET request request to http://localhost:9292/artists which should return the data from the artist_qery table


To amend an existing record in postman send a PUT request to http://localhost:9191/artist/1 where 1 is the id of the record to be updated
this will update the record in the artist_command table and also sync the artist_query table via a kafka event and listener



to check the DB,

Open a command prompt

enter docker ps and get the mysql container id

now in the same command prompt enter the below (23b6da1aa5db is the container id replace with yours) 

docker exec -it 23b6da1aa5db mysql -u root -p
				
enter password

use cqrs;

select * from command_artist;

select * from command_query;