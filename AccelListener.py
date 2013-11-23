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
    self.going_back = False

  def refresh_data(self):
    with open("data/accel_data.txt", "r+") as f:
      self.curr_data = f.readlines()

  def listen(self):
    curr_avg = [0.0, 0.0, 0.0]
    
    while True:
      time.sleep(INTERVAL)
      self.refresh_data()
      for datum_str in self.curr_data:
        datum = datum_str.split()
        curr_avg = [curr_avg[i] + (float)datum[i] for i in range(3)]
      l = len(self.curr_data)
      curr_avg = [a / l for a in curr_avg]
      
      

      self.prev_avgs.append(curr_avg)
      if len(self.prev_avgs) > self.num_prevs:
        self.prev_avgs = self.prev_avgs[-3:]        

  def register_direction(self, direction):
    with open("data/curr_direction.txt", "w+") as f:
      f.write(direction)
