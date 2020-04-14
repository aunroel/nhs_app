import os
from flask import send_from_directory
from flask import Flask, render_template, url_for, redirect, flash, request, make_response, jsonify
from flask_wtf.csrf import CSRFProtect
from flask_sqlalchemy import SQLAlchemy
from flask_migrate import Migrate
from flask_login import LoginManager, current_user, login_user
from flask_restful import Api
from flask_bootstrap import Bootstrap
from config import Config
from werkzeug.urls import url_parse
from werkzeug.security import generate_password_hash, check_password_hash
from webargs import fields, validate, ValidationError
from webargs.flaskparser import use_args, use_kwargs
import jwt
import json
import datetime



app = Flask(__name__)
app.config.from_object(Config)
config = app.config
# csrf = CSRFProtect(app)
db = SQLAlchemy(app)
migrate = Migrate(app, db)
login = LoginManager(app)
login.login_view = 'login'
api = Api(app)
bootstrap = Bootstrap(app)

from auth.main import login_required
from nhs_app.models.user_model import User
from nhs_app.forms.user_forms import UserLogin, UserRegister
from nhs_app.resource.project import Dashboard, Homepage, ApiDoc
from nhs_app.resource.ml_resource import MLDownload, MLTrainingResource, ModelAvailability
from nhs_app.resource.user import UserLogout
from nhs_app.resource.node import NodeRegister
from nhs_app.resource.update_aggregator import Aggregator

api.add_resource(Homepage, '/index', '/', endpoint='index')
api.add_resource(Aggregator, '/update/<string:uid>', endpoint='update')
api.add_resource(NodeRegister, '/node', endpoint='node')
api.add_resource(UserLogout, '/logout', endpoint='logout')
api.add_resource(ApiDoc, '/doc', endpoint='doc')
api.add_resource(Dashboard, '/dashboard', endpoint='dashboard')
api.add_resource(MLDownload, '/model', endpoint='model')
api.add_resource(MLTrainingResource, '/train', endpoint='train')
api.add_resource(ModelAvailability, '/available', endpoint='available')


@app.route('/login', methods=['GET', 'POST'])
def logino():
    if current_user.is_authenticated:
        return redirect(url_for('index'))

    form = UserLogin()
    if form.validate_on_submit():
        user = User.find_by_username(form.username.data)
        if user is None \
            or not user.check_password(form.password.data):
            # or user.user_type != 1:
            flash('Invalid username or password')
            return redirect(url_for('login'))

        login_user(user, remember=form.remember_me.data)
        next_page = request.args.get('next')
        if not next_page or url_parse(next_page).netloc != '':
            next_page = url_for('index')

        return redirect(next_page)

    return render_template('login.html', form=form)


@app.route('/api/auth/login', methods=["POST"])
@use_kwargs({
    "email": fields.Email(required=True),
    "password": fields.Str(required=True), 
})
def login(email, password):

    try :
        user = User.find_by_email(email)

        if user is None or not user.check_password(password):
            raise ValidationError('User or password are incorrect')

        encoded_jwt = user.encode_auth_token()

        return jsonify({'token': encoded_jwt})

    except ValidationError as e:
        print(str(e))
        return jsonify( { 'error' : str(e) } )


@app.route('/register', methods=['GET', 'POST'])
def registero():
    if current_user.is_authenticated:
        return redirect(url_for('dashboard'))
    
    form = UserRegister()
    if form.validate_on_submit():
        user = User(username=form.username.data, email=form.email.data, user_type=False,
                    password=form.password.data)
        user.save_to_db()
        flash('Congratulations, registration completed. System admin will review and approve your permissions shortly.')
        return redirect(url_for('index'))

    return render_template('register.html', form=form)


@app.route('/api/auth/test', methods=["POST"])
@login_required
def test(userData):
    userId = userData['id']
    return str(userId)


@app.route('/api/auth/register', methods=['POST'])
@use_kwargs({
    "username": fields.Str(required=True), 
    "email": fields.Email(required=True),
    "password": fields.Str(required=True), 
    "password2": fields.Str(required=True), 
    })
def register(username, email, password, password2):
    try: 
        if User.find_by_email(email):
            raise ValidationError('Email already in use. Please use different email address')

        if User.find_by_username(username):
            raise ValidationError('Username already exists. Please choose another')

        if password != password2:
            raise ValidationError("Password and Repeated Password must match")
        
        user = User(username = username, 
                    email = email, 
                    password = password,
                    user_type = False)

        user.save_to_db()

        encoded_jwt = user.encode_auth_token()

        return jsonify({'token': encoded_jwt})
         
        
    except ValidationError as e:
        return jsonify( { 'error' : str(e) } )

    except Exception as e:
        print(str(e))
        return jsonify( { 'error' : "An error occurred saving the user to the database " } ), 500



@app.errorhandler(404)
def not_found_error(error):
    headers = {'Content-Type': 'text/html'}
    return make_response(render_template('404.html'), 400, headers)


@app.errorhandler(500)
def internal_error(error):
    headers = {'Content-Type': 'text/html'}
    return make_response(render_template('500.html'), 500, headers)


@app.route('/favicon.ico')
def favicon():
    return send_from_directory(os.path.join(app.root_path, 'static'),
                               'favicon.ico', mimetype='image/vnd.microsoft.icon')


if __name__ == '__main__':
    db.init_app(app)
    app.run(port=5000, debug=True)

