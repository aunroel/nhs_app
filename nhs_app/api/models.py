from flask.blueprints import Blueprint
from flask import request, jsonify
from app import config
from auth.main import login_required
from sqlalchemy import update
from webargs import fields
from webargs.flaskparser import use_args, use_kwargs

from nhs_app.models.uploaded_ml_model import UploadedMLModelMeta
from nhs_app.machine_learning.ml_model import ML
from nhs_app.file_system.ml_model_filename_builder import \
    build_uploaded_model_file_name, \
    file_format_is_h5

uploaded_save_dir = config['UPLOADED_MODELS_DIR']


models = Blueprint("models", __name__)


@models.route('/list', methods=["GET"])
def model_list():
    models = UploadedMLModelMeta.find_all()
    return jsonify([str(model.to_dict()) for model in models])


@models.route('/setDeployed', methods=["PUT"])
@use_kwargs({
    "filename": fields.Str(required=True),
})
def set_deployed(filename):

    model_meta = UploadedMLModelMeta.find_by_filename(filename)
    if not model_meta:
        return "No model with name found", 400

    UploadedMLModelMeta.set_deployed_by_filename(filename)

    return "Update successful"



@models.route('/upload', methods=["POST"])
# @login_required
def upload():
    file = request.files['tf_model']

    if not file_format_is_h5(file):
        return 'Wrong file format', 400

    filename = build_uploaded_model_file_name(file.filename)

    # Model saving
    save_path = uploaded_save_dir + filename
    file.save(save_path)

    # Get summary
    model = ML().load(save_path).model
    json_summary = model.to_json()

    # Save to db
    model_meta = UploadedMLModelMeta(filename, json_summary)
    model_meta.save_to_db()

    return 'Model saved successfully'
