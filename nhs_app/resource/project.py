from flask_restful import Resource
from flask import render_template, make_response
from flask_login import login_required


class Dashboard(Resource):

    @login_required
    def get(self):
        headers = {'Content-Type': 'text/html'}
        return make_response(render_template('dashboard.html'), 200, headers)


class Homepage(Resource):

    def get(self):
        headers = {'Content-Type': 'text/html'}
        return make_response(render_template('index.html'), 200, headers)


class ApiDoc(Resource):

    @login_required
    def get(self):
        headers = {'Content-Type': 'text/html'}
        return make_response(render_template('api_doc.html'), 200, headers)
