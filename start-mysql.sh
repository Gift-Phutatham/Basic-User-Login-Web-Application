#!/bin/bash

docker run -p 127.0.0.1:13306:3306 --name mariadb -e MARIADB_ROOT_PASSWORD=securedpassword -d --restart=always mariadb:10
