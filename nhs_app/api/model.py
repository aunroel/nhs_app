from webargs import fields, validate, ValidationError
from flask.json import jsonify
from nhs_app.models.user_model import User
from flask.blueprints import Blueprint
from functools import wraps
from flask import request, g, abort
import jwt
from jwt import decode, exceptions
import json
from app import config
from webargs.flaskparser import use_args, use_kwargs
from auth.main import login_required
from nhs_app.models.update_aggregator_model import UpdateAggregator
from werkzeug.utils import secure_filename
import os
from datetime import datetime


modelRouter = Blueprint("model", __name__)


@modelRouter.route('/upload', methods=["POST"])
# @login_required
def upload():
    f = request.files['tf_model']
    filename = f.filename
    if filename.find(".h5", -3) == -1:
        return 'Wrong file format', 400

    # Model saving
    path_prefix = config['UPLOADED_MODELS_PATH']
    filename_prefix = config['UPLOADED_MODEL_FILENAME_PREFIX']
    path = path_prefix + filename_prefix + secure_filename(f.filename)

    if os.path.isfile(path):
        d_string = datetime.now().strftime("%d.%m.%Y-%H.%M")
        path = path[:-3] + "-t-" + d_string + '.h5'

    f.save(path)
    return 'ok'
