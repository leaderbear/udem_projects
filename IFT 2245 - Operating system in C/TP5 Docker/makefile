hubimg=bishaa/tp5:1.0

# Default values
IMAGE ?= tp0_img
INPUT ?= 0000
TURING ?= machine.turing

run:
	docker run -v $(PWD):/app $(IMAGE) $(TURING) $(INPUT)

build:
	docker build -t $(IMAGE) .

# DON'T CHANGE ANYTHING BELLOW THIS LINE
pull:
	docker pull $(hubimg)
	docker tag $(hubimg) $(IMAGE)

dockerhub:
	echo $(hubimg)

