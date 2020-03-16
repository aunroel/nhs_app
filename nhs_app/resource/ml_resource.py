import os
from flask import send_from_directory, make_response, render_template
from flask_restful import Resource, reqparse
from nhs_app.models.mobile_model import Node
from flask_login import login_required
from nhs_app.machine_learning.ml_model import ML


_user_parser = reqparse.RequestParser()
_user_parser.add_argument('uid', type=str, required=True, help='uid required')


class MLDownload(Resource):

    def post(self):
        data = _user_parser.parse_args()
        if not Node.find_by_uid(data['uid']):
            return {'message': 'Access denied'}, 403

        if not os.path.isfile('models/lite/latest_converted_model.tflite'):
            return {'message': 'Access denied'}, 403

        model_directory = 'models/lite'
        file_name = 'latest_converted_model.tflite'

        return send_from_directory(model_directory, file_name, as_attachment=True)


class MLTrainingResource(Resource):

    @login_required
    def get(self):
        ml = ML()
        ml.updated_refresh()
        ml.predict()
        ml.convert_to_lite_and_save()

        headers = {'Content-Type': 'text/html'}

        return make_response(render_template('training_complete.html'), 200, headers)


class ModelAvailability(Resource):

    @login_required
    def get(self):
        headers = {'Content-Type': 'text/html'}
        if os.path.isfile('models/lite/latest_converted_model.tflite'):
            return make_response(render_template('model_ready.html', code=200), 200, headers)
        else:
            return make_response(render_template('model_ready.html', code=404), 404, headers)

