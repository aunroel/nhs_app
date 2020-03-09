from app import db


class MainData(db.Model):
    __tablename__ = 'MainData'

    id = db.Column(db.Integer, primary_key=True)
    supportCode = db.Column(db.String(50), nullable=False)
    wellBeingScore = db.Column(db.Integer, nullable=False)
    weeklySteps = db.Column(db.Integer, nullable=False)
    weeklyCalls = db.Column(db.Integer, nullable=False)
    errorRate = db.Column(db.Integer, nullable=False)
    postCode = db.Column(db.String(10), nullable=False)

    def __init__(self, support_code, score, steps, calls, error_rate, post_code):
        self.supportCode = support_code
        self.wellBeingScore = score
        self.weeklySteps = steps
        self.weeklyCalls = calls
        self.errorRate = error_rate
        self.postCode = post_code

    def __str__(self):
        return {
            'id': self.id,
            'support_code':     self.supportCode,
            'well_being_score': self.wellBeingScore,
            'weekly_steps':     self.weeklySteps,
            'weekly_calls':     self.weeklyCalls,
            'error_rate':       self.errorRate,
            'post_code':        self.postCode
        }

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
    def find_by_id(cls, _id):
        return cls.query.filter_by(id=_id).first()


