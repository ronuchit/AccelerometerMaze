import Tkinter as tk

class App(object):

  def keyPressed(self,event):
    print "HERE"
    if event.keysym == 'Escape':
      root.destroy()
    elif event.keysym == 'Right':
      self.register_direction(3)
    elif event.keysym == 'Left':
      self.register_direction(2)
    elif event.keysym == 'Up':
      self.register_direction(0)
    elif event.keysym == 'Down':
      self.register_direction(1)

  def register_direction(self, direction):
    with open("../data/curr_direction.txt", "w+") as f:
      f.write(str(direction))

application = App()
root = tk.Tk()
print( "Press arrow key (Escape key to exit):" )
root.bind_all('<Key>', application.keyPressed)
#root.withdraw()
root.mainloop()

