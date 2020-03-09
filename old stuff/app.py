from flask import Flask
from flask_sqlalchemy import SQLAlchemy
from flask import (Blueprint, flash, g, redirect, render_template, request, session, url_for)
from werkzeug.security import check_password_hash, generate_password_hash

app = Flask(__name__)

app.config.from_pyfile('config.cfg')

db = SQLAlchemy()
db.init_app(app)

#
# class Test(db.Model):
#     __tablename__ = 'test_table'
#     id = db.Column(db.Integer(), primary_key=True)
#     postcode = db.Column(db.String())
#     data = db.Column(db.String())


@app.route('/test')
def test():
    return """
            <html>
                <body>

                <h1>WHAT THE HELL</h1>

<h2>Project description: Federated ML application for general population well-being prediction</h2>
<h3>Project stakeholders: NHS, UCL, group 4.</h3>

<h4>Group members: Marta Smigielska, Davinder Bassan, Najat Baqadir, Sam Xu, Yiren Zhang, Roman Matios, Wojciech Golaszewski</h4>


</body>
</html>
            """


#
# @app.route('/test_db')
# def test_db():
#     db.create_all()
#     db.session.commit()
#     entry = Test.query.first()
#     if not entry:
#         e = Test(postcode='W1', data='37')
#         db.session.add(e)
#         db.session.commit()
#     entry = Test.query.first()
#     return "Test entry '{} {}' is from database".format(entry.postcode, entry.data)


# bp = Blueprint('auth', __name__, url_prefix='/auth')


@app.route('/register', methods=['POST'])
def register():
    if request.method == 'POST':
        username = request.form['username']
        password = request.form['password']
        return "Hello, " + username + ", your password is " + password
    #
    # db.session.
    #     if not username:
    #         error
