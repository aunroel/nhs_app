from flask import Flask, render_template, url_for, redirect, flash, request, make_response
from flask_sqlalchemy import SQLAlchemy
from flask_migrate import Migrate
from flask_login import LoginManager, current_user, login_user
from flask_restful import Api
from flask_bootstrap import Bootstrap
from config import Config
from werkzeug.urls import url_parse


app = Flask(__name__)
app.config.from_object(Config)
db = SQLAlchemy(app)
migrate = Migrate(app, db)
login = LoginManager(app)
login.login_view = 'login'
api = Api(app)
bootstrap = Bootstrap(app)

from nhs_app.resource.update_aggregator import Aggregator
from nhs_app.resource.node import NodeRegister
from nhs_app.resource.user import UserLogout, UserRegister
from nhs_app.resource.project import Dashboard, Homepage, ApiDoc
from nhs_app.forms.user_forms import UserLogin
from nhs_app.models.user_model import User

# @app.route('/')
# @app.route('/index')
# def index():
#     project = {
#         'lead': "Marta Smigielska",
#         'team_list': 'Android, iOS, Server',
#         'teams': [
#             {
#                 'name': 'Android Team',
#                 'contributors': 'Xu Yichao, Zhang Yiren'
#             },
#             {
#                 'name': 'iOS Team',
#                 'contributors': 'Baqadir Najat, Bassan Davinderpal'
#             },
#             {
#                 'name': 'Server Team',
#                 'contributors': 'Smigielska Marta, Matios Roman, Wojciech Golaszewski'
#             }
#         ]
#     }
#     return render_template('index.html', project=project)


# @app.route('/dashboard')
# @login_required
# def dashboard():
#     return render_template('dashboard.html')


# @app.route('/login', methods=['GET', 'POST'])
# def login():
#     if current_user.is_authenticated:
#         return redirect(url_for('index'))
#
#     form = UserLogin()
#     if form.validate_on_submit():
#         user = User.query.filter_by(username=form.username.data).first()
#         if user is None or not user.check_password(form.password.data):
#             flash('Invalid username or password')
#             return redirect(url_for('login'))
#
#         login_user(user, remember=form.remember_me.data)
#         next_page = request.args.get('next')
#         if not next_page or url_parse(next_page).netloc != '':
#             next_page = url_for('index')
#
#         return redirect(next_page)
#
#     return render_template('login.html', form=form)


# @app.route('/logout')
# def logout():
#     logout_user()
#     return redirect(url_for('index'))


# @app.route('/register', methods=['GET', 'POST'])
# def register():
#     if current_user.is_authenticated:
#         return redirect(url_for('dashboard'))
#
#     form = UserRegister()
#     if form.validate_on_submit():
#         user = User(username=form.username.data, email=form.email.data, user_type=False,
#                     password=form.password.data)
#         user.save_to_db()
#         flash('Congratulations, registration completed')
#         return redirect(url_for('login'))
#
#     return render_template('register.html', form=form)


api.add_resource(Homepage, '/index', '/', endpoint='index')
api.add_resource(Aggregator, '/update/<string:uid>', endpoint='update')
api.add_resource(NodeRegister, '/node', endpoint='node')
api.add_resource(UserLogout, '/logout', endpoint='logout')
api.add_resource(UserRegister, '/register', endpoint='register')
api.add_resource(ApiDoc, '/doc', endpoint='doc')
api.add_resource(Dashboard, '/dashboard', endpoint='dashboard')


@app.route('/login', methods=['GET', 'POST'])
def login():
    if current_user.is_authenticated:
        return redirect(url_for('index'))

    form = UserLogin()
    if form.validate_on_submit():
        user = User.find_by_username(form.username.data)
        if user is None or not user.check_password(form.password.data):
            flash('Invalid username or password')
            return redirect(url_for('login'))

        login_user(user, remember=form.remember_me.data)
        next_page = request.args.get('next')
        if not next_page or url_parse(next_page).netloc != '':
            next_page = url_for('index')

        return redirect(next_page)

    return render_template('login.html', form=form)


@app.errorhandler(404)
def not_found_error(error):
    headers = {'Content-Type': 'text/html'}
    return make_response(render_template('404.html'), 400, headers)


@app.errorhandler(500)
def internal_error(error):
    headers = {'Content-Type': 'text/html'}
    return make_response(render_template('500.html'), 500, headers)

