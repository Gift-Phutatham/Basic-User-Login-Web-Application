#!/bin/bash

docker run --name some-mysql -e MYSQL_ROOT_PASSWORD=my-secret-pw -d -p 127.0.0.1:5555:3306 mysql
