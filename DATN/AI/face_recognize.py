from multiprocessing import Lock
from threading import Thread
import face_recognition
import cv2
import numpy as np
from load_module import loadData
import time
import json
from call_api import call_update_door



# This is a demo of running face recognition on live video from your webcam. It's a little more complicated than the
# other example, but it includes some basic performance tweaks to make things run a lot faster:
#   1. Process each video frame at 1/4 resolution (though still display it at full resolution)
#   2. Only detect faces in every other frame of video.

# PLEASE NOTE: This example requires OpenCV (the `cv2` library) to be installed only to read from your webcam.
# OpenCV is *not* required to use the face_recognition library. It's only required if you want to run this
# specific demo. If you have trouble installing it, try any of the other demos that don't require it instead.

# Get a reference to webcam #0 (the default one)
video_capture = cv2.VideoCapture("rtsp://192.168.77.102:5555")

# Create arrays of known face encodings and their names

lock = Lock();


known_face_names, known_face_encodings = loadData()
# Initialize some variables
count = 0;
process_this_frame = True
# counter to open the door if this index > 10;
time_counter_open = 0;
# counter to close the door if this index >1 0
time_counter_close = 0;
# update the door state when the door ready change
update_door_state = False;
reconize_done = True
def recognize_face(frame, known_face_names, known_face_encodings):
    global time_counter_open
    global time_counter_close
    global reconize_done
    global update_door_state
    face_locations = []
    face_encodings = []
    face_names = []
    small_frame = cv2.resize(frame, (0, 0), fx=0.5, fy=0.5)

    # Convert the image from BGR color (which OpenCV uses) to RGB color (which face_recognition uses)
    rgb_small_frame = small_frame[:, :, ::-1]

    # Only process every other frame of video to save time
    if process_this_frame:
        # Find all the faces and face encodings in the current frame of video
        face_locations = face_recognition.face_locations(rgb_small_frame)
        face_encodings = face_recognition.face_encodings(rgb_small_frame, face_locations)

        face_names = []
        for face_encoding in face_encodings:
            # See if the face is a match for the known face(s)
            matches = face_recognition.compare_faces(known_face_encodings, face_encoding)
            name = "Unknown"

            # # If a match was found in known_face_encodings, just use the first one.
            # if True in matches:
            #     first_match_index = matches.index(True)
            #     name = known_face_names[first_match_index]

            # Or instead, use the known face with the smallest distance to the new face
            face_distances = face_recognition.face_distance(known_face_encodings, face_encoding)
            best_match_index = np.argmin(face_distances)
            if matches[best_match_index]:
                name = known_face_names[best_match_index]
            face_names.append(name)
        # if not have any people in front of camera => counter to increase close count number
        if len(face_names) == 0:
            if time_counter_close < 10:
                time_counter_close = time_counter_close + 1;
            if time_counter_open > 0:
                update_door_state = False;
                time_counter_open = time_counter_open - 1;
        else:
            is_recognize = False;
            for name in face_names:
                if name != "Unknown":
                    is_recognize = True
                    break;
            if is_recognize:
                if time_counter_open < 10:
                    time_counter_open = time_counter_open + 1
                if time_counter_close > 0:
                    update_door_state = False;
                    time_counter_close = time_counter_close -1;
            else:
                if time_counter_close < 10:
                    time_counter_close = time_counter_close + 1;
                if time_counter_open > 0:
                    update_door_state = False;
                    time_counter_open = time_counter_open -1;
        print("time open: " + str(time_counter_open) + " time close: " +  str(time_counter_close));
        if time_counter_close >= 10 and not update_door_state:
            print("close the door");
            call_update_door(False)
            update_door_state = True
            time_counter_open = 0
        if time_counter_open >= 10 and not update_door_state:
            # call api to open the door
            print("open the door");
            call_update_door(True);
            update_door_state = True
            time_counter_close = 0;
    reconize_done = True
    # Display the resulting image
while True:
    # Grab a single frame of video
    ret, frame = video_capture.read()
    # Resize frame of video to 1/4 size for faster face recognition processing
    if reconize_done:
        reconize_done = False
        Thread(target=recognize_face, args=(frame,known_face_names, known_face_encodings,), daemon=True).start()
        print("show frame")
    cv2.imshow('Video', frame)
    # Hit 'q' on the keyboard to quit!
    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

# Release handle to the webcam
video_capture.release()
cv2.destroyAllWindows()