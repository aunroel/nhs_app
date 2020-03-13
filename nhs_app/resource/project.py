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
        mobile = [{
            'status': 'dev',
            'method': 'POST',
            'url': '/update/:uid',
            'body': 'standard data in json format',
            'description': 'An endpoint to receive updates from the nodes'
        },
        {
                'status': 'ready',
                'method': 'POST',
                'url': '/node',
                'body': '{"uid": "unique id for the device"}',
                'description': 'An endpoint to register the device with the server'
        },
        {
                'status': 'ready',
                'method': 'GET',
                'url': '	/available',
                'body': '--//--',
                'description': 'Ask the server whether a tflite model is ready for download'
        },
        {
                'status': 'ready',
                'method': 'GET',
                'url': '	/model',
                'body': '{"uid": "<unique device id>"}',
                'description': 'An endpoint to download a TFLite model from the server'
        }
        ]
        headers = {'Content-Type': 'text/html'}
        return make_response(render_template('api_doc.html', mobile=mobile), 200, headers)
