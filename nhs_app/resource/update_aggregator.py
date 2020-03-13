from flask_restful import Resource, reqparse
from nhs_app.models.update_aggregator_model import UpdateAggregator
from nhs_app.models.mobile_model import Node


class Aggregator(Resource):

    parser = reqparse.RequestParser()
    parser.add_argument('supportCode', type=str, required=True, help='support code')
    parser.add_argument('wellBeingScore', type=int, required=True, help='wb score')
    parser.add_argument('weeklySteps', type=int, required=True, help='steps')
    parser.add_argument('weeklyCalls', type=int, required=True, help='calls')
    parser.add_argument('errorRate', type=int, required=True, help='error rate')
    parser.add_argument('postCode', type=str, required=True, help='postcode')

    def post(self, uid):
        if not Node.find_by_uid(uid):
            return {'message': f'App is not registered'}, 403

        data = Aggregator.parser.parse_args()
        update = UpdateAggregator(
            support_code=data['supportCode'],
            score=data['wellBeingScore'],
            steps=data['weeklySteps'],
            calls=data['weeklyCalls'],
            error_rate=data['errorRate'],
            post_code=data['postCode']
        )

        try:
            update.save_to_db()
        except:
            return {'message': "An error occured while inserting update"}, 500

        return "ok", 201