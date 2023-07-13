import re
import contextlib
import os
from shutil import copyfile

copyfile("../code/main.c", "../code/main_bis.c")

with open("../code/main.c", "r", encoding="utf-8") as mainc:
    mainc_string = mainc.read()

mainc_without_main = re.sub(r"// ༽つ۞﹏۞༼つ[\S\n\t\v ]*// ༽つ۞﹏۞༼つ", "", mainc_string)

with open("../code/main.c", "w", encoding="utf-8") as mainc:
    mainc.write(mainc_without_main)

