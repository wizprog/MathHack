from tensorflow.examples.tutorials.mnist import input_data

import numpy as np



"""with open('podaci.txt','r') as f:
    #h = [float(x) for x in next(f).split(',')] # read first line
    #array = []
    for line in f: # read rest of lines
        array = []
        #array.append([float(x)
        for x in line.split(','):
            array.append(x)

        print(array)
    #nparray = np.array(array)
    #print(nparray)
"""

batch_size = 2

a = 0

def return_array2(filename, cnt):
    Niz = []
    i = batch_size
    new_cnt=0
    counter = cnt*batch_size
    with open(filename, 'r') as f:

        for line in f:
            if(new_cnt<counter):
                new_cnt+=1
            else:
                if(i>0):
                    array = []

                    for x in line.split(','):
                        array.append(float(x))

                    Niz.append(array)
                    i-=1
                else:
                    break
    return Niz


def next_batch():
    global a
    input = []
    output = []
    input = return_array2('podaci.txt',a)
    output = return_array2('rezultati.txt', a)
    a+=1
    print(input)
    print(output)

next_batch()