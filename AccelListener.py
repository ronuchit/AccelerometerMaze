DATA_PER_INTERVAL = 5;
INTERVAL = 0.1;

class AccelListener(object):
  up, down, left, right = 0, 1, 2, 3
  
  def __init__(self):
    self.prev_avg = 0.0;
    self.change_threshold = 1.0;
    self.going_back = False

  def refresh(self):
    with open("data/accel_data.txt", "r+") as f:
      curr_data = f.readlines()[-DATA_PER_INTERVAL:]

  def listen(self):
    
