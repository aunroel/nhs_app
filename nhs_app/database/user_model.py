from werkzeug.security import generate_password_hash, check_password_hash
from flask_login import UserMixin
from app import db, login, config
import datetime
import jwt


class User(UserMixin, db.Model):
    __tablename__ = 'User'

    id = db.Column(db.Integer, primary_key=True)
    username = db.Column(db.String(25), index=True, unique=True, nullable=False)
    email = db.Column(db.String(50), index=True, unique=True, nullable=False)
    user_type = db.Column(db.Boolean(), default=False, unique=False, nullable=False)
    password_hash = db.Column(db.String(128), nullable=False)

    def __init__(self, username, email, user_type, password):
        self.username = username
        self.email = email
        self.user_type = user_type
        self.password_hash = generate_password_hash(password)

    def __str__(self):
        return {
            'username': self.username,
            'email': self.email,
            'type': self.user_type
        }

    def encode_auth_token(self):
        """
        Generates the Auth Token
        :return: string
        """

        user_id = self.id

        payload = {
            'exp': datetime.datetime.utcnow() + \
                datetime.timedelta(seconds=config['JWT_TOKEN_EXPIRY_S']),
            'iat': datetime.datetime.utcnow(),
            'user': {
                "id": user_id
            }
        }
        
        return jwt.encode(
            payload,
            config['SECRET_KEY'],
            algorithm='HS256'
        ).decode('utf-8')

       

    def set_password(self, password):
        self.password_hash = generate_password_hash(password)

    def check_password(self, password):
        return check_password_hash(self.password_hash, password)

    def save_to_db(self):
        db.session.add(self)
        db.session.commit()

    def delete_from_db(self):
        db.session.delete(self)
        db.session.commit()

    @classmethod
    def find_by_username(cls, username):
        return cls.query.filter_by(username=username).first()

    @classmethod
    def find_by_email(cls, email):
        return cls.query.filter_by(email=email).first()

    @classmethod
    def find_by_id(cls, _id):
        return cls.query.filter_by(id=_id).first()

    # @classmethod
    # def get_admins(cls):
    #     return cls.query.filter_by(user_type=True).all()

    @staticmethod
    @login.user_loader
    def load_user(_id):
        return User.query.get(int(_id))

    # def get_reset_password_token(self, expires_in=86_400):
    #     return jwt.encode(
    #         {'reset_password': self.id, 'exp': time() + expires_in},
    #         app.config['SECRET_KEY'], algorithm='HS256').decode('utf-8')
    #
    # @staticmethod
    # def verify_reset_password_token(token):
    #     try:
    #         id = jwt.decode(token, app.config['SECRET_KEY'],
    #                         algorithms=['HS256'])['reset_password']
    #     except:
    #         return
    #
    #     return User.find_by_id(id)
