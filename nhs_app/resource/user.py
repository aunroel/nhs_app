from flask_restful import Resource
from nhs_app.models.user_model import User
from flask import render_template, flash, redirect, url_for, request
from flask_login import current_user, login_user, logout_user, login_required


class UserLogout(Resource):

    def get(self):
        logout_user()
        return redirect(url_for('index'))


class UserRegister(Resource):

    def post(self):
        if current_user.is_authenticated:
            return redirect(url_for('dashboard'))

        form = UserRegister()
        if form.validate_on_submit():
            user = User(username=form.username.data, email=form.email.data, user_type=False,
                        password=form.password.data)
            user.save_to_db()
            flash('Congratulations, registration completed')
            return redirect(url_for('login'))

        return render_template('register.html', form=form)
