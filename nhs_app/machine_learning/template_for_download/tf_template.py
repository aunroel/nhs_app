import json

import tensorflow as tf
from tensorflow import keras
import tensorflow_docs as tfdocs
import tensorflow_docs.plots
import tensorflow_docs.modeling
from sklearn.model_selection import train_test_split

from model_storage_manager import download_data, \
  load_from_json, format_data_for_training, save_model_to_h5


"""
Data format
{
    "errorRate": 26,
    "weeklyCalls": 15,
    "weeklySteps": 1610,
    "wellBeingScore": 8,
    "postCode": "NP3",
    "supportCode": "Physio"   # optional, discarded by default
}
"""
# ------------------ Load data --------------------

# ------ option 1 ------
# Download data from server each time
data = download_data()

# ------ option 2 ------
# Download data from server once, save to json, read the json
#
# First download data once:  
# download_data(json_save_path="data.json")
# Then
# data = load_from_json("data.json")



# ----------- Format data for training ------------

X, y = format_data_for_training(data, postcode=None)

X_train, X_test, y_train, y_test = train_test_split(
  X, y, test_size=0.33, random_state=42
)

X_train, X_validate, y_train, y_validate = train_test_split(
  X_train, y_train, test_size = 0.2, random_state=42
)


# ------------------ Create model ------------------

model = keras.Sequential([
    keras.layers.Dense(1, activation='linear', input_shape=[3]),
])

optimizer = tf.keras.optimizers.RMSprop(0.001)

model.compile(loss='mse',
            optimizer=optimizer,
            metrics=['mae', 'mse'])



# -------------------- Train ---------------------

EPOCHS = 10
history = model.fit(
  X_train, y_train,
  epochs=EPOCHS, verbose=0,
  validation_data = (X_validate, y_validate),
  callbacks=[tfdocs.modeling.EpochDots()])

model.evaluate(X_test, y_test)



# ------------ Save params and results -------------
save_model_to_h5(model, history)
