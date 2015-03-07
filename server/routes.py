from __init__ import app, db
from subprocess import call
from models import User, Data

from flask import request
from flask import abort
from flask import jsonify
from flask import json

@app.route('/register', methods=['POST'])
def register():
    if not request.json or not 'guid' in request.json:
        abort(400) # Malformed Packet
    guid = request.json['guid']
    user = User(guid)

    alreadyCreatedUser = User.query.filter_by(id=user.id).first()

    if alreadyCreatedUser:
        abort(403) # User already created

    db.session.add(user)
    db.session.commit()

    registerObject = {
    'id': user.id
    }

    return jsonify(registerObject), 201

@app.route('/phone', methods=['POST'])
def phone():
    if not request.json or (not ('call-time' in request.json)) or (not ('id' in request.json)):
        abort(400) # Malformed Packet

    user = User.query.filter_by(id=request.json["id"]).first()

    if not user: #Check database for id to make sure it exists
        abort(401)

    seconds = request.json['call-time']
    user.add_phone_seconds(seconds)

    d = Data.query.all().first()
    d.incrementButtonClicks()
    db.session.add(d)

    db.session.add(user)
    db.session.commit()

    return "", 200

@app.route('/msg_to', methods=['POST'])
def msg_to():
    if not request.json or (not ('phone_number' in request.json)) or (not ('id' in request.json)):
        abort(400) # Malformed Packet

    user = User.query.filter_by(id=request.json["id"]).first()

    if not user: #Check database for id to make sure it exists
        abort(401)

    user.increment_messages()

    d = Data.query.all().first()
    d.incrementButtonClicks()
    db.session.add(d)

    db.session.add(user)
    db.session.commit()

    return "", 200

@app.route('/donate', methods=['POST'])
def msg_to():
    if not request.json or (not ('donated' in request.json)) or (not ('id' in request.json)):
        abort(400) # Malformed Packet

    user = User.query.filter_by(id=request.json["id"]).first()

    if not user: #Check database for id to make sure it exists
        abort(401)

    user.add_donation(request.json['donated'])

    d = Data.query.all().first()
    d.incrementButtonClicks()
    db.session.add(d)

    db.session.add(user)
    db.session.commit()

    return "", 200

@app.route("/mail", methods=['POST'])
def mail():
    if not request.json or (not ('id' in request.json)):
        abort(400) # Malformed Packet

    user = User.query.filter_by(id=request.json["id"]).first()

    if not user: #Check database for id to make sure it exists
        abort(401)

    user.increment_mail()

    d = Data.query.all().first()
    d.incrementButtonClicks()
    db.session.add(d)

    db.session.add(user)
    db.session.commit()

    return "", 200

@app.route('/get_data', methods=['GET'])
def get_data():
    return json.dumps([user.serialize() for user in User.query.all()]), 200

@app.route('/')
def landing_page():
    return 'Nothing seems to be here'

@app.route('/update-server', methods=['GET', 'POST'])
def update():
    call(["git pull"], shell=True)
    return 'Success!'

@app.route('/remake-database', methods=['GET', 'POST'])
def remake():
    call(["rm webapp.db"], shell=True)
    db.create_all()
    return 'Database Remade!'
