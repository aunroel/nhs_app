# NHS Federated ML appliation

## Requisites

| CI/CD     | Python             | Federated ML  |
| --------- | ------------------ | ------------- |
| \* Docker | Python (3.5-3.6.9) | \* Tensorflow |

# Development

For using both server and client side in development run both concurrently (in different terminals).

## Mobile frameworks

In order to access code for the iOS or Android, please switch to 'iOS' or 'android_branch' branch.

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
npm start
```

## Running server

### Install (first time)

Create virtual environment and install dependencies.

Install Docker on your machine

##### Unix

```
python3 -m venv flaskenv
source flaskenv/bin/activate
pip install -r requirements.txt
```

if module `tensorflow-docs==0.0.0` causes trouble, please remove it from requiremetns and run:

```
pip install git+https://github.com/tensorflow/docs
```

##### PowerShell

Run `.\initFlask.ps1` or:

```
py -3 -m venv flaskenv
flaskenv\Scripts\activate
python -m pip install --upgrade pip
pip install -r requirements.txt
```

if module `tensorflow-docs==0.0.0` causes trouble, please remove it from requiremetns and run:

```
pip install git+https://github.com/tensorflow/docs
```

### Run the server

Make sure that you have the '.env' file in the project root directory with all the entries filled with values.
In order to run the project for local development, please switch to the 'develop' branch.

##### Unix

```
docker-compose up -d
source flaskenv/bin/activate
export FLASK_APP=app.py
export FLASK_ENV=development
flask run
```

##### PowerShell

Run `.\runApp.ps1` or:

```
docker-compose up
flaskenv\Scripts\activate
$env:FLASK_APP = "app.py"
$env:FLASK_ENV = "development"
flask run
```

Once you finish working on the project, run `docker-compose down` to stop the Docker container with local database.

## Training of local models

Training of local models is currently disabled due to the amount of time it takes t re-train model for all of the postcode areas (around an hour for 253 postcode areas currently in the database).
If you want to enable local training - uncomment the following line in flush_and_retrain() method in app.py file:

```
    # cron_scripts.train_locals()

```

## Log-in into the web-interface

Username: admin

Password: admin

## Code coverage

In order to get the test coverage report, execute the following command from the project root:

```
coverage run --omit 'flaskenv/*' -m unittest project_tests.py && coverage html
```

It will execute all of the tests and generate a coverage report in HTML format in 'htmlcov' folder.

## Create MySQL database

1. Make sure you have run `docker-compose up` to run MySQL server
2. In Python interactive console execute:
   ```
   from app import db
   db.create_all()
   ```

### Troubleshooting

#### - CUDA dll files troubleshooting on Windows

Download `cudart64_101.dll` (or another version e.g. `cudart64_102.dll`) and put it in the appopriate folder and reinstall flaskenv.

1. Download Link : https://www.dll-files.com/download/1d7955354884a9058e89bb8ea34415c9/cudart64_101.dll.html?c=N09pWDBSTVYxRE1rM2hpNjVUa2doQT09
1. Directory : C:\Program Files\NVIDIA GPU Computing Toolkit\CUDA\v10.2\bin\
1. Reinstall flaskenv.

## Project contributors:

Marta Smigielska, Davinder Bassan, Najat Baqadir, Sam Xu, Yiren Zhang, Roman Matios, Wojciech Golaszewski

| iOS             | Android     | Backend              |
| --------------- | ----------- | -------------------- |
| Davinder Bassan | Sam Xu      | Marta Smigielska     |
| Najat Baqadir   | Yiren Zhang | Wojciech Golaszewski |
|                 |             | Roman Matios         |

./
