all:
	./gradlew :server:run

website:
	./gradlew :server:build
	scp server/build/libs/server-0.1.1.jar root@46.254.19.56:/var/www/duma/pobeda/pobeda_server.jar
	ssh root@46.254.19.56