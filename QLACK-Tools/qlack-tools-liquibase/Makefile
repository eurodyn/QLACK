all: build push

build:
	docker build . -t eurodyn/qlack-tools-liquibase:1

push:
	docker login
	docker push eurodyn/qlack-tools-liquibase:1
