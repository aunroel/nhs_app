import pandas as pd
from nhs_app.models.main_data_model import MainData


class ML:

    def __init__(self):
        self.data = []
        self.model = None

    @staticmethod
    def get_data_as_json_list():
        return  MainData.find_all()

    def convert_to_dataframe(self, data_list):
        dataFrame = pd.DataFrame()
        for entry in data_list:
            supportCode = entry['supportCode']
            wellBeingScore = entry['wellBeingScore']
            weeklySteps = entry['weeklySteps']
            weeklyCalls = entry['weeklyCalls']
            errorRate = entry['errorRate']
            postCode = entry['postCode']

