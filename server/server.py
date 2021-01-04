import json
from flask import Flask, request, jsonify

app = Flask(__name__)


def fetchId():
    with open('data.txt','r') as f:
        data = f.read()
        records = json.loads(data)
        return int(records[-1]['id']) + 1

ID = fetchId()

@app.route('/')
def index():
    print(ID)
    return jsonify({'msg': 'hello world'})


@app.route('/api/watchlist', methods=['GET'])
def query_records():
    with open('data.txt', 'r') as f:
        data = f.read()
        records = json.loads(data)
        return jsonify(records)


@app.route('/api/watchlist/<id>', methods=['GET'])
def query_by_id(id):
    id = int(request.view_args['id'])
    with open('data.txt', 'r') as f:
        data = f.read()
        records = json.loads(data)
        for r in records:
            if int(r['id']) == id:
                return jsonify(r)
        return jsonify({'error': 'no such record'})


@app.route('/api/watchlist', methods=['POST'])
def create_record():
    global ID
    record = json.loads(request.data)
    record['id'] = str(ID)
    ID += 1
    with open('data.txt', 'r') as f:
        data = f.read()
    if not data:
        records = [record]
    else:
        records = json.loads(data)
        records.append(record)
    with open('data.txt', 'w') as f:
        f.write(json.dumps(records, indent=2))
    record['id'] = int(record['id'])
    return jsonify(record)


@app.route('/api/watchlist', methods=['PUT'])
def update_record():
    record = json.loads(request.data)
    new_records = []
    with open('data.txt', 'r') as f:
        data = f.read()
        records = json.loads(data)
    for r in records:
        if int(r['id']) == record['id']:
            r['title'] = record['title']
            r['director'] = record['director']
            r['year'] = record['year']
            r['rating'] = record['rating']
        new_records.append(r)
    with open('data.txt', 'w') as f:
        f.write(json.dumps(new_records, indent=2))
    return jsonify(record)


@app.route('/api/watchlist/<id>', methods=['DELETE'])
def delete_record(id):
    id = int(request.view_args['id'])
    new_records = []
    with open('data.txt', 'r') as f:
        data = f.read()
        records = json.loads(data)
        for r in records:
            if int(r['id']) == id:
                continue
            new_records.append(r)
    with open('data.txt', 'w') as f:
        f.write(json.dumps(new_records, indent=2))
    return jsonify()


app.run()
