To start zookeeper command:
bin\windows\zookeeper-server-start.bat config\zookeeper.properties

To start kafka command:
bin\windows\kafka-server-start.bat config\server.properties

now run all 6 services
UserService on port 8010
MusicService on port 8020
ListService on port 8030
authService on port 8040
spotify-gateway on port 8050
eureka-server on port 8761

To search music
http://localhost:8050/api/v1/music/search?q=balaya

To get favorite list: "123" is the userid
http://localhost:8050/api/v1/list/favorites/123

To register new User
http://localhost:8050/api/v1/user/register

To login for jwt token
http://localhost:8050/api/v1/auth/login