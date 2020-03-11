from flask_restful import Resource
from flask import redirect, url_for
from flask_login import logout_user


class UserLogout(Resource):

    def get(self):
        logout_user()
        return redirect(url_for('index'))




