import os
from dotenv import load_dotenv

basedir = os.path.abspath(os.path.dirname(__file__))
load_dotenv(os.path.join(basedir, '.env'))


def generate_db_uri(user, password, host, port, db_name):
    return f'mysql+pymysql://{user}:{password}@{host}:{port}/{db_name}'


class Config(object):
    """ Base config, uses dev database server"""
    DEBUG = True
    TESTING = True
    PROPAGATE_EXCEPTIONS = True
    SECRET_KEY = os.environ.get("SECRET_KEY")

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
    # SECRET_KEY = "flyingavocados"
    JWT_TOKEN_EXPIRY_S = 3600000

    # Model saving
    UPLOADED_MODELS_DIR = "./models/trained_models/"
    TFLITE_MODELS_DIR = "./models/uploaded_lite/"
    LOCAL_MODELS_GRAPHS = "./models/local/"
    NATIONAL_MODELS_GRAPHS = "./models/national/"
    UPLOADED_MODEL_FILENAME_PREFIX = "uploaded"

    # Model template
    MODEL_TEMPLATE_FILES_DIR = "./nhs_app/machine_learning/template_for_download/"
    MODEL_TEMPLATE_ZIP_SAVE = "./nhs_app/machine_learning/"


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
