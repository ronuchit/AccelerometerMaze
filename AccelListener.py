import time

# DATA_PER_INTERVAL = 5;
INTERVAL = 0.1;

class AccelListener(object):
  up, down, left, right = 0, 1, 2, 3
  
  def __init__(self):
    self.prev_avgs = []
    self.curr_data = []
    self.num_prevs = 1
    self.motion_threshold = 1.0
    #self.movement_occurred = False
    self.ignore_up = False
    self.ignore_down = False
    self.ignore_left = False
    self.ignore_right = False

  def refresh_data(self):
    with open("data/accel_data.txt", "r+") as f:
      self.curr_data = f.readlines()

  def listen(self):
    curr_avg = [0.0, 0.0]
    
    while True:
      #time.sleep(INTERVAL)
      raw_input("...")
      self.refresh_data()
      for datum_str in self.curr_data:
        datum = datum_str.split()
        curr_avg = []
        for i in range(len(curr_avg)):
          curr_avg = [curr_avg[i] + (float)(datum[i]) for i in range(3)]
      l = len(self.curr_data)
      curr_avg = [a / l for a in curr_avg]
      #figure out which direction the phone moved
      for i in range(len(curr_avg)):
        if abs(curr_avg[i] - prev_avgs[i]) > motion_threshold:
          #movement_occurred = True
          if (curr_avg[i] - prev_avgs[i]) > motion_threshold:
            if i==1:
              #it moved in the right direction
              if self.ignore_right == False:
                self.ignore_left = True
                self.register_direction(3)
              else:
                self.ignore_right = False
            else:
              #it moved in the up direction
              if self.ignore_up == False:
                self.ignore_down = True
                self.register_direction(0)
              else:
                self.ignore_up = False
          elif (prev_avgs[i] - curr_avg[i]) > motion_threshold:
            if i==1:
              #it moved in the left direction
              if self.ignore_left == False:
                self.ignore_right = True
                self.register_direction(2)
              else:
                self.ignore_left = False
            else:
              #it moved in the down direction
              if self.ignore_down == False:
                self.ignore_up = True
                self.register_direction(1)
              else:
                self.ignore_down = False
      self.prev_avgs.append(curr_avg)
      if len(self.prev_avgs) > self.num_prevs:
        self.prev_avgs = self.prev_avgs[-2:]        

  def register_direction(self, direction):
    with open("data/curr_direction.txt", "w+") as f:
      f.write(direction)
