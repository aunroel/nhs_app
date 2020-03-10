# NHS Federated ML appliation

## Requisites
  CI/CD            | Python              | Federated ML
---------          |-------------------- | -------------
* Docker           | * Python 3.6        | * PySyft
* Kubernetes       | * Flask             |
* MS Azure account | * flask-sqlalchemy  |
|                  | * psycopg2-binary   |
|                  | * gunicorn          |


# Development 
For using both server and client side in development run both concurrently (in different terminals).

## Running client
Worklfow is the same for windows and unix
### Install (first time) 
1. Install node.js
1. Install 'nodemon' globally. It is package enabling live-reload of the client server.
    ```
    npm install -g nodemon
    ```
1. Install all node dependencies
    ```
    cd client
    npm install
    ```
### Run
```
cd client
nodemon start
```

## Running server
### Install (first time) 
Create virtual environment and install dependencies.
##### Unix
```
python3 -m venv flaskenv
source flaskenv/bin/activate
pip install -r requirements.txt
```
##### PowerShell
```
py -3 -m venv flaskenv
flaskenv\Scripts\activate
pip install -r requirements.txt
```
### Run
##### Unix 
```
source flaskenv/bin/activate
export FLASK_APP=app.py
export FLASK_ENV=development
flask run
```

##### PowerShell
Run `.\runApp.ps1` or:
```
flaskenv\Scripts\activate
$env:FLASK_APP = "app.py"
$env:FLASK_ENV = "development"
flask run
```

## Project contributors: 
Marta Smigielska, Davinder Bassan, Najat Baqadir, Sam Xu, Yiren Zhang, Roman Matios, Wojciech Golaszewski

   iOS          | Android       | Backend
----------------| ------------- | ------------
Davinder Bassan | Sam Xu        | Marta Smigielska
Najat Baqadir   | Yiren Zhang   | Wojciech Golaszewski
|               |               | Roman Matios

