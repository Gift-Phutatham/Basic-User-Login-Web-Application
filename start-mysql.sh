#!/bin/bash

docker run -p 127.0.0.1:3306:3306 -p 172.17.0.1:3306:3306 --name some-mysql -e MYSQL_ROOT_PASSWORD=my-secret-pw -d mysql
