import os
from flask import send_from_directory, make_response, render_template
from flask_restful import Resource, reqparse
from nhs_app.database.mobile_model import Node
from flask_login import login_required
from app import config
from nhs_app.database.uploaded_model import UploadedModelMeta
from nhs_app.database.main_data_model import MainData
from nhs_app.file_system.ml_model_filename_builder import \
    change_ext_from_h5_to_tflite

tflite_model_save_dir = config['TFLITE_MODELS_DIR']

_user_parser = reqparse.RequestParser()
_user_parser.add_argument('uid', type=str, required=True, help='uid required')


class NationalMLDownload(Resource):

    def post(self):
        data = _user_parser.parse_args()
        if not Node.find_by_uid(data['uid']):
            return {'message': 'Access denied'}, 403

        directory = './models/national/lite'
        filename = 'default_model.tflite'

        return send_from_directory(
            directory, filename, as_attachment=True)
    

class LocalMLDownload(Resource):

    def post(self, postcode):
        if not MainData.find_by_postcode(postcode):
            return {'message': f'No such postcode'}, 403
        
        data = _user_parser.parse_args()
        if not Node.find_by_uid(data['uid']):
            return {'message': 'Access denied'}, 403

        directory = f'./models/local/lite/{postcode}/'
        filename = 'default_model.tflite'

        return send_from_directory(
            directory, filename, as_attachment=True)


class UploadedMLDownload(Resource):

    def post(self):
        data = _user_parser.parse_args()
        if not Node.find_by_uid(data['uid']):
            return {'message': 'Access denied'}, 403

        depl_filename = UploadedModelMeta.get_deployed_filename()
        depl_filename = change_ext_from_h5_to_tflite(depl_filename)

        return send_from_directory(
            tflite_model_save_dir, depl_filename, as_attachment=True)


# class MLTrainingResource(Resource):
#
#     @login_required
#     def get(self):
#         ml = ML()
#         ml.refresh_model()
#         ml.save_default_and_convert()
#
#         headers = {'Content-Type': 'text/html'}
#
#         return make_response(render_template('training_complete.html'), 200, headers)
#
#
# class LocalMLTrainingResource(Resource):
#
#     @login_required
#     def get(self, postcode):
#         if not MainData.find_by_postcode(postcode):
#             return {'message': f'No such postcode'}, 403
#
#         ml = ML()
#         ml.postcode = postcode
#         ml.national = False
#         ml.directory = './database/local'
#         ml.refresh_model()
#         ml.save_default_and_convert()
#
#         headers = {'Content-Type': 'text/html'}
#
#         return make_response(render_template('training_complete.html'), 200, headers)


class NationalModelAvailability(Resource):

    @login_required
    def get(self):
        headers = {'Content-Type': 'text/html'}

        if os.path.isfile(f'./models/national/lite/default_model.tflite'):
            return make_response(render_template('model_ready.html', code=200), 200, headers)
        else:
            return make_response(render_template('model_ready.html', code=404), 404, headers)


class LocalModelAvailability(Resource):

    @login_required
    def get(self, postcode):
        if not MainData.find_by_postcode(postcode):
            return {'message': f'No such postcode'}, 403

        headers = {'Content-Type': 'text/html'}
        if os.path.isfile(f'models/local/lite/{postcode}/default_model.tflite'):
            return make_response(render_template('model_ready.html', code=200), 200, headers)
        else:
            return make_response(render_template('model_ready.html', code=404), 404, headers)


