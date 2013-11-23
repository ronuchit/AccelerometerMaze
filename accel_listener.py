import time

# DATA_PER_INTERVAL = 5;
INTERVAL = 0.1;
UP, DOWN, LEFT, RIGHT = 0, 1, 2, 3

class AccelListener(object):  
  def __init__(self):
    self.prev_avg = []
    self.curr_data = []
    self.motion_threshold = 1.0
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
      # time.sleep(INTERVAL)
      raw_input("...")
      self.refresh_data()
      for datum_str in self.curr_data:
        datum = datum_str.split()
        curr_avg = []
        for i in range(len(curr_avg)):
          curr_avg = [curr_avg[i] + (float)(datum[i]) for i in range(2)]
      l = len(self.curr_data)
      curr_avg = [a / l for a in curr_avg]
      # figure out which direction the phone moved
      for i in range(len(curr_avg)):
        if abs(curr_avg[i] - prev_avg[i]) > motion_threshold:
          if (curr_avg[i] - prev_avg[i]) > motion_threshold:
            if i == 0:
              # it moved in the right direction
              if not self.ignore_right:
                self.ignore_left = True
                self.register_direction(AccelListener.RIGHT)
              else:
                self.ignore_right = False
            else:
              # it moved in the up direction
              if not self.ignore_up:
                self.ignore_down = True
                self.register_direction(AccelListener.DOWN)
              else:
                self.ignore_up = False
          else:
            if i == 0:
              # it moved in the left direction
              if not self.ignore_left:
                self.ignore_right = True
                self.register_direction(AccelListener.LEFT)
              else:
                self.ignore_left = False
            else:
              # it moved in the down direction
              if not self.ignore_down:
                self.ignore_up = True
                self.register_direction(AccelListener.DOWN)
              else:
                self.ignore_down = False
      # self.prev_avg.append(curr_avg)
      # if len(self.prev_avg) > self.num_prevs:
      #   self.prev_avg = self.prev_avg[-2:]
      self.prev_avg = curr_avg

  def register_direction(self, direction):
    with open("data/curr_direction.txt", "w+") as f:
      f.write(direction)

if __name__ == "__main__":
  AccelListener().listen()
