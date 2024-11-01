const isArray = Array.isArray || function(value) {
	return {}.toString.call(value) !== "[object Array]"
};

export default {

	/* TODO
	Changes position using velocity and time delta
	Use .clone() to prevent modifying vectors when doing operations!
	------------------------
	p is a Three.js Vector2 or Vector3 (position of the object)
	v is a Three.js Vector2 or Vector3 (velocity of the object)
	delta is a float (time delta)
	Note: Vectors are always of same size, eg. if p is a Vector2, v will be a Vector2.
	*/
	applyMovement(p, v, delta) {
		p.add(v.clone().multiplyScalar(delta), p.clone());
		/*
		TODO: Write code here

		Hint: Use p.add(...) to modify the position of objects
		https://threejs.org/docs/?q=vector#api/en/math/Vector3.add
		*/
	},

	/* TODO
	Changes the sphere's rotation using position delta
	Careful, dp is always an 3D vector with y=0 (no movement along the y axis)!
	Use .clone() to prevent modifying vectors when doing operations!
	You can use for example, dp.clone().set(1, 2, 3) to create a new vector with components (1, 2, 3), if necessary
	------------------------
	dp is a Three.js Vector3 (position delta)
	sphereRadius is a float (radius of the sphere)
	sphereObj is a Three.js Object3D (object representation of a sphere)
	*/
	applyRotation(dp, sphereRadius, sphereObj) {

		let circumference = 2.0 * Math.PI * sphereRadius;
		let angle = circumference / Math.PI  ;               //TODO : revoir (quelque chose à faire avec delta)
		let axe_X = dp.clone().set(1,0,0);

		sphereObj.rotateOnWorldAxis(axe_X, angle);
		/*
		Hint: use sphereObj.rotateOnWorldAxis(...) to rotate the objects
		https://threejs.org/docs/#api/en/core/Object3D.rotateOnWorldAxis
		*/
	},

	/* TODO
	Checks if two circles/spheres have collided
	Returns true if collision has occured, false otherwise
	------------------------
	circlePos0 is a Three.js Vector2 or Vector3 (position of first object)
	circlePos1 is a Three.js Vector2 or Vector3 (position of second object)
	circleRadius is a float (radius of the object)
	Note: Vectors are always of same size, eg. if circlePos0 is a Vector2, every other Vector parameter will be a Vector2.
	*/
	checkCollisionCircleCircle(circlePos0, circlePos1, circleRadius) {

		let vecDiff = circlePos0.clone().sub(circlePos1.clone()); // Vecteur de différence entre pos de sphere 1 & sph 2
		let normeVecDiff = vecDiff.length()                       // Sa Norme

		if (normeVecDiff < circleRadius * 2) {
			return true;
		}
		else {return false};

		/*

		TODO: Write code here
		*/
		//return false;
	},

	/* TODO
	Checks if a circle/sphere has collided with the outer part of a line segment
	Returns true if collision has occured, false otherwise
	------------------------
	circlePos is a Three.js Vector2 or Vector3 (position of the object)
	circleRadius is a float (radius of the object)
	linePos0 is a Three.js Vector2 or Vector3 (start point of the segment)
	linePos1 is a Three.js Vector2 or Vector3 (end point of the segment)
	Note: Vectors are always of same size, eg. if circlePos is a Vector2, every other Vector parameter will be a Vector2.
	*/
	checkCollisionCircleSegmentOuter(circlePos, circleRadius, linePos0, linePos1) {

		let vecDiff0 = circlePos.clone().sub(linePos0.clone()); // Vecteur de différence entre pos de sphere 1 & sph 2
		let normeVecDiff0 = vecDiff0.length()                       // Sa Norme

		let vecDiff1 = circlePos.clone().sub(linePos1.clone()); // Vecteur de différence entre pos de sphere 1 & sph 2
		let normeVecDiff1 = vecDiff1.length()                       // Sa Norme


		if (normeVecDiff0 < circleRadius || normeVecDiff1 < circleRadius ) {
			return true;
		}
		else {return false};
	},

	/* TODO
	Checks if a circle/sphere has collided with the inner part of a line segment
	Returns true if collision has occured, false otherwise
	------------------------
	circlePos is a Three.js Vector2 or Vector3 (position of the object)
	circleRadius is a float (radius of the object)
	linePos0 is a Three.js Vector2 or Vector3 (start point of the segment)
	linePos1 is a Three.js Vector2 or Vector3 (end point of the segment)
	Note: Vectors are always of same size, eg. if circlePos is a Vector2, every other Vector parameter will be a Vector2.
	*/
	checkCollisionCircleSegmentInner(circlePos, circleRadius, linePos0, linePos1) {


		let v = linePos1.clone().sub(linePos0.clone());   // Vecteur LIGNE : linePos0 --> linePos1   | v : ligne
		let u = linePos0.clone().sub(circlePos.clone());  // Vecteur inter : circlePos --> lignePos0 | u : cercleLigne

		// Formule Projection : proj v u = u * v // ||v||^2 * v
		let projvU = v.multiplyScalar(v.clone().dot(u.clone()) / Math.pow(v.length(), 2)); //TODO : not working !

		if (projvU.length() < circleRadius ){return false;}
		else{return false};
	},

	//Applies linear and quadratic friction on velocity using time delta
	applyFriction(p, v, delta, qfric, lfric) {
		let speed = v.length();
		let newspeed = speed - (speed * qfric + lfric) * delta;
		if (newspeed < 0) {
			newspeed = 0;
		}
		v.setLength(newspeed);
	},

	checkCircleInHole(circlePos, holePos, holeRadius) {
		//If out of bounds
		if (circlePos.x < -98.5 || circlePos.x > 99.5 || circlePos.y < -53 || circlePos.y > 53) {
			return true;
		}

		//If in hole
		let distSq = circlePos.distanceToSquared(holePos);
		return distSq <= (holeRadius * holeRadius);
	},

	resolvePositionCollisionCircleCircle(p0, p1, circleRadius) {
		const epsilon = 0.0001;

		//Resolve if p0 and p1 are at same position
		if (p0.distanceToSquared(p1) == 0) {
			p0.x += (Math.random() - 0.5) * epsilon;
			p0.y += (Math.random() - 0.5) * epsilon;
		}

		//Solve position
		let midpoint = p0.clone().add(p1).divideScalar(2);

		let newp0 = p0.clone().sub(midpoint).setLength(circleRadius + epsilon).add(midpoint);
		let newp1 = p1.clone().sub(midpoint).setLength(circleRadius + epsilon).add(midpoint);
		p0.copy(newp0);
		p1.copy(newp1);
	},

	resolveVelocityCollisionCircleCircle(p0, v0, p1, v1, circleRadius, cor) {
		const epsilon = 0.0001;

		//Resolve if p0 and p1 are at same position
		if (p0.distanceToSquared(p1) == 0) {
			p0.x += (Math.random() - 0.5) * epsilon;
			p0.y += (Math.random() - 0.5) * epsilon;
		}

		//Solve velocity using conservation of momentum
		let diffp01 = p0.clone().sub(p1);
		let diffp10 = p1.clone().sub(p0);
		let diffv01 = v0.clone().sub(v1);
		let diffv10 = v1.clone().sub(v0);

		//Perfect elastic collision
		let ev0 = v0.clone().sub(diffp01.clone().multiplyScalar(diffv01.dot(diffp01)/diffp01.lengthSq()));
		let ev1 = v1.clone().sub(diffp10.clone().multiplyScalar(diffv10.dot(diffp10)/diffp10.lengthSq()));

		//Inelastic collision
		let avgv = ev0.clone().add(ev1).divideScalar(2);

		//Approximate collision with CoR
		let newv0 = ev0.clone().multiplyScalar(cor).add(avgv.clone().multiplyScalar(1 - cor));
		let newv1 = ev1.clone().multiplyScalar(cor).add(avgv.clone().multiplyScalar(1 - cor));

		v0.copy(newv0);
		v1.copy(newv1);
	},

	resolveVelocityCollisionCircleSegmentOuter(p, v, ps0, ps1, circleRadius) {
		const epsilon = 0.0001;

		//Resolve if two objects are exactly at same position
		if (p.distanceToSquared(ps0) == 0 || p.distanceToSquared(ps1) == 0) {
			p.x += (Math.random() - 0.5) * epsilon;
			p.y += (Math.random() - 0.5) * epsilon;
		}

		let d0 = p.clone().sub(ps0);
		let d1 = p.clone().sub(ps1);

		let midpoint;
		if (d0.lengthSq() < d1.lengthSq()) {
			midpoint = ps0;
		} else {
			midpoint = ps1;
		}

		//Solve velocity using reflection equation
		let normal = p.clone().sub(midpoint).normalize();
		let newv = v.clone().sub(normal.clone().multiplyScalar(2*v.dot(normal)));
		v.copy(newv);
	},

	resolvePositionCollisionCircleSegmentOuter(p, ps0, ps1, circleRadius) {
		const epsilon = 0.0001;

		//Resolve if two objects are exactly at same position
		if (p.distanceToSquared(ps0) == 0 || p.distanceToSquared(ps1) == 0) {
			p.x += (Math.random() - 0.5) * epsilon;
			p.y += (Math.random() - 0.5) * epsilon;
		}

		//Solve position
		let d0 = p.clone().sub(ps0);
		let d1 = p.clone().sub(ps1);

		let midpoint;
		if (d0.lengthSq() < d1.lengthSq()) {
			midpoint = ps0;
		} else {
			midpoint = ps1;
		}

		let newp = p.clone().sub(midpoint).setLength(circleRadius + epsilon).add(midpoint);
		p.copy(newp);
	},

	resolveVelocityCollisionCircleSegmentInner(p, v, ps0, ps1, circleRadius) {
		const epsilon = 0.0001;

		//Resolve if two objects are exactly at same position
		if (p.distanceToSquared(ps0) == 0 || p.distanceToSquared(ps1) == 0) {
			p.x += (Math.random() - 0.5) * epsilon;
			p.y += (Math.random() - 0.5) * epsilon;
		}

		let a = p.clone().sub(ps0);
		let b = ps1.clone().sub(ps0);
		let aprojb = b.clone().multiplyScalar(a.dot(b)/b.dot(b));

		//Solve velocity using reflection equation
		let normal = a.clone().sub(aprojb);
		if (normal.lengthSq() == 0) {
			normal.x += (Math.random() - 0.5) * epsilon;
			normal.y += (Math.random() - 0.5) * epsilon;
		}
		normal.normalize();

		let newv = v.clone().sub(normal.setLength(2*v.dot(normal)));
		v.copy(newv);
	},

	resolvePositionCollisionCircleSegmentInner(p, ps0, ps1, circleRadius) {
		const epsilon = 0.0001;

		//Resolve if two objects are exactly at same position
		if (p.distanceToSquared(ps0) == 0 || p.distanceToSquared(ps1) == 0) {
			p.x += (Math.random() - 0.5) * epsilon;
			p.y += (Math.random() - 0.5) * epsilon;
		}

		//Solve position
		let a = p.clone().sub(ps0);
		let b = ps1.clone().sub(ps0);
		let aprojb = b.clone().multiplyScalar(a.dot(b)/b.dot(b));

		let normal = a.clone().sub(aprojb);
		if (normal.lengthSq() == 0) {
			normal.x += (Math.random() - 0.5) * epsilon;
			normal.y += (Math.random() - 0.5) * epsilon;
		}

		let newp = normal.setLength(circleRadius + epsilon).add(aprojb).add(ps0);
		p.copy(newp);
	},

	//Shuffle from https://stackoverflow.com/questions/18194745/shuffle-multiple-javascript-arrays-in-the-same-way
	shuffle() {
		var arrLength = 0;
		var argsLength = arguments.length;
		var rnd, tmp;

		for (var index = 0; index < argsLength; index += 1) {
			if (!isArray(arguments[index])) {
				throw new TypeError("Argument is not an array.");
			}

			if (index === 0) {
				arrLength = arguments[0].length;
			}

			if (arrLength !== arguments[index].length) {
				throw new RangeError("Array lengths do not match.");
			}
		}

		while (arrLength) {
			rnd = Math.floor(Math.random() * arrLength);
			arrLength -= 1;
			for (let argsIndex = 0; argsIndex < argsLength; argsIndex += 1) {
				tmp = arguments[argsIndex][arrLength];
				arguments[argsIndex][arrLength] = arguments[argsIndex][rnd];
				arguments[argsIndex][rnd] = tmp;
			}
		}
	}
}