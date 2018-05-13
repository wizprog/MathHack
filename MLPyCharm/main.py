import cv2
import sys
import math
import tensorflow as tf

(major_ver, minor_ver, subminor_ver) = (cv2.__version__).split('.')

n_nodes_hl1 = 500
n_nodes_hl2 = 500
n_nodes_hl3 = 500
n_classes = 3

pastPositions = []

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

sess = tf.Session()
# Restore variables from disk.
saver.restore(sess, "/tmp/model2.ckpt")
print("Model restored.")

# Add ops to save and restore all the variables.

# Later, launch the model, use the saver to restore variables from disk, and

def sizeToAngle(size):
    return size/pixelsPerDegree

output = []

def get3dPos(x, y, z):
    newX = ((x - width/ 2) / (width/ 2) * z * (1 / 0.86602) * aspect)
    newY = ((y - height/2)/(height/2)*z*(1/0.86602))
    output.append((newX, newY, z))
    return (newX, newY, z)

def pos3dToScreen(x, y, z):
    newX = x/aspect*0.86602/z*(width/ 2)
    newX += width/ 2

    newY = y*0.86602/z*(height/2)
    newY += height/2

    print((newX, newY, z))
    #return (newX, newY, z)

if __name__ == '__main__':

    # Set up tracker.
    # Instead of MIL, you can also use


    tracker = cv2.TrackerMedianFlow_create()

    startingDistance = float(input("Aproximate distance to the object\n"))

    # Read video
    video = cv2.VideoCapture(0)

    # Exit if video not opened.
    if not video.isOpened():
        print
        "Could not open video"
        sys.exit()

    # Read first frame.
    ok, frame = video.read()
    if not ok:
        print
        'Cannot read video file'
        sys.exit()

    # Define an initial bounding box
    bbox = (287, 23, 86, 320)

    # Uncomment the line below to select a different bounding box
    bbox = cv2.selectROI(frame, False)
    startingWidth = bbox[2]
    speed = (0, 0)
    pixelsPerDegree = video.get(3)/(60*4/3)

    width = video.get(3)
    height = video.get(4)
    aspect = width/height

    startingSize = math.tan(math.radians(sizeToAngle(startingWidth)))*startingDistance

    print(startingSize )

    previousPos = (bbox[0], bbox[1])
    # Initialize tracker with first frame and bounding box
    ok = tracker.init(frame, bbox)

    while True:
        # Read a new frame
        ok, frame = video.read()
        if not ok:
            break

        # Start timer
        timer = cv2.getTickCount()

        # Update tracker
        ok, bbox = tracker.update(frame)

        # Calculate Frames per second (FPS)
        fps = cv2.getTickFrequency() / (cv2.getTickCount() - timer);

        # Draw bounding box
        if ok:
            # Tracking success
            p1 = (int(bbox[0]), int(bbox[1]))
            p2 = (int(bbox[0] + bbox[2]), int(bbox[1] + bbox[3]))
            center = (int((p1[0] + p2[0])/2), int((p1[1] + p2[1])/2))
            #center = p2
            distance = startingSize / (math.tan(math.radians(sizeToAngle(bbox[2]))))
            speed = ((bbox[0] - previousPos[0])*6, (bbox[1] - previousPos[1])*6 - math.sqrt(distance*20))
            cv2.rectangle(frame, p1, p2, (255, 0, 0), 2, 1)

            if p1[1] +int(speed[1]) < 0 or p2[1] +int(speed[1]) > height or p1[1] < 0 or p2[1] > height:
                speed = (speed[0], 0)
            if p1[0] +int(speed[0]) < 0 or p2[0] +int(speed[0]) > width or p1[0] < 0 or p2[0] > width:
                speed = (0, speed[1])

            ghostPos = (p1[1] +int(speed[1]), p2[1] + int(speed[1]), p1[0]+int(speed[0]), p2[0]+int(speed[0]))

            grayImage = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
            gray = cv2.merge((grayImage, grayImage, grayImage))

            #frame = cv2(frame, gray[p1[1]:p2[1], p1[0]:p2[0]], (100, 0), 0.7)

            frame[ghostPos[0]:ghostPos[1], ghostPos[2]:ghostPos[3]] = gray[p1[1]:p2[1], p1[0]:p2[0]]

            cv2.circle(frame, center, 10, (255, 0, 0), 2, 1)
            cv2.circle(frame, (int(center[0] + speed[0]), int(center[1] + speed[1])), 10, (0, 0, 255), 2, 1)

            #print(sizeToAngle(bbox[2]))

            #print(distance)
            previousPos = (bbox[0], bbox[1])

            realPos = get3dPos(center[0], center[1], distance)

            if len(pastPositions) >= 30:
                pastPositions.pop(0)
                pastPositions.pop(0)
                pastPositions.pop(0)

            pastPositions.append(realPos[0])
            pastPositions.append(realPos[1])
            pastPositions.append(realPos[2])

            if len(pastPositions) == 30:
                print(prediction.eval(feed_dict={x: [pastPositions]}, session=sess)/6000)
            cv2.putText(frame, "X " + "{:6.4f}".format(realPos[0]), (20, 50), cv2.FONT_HERSHEY_SIMPLEX, 0.75, (0, 0, 255), 2)
            cv2.putText(frame, "Y " + "{:6.4f}".format(-realPos[1]), (20, 80), cv2.FONT_HERSHEY_SIMPLEX, 0.75, (0, 255, 0), 2);
            cv2.putText(frame, "Z " + "{:6.4f}".format(realPos[2]), (20, 110), cv2.FONT_HERSHEY_SIMPLEX, 0.75, (255, 0, 0), 2);
        else:
            # Tracking failure
            cv2.putText(frame, "Tracking failure detected", (100, 80), cv2.FONT_HERSHEY_SIMPLEX, 0.75, (0, 0, 255), 2)

        # Display tracker type on frame
        #cv2.putText(frame, tracker_type + " Tracker", (100, 20), cv2.FONT_HERSHEY_SIMPLEX, 0.75, (50, 170, 50), 2);

        # Display FPS on frame
        #cv2.putText(frame, "FPS : " + str(int(fps)), (100, 50), cv2.FONT_HERSHEY_SIMPLEX, 0.75, (50, 170, 50), 2);

        # Display result
        cv2.imshow("Tracking", frame)

        # Exit if ESC pressed
        k = cv2.waitKey(1) & 0xff
        if k == 27: break
        if k == 65:
            bbox = cv2.selectROI(frame, False)
            ok = tracker.update(bbox)

    with open('input.txt', 'a') as out:
        with open('output.txt', 'a') as resenje:
            for i in range(0, len(output)-16, 2):
                for j in range(10):
                    out.write(str(output[i+j][0])+" "+str(output[i+j][1])+" "+str(output[i+j][2])+"\n")
                resenje.write(str(output[i + 10][0]) + " " + str(output[i + 10][1]) + " " + str(output[i + 10][2]) + "\n")
