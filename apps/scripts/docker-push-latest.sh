#! bin/bash

docker build -t na0105/se-form-webapp:latest ../SE-form-backend

docker push na0105/se-form-webapp:latest
