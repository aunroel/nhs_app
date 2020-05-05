import requests
from getpass import getpass
import json
import h5py
import numpy

def download_data(includeSupportCode = False, json_save_path = None):

  print("### Data downloader ###")

  print("Please insert your credentials")
  username = input("username: ")
  password = getpass("password: ")

  url = "http://localhost:5000/api/models/data"
  body = {'username': username, 'password': password}

  # sending get request and saving the response as response object
  r = requests.get(url=url, json=body)

  # extracting data in json format
  data = r.json()['data']

  if not includeSupportCode:
    def removeSuppportCode(dataPoint):
      del dataPoint['supportCode']
      return dataPoint

    data = [removeSuppportCode(d) for d in data]

  if json_save_path:
    with open(json_save_path, "w") as json_file:
        json_file.write(json.dumps(data))

  return data


def load_from_json(path):
  with open(path) as json_file:
    data = json.load(json_file)
  return data


def format_data_for_training(data, postcode = None):
  if postcode:
    data = list(filter(lambda d: d['postcode'] == postcode, data))

    if len(data) == 0:
      raise ValueError(f'There are no entries for postcode "{postcode}"')

    print (f'There are {len(data)} entries for postcode "{postcode}"')

  features = [
    [ 
      d['weeklyCalls'],
      d['weeklySteps'],
      d['wellBeingScore']
    ] 
    for d in data
  ]

  labels = [d['errorRate'] for d in data]

  return features, labels


def save_model_to_h5(model, history):
  s = model.get_config()

  model_json = model.to_json()
  with open("model.json", "w") as json_file:
      json_file.write(model_json)

  model.summary()

  filename = 'template_model.h5'
  model.save(filename)

  f = h5py.File(filename, 'r+')

  training_history = history.history
  for key in list(training_history.keys()):
    vals = training_history[key] 

    if len(vals) > 0 and type(vals[0]) == numpy.float32:
      training_history[key] = \
        [val.astype(float) for val in training_history[key]]


  history_to_save = json.dumps({
    "epoch": history.epoch,
    "history": training_history,
    "params": history.params
  })

  f.create_dataset("history", data=history_to_save)



