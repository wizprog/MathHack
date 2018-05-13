import tensorflow as tf
from tensorflow.examples.tutorials.mnist import input_data
import numpy as np

mnist = input_data.read_data_sets("tmp/data/", one_hot=True)

n_nodes_hl1 = 50
n_nodes_hl2 = 50
n_nodes_hl3 = 50

num_numbers = 770114

n_classes = 3
batch_size = 1000

# input feature size = 28x28 pixels = 784
x = tf.placeholder('float', [None, 6], 'x')
y = tf.placeholder('float', [None, 3], 'y')


def neural_network_model(data):
    # input_data * weights + biases
    hidden_l1 = {'weights': tf.Variable(tf.random_normal([6, n_nodes_hl1])),
                 'biases': tf.Variable(tf.random_normal([n_nodes_hl1]))}

    hidden_l2 = {'weights': tf.Variable(tf.random_normal([n_nodes_hl1, n_nodes_hl2])),
                 'biases': tf.Variable(tf.random_normal([n_nodes_hl2]))}

    hidden_l3 = {'weights': tf.Variable(tf.random_normal([n_nodes_hl2, n_nodes_hl3])),
                 'biases': tf.Variable(tf.random_normal([n_nodes_hl3]))}

    output_l = {'weights': tf.Variable(tf.random_normal([n_nodes_hl3, n_classes])),
                'biases': tf.Variable(tf.random_normal([n_classes]))}

    l1 = tf.add(tf.matmul(data, hidden_l1['weights']), hidden_l1['biases'])
    l1 = tf.nn.relu(l1)

    l2 = tf.add(tf.matmul(l1, hidden_l2['weights']), hidden_l2['biases'])
    l2 = tf.nn.relu(l2)

    l3 = tf.add(tf.matmul(l2, hidden_l3['weights']), hidden_l3['biases'])
    l3 = tf.nn.relu(l3)

    output = tf.add(tf.matmul(l3, output_l['weights']), output_l['biases'])
    return output

i= 0


Niz_input = []
Niz_output = []
def return_array2(filename):
    Niz = []

    with open(filename, 'r') as f:
        for line in f:
            array = []

            for x in line.split(','):
                array.append(float(x))

            Niz.append(array)


    return Niz

Niz_input = return_array2('podaci.txt')
Niz_output = return_array2('rezultati.txt')

def next_batch():
    global i
    input = []
    output = []
    input = Niz_input[i*batch_size:i*batch_size+batch_size]
    output = Niz_output[i * batch_size:i * batch_size + batch_size]
    return input, output



def train_neural_network(x):
    global i
    prediction = neural_network_model(x)
    print(y,prediction, y-prediction)
    #cost = tf.cost.cross_entropy(y, prediction)
    cost=tf.reduce_mean(tf.reduce_sum(tf.abs(y-prediction)))

    # optimizer value = 0.001, Adam similar to SGD
    optimizer = tf.train.AdamOptimizer().minimize(cost)
    epochs_no = 10

    with tf.Session() as sess:
        sess.run(tf.global_variables_initializer())  # v1.0 changes
        saver = tf.train.Saver()
        # training

        for epoch in range(epochs_no):
            i=0
            epoch_loss = 0

            for _ in range(int(num_numbers / batch_size)-2):

                epoch_x, epoch_y = next_batch()

                i+=1
                _, c = sess.run([optimizer, cost], feed_dict={x: epoch_x, y: epoch_y})
                # code that optimizes the weights & biases
                epoch_loss += c
            print('Epoch', epoch, 'completed out of', epochs_no, 'loss:', epoch_loss)

        # testing
        correct = tf.equal(tf.argmax(prediction, 1), tf.argmax(y, 1))
        accuracy = tf.reduce_mean(tf.cast(correct, 'float'))
        print('Accuracy:', accuracy.eval({x: Niz_input, y: Niz_output}))

        result = sess.run(tf.argmax(prediction.eval(feed_dict={x:[[-22.630869,18.709106,9.433293,3.6149411,2.3565452,2.9892392]]}), 1))
        print(prediction.eval(feed_dict={x:[[-22.630869,18.709106,9.433293,3.6149411,2.3565452,2.9892392]]}))

        save_path = saver.save(sess, "tmp/model.ckpt")
        print(save_path)



train_neural_network(x)


