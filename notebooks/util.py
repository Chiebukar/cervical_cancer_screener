import numpy as np
from keras.preprocessing import image
import os
from keras.models import load_model
from tensorflow import lite

model = load_model('cancer_screen_model.h5')
converter = lite.TFLiteConverter.from_keras_model(model)
tfmodel= converter.convert()
open('cancer_screen_model.tflite', 'wb').write(tfmodel)

# load sample image for testing
def loadImage(filepath):
  test_img = image.load_img(filepath, target_size = (180,180))
  test_img = image.img_to_array(test_img)
  test_img = np.expand_dims(test_img, axis= 0)
  test_img /= 255
  return test_img

test_path = './test_images/'
filename = '0type1.jpg'

labels = ['Type 1', 'Type 2', 'Type 3']

for filename in os.listdir(test_path):
    truth = filename.split('.')[1]
    filepath = os.path.join(test_path, filename)
    test_X = loadImage(filepath)
    probability = model.predict(test_X)[0]
    predicted = np.argmax(probability)
    prediction = labels[predicted]
    print(f'Actual: {truth}; Predicted: {prediction}')
    print(np.round(probability, 2))



