from flask import send_from_directory
from flask_restful import Resource, reqparse
from nhs_app.models.mobile_model import Node
from flask_login import login_required
from nhs_app.machine_learning.ml_model import ML


_user_parser = reqparse.RequestParser()
_user_parser.add_argument('uid', type=str, required=True, help='uid required')


class MLResource(Resource):

    def get(self):
        data = _user_parser.parse_args()
        if not Node.find_by_uid(data['uid']):
            return {'message': 'Access denied'}, 403

        model_directory = 'models/lite'
        file_name = 'latest_converted_model.tflite'

        return send_from_directory(model_directory, file_name, as_attachement=True)


class MLTrainingResource(Resource):

    @login_required
    def get(self):
        ml = ML()
        ml.updated_refresh()
        ml.predict()
        ml.convert_to_lite_and_save()
        return 'ok'
