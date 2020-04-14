from sqlalchemy import func, desc

from app import db
import json


class UpdateAggregator(db.Model):
    __tablename__ = 'UpdateAggregator'

    id = db.Column(db.Integer, primary_key=True)
    supportCode = db.Column(db.String(50), index=True, nullable=False)
    wellBeingScore = db.Column(db.Integer, index=True, nullable=False)
    weeklySteps = db.Column(db.Integer, index=True, nullable=False)
    weeklyCalls = db.Column(db.Integer, index=True, nullable=False)
    errorRate = db.Column(db.Integer, index=True, nullable=False)
    postCode = db.Column(db.String(10), index=True, nullable=False)

    def __init__(self, support_code, score, steps, calls, error_rate, post_code):
        self.supportCode = support_code
        self.wellBeingScore = score
        self.weeklySteps = steps
        self.weeklyCalls = calls
        self.errorRate = error_rate
        self.postCode = post_code

    def __str__(self):
        return json.dumps({
            'supportCode':     self.supportCode,
            'wellBeingScore': self.wellBeingScore,
            'weeklySteps':     self.weeklySteps,
            'weeklyCalls':     self.weeklyCalls,
            'errorRate':       self.errorRate,
            'postCode':        self.postCode
        })

    def save_to_db(self):
        db.session.add(self)
        db.session.commit()

    def delete_from_db(self):
        db.session.delete(self)
        db.session.commit()

    @classmethod
    def find_all(cls):
        return cls.query.all()

    @classmethod
    def find_latest(cls, howMany=10):
        return cls.query.order_by(cls.id.desc()).limit(howMany).all()

    @classmethod
    def find_by_id(cls, _id):
        return cls.query.filter_by(id=_id).first()

    @classmethod
    def count_table_rows(cls):
        return db.session.execute(
            db.session.query(cls).statement.with_only_columns(
                [func.count()]).order_by(None)
        ).scalar()
