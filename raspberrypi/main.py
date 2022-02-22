
import time
import os
import math
import glob
import numpy as np
from scipy.signal import find_peaks
import RPi.GPIO as GPIO
import requests
import json
import subprocess

import oled_display
import sensors
import basichrv
import eeglib
import dataupload

show_display = True
use_ECG = True
use_EEG = False
use_GSR = False
use_temp = False

draw = None
image = None

if show_display:
    global draw, image
    draw, image = oled_display.init_display()


if use_temp:
    # initialize temperature probe
    os.system('modprobe w1-gpio')
    os.system('modprobe w1-therm')

    base_dir = '/sys/bus/w1/devices/'
    device_folder = glob.glob(base_dir + '28*')[0]
    device_file = device_folder + '/w1_slave'


# initialize pin numbers
if use_ECG:
    ECG_A = 4
if use_EEG:
    EEG_A = 0
if use_GSR:
    GSR_A = 2

delay = 0.001
#temp_update = 20

def read_temp_raw():
    f = open(device_file, 'r')
    lines = f.readlines()
    f.close()
    return lines

def gettemp():
    while True:
        print("Getting temperature...")
        lines = read_temp_raw()
        while lines[0].strip()[-3:] != 'YES':
            time.sleep(0.2)
            lines = read_temp_raw()
        equals_pos = lines[1].find('t=')
        if equals_pos != -1:
            temp_string = lines[1][equals_pos+2:]
            temp_c = float(temp_string) / 1000.0
            print(temp_c)
            return temp_c


def ema_filtering(new_value, prev_value):
    # initialize EMA stuff
    ema_a = 0.1
    return (ema_a*new_value) + ((1-ema_a)*prev_value)


def calculate_rr(ecg_data, time_elapsed):
    peaks, _ = find_peaks(ecg_data, height=5000, distance=30)
    rr_arr = np.diff(peaks)
    rr_arr = rr_arr * time_elapsed / len(ecg_data)
    print(f"Number of beats: {len(rr_arr)}")
    return rr_arr


def get_hrv(rr):
    tdf, oc = basichrv.gethrv(rr)
    reading = {}
    reading['ectopic'] = oc
    reading['hrstd'] = tdf['std_hr']
    print(tdf['std_hr'])
    reading['hr'] = tdf['mean_hr']
    reading['hrv'] = tdf['sdnn']

    return reading

def sensors_settle():
    print("Start settling")
    for i in range(5):
        if use_ECG:
            ecg_reading = sensors.readAnalog(ECG_A)*2000
        if use_EEG:
            eeg_reading = sensors.readAnalog(EEG_A)
        if use_GSR:
            gsr_reading = sensors.readAnalog(GSR_A)
            #gsr_reading = adc.read_voltage(GSR_A)
    print("Done settling")


def read_fast_data():
    if use_ECG:
        ecg_batch = []
    if use_EEG:
        eeg_batch = []
    if use_GSR:
        gsr_batch = []

    batch_size = 3000
    i = 0

    start_time = time.time_ns()
    while 1:
        i = i + 1

        if use_ECG:
            ecg_reading = sensors.readAnalog(ECG_A)*2000
            ecg_batch.append(ecg_reading)
            #print(ecg_reading)

        if use_EEG:
            #EEG reading records the voltage level, no multiplier
            eeg_reading = sensors.readAnalog(EEG_A)
            eeg_batch.append(eeg_reading)

        if use_GSR:
            #gsr_reading = adc.read_voltage(GSR_A)
            gsr_reading = sensors.readAnalog(GSR_A)
            gsr_batch.append(gsr_reading)

        #print(i)
        if i >=batch_size:
            end_time = time.time_ns()
            time_elapsed = end_time - start_time

            if use_ECG:
                # process ECG data
                rr_arr = calculate_rr(ecg_batch, time_elapsed/1000000.0)
                hrv_dict = get_hrv(rr_arr)
                print(hrv_dict)
                #np.savetxt(f'ecg_data_{start_time}_to_{end_time}.csv', np.array(ecg_batch), delimiter=",")

            if use_EEG:
                # process EEG data
                samplerate = len(eeg_batch) / (time_elapsed/1000000000.0)
                brainwaves = eeglib.getbrainwaves(eeg_batch, samplerate)
                print(brainwaves)
                #np.savetxt(f'eeg_data_{start_time}_to_{end_time}.csv', np.array(eeg_batch), delimiter=",")

            if use_GSR:
                # process GSR data
                gsr_avg = np.array(gsr_batch).mean()
                print(gsr_avg)
                #np.savetxt(f'gsr_data_{start_time}_to_{end_time}.csv', np.array(gsr_batch), delimiter=",")

            if use_temp:
                # get temperature
                temp_reading = gettemp()
                print(temp_reading)
                #np.savetxt('temp_reading_{end_time}.csv', np.array([temp_reading]), delimiter=",")

            payload = {"TimeStamp":int(time.time())}
            if use_ECG:
                ecg_payload = {"hr": hrv_dict['hr'],
                               "hrstd": hrv_dict['hrstd'],
                               "hrv": hrv_dict['hrv'],
                               "ectopic": hrv_dict['ectopic']}
                            #  "HeartRateValues":ecg_batch
                payload = {**payload, **ecg_payload}
            if use_EEG:
                eeg_payload = {"alpha": brainwaves['alpha'],
                               "beta": brainwaves['beta'],
                               "delta": brainwaves['delta'],
                               "gamma": brainwaves['gamma'],
                               "theta": brainwaves['theta'],
                               "eegraw":eeg_batch,
                               "eegsamplerate":samplerate}
                payload = {**payload, **eeg_payload}

            if use_GSR:
                gsr_payload = {"gsr": gsr_avg}
                payload = {**payload, **gsr_payload}

            if use_temp:
                temp_payload = {"temp": temp_reading}
                payload = {**payload, **temp_payload}

            dataupload.upload_data(payload)

            global draw, image
            oled_display.display_text(draw, image, [f"HR: {hrv_dict['hr']}",
                                                    f"TimeStamp: {int(time.time())}"])

            #print(response.text)
            print("Data saved")
            #print(f"Data saved.")
            #print(f"Start time {start_time}, end time {end_time}")
            if use_ECG:
                ecg_batch = []
            if use_GSR:
                gsr_batch = []
            if use_EEG:
                eeg_batch = []
            i = 0
            start_time = time.time_ns()
            #exit()

print("Starting application")
sensors_settle()
read_fast_data()
