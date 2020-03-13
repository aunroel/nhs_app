from flask_restful import Resource, reqparse
from nhs_app.models.mobile_model import Node

_user_parser = reqparse.RequestParser()
_user_parser.add_argument('uid', type=str, required=True, help='uid required')


class NodeRegister(Resource):
    def post(self):
        data = _user_parser.parse_args()
        if Node.find_by_uid(data['uid']):
            return {'message': 'Registration denied'}, 403

        try:
            node = Node(uid=data['uid'])
            node.save_to_db()
        except:
            return {'message': 'Error occurred during registration'}, 500

        return {'message': 'ok'}, 201
