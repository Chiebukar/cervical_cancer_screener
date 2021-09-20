from flask import Flask, request, jsonify, render_template
import _json
import numpy as np
from keras.models import load_model
from base64 import b64decode
import cv2

app = Flask(__name__)

print('loading saved artifacts')
classes = labels = ['Type 1', 'Type 2', 'Type 3']
model = load_model('cancer_screen_model.h5')


def get_img_from_base64_img(base64_img):
    # with open(base64_file) as f:
    #     base64_img = f.read()
    data = base64_img.split(',')[1]
    nparr = np.frombuffer(b64decode(data), np.uint8)
    img = cv2.imdecode(nparr, cv2.IMREAD_COLOR)
    return img


def preprocess(base64_img):
    img_arr = get_img_from_base64_img(base64_img)
    img_arr = cv2.resize(img_arr, (180, 180))
    img_arr = np.expand_dims(img_arr, axis=0)
    img_arr = img_arr/ 255
    return img_arr


def classify_img(base64_img):
    print('classifying image')
    feature = preprocess(base64_img)
    probability = model.predict(feature)[0]
    prediction = labels[np.argmax(probability)]
    result = {'class': prediction, 'probability': probability.tolist()}
    return result


# print(classify_img('./test_images/1020type3.txt'))

# connect to html
@app.route('/')
def home():
    return render_template('app.html')


@app.route('/classify_image', methods=['GET', 'POST'])
def predict_home_price():
    image_data = request.form['image_data'] # image data is b64encoded image

    response = jsonify(classify_img(image_data))

    response.headers.add('Access-Control-Allow-Origin', '*')
    return response


if __name__ == '__main__':
    print('Starting python flask server')
    app.run(port=5000, debug=True)




