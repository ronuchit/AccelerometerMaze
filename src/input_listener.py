import Tkinter as tk
from accel_listener import AccelListener

class InputListener(object):
  def __init__(self):
    with open("data/curr_direction.txt", "w+") as f:
      f.write("3"); # start off going to right
      
  def keyPressed(self,event):
    if event.keysym == 'Right':
      self.register_direction(AccelListener.RIGHT)
    elif event.keysym == 'Left':
      self.register_direction(AccelListener.LEFT)
    elif event.keysym == 'Up':
      self.register_direction(AccelListener.UP)
    elif event.keysym == 'Down':
      self.register_direction(AccelListener.DOWN)
    elif event.char == event.keysym:
      root.destroy()
    elif len(event.char) == 1:
      root.destroy()

  def register_direction(self, direction):
    with open("data/curr_direction.txt", "w+") as f:
      f.write(str(direction))

if __name__ == "__main__":
  listener = InputListener()
  root = tk.Tk()
  print("Press arrow keys to move (enter to exit) ")
  root.bind_all('<Key>', listener.keyPressed)
  root.mainloop()
