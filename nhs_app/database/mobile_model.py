from app import db


class Node(db.Model):
    __tablename__ = 'RegisteredNodes'

    id = db.Column(db.Integer, primary_key=True)
    unique_node_id = db.Column(db.String(128), unique=True, nullable=False)

    def __init__(self, uid):
        self.unique_node_id = uid

    def save_to_db(self):
        db.session.add(self)
        db.session.commit()

    def delete_from_db(self):
        db.session.delete(self)
        db.session.commit()

    @classmethod
    def find_by_uid(cls, uid):
        return cls.query.filter_by(unique_node_id=uid).first()

    @classmethod
    def find_by_id(cls, _id):
        return cls.query.filter_by(id=_id).first()
