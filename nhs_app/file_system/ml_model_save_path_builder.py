"""
Module to build paths and filenames for saving TF and TF Lite models. It allows 
for maintaining consistency in save paths of TF models and their Lite 
equivalents. 
"""

from app import config
from datetime import datetime
from werkzeug.utils import secure_filename

uploaded_save_dir = config['UPLOADED_MODELS_DIR']
tflite_save_dir = config['TFLITE_MODELS_DIR']
uploaded_filename_prefix = config['UPLOADED_MODEL_FILENAME_PREFIX']


def file_format_is_h5(file):
    return file.filename.find(".h5", -3) != -1


def build_uploaded_model_file_name(org_filename):
    """
        converts 'name.h5' -> 'prefix_name_date.h5'
    """

    filename = uploaded_filename_prefix + "_" + secure_filename(org_filename)

    date_str = get_date_string()
    filename = filename[:-3] + "_" + date_str + '.h5'

    return filename

def get_date_string():
    return datetime.now().strftime("%d%m%Y_%H%M%S")
