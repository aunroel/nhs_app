from app import db
from nhs_app.database.main_data_model import MainData
from nhs_app.database.update_aggregator_model import UpdateAggregator
from nhs_app.machine_learning.ml_util import ML
import logging


def flush():
    logging.warning(f'\n---------------> Starting aggregator flushing.')

    keys = db.inspect(UpdateAggregator).columns.keys()
    # remove 'id' from the list of keys
    keys.pop(0)
    get_columns = lambda post: {key: getattr(post, key) for key in keys}

    updates = UpdateAggregator.find_all()
    db.session.bulk_insert_mappings(MainData, (get_columns(update) for update in updates))
    UpdateAggregator.delete_entries()
    db.session.commit()

    logging.warning(f'\n---------------> Finished aggregator flushing.')


def train_national():
    logging.warning(f'\n---------------> Starting national model training.')

    ml = ML()
    ml.refresh_model()
    ml.save_default_and_convert()

    logging.warning(f'\n---------------> Finished national model training.')


def train_locals():
    logging.warning(f'\n---------------> Started local model training.')

    postcodes = MainData.get_unique_postcodes()
    last15 = postcodes[-1:]
    for num, code in enumerate(last15, start=1):
        logging.warning(f'\n---------------> Processing postcode: {code} ({num} of {len(last15)})')
        ml = ML(code)
        ml.refresh_model()
        ml.save_default_and_convert()

    logging.warning(f'\n---------------> Finished local model training.')


def retrain():
    train_national()
    train_locals()
