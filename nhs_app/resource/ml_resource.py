import os
from flask import send_from_directory, make_response, render_template
from flask_restful import Resource, reqparse
from nhs_app.models.mobile_model import Node
from nhs_app.machine_learning.ml_model import ML
from flask_login import login_required
from app import config
from nhs_app.models.uploaded_model import UploadedModelMeta
from nhs_app.file_system.ml_model_filename_builder import \
    change_ext_from_h5_to_tflite

tflite_model_save_dir = config['TFLITE_MODELS_DIR']

_user_parser = reqparse.RequestParser()
_user_parser.add_argument('uid', type=str, required=True, help='uid required')


class MLDownload(Resource):

    def post(self):
        data = _user_parser.parse_args()
        if not Node.find_by_uid(data['uid']):
            return {'message': 'Access denied'}, 403

        depl_filename = UploadedModelMeta.get_deployed_filename()
        depl_filename = change_ext_from_h5_to_tflite(depl_filename)

        return send_from_directory(
            tflite_model_save_dir, depl_filename, as_attachment=True)


class MLTrainingResource(Resource):

    @login_required
    def get(self):
        ml = ML()
        ml.updated_refresh()
        ml.predict()
        # TODO verify this line
        # ml.convert_to_lite_and_save()

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
