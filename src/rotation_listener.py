import time

# DATA_PER_INTERVAL = 5;
INTERVAL = 0.1;

class RotationListener(object):
  UP, DOWN, LEFT, RIGHT = 0, 1, 2, 3

  def __init__(self):
    self.prev_avg = [0.0, 0.0]
    self.curr_data = []
    self.motion_threshold = 2.0
    self.zero_threshold = 0.5
    self.can_move = True
    with open("data/curr_direction.txt", "w+") as f:
      f.write("3"); # start off going to right

  def refresh_data(self):
    with open("data/accel_data.txt", "r+") as f:
      self.curr_data = f.readlines()

  def listen(self):
    while True:
      # time.sleep(INTERVAL)
      raw_input("...")
      self.refresh_data()
      curr_avg = [0.0, 0.0]
      for datum_str in self.curr_data:
        datum = datum_str.split()
        if datum != "[]":
          curr_avg = [curr_avg[i] + (float)(datum[i]) for i in range(2)]
      l = len(self.curr_data)
      curr_avg = [a / l for a in curr_avg]
      print(curr_avg)
      print(self.prev_avg)
      # figure out which direction the phone moved
      if ((abs(curr_avg[0]) < zero_threshold) and (abs(curr_avg[1]) < zero_threshold)):
        self.can_move = True
      else:
        for i in range(len(curr_avg)):
          if abs(curr_avg[i]) > self.motion_threshold:
            if curr_avg[i] > self.motion_threshold:
              if i == 0:
                # it moved in the right direction
                print("right")
                if can_move:
                  self.register_direction(RotationListener.RIGHT)
                self.can_move = False
              else:
                # it moved in the up direction
                print("up")
                if can_move:
                  self.register_direction(RotationListener.DOWN)
                self.can_move = False
            else:
              if i == 0:
                # it moved in the left direction
                print("left")
                if can_move:
                  self.register_direction(RotationListener.LEFT)
                self.can_move = False
              else:
                # it moved in the down direction
                print("down")
                if can_move:
                  self.register_direction(RotationListener.DOWN)
                self.can_move = False
      self.prev_avg = curr_avg

  def register_direction(self, direction):
    with open("../data/curr_direction.txt", "w+") as f:
      f.write(str(direction))

if __name__ == "__main__":
  RotationListener().listen()
