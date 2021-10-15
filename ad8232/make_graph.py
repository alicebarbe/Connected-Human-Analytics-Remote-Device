# -*- coding: utf-8 -*-
"""
Created on Tue Oct 12 09:56:50 2021

@author: Alice
"""

#from plotly.offline import plot as htmlplot  # used to export as html
#import plotly.graph_objects as go
import pandas as pd


import os
import time
import datetime
import sys

import numpy as np
import pyqtgraph as pg
from pyqtgraph.Qt import QtGui
import math
import serial


def update(value):
    """Update live QT plot with new value"""
    global curve, ptr, x
    ptr += 1  # update x position for displaying the curve
    x[:-1] = x[1:]  # shift data in the temporal mean 1 sample left
    try:
        x[-1] = float(value)  # vector containing the instantaneous values
    except:
        x[-1] = 0
    curve.setData(x)  # set the curve with this data
    curve.setPos(ptr, 0)  # set x position in the graph to 0

    QtGui.QApplication.processEvents()  # you MUST process the plot now

ser = serial.Serial('COM7')

data = []
times = []
app = QtGui.QApplication([])
win = pg.GraphicsWindow(title="Sideload")  # creates a window
p = win.addPlot(title="Realtime plot")  # creates empty space for the plot in the window
curve = p.plot()
windowWidth = 500  # width of the window displaying the curve
x = np.linspace(0, 0, windowWidth)  # create array that will contain the relevant time series
ptr = -windowWidth  # set first x position
f = open('data.txt', 'wb', buffering=4096)
ft = open('tdata.txt', 'w')

while True:
    try:
        ser_bytes = ser.readline()
        data.append(ser_bytes)
        times.append(datetime.datetime.now())
        # start QT application to see plot
        update(ser_bytes)
        f.write(ser_bytes)
        ft.write(str(datetime.datetime.now())+'\n')

    except(KeyboardInterrupt):
        ser.close()
        f.close()
        ft.close()
        QtGui.QApplication.closeAllWindows()
        app.quit()
        sys.exit(0)
