from flask import Flask, request, json

app = Flask(__name__)


@app.route('/')
def index():
    return '<p>Hello, World!</p>'

@app.route('/recommend', methods=['POST'])
def getRecommend2():  # put application's code here
    print("들어옴")

    print(request.is_json)
    data = request.get_json()
    print(data['surveyRegistReq'])
    print(data['flaskJejuPlaceItemList'])

    arr = [1, 2, 3]
    # return data
    return arr

if __name__ == '__main__':
    app.run(host='0.0.0.0')
