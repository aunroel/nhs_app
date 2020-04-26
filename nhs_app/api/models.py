from os import walk, remove
from flask.blueprints import Blueprint
from flask import request, jsonify
from app import config
from auth.main import login_required
from sqlalchemy import update
from webargs import fields
from webargs.flaskparser import use_args, use_kwargs

from nhs_app.database.uploaded_model import UploadedModelMeta
from nhs_app.machine_learning.ml_util import ML
from nhs_app.file_system.ml_model_filename_builder import \
    build_uploaded_model_file_name, \
    file_format_is_h5, \
    change_ext_from_h5_to_tflite

uploaded_save_dir = config['UPLOADED_MODELS_DIR']
tflite_model_save_dir = config['TFLITE_MODELS_DIR']


models = Blueprint("models", __name__)


@models.route('/list', methods=["GET"])
def model_list():
    models = UploadedModelMeta.find_all()
    return jsonify([str(model.to_dict()) for model in models])


@models.route('/setDeployed', methods=["PUT"])
@use_kwargs({
    "filename": fields.Str(required=True),
})
def set_deployed(filename):

    model_meta = UploadedModelMeta.find_by_filename(filename)
    if not model_meta:
        return "No model with name {} found".format(filename), 400

    old_depl_model_filename = UploadedModelMeta.get_deployed_filename()

    ML().load_and_convert_to_lite(
        filename,
        uploaded_save_dir,
        tflite_model_save_dir
    )

    UploadedModelMeta.set_deployed_by_filename(filename)

    if (old_depl_model_filename != filename):
        try:
            old_tflite_mdl_filename = change_ext_from_h5_to_tflite(
                old_depl_model_filename)
            remove(tflite_model_save_dir + old_tflite_mdl_filename)
        except FileNotFoundError as e:
            print("Exisiting model {} did not have a deployed version in models/uploaded_lite"
                  .format(old_tflite_mdl_filename))

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
    model_meta = UploadedModelMeta(filename, json_summary)
    model_meta.save_to_db()

    return 'Model saved successfully'
