#!/usr/bin/env bash

brew install postgresql

createdb pongstorm

## Password is pongstorm_user
createuser -P pongstorm_user

psql -d pongstorm -l src/main/resources/create-tables.sql
