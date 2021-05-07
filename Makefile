all:
	./gradlew :server:run

website:
	./gradlew :server:build
	scp server/build/libs/server-0.1.1.jar root@95.181.224.251:/var/www/duma/pobeda/pobeda_server.jar
	ssh root@95.181.224.251