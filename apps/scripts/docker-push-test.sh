#! bin/bash

docker build -t na0105/se-form-webapp:test ../SE-form-backend

docker push na0105/se-form-webapp:test
