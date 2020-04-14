from functools import wraps
from flask import request, g, abort
import jwt
from jwt import decode, exceptions
import json
from app import config


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
