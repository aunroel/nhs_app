import os
from dotenv import load_dotenv

basedir = os.path.abspath(os.path.dirname(__file__))
load_dotenv(os.path.join(basedir, '.env'))


def generate_db_uri(user, password, host, port, db_name):
    return f'mysql+pymysql://{user}:{password}@{host}:{port}/{db_name}'


class Config(object):
    """ Base config, uses dev database server"""
    # TODO EMAIL SUPPORT
    DEBUG = True
    TESTING = True
    PROPAGATE_EXCEPTIONS = True
    SECRET_KEY = os.environ.get("SECRET_KEY")

    # Mail server config
    # MAIL_SERVER = os.environ.get('MAIL_SERVER')
    # MAIL_PORT = os.environ.get('MAIL_PORT')
    # MAIL_USE_TLS = os.environ.get('MAIL_USE_TLS')
    # MAIL_USERNAME = os.environ.get('MAIL_USERNAME')
    # MAIL_PASSWORD = os.environ.get('SENDGRID_API_KEY')

    # Database config
    USER = os.environ.get("DEV_DB_USER")
    PASSWORD = os.environ.get("DEV_DB_PSWD")
    HOST = os.environ.get("DEV_DB_HOST")
    PORT = os.environ.get("DEV_DB_PORT")
    DB_NAME = os.environ.get("DEV_DB_NAME")
    SQLALCHEMY_DATABASE_URI = generate_db_uri(
        USER, PASSWORD, HOST, PORT, DB_NAME)
    SQLALCHEMY_TRACK_MODIFICATIONS = False

    # JWT
    SECRET_KEY = "flyingavocados"
    JWT_TOKEN_EXPIRY_S = 3600000

    # Model saving
    UPLOADED_MODELS_DIR = "./models/trained_models/"
    TFLITE_MODELS_DIR = "./models/lite"
    UPLOADED_MODEL_FILENAME_PREFIX = "uploaded"


class ProductionConfig(Config):
    DEBUG = False
    PROPAGATE_EXCEPTIONS = False


class StagingConfig(Config):
    DEBUG = False
    USER = os.environ.get("STAGING_DB_USER")
    PASSWORD = os.environ.get("STAGING_DB_PSWD")
    HOST = os.environ.get("STAGING_DB_HOST")
    PORT = 3306
    DB_NAME = os.environ.get("STAGING_DB_NAME")
    SQLALCHEMY_DATABASE_URI = generate_db_uri(
        USER, PASSWORD, HOST, PORT, DB_NAME)
