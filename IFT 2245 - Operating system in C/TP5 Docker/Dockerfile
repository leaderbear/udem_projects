FROM gcc:latest

# Install required packages
RUN apt-get update && apt-get install -y gcc cmake make
RUN apt-get update && apt-get install -y git

#Add current files to working directory
COPY . /app

#1.2 Add machine.turing
ADD machine.turing /app

#Get TP0 from git
RUN git clone https://github.com/IFT2245/TP0-docker /app/TP5-docker

#Init work directory
WORKDIR /app

#Build TP0
RUN cmake ./TP5-docker

#Compile TP0
#RUN make

# Set executable permissions for machine.turing
RUN chmod +x machine.turing

#Change working directory
WORKDIR /app/TP5-docker

#Set entry point to out compiled TP0
ENTRYPOINT ["../CMakeFiles/3.25.1/CompilerIdC/a.out"]

#docker run command
CMD ["../machine.turing", "0000"]

#Add environment variable to change turing machine at runtime
#To use this, add --env TURING_MACHINE=new_machine.turing to docker run.
#Example: docker run --env TURING_MACHINE=new_machine.turing tp5 1234
ENV TURING_MACHINE machine.turing
