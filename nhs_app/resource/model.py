from flask_restful import Resource, reqparse
from nhs_app.models.nodes_model import Node


_user_parser = reqparse.RequestParser()
_user_parser.add_argument('uid', type=str, required=True, help='uid required')


class Model(Resource):

    def post(self):
        data = _user_parser.parse_args()
        if not Node.find_by_uid(data['uid']):
            return {'message': 'Access denied'}, 403

        return 'this will be model', 201