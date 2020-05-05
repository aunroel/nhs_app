#!/bin/bash
source flaskenv/bin/activate
export FLASK_APP=app.py
export FLASK_ENV=development
flask run