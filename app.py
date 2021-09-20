import numpy as np
from flask import Flask, request, jsonify
import werkzeug
from keras.models import load_model
from keras.preprocessing.image import load_img, img_to_array

app = Flask(__name__)

print('loading saved artifacts')
classes = labels = ['Type 1', 'Type 2', 'Type 3']
model = load_model('cancer_screen_model.h5')


def preprocess(imagefile):
    filename = werkzeug.utils.secure_filename(imagefile.filename)
    print("\nReceived image File name : " + filename)
    img_path = './data/raw/test_images/' + filename
    imagefile.save(img_path)
    img = load_img(img_path, target_size= (180, 180))
    img_arr = img_to_array(img)/255.0
    img_arr = img_arr.reshape(1, 180, 180, 3)
    return img_arr


def classify_img(image_file):
    print('processing image...')
    img = preprocess(image_file)
    print('classifying image...')
    probability = np.round(model.predict(img)[0], 2)
    print("rounded probability", probability)
    prediction = labels[np.argmax(probability)]
    print('getting results...')
    result = {'class': prediction, 'probability': probability.tolist()}
    print(result)
    return result


# server to get image file
# and return prediction result
@app.route('/classify', methods=['GET', 'POST'])
def handle_request():
    image_file = request.files['image']

    response = jsonify(classify_img(image_file))

    response.headers.add('Access-Control-Allow-Origin', '*')
    return response


if __name__ == '__main__':
    print('Starting python flask server')
    app.run(host="0.0.0.0", port=5000, debug=True)