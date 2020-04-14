flaskenv\Scripts\activate
$env:FLASK_APP = "app.py"
# $env:FLASK_DEBUG = 0
$env:FLASK_ENV = "development"
flask run
