from app import db, config
import datetime


class UploadedModelMeta(db.Model):
    __tablename__ = 'UploadedModelsMeta'

    id = db.Column(db.Integer, primary_key=True)
    filename = db.Column(db.String(256), index=True,
                         unique=True, nullable=False)
    json_summary = db.Column(db.JSON)
    deployed = db.Column(db.Boolean(), default=False,
                         unique=False, nullable=False)

    def __init__(self, filename, json_summary):
        self.filename = filename
        self.json_summary = json_summary
        self.deployed = False

    def to_dict(self):
        return {
            'filename': self.filename,
            'json_summary': self.json_summary,
            'deployed': self.deployed
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
    def find_by_filename(cls, filename):
        return cls.query.filter_by(filename=filename).first()

    @classmethod
    def find_by_id(cls, _id):
        return cls.query.filter_by(id=_id).first()

    @classmethod
    def get_deployed_filename(cls):
        return cls.query.filter_by(deployed=True).first().filename

    @classmethod
    def set_deployed_by_filename(cls, filename):
        db.session.query(cls).update({'deployed': False})
        db.session.query(cls).filter_by(
            filename=filename).update({'deployed': True})
        db.session.commit()
