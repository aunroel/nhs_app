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


data = Blueprint("data", __name__)

@data.route('/getData', methods=["POST"])
@login_required
def test(userData):
    userId = userData['id']
    return str(userId)