import tensorflow as tf

n_nodes_hl1 = 500
n_nodes_hl2 = 500
n_nodes_hl3 = 500
n_classes = 3

def neural_network_model(data):
    # input_data * weights + biases
    hidden_l1 = {'weights': tf.Variable(tf.random_normal([30, n_nodes_hl1])),
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

tf.reset_default_graph()
x = tf.placeholder('float', [None, 30], 'x')
prediction = neural_network_model(x);
saver = tf.train.Saver()

with tf.Session() as sess:
  # Restore variables from disk.
  saver.restore(sess, "/temp/model2.ckpt")
  print("Model restored.")
#First let's load meta graph and restore weights
#saver = tf.train.import_meta_graph('C:/Users/mbabi/Desktop/model.ckpt.meta')
#saver.restore(sess,"model.ckpt")
  result = sess.run(tf.argmax(prediction.eval(feed_dict={x:[[-11.434689348955914,17.580834874019715,29.7080090099484,-11.434689348955914,17.580834874019715,29.7080090099484,-11.434689348955914,17.580834874019715,29.7080090099484,-11.434689348955914,17.580834874019715,29.7080090099484,-11.434689348955914,17.580834874019715,29.7080090099484,-11.434689348955914,17.580834874019715,29.7080090099484,-11.434689348955914,17.580834874019715,29.7080090099484,-11.434689348955914,17.580834874019715,29.7080090099484,-11.434689348955914,17.580834874019715,29.7080090099484,-11.434689348955914,17.580834874019715,29.7080090099484]]}), 1))
  print(prediction.eval(feed_dict={x:[[-11.434689348955914,17.580834874019715,29.7080090099484,-11.434689348955914,17.580834874019715,29.7080090099484,-11.434689348955914,17.580834874019715,29.7080090099484,-11.434689348955914,17.580834874019715,29.7080090099484,-11.434689348955914,17.580834874019715,29.7080090099484,-11.434689348955914,17.580834874019715,29.7080090099484,-11.434689348955914,17.580834874019715,29.7080090099484,-11.434689348955914,17.580834874019715,29.7080090099484,-11.434689348955914,17.580834874019715,29.7080090099484,-11.434689348955914,17.580834874019715,29.7080090099484]]}))

# Add ops to save and restore all the variables.

# Later, launch the model, use the saver to restore variables from disk, and
