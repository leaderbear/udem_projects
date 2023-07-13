# open file & remove spaces from first line
seqPath = "sequences.fasta"
lines = []
with open(seqPath) as file:
    for line in file:
        lines.append(line)

# Cette fonction s'occupe seulement de lire et transformer en liste le fichier seq fasta.
global seqList
seqList = []
seq = ""
for line in lines:
    if (line[0] == ">"):            # we are about to start new seq go to next line.
        continue
    elif line in ['\n', '\r\n']:    # if line is empty we are done. Add seq and reset.
        seqList.append(seq)
        seq = ""
    else:
        seq = seq + line[:-1]       # Add every line while no done. Remove "\n".
seqList.append(seq)

def getSeq():
    return seqList


