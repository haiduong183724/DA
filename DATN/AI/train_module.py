from cmath import cos
from email.mime import base
import opcode
import face_recognition
import cv2
import numpy as np
import os
from face_recognizition.face_detection_cli import folder_in_folder
from face_recognizition.face_detection_cli import image_files_in_folder
train_directory = {};
known_people_folder = "D:\\Python\\DA\\DATN\\AI\\Know";
labels = [];
data = []
for direc in folder_in_folder(known_people_folder):
    base_name = os.path.abspath(direc);   
    base = os.path.basename(direc);
    i = 0;
    for file in image_files_in_folder(base_name):
        img = face_recognition.load_image_file(file);
        face_encodings = face_recognition.face_encodings(img)[0];
        labels.append(base);
        data.append(face_encodings)
        i += 1;

file_save = open("modulesave.txt", "w");
file_labels = open("labels.txt", "w");

for lb in labels:
    file_labels.write(lb + ",");

for data_train in data:
    file_save.write("[")
    for d in data_train:
        file_save.write(str(d) + ",")
    file_save.write("]\n")




