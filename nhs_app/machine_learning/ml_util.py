from enum import Enum
from nhs_app.database.main_data_model import MainData
import matplotlib.pyplot as plt
from datetime import datetime
import os
import pandas as pd
import seaborn as sns
import tensorflow as tf
from tensorflow import keras
import tensorflow_docs as tfdocs
import tensorflow_docs.plots
import tensorflow_docs.modeling
from nhs_app.database.uploaded_model import UploadedModelMeta
from nhs_app.file_system.ml_model_filename_builder import change_ext_from_h5_to_tflite


class FeatureFlag(Enum):
    STEPS_CALLS_MSGS = 3
    DEFAULT = 4,
    INCLUDE_ALL = 5


class ML:
    # matplotlib crashes python in dev mode if this is not set on Mac
    plt.switch_backend('Agg')

    def __init__(self, postcode=None):
        self.raw_data = []
        self.df_data = None
        self.model = None
        self.national = True if postcode is None else False
        self.postcode = postcode
        self.train_ds = None
        self.test_ds = None
        self.train_stats = None
        self.train_labels = None
        self.test_labels = None
        self.normed_train_data = None
        self.normed_test_data = None
        self.directory = './models/national' if postcode is None else './models/local'

    def verify_or_create_dir(self, folder, date):
        if not os.path.exists(f'{self.directory}/{folder}/{date}'):
            os.makedirs(f'{self.directory}/{folder}/{date}')

    def get_data_as_list(self):
        if self.national:
            self.raw_data = MainData.find_all()
        else:
            self.raw_data = MainData.find_all_by_postcode(self.postcode)
            
        return self

    def convert_to_df(self, features=FeatureFlag.DEFAULT):
        if features == FeatureFlag.STEPS_CALLS_MSGS:
            self.df_data = pd.DataFrame().from_records(
                [s.calls_steps_score() for s in self.raw_data])
            self.df_data.columns = ['wellBeingScore', 'weeklySteps', 'weeklyCalls']

        if features == FeatureFlag.DEFAULT:
            self.df_data = pd.DataFrame().from_records(
                [s.calls_steps_score_errors() for s in self.raw_data])
            self.df_data.columns = ['wellBeingScore', 'weeklySteps', 'weeklyCalls', 'errorRate']

        if features == FeatureFlag.INCLUDE_ALL:
            self.df_data = pd.DataFrame().from_records(
                [s.all_features() for s in self.raw_data])
            self.df_data.columns = ['wellBeingScore', 'supportCode', 'weeklySteps',
                                    'weeklyCalls', 'errorRate', 'postCode']

    def specify_train(self, train=0.8):
        self.train_ds = self.df_data.sample(frac=train, random_state=0)
        self.test_ds = self.df_data.drop(self.train_ds.index)

        # Create and save data metrics graphs
        plt.figure(figsize=(11, 11))
        plt.subplots_adjust(top=0.9)
        sns.pairplot(self.train_ds[self.df_data.columns], diag_kind='kde', height=4)

        date_string = datetime.now().strftime('%d_%m_%y')
        time_string = datetime.now().strftime('%H%M%S')

        if self.national:
            self.verify_or_create_dir('graphs', date_string)
            figure_path = f'{self.directory}/graphs/{date_string}/{time_string}.png'
        else:
            self.verify_or_create_dir(f'graphs/{self.postcode}', date_string)
            figure_path = f'{self.directory}/graphs/{self.postcode}/{date_string}/{time_string}.png'

        plt.savefig(figure_path)
        plt.clf()
        plt.close('all')

    def stats(self):
        self.train_stats = self.train_ds.describe()
        self.train_stats.pop('wellBeingScore')
        self.train_stats = self.train_stats.transpose()
        self.train_labels = self.train_ds.pop('wellBeingScore')
        self.test_labels = self.test_ds.pop('wellBeingScore')

    def norm(self):
        self.normed_train_data = (self.train_ds - self.train_stats['mean']) / self.train_stats['std']
        self.normed_test_data = (self.test_ds - self.train_stats['mean']) / self.train_stats['std']

    def define_model(self):
        layers = len(self.df_data.columns)

        self.model = keras.Sequential()
        self.model.add(keras.layers.Dense(layers, activation='linear', input_shape=[len(self.train_ds.keys())]))
        self.model.add(keras.layers.Dense(1, activation='linear'))

        optimizer = keras.optimizers.RMSprop(0.001)

        self.model.compile(loss='mse', optimizer=optimizer, metrics=['mae', 'mse'])

    def train_model(self):
        EPOCHS = 1000

        early_stop = keras.callbacks.EarlyStopping(monitor='val_loss', patience=10)

        early_history = self.model.fit(self.normed_train_data, self.train_labels,
                                       epochs=EPOCHS, validation_split=0.2, verbose=0,
                                       callbacks=[early_stop, tfdocs.modeling.EpochDots()])

        # create and save training graphs
        plotter = tfdocs.plots.HistoryPlotter()
        plotter.plot({'Early Stopping': early_history}, metric="mae")

        plt.ylim([0, 10])
        plt.ylabel('MAE [MPG]')
        date_string = datetime.now().strftime('%d_%m_%y')
        time_string = datetime.now().strftime('%H%M%S')

        if self.national:
            self.verify_or_create_dir('results', date_string)
            figure_path = f"{self.directory}/results/{date_string}"
        else:
            self.verify_or_create_dir(f'results/{self.postcode}', date_string)
            figure_path = f'{self.directory}/results/{self.postcode}/{date_string}'

        plt.savefig(f'{figure_path}/mae_{time_string}.png')
        plt.clf()

        plotter.plot({'Early Stopping': early_history}, metric="mse")
        plt.ylim([0, 20])
        plt.ylabel('MSE [MPG^2]')
        plt.savefig(f'{figure_path}/mse_{time_string}.png')
        plt.clf()
        plt.close('all')

    def refresh_model(self):
        self.get_data_as_list()
        self.convert_to_df()
        self.specify_train()
        self.stats()
        self.norm()
        self.define_model()
        self.train_model()
        self.predict()

        loss, mae, mse = self.model.evaluate(
            self.normed_test_data, self.test_labels, verbose=2)

        return {'loss': loss, 'mae': mae, 'mse': mse}

    def predict(self):
        test_predictions = self.model.predict(self.normed_test_data).flatten()

        a = plt.axes(aspect='equal')
        plt.scatter(self.test_labels, test_predictions)
        plt.xlabel('True Values [MPG]')
        plt.ylabel('Predictions [MPG]')
        lims = [0, 50]
        plt.xlim(lims)
        plt.ylim(lims)
        _ = plt.plot(lims, lims)

        date_string = datetime.now().strftime('%d_%m_%y')
        time_string = datetime.now().strftime('%H%M%S')

        if self.national:
            self.verify_or_create_dir('results', date_string)
            figure_path = f'{self.directory}/results/{date_string}'
        else:
            self.verify_or_create_dir(f'results/{self.postcode}', date_string)
            figure_path = f'{self.directory}/results/{self.postcode}/{date_string}'

        plt.savefig(f'{figure_path}/predictions_{time_string}.png')
        plt.clf()

        error = test_predictions - self.test_labels
        plt.hist(error, bins=25)
        plt.xlabel("Prediction Error [MPG]")
        _ = plt.ylabel("Count")
        plt.savefig(f'{figure_path}/error_distribution_{time_string}.png')
        plt.clf()
        plt.close('all')

    def load(self, path):
        self.model = tf.keras.models.load_model(path)
        return self

    def load_and_convert_to_lite(self, filename, load_directory, save_directory):
        self.load(load_directory + filename)

        converter = tf.lite.TFLiteConverter.from_keras_model(self.model)
        tflite_model = converter.convert()
        filename = change_ext_from_h5_to_tflite(filename)

        open(save_directory + filename, 'wb').write(tflite_model)

    def save_default_and_convert(self):
        date_string = datetime.now().strftime('%d_%m_%y')

        if self.national:
            self.verify_or_create_dir('models/', date_string)
            path = f'{self.directory}/models/{date_string}/'
            filename = f'{path}default_model_{datetime.now().strftime("_%H%M%S")}'

        else:
            self.verify_or_create_dir(f'models/{self.postcode}', date_string)
            path = f'{self.directory}/models/{self.postcode}/{date_string}/'
            filename = f'{path}default_model_{datetime.now().strftime("_%H%M%S")}'

        self.model.save(filename + '.h5')

        converter = tf.lite.TFLiteConverter.from_keras_model(self.model)
        tflite_model = converter.convert()

        if self.national:
            self.verify_or_create_dir('lite/', date_string)
            open(f'{self.directory}/lite/{date_string}/default_model_' + \
                 datetime.now().strftime("_%H%M%S") + '.tflite', 'wb').write(tflite_model)

            if os.path.exists(f'{self.directory}/lite/default_model.tflite'):
                os.remove(f'{self.directory}/lite/default_model.tflite')
            open(f'{self.directory}/lite/default_model.tflite', 'wb').write(tflite_model)
        else:
            self.verify_or_create_dir('lite/' + self.postcode, date_string)
            open(f'{self.directory}/lite/{self.postcode}/{date_string}/default_model_' + \
                 datetime.now().strftime("_%H%M%S") + '.tflite', 'wb').write(tflite_model)

            if os.path.exists(f'{self.directory}/lite/{self.postcode}/default_model.tflite'):
                os.remove(f'{self.directory}/lite/{self.postcode}/default_model.tflite')
            open(f'{self.directory}/lite/{self.postcode}/default_model.tflite', 'wb').write(tflite_model)
