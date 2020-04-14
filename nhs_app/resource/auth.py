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


auth = Blueprint("auth", __name__)

def login_required(f):
  @wraps(f)
  def wrap(*args, **kwargs):
    token = request.headers.get("x-auth-token")
    if not token:
      return json.dumps({'error': 'no authorization token provided'}), \
        403, {'Content-type': 'application/json'}

    try: 
      resp = decode(token, config["SECRET_KEY"], algorithm='HS256')
      
      return f(resp['user'], *args, **kwargs)
    
    except jwt.ExpiredSignatureError:
      return 'Signature expired. Please log in again.'
    except jwt.InvalidTokenError:
      return 'Invalid token. Please log in again.'

    except exceptions.DecodeError as indentifier:
      return json.dumps({'error':'invalid authorization token'}), \
        403, {'Content-type': 'application/json'}
      
 
  return wrap


@auth.route('/login', methods=["POST"])
@use_kwargs({
    "email": fields.Email(required=True),
    "password": fields.Str(required=True), 
})
def login(email, password):

    try :
        user = User.find_by_email(email)

        if user is None or not user.check_password(password):
            raise ValidationError('User or password are incorrect')

        encoded_jwt = user.encode_auth_token()

        return jsonify({'token': encoded_jwt})

    except ValidationError as e:
        print(str(e))
        return jsonify( { 'error' : str(e) } )



@auth.route('/test', methods=["POST"])
@login_required
def test(userData):
    userId = userData['id']
    return str(userId)


@auth.route('/register', methods=['POST'])
@use_kwargs({
    "username": fields.Str(required=True), 
    "email": fields.Email(required=True),
    "password": fields.Str(required=True), 
    "password2": fields.Str(required=True), 
    })
def register(username, email, password, password2):
    try: 
        if User.find_by_email(email):
            raise ValidationError('Email already in use. Please use different email address')

        if User.find_by_username(username):
            raise ValidationError('Username already exists. Please choose another')

        if password != password2:
            raise ValidationError("Password and Repeated Password must match")
        
        user = User(username = username, 
                    email = email, 
                    password = password,
                    user_type = False)

        user.save_to_db()

        encoded_jwt = user.encode_auth_token()

        return jsonify({'token': encoded_jwt})
         
        
    except ValidationError as e:
        return jsonify( { 'error' : str(e) } )

    except Exception as e:
        print(str(e))
        return jsonify( { 'error' : "An error occurred saving the user to the database " } ), 500