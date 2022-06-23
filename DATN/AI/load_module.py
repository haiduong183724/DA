from socket import AI_ADDRCONFIG
from sys import flags
import face_recognition
import numpy as np
import cv2
def loadData():
# list label
    labels = [];
# list value
    data_train = [];
# Load module AI
    module_save = "modulesave.txt"
    label = "labels.txt"
    with open(label, "r") as f:
        labels = f.readline()[0:-2].split(",");
    with open(module_save) as f:
        d = f.readline();
        while d:
            data_train.append(eval(d))
            d = f.readline();
    print("load data done")
    return labels, data_train

def findEncodings(images):
    encodeList = []
    for img in images:
        img = cv2.cvtColor(img, cv2.COLOR_BGR2RGB)
        encode = face_recognition.face_encodings(img)[0]
        print(encode)
        encodeList.append(encode)
    return encodeList


