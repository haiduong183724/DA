from urllib import response
import requests
import json



def call_update_door(state):

    # load the module config
    Url = ""
    door_id = ""
    with open("module.json", "r") as jsonfile:
        data = json.load(jsonfile)
        Url = "".join([data['server_url'],"/",data['update_door_state']]);
        door_id = data["door_id"]
        jsonfile.close();   
    # call the api
    PARAMS = {'state':state, "door_id": door_id}
    response = requests.post(url = Url, params= PARAMS)
    print(response.status_code);


call_update_door(True);