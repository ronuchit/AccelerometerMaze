# DATA_PER_INTERVAL = 5;
INTERVAL = 0.1;

class AccelListener(object):
  up, down, left, right = 0, 1, 2, 3
  
  def __init__(self):
    self.prev_samples = [];
    self.curr_data = []
    self.change_threshold = 1.0;
    self.going_back = False

  def refresh(self):
    with open("data/accel_data.txt", "r+") as f:
      self.curr_data = f.readlines()

  def listen(self):
    while True:
      
    
