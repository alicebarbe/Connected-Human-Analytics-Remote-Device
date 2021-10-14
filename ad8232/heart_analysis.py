# -*- coding: utf-8 -*-
"""
Created on Wed Oct 13 12:19:16 2021

@author: Alice
"""

from plotly.offline import plot as htmlplot
import plotly.graph_objects as go
import pandas as pd

# %% Import in file, make basic plotly graph

timedatafile = open("tdata.txt", "r")
timedata = timedatafile.read()
timedatafile.close()
split_strings = []
n = 26
for index in range(0, len(timedata), n):
    split_strings.append(timedata[index : index + n])

data = pd.read_csv('data.txt', names=['data'])
data = data.replace('!', '0')
data['Time'] = pd.to_datetime(pd.Series(split_strings))
fig = go.Figure()
fig.add_trace(go.Scatter(x=data['Time'], y=data['data'].astype(int)))
fig.update_yaxes(range=[0, 700])

htmlplot(fig)

# %%
import heartpy as hp
import numpy as np
working_data, measures = hp.process_segmentwise(np.array(data['data']), sample_rate=10.0, segment_width = 40, segment_overlap = 0.25)
