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

    a = User.query.filter_by(id=request.json["id"]).first()
    a.incCall()
    db.session.add(a)

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

    a = User.query.filter_by(id=request.json["id"]).first()
    a.incMessage()
    db.session.add(a)

    db.session.add(user)
    db.session.commit()

    return "", 200

@app.route('/donate', methods=['POST'])
def donate():
    if not request.json or (not ('donated' in request.json)) or (not ('id' in request.json)):
        abort(400) # Malformed Packet

    user = User.query.filter_by(id=request.json["id"]).first()

    if not user: #Check database for id to make sure it exists
        abort(401)

    user.add_donation(request.json['donated'])

    d = Data.query.first()
    d.incDonate()
    db.session.add(d)

    a = User.query.filter_by(id=request.json["id"]).first()
    a.incDonate()
    db.session.add(a)

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

    d = Data.query.first()
    d.incMail()
    db.session.add(d)

    a = User.query.filter_by(id=request.json["id"]).first()
    a.incMail()
    db.session.add(a)

    db.session.add(user)
    db.session.commit()

    return "", 200

@app.route('/click_data', methods=['GET'])
def clickData():
    d = Data.query.first()
    donate = d.getDonate()
    call = d.getCall()
    message = d.getMessage()
    mail = d.getMail()
    return jsonify(call=call,post=message,donate=donate,mail=mail), 200

@app.route('/user_click_data', methods=['POST'])
def user_click_data():

    if not request.json or (not ('id' in request.json)):
        abort(400) # Malformed Packet

    d = User.query.filter_by(id=request.json["id"]).first()
    if not d:
        abort(401)

    id = d.getID()
    donate = d.getDonate()
    call = d.getCall()
    message = d.getMessage()
    mail = d.getMail()

    return jsonify(id=id, call=call, post=message, donate=donate, mail=mail), 200

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
    from db import incClick
    incClick()
    return 'Database Remade!'

