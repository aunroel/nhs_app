import os
import unittest

from app import app, db
from config import basedir
from nhs_app.database.user_model import User
from nhs_app.database.mobile_model import Node
from nhs_app.database.main_data_model import MainData
from nhs_app.database.update_aggregator_model import UpdateAggregator
from nhs_app.machine_learning.ml_util import ML

TEST_DB = 'test.db'


class ProjectTests(unittest.TestCase):
    """ Setup and teardown """

    # executed prior each test
    def setUp(self):
        app.config['TESTING'] = True
        app.config['WTF_CSRF_ENABLED'] = False
        app.config['DEBUG'] = False
        app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///' + \
                                                os.path.join(basedir, TEST_DB)
        self.app = app.test_client()
        db.drop_all()
        db.create_all()

        admin = User(username='admin', email='test123@gmail.com', user_type=True, password='admin')
        guest = User(username='guest', email='test23@gmail.com', user_type=False, password='guest')
        admin.save_to_db()
        guest.save_to_db()

        node = Node(uid='12345')
        node.save_to_db()

        data1 = MainData('Therapist', 8, 23141, 87, 2, 'NP44')
        data2 = MainData('Physio', 3, 12312, 11, 2, 'SA47')
        data3 = MainData('GP', 3, 3123, 2, 2, 'NP44')
        data4 = MainData('Care Network', 3, 1231, 13, 3, 'SA47')
        data5 = MainData('Therapist', 5, 12341, 54, 1, 'NP44')
        data6 = MainData('Therapist', 2, 312, 15, 3, 'NP44')
        data7 = MainData('Physio', 4, 8009, 12, 3, 'SA47')
        data8 = MainData('Therapist', 5, 12543, 56, 2, 'NP44')
        data9 = MainData('GP', 6, 15423, 87, 1, 'NP44')
        data10 = MainData('Care Network', 7, 27643, 153, 2, 'SA47')

        data1.save_to_db()
        data2.save_to_db()
        data3.save_to_db()
        data4.save_to_db()
        data5.save_to_db()
        data6.save_to_db()
        data7.save_to_db()
        data8.save_to_db()
        data9.save_to_db()
        data10.save_to_db()

        agg1 = UpdateAggregator('Therapist', 8, 23141, 87, 2, 'NP44')
        agg2 = UpdateAggregator('Physio', 3, 12312, 11, 2, 'SA47')
        agg3 = UpdateAggregator('GP', 3, 3123, 2, 2, 'NP44')
        agg4 = UpdateAggregator('Care Network', 3, 1231, 13, 3, 'SA47')
        agg5 = UpdateAggregator('Therapist', 5, 12341, 54, 1, 'NP44')
        agg6 = UpdateAggregator('Therapist', 2, 312, 15, 3, 'NP44')
        agg7 = UpdateAggregator('Physio', 4, 8009, 12, 3, 'SA47')
        agg8 = UpdateAggregator('Therapist', 5, 12543, 56, 2, 'NP44')
        agg9 = UpdateAggregator('GP', 6, 15423, 87, 1, 'NP44')
        agg10 = UpdateAggregator('Care Network', 7, 27643, 153, 2, 'SA47')

        agg1.save_to_db()
        agg2.save_to_db()
        agg3.save_to_db()
        agg4.save_to_db()
        agg5.save_to_db()
        agg6.save_to_db()
        agg7.save_to_db()
        agg8.save_to_db()
        agg9.save_to_db()
        agg10.save_to_db()

    def tearDown(self):
        db.session.remove()
        db.drop_all()

    """ Helper methods """

    def register(self, email, password, confirm):
        return self.app.post(
            '/register',
            data=dict(username='dummy', email=email, password=password, confirm=confirm),
            follow_redirects=True
        )

    def login(self, username, password):
        return self.app.post(
            '/login',
            data=dict(username=username, password=password),
            follow_redirects=True
        )

    def logout(self):
        return self.app.get(
            '/logout',
            follow_redirects=True
        )

    """ Tests """

    def test_homepage(self):
        response = self.app.get('/', follow_redirects=True)
        self.assertEqual(response.status_code, 200)

    def test_invalid_user_registration(self):
        response = self.register('test123@gmail.com', '123', '234')
        self.assertEqual(response.status_code, 406)

    def test_valid_login(self):
        response = self.login('admin', 'admin')
        self.assertEqual(response.status_code, 200)
        self.assertIn(b'API Doc', response.data)

    def test_invalid_login(self):
        response = self.login('qwert', 'qwerte')
        self.assertEqual(response.status_code, 200)
        self.assertIn(b'Invalid username or password', response.data)

    def test_valid_node_register(self):
        response = self.app.post('/node', data=dict(uid='easdasgf32423'), follow_redirects=True)
        self.assertEqual(response.status_code, 201)

    def test_duplicate_node_register(self):
        response = self.app.post('/node', data=dict(uid='12345'), follow_redirects=True)
        self.assertEqual(response.status_code, 403)
        self.assertIn(b'Registration denied', response.data)

    def test_logout(self):
        response = self.logout()
        self.assertEqual(response.status_code, 200)
        self.assertNotIn(b'API Doc', response.data)

    def test_valid_update_from_device(self):
        response = self.app.post(
            '/update/12345',
            data=dict(supportCode='Therapist', wellBeingScore='5',
                      weeklySteps='23423', weeklyCalls='124', errorRate='1', postCode='NP4'),
            follow_redirects=True
        )
        self.assertEqual(response.status_code, 201)
        self.assertIn(b'ok', response.data)

    def test_malformed_update_from_device(self):
        response = self.app.post(
            '/update/12345',
            data=dict(code='Therapist', score='5',
                      steps='23423', calls='124', rate='1', post_code='NP4'),
            follow_redirects=True
        )
        self.assertEqual(response.status_code, 400)

    def test_invalid_update_from_device(self):
        response = self.app.post(
            '/update/4123',
            data=dict(supportCode='Therapist', wellBeingScore='5',
                      weeklySteps='23423', weeklyCalls='124', errorRate='1', postCode='NP4'),
            follow_redirects=True
        )
        self.assertEqual(response.status_code, 403)
        self.assertIn(b'App is not registered', response.data)

    def test_logged_in_doc_availability(self):
        self.login('admin', 'admin')
        response = self.app.get('/doc', follow_redirects=True)
        self.assertEqual(response.status_code, 200)
        self.assertIn(b'Ask the server whether a tflite model is ready for download', response.data)

    def test_doc_inavailability(self):
        response = self.app.get('/doc', follow_redirects=True)
        self.assertEqual(response.status_code, 200)
        self.assertIn(b'Please log in to access this page', response.data)

    def test_logged_in_dashboard_availability(self):
        self.login('admin', 'admin')
        response = self.app.get('/dashboard', follow_redirects=True)
        self.assertEqual(response.status_code, 200)
        self.assertIn(b'You need to enable JavaScript to run this app.', response.data)

    def test_dashboard_inavailability(self):
        response = self.app.get('/dashboard', follow_redirects=True)
        self.assertEqual(response.status_code, 200)
        self.assertIn(b'Please log in to access this page', response.data)

    def test_unauthorized_national_ml_download(self):
        response = self.app.post('/model', data=dict(uid='fsadg'), follow_redirects=True)
        self.assertEqual(response.status_code, 403)
        self.assertIn(b'Access denied', response.data)

    def test_unauthorized_local_ml_download(self):
        response = self.app.post('/local_model/NP44', data=dict(uid='fsadg'), follow_redirects=True)
        self.assertEqual(response.status_code, 403)
        self.assertIn(b'Access denied', response.data)

    def test_invalid_local_ml_download(self):
        response = self.app.post('/local_model/GC331', data=dict(uid='12345'), follow_redirects=True)
        self.assertEqual(response.status_code, 403)
        self.assertIn(b'No such postcode', response.data)

    def test_unauthorized_uploaded_ml_download(self):
        response = self.app.post('/uploaded_model', data=dict(uid='fsadg'), follow_redirects=True)
        self.assertEqual(response.status_code, 403)
        self.assertIn(b'Access denied', response.data)

    def test_local_ml_invalid_postcode(self):
        self.login('admin', 'admin')
        response = self.app.get('/local_available/zzz', follow_redirects=True)
        self.assertEqual(response.status_code, 403)
        self.assertIn(b'No such postcode', response.data)

    def test_flush(self):
        self.assertEqual(10, len(UpdateAggregator.find_all()))
        self.assertEqual(10, len(MainData.find_all()))
        response = self.app.get('/flush_test')
        self.assertEqual(response.status_code, 200)
        self.assertIn(b'success', response.data)
        self.assertEqual(0, len(UpdateAggregator.find_all()))
        self.assertEqual(20, len(MainData.find_all()))

    def test_ml(self):
        ml = ML()
        ml.get_data_as_list()

        self.assertEqual(10, len(ml.raw_data))

        ml.convert_to_df()

        self.assertEqual(10, len(ml.df_data))

        ml.train_ds = ml.df_data.sample(frac=0.8, random_state=0)
        ml.test_ds = ml.df_data.drop(ml.train_ds.index)
        ml.stats()
        ml.norm()

        self.assertTrue(ml.normed_train_data is not None)
        self.assertTrue(ml.normed_test_data is not None)

        ml.define_model()

        self.assertTrue(ml.model)


if __name__ == '__main__':
    unittest.main()