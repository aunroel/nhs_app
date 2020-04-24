from webargs import fields, validate, ValidationError
from flask.json import jsonify
from nhs_app.database.user_model import User
from flask.blueprints import Blueprint
from functools import wraps
from flask import request, g, abort
import jwt
from jwt import decode, exceptions
import json
from app import config
from webargs.flaskparser import use_args, use_kwargs
from auth.main import login_required
from nhs_app.database.update_aggregator_model import UpdateAggregator


data = Blueprint("data", __name__)


@data.route('/', methods=["GET"])
# @login_required
def getData():
    data = UpdateAggregator.find_latest(howMany=10)

    numberOfEntries = UpdateAggregator.count_table_rows()

    data = [str(d) for d in data]

    return jsonify({
        "numberOfEntries": numberOfEntries,
        "data": data
    })

