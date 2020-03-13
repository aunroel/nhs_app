from enum import Enum
from nhs_app.models.main_data_model import MainData
import matplotlib.pyplot as plt
from datetime import datetime
import numpy as np
import pandas as pd
import seaborn as sns
import tensorflow as tf
from tensorflow import keras
import tensorflow_docs as tfdocs
import tensorflow_docs.plots
import tensorflow_docs.modeling


class FeatureFlags(Enum):
    DEFAULT = 3,
    INCLUDE_ERROR_RATE = 4,
    INCLUDE_ALL = 5


class ML:

    def __init__(self):
        self.raw_data = []
        self.df = None
        self.model = None
        self.train_ds = None
        self.test_ds = None
        self.train_stats = None
        self.train_labels = None
        self.test_labels = None
        self.normed_train_data = None
        self.normed_test_data = None

    def get_data_as_json_list(self):
        self.raw_data = MainData.find_all()

    def convert_to_df(self, features=FeatureFlags.DEFAULT):
        if features == FeatureFlags.DEFAULT:
            self.df = pd.DataFrame().from_records(
                [s.calls_steps_score() for s in self.raw_data])
            self.df.columns = ['wellBeingScore', 'weeklySteps', 'weeklyCalls']

        if features == FeatureFlags.INCLUDE_ERROR_RATE:
            self.df = pd.DataFrame().from_records(
                [s.calls_steps_score_errors() for s in self.raw_data])
            self.df.columns = ['wellBeingScore', 'weeklySteps', 'weeklyCalls', 'errorRate']

        if features == FeatureFlags.INCLUDE_ALL:
            self.df = pd.DataFrame().from_records(
                [s.all_features() for s in self.raw_data])
            self.df.columns = ['wellBeingScore', 'supportCode', 'weeklySteps',
                               'weeklyCalls', 'errorRate', 'postCode']

    def specify_train(self, train=0.8):
        self.train_ds = self.df.sample(frac=train, random_state=0)
        self.test_ds = self.df.drop(self.train_ds.index)

        plt.figure(figsize=(10, 10))
        plt.subplots_adjust(top=0.85)
        sns.pairplot(self.train_ds[['wellBeingScore', 'weeklySteps', 'weeklyCalls']]
                     , diag_kind='kde', height=4)
        dt_string = datetime.now().strftime('%d%m%y_%H%M%S')
        plt.savefig('models/graphs/graph_' + dt_string + '.png')
        plt.clf()

    def stats(self):
        self.train_stats = self.train_ds.describe()
        self.train_stats.pop('wellBeingScore')
        self.train_stats = self.train_stats.transpose()
        self.train_labels = self.train_ds.pop('wellBeingScore')
        self.test_labels = self.test_ds.pop('wellBeingScore')

    def norm(self):
        self.normed_train_data = (self.train_ds - self.train_stats['mean']) / self.train_stats['std']
        self.normed_test_data = (self.test_ds - self.train_stats['mean']) / self.train_stats['std']

    def build_model(self):
        self.model = keras.Sequential([
            keras.layers.Dense(64, activation='relu', input_shape=[len(self.train_ds.keys())]),
            keras.layers.Dense(64, activation='relu'),
            keras.layers.Dense(1)
        ])

        optimizer = keras.optimizers.RMSprop(0.001)

        self.model.compile(loss='mse', optimizer=optimizer, metrics=['mae', 'mse'])

    def train_model(self):
        EPOCHS = 1000

        history = self.model.fit(
            self.normed_train_data, self.train_labels,
            epochs=EPOCHS, validation_split=0.2, verbose=0,
            callbacks=[tfdocs.modeling.EpochDots()])

        plotter = tfdocs.plots.HistoryPlotter(smoothing_std=2)
        plotter.plot({'Basic': history}, metric='mae')
        plt.ylim([0, 10])
        plt.ylabel('MAE [MPG]')
        dt_string = datetime.now().strftime('%d%m%y_%H%M%S')
        plt.savefig('models/results/graph_basic_mae_' + dt_string + '.png')
        plt.clf()
        plotter.plot({'Basic': history}, metric="mse")
        plt.ylim([0, 20])
        plt.ylabel('MSE [MPG^2]')
        plt.savefig('models/results/graph_basic_mse_' + dt_string + '.png')
        plt.clf()

    def refresh_model(self):
        self.get_data_as_json_list()
        self.convert_to_df()
        self.specify_train()
        self.stats()
        self.norm()
        self.build_model()
        self.train_model()

    def updated_refresh(self):
        self.get_data_as_json_list()
        self.convert_to_df()
        self.specify_train()
        self.stats()
        self.norm()
        self.build_model()

        EPOCHS = 1000

        early_stop = keras.callbacks.EarlyStopping(monitor='val_loss', patience=10)

        early_history = self.model.fit(self.normed_train_data, self.train_labels,
                    epochs=EPOCHS, validation_split=0.2, verbose=0,
                    callbacks=[early_stop, tfdocs.modeling.EpochDots()])

        plotter = tfdocs.plots.HistoryPlotter(smoothing_std=2)
        plotter.plot({'Early Stopping': early_history}, metric="mae")
        plt.ylim([0, 10])
        plt.ylabel('MAE [MPG]')
        dt_string = datetime.now().strftime('%d%m%y_%H%M%S')
        plt.savefig('models/results/early_history_mae_' + dt_string + '.png')
        plt.clf()
        plotter.plot({'Early Stopping': early_history}, metric="mse")
        plt.ylim([0, 20])
        plt.ylabel('MSE [MPG^2]')
        plt.savefig('models/results/early_history_mse' + dt_string + '.png')
        plt.clf()

        loss, mae, mse = self.model.evaluate(
            self.normed_test_data, self.test_labels, verbose=2)

        return "Testing set Mean Abs Error: {:5.2f} MPG".format(mae)

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
        dt_string = datetime.now().strftime('%d%m%y_%H%M%S')
        plt.savefig('models/results/predictions_' + dt_string + '.png')
        plt.clf()

        error = test_predictions - self.test_labels
        plt.hist(error, bins=25)
        plt.xlabel("Prediction Error [MPG]")
        _ = plt.ylabel("Count")
        plt.savefig('models/results/error_distribution_' + dt_string + '.png')
        plt.clf()



