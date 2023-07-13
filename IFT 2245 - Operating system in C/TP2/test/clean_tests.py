import os
from shutil import copyfile

os.remove("../code/main.c")
copyfile("../code/main_bis.c", "../code/main.c")
os.remove("../code/main_bis.c")
