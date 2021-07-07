#!/bin/bash

docker run -p 127.0.0.1:3300:3306 --name mariadbname -e MARIADB_ROOT_PASSWORD=securedpassword -d --restart=always mariadb:10
