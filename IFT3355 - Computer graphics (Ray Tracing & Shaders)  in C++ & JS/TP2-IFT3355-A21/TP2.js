/**
 *	[Tabta]
 *	[Abderrahim]
 *	[20133680]
 */

import * as THREE from './build/three.module.js';

import Stats from './jsm/libs/stats.module.js';

import {
    ColladaLoader
}
    from './jsm/loaders/ColladaLoader.js';

import {
    OrbitControls
}
    from './jsm/controls/OrbitControls.js'

//SPECIAL IMPORT
// THREEx.KeyboardState.js keep the current state of the keyboard.
// It is possible to query it at any time. No need of an event.
// This is particularly convenient in loop driven case, like in
// 3D demos or games.
//
// # Usage
//
// **Step 1**: Create the object
//
// ```var keyboard	= new THREEx.KeyboardState();```
//
// **Step 2**: Query the keyboard state
//
// This will return true if shift and A are pressed, false otherwise
//
// ```keyboard.pressed("shift+A")```
//
// **Step 3**: Stop listening to the keyboard
//
// ```keyboard.destroy()```
//
// NOTE: this library may be nice as standaline. independant from three.js
// - rename it keyboardForGame
//
// # Code
//

/** @namespace */
var THREEx = THREEx || {};

/**
 * - NOTE: it would be quite easy to push event-driven too
 *   - microevent.js for events handling
 *   - in this._onkeyChange, generate a string from the DOM event
 *   - use this as event name
 */
THREEx.KeyboardState = function (domElement) {
    this.domElement = domElement || document;
    // to store the current state
    this.keyCodes = {};
    this.modifiers = {};

    // create callback to bind/unbind keyboard events
    var _this = this;
    this._onKeyDown = function (event) {
        _this._onKeyChange(event)
    }
    this._onKeyUp = function (event) {
        _this._onKeyChange(event)
    }

    // bind keyEvents
    this.domElement.addEventListener("keydown", this._onKeyDown, false);
    this.domElement.addEventListener("keyup", this._onKeyUp, false);

    // create callback to bind/unbind window blur event
    this._onBlur = function () {
        for (var prop in _this.keyCodes)
            _this.keyCodes[prop] = false;
        for (var prop in _this.modifiers)
            _this.modifiers[prop] = false;
    }

    // bind window blur
    window.addEventListener("blur", this._onBlur, false);
}

/**
 * To stop listening of the keyboard events
 */
THREEx.KeyboardState.prototype.destroy = function () {
    // unbind keyEvents
    this.domElement.removeEventListener("keydown", this._onKeyDown, false);
    this.domElement.removeEventListener("keyup", this._onKeyUp, false);

    // unbind window blur event
    window.removeEventListener("blur", this._onBlur, false);
}

THREEx.KeyboardState.MODIFIERS = ['shift', 'ctrl', 'alt', 'meta'];
THREEx.KeyboardState.ALIAS = {
    'left': 37,
    'up': 38,
    'right': 39,
    'down': 40,
    'space': 32,
    'pageup': 33,
    'pagedown': 34,
    'tab': 9,
    'escape': 27
};

/**
 * to process the keyboard dom event
 */
THREEx.KeyboardState.prototype._onKeyChange = function (event) {
    // log to debug
    //console.log("onKeyChange", event, event.keyCode, event.shiftKey, event.ctrlKey, event.altKey, event.metaKey)

    // update this.keyCodes
    var keyCode = event.keyCode
    var pressed = event.type === 'keydown' ? true : false
    this.keyCodes[keyCode] = pressed
    // update this.modifiers
    this.modifiers['shift'] = event.shiftKey
    this.modifiers['ctrl'] = event.ctrlKey
    this.modifiers['alt'] = event.altKey
    this.modifiers['meta'] = event.metaKey
}

/**
 * query keyboard state to know if a key is pressed of not
 *
 * @param {String} keyDesc the description of the key. format : modifiers+key e.g shift+A
 * @returns {Boolean} true if the key is pressed, false otherwise
 */
THREEx.KeyboardState.prototype.pressed = function (keyDesc) {
    var keys = keyDesc.split("+");
    for (var i = 0; i < keys.length; i++) {
        var key = keys[i]
        var pressed = false
        if (THREEx.KeyboardState.MODIFIERS.indexOf(key) !== -1) {
            pressed = this.modifiers[key];
        } else if (Object.keys(THREEx.KeyboardState.ALIAS).indexOf(key) != -1) {
            pressed = this.keyCodes[THREEx.KeyboardState.ALIAS[key]];
        } else {
            pressed = this.keyCodes[key.toUpperCase().charCodeAt(0)]
        }
        if (!pressed)
            return false;
    };
    return true;
}

/**
 * return true if an event match a keyDesc
 * @param  {KeyboardEvent} event   keyboard event
 * @param  {String} keyDesc string description of the key
 * @return {Boolean}         true if the event match keyDesc, false otherwise
 */
THREEx.KeyboardState.prototype.eventMatches = function (event, keyDesc) {
    var aliases = THREEx.KeyboardState.ALIAS
    var aliasKeys = Object.keys(aliases)
    var keys = keyDesc.split("+")
    // log to debug
    // console.log("eventMatches", event, event.keyCode, event.shiftKey, event.ctrlKey, event.altKey, event.metaKey)
    for (var i = 0; i < keys.length; i++) {
        var key = keys[i];
        var pressed = false;
        if (key === 'shift') {
            pressed = (event.shiftKey ? true : false)
        } else if (key === 'ctrl') {
            pressed = (event.ctrlKey ? true : false)
        } else if (key === 'alt') {
            pressed = (event.altKey ? true : false)
        } else if (key === 'meta') {
            pressed = (event.metaKey ? true : false)
        } else if (aliasKeys.indexOf(key) !== -1) {
            pressed = (event.keyCode === aliases[key] ? true : false);
        } else if (event.keyCode === key.toUpperCase().charCodeAt(0)) {
            pressed = true;
        }
        if (!pressed)
            return false;
    }
    return true;
}

let container, stats, clock, controls;
let lights, camera, scene, renderer, human, humanGeometry, humanMaterial, humanMesh, robot;
let skinWeight, skinIndices, boneArray, realBones, boneDict, centerOfMass;

THREE.Cache.enabled = true;


THREE.Object3D.prototype.setMatrix = function (a) {
    this.matrix = a;
    this.matrix.decompose(this.position, this.quaternion, this.scale);
};


class Robot {

    constructor(h) {
        this.spineLength = 0.65305 ;
        this.chestLength =0.46487;
        this.neckLength = 0.24523
        this.headLength = 0.39284;

        this.armLength = 0.72111;
        this.forearmLength = 0.61242;
        this.legLength = 1.16245;
        this.shinLength = 1.03432;

        this.armLeftRotation = realBones[4].rotation;
        this.forearmLeftRotation = realBones[5].rotation;
        this.armRightRotation  = realBones[6].rotation;
        this.forearmRightRotation = realBones[7].rotation;

        this.legLeftRotation = realBones[8].rotation;
        this.shinLeftRotation = realBones[9].rotation;
        this.legRightRotation = realBones[10].rotation;
        this.shinRightRotation = realBones[11].rotation;

        this.spineTranslation = realBones[0].position;
        this.chestTranslation = realBones[1].position;
        this.neckTranslation = realBones[2].position;
        this.headTranslation = realBones[3].position;
        this.armLeftTranslation = realBones[4].position;
        this.forearmLeftTranslation =  realBones[5].position;
        this.armRightTranslation  = realBones[6].position;
        this.forearmTranslation = realBones[7].position;

        this.legLeftTranslation =  realBones[8].position;
        this.shinLeftTranslation =  realBones[9].position;
        this.legRightTranslation=  realBones[10].position;
        this.shinRightTranslation =  realBones[11].position;


        this.bodyWidth = 0.2;
        this.bodyDepth = 0.2;


        this.neckRadius = 0.1;

        this.headRadius = 0.32;


        this.legRadius = 0.10;
        this.thighRadius = 0.1;
        this.footDepth = 0.4;
        this.footWidth = 0.25;

        this.armRadius = 0.10;

        this.handRadius = 0.1;

        // Material
        this.material = new THREE.MeshNormalMaterial();
        this.human = h;
        // Initial pose
        this.initialize()
    }

    initialize() {
        // Spine geomerty
        var spineGeometry = new THREE.CylinderGeometry(0.5 * this.bodyWidth / 2, this.bodyWidth / 2, this.spineLength, 64);
        if (!this.hasOwnProperty("spine"))
            this.spine = new THREE.Mesh(spineGeometry, this.material);

        var chestGeometry = new THREE.CylinderGeometry(0.5 * this.bodyWidth / 2, this.bodyWidth / 2, this.chestLength, 64);
        if (!this.hasOwnProperty("chest"))
            this.chest = new THREE.Mesh(chestGeometry, this.material);

        // Neck geomerty
        var neckGeometry = new THREE.CylinderGeometry(0.5 * this.neckRadius, this.neckRadius, this.neckLength, 64);
        if (!this.hasOwnProperty("neck"))
            this.neck = new THREE.Mesh(neckGeometry, this.material);

        // Head geomerty
        var headGeometry = new THREE.SphereGeometry(this.headLength / 2, 64, 3);
        if (!this.hasOwnProperty("head"))
            this.head = new THREE.Mesh(headGeometry, this.material);



        // Added Geomtry :

        // ARM RIGHT ___________________________________________________________________________________________________
        var armRightGeometry = new THREE.CylinderGeometry(0.5 * this.armRadius , this.armRadius , this.armLength, 64);
        if (!this.hasOwnProperty("armRight"))
            this.armRight = new THREE.Mesh(armRightGeometry, this.material);

        var forearmRightGeometry = new THREE.CylinderGeometry(0.5 * this.armRadius , this.armRadius , this.forearmLength, 64);
        if (!this.hasOwnProperty("forearmRight"))
            this.forearmRight = new THREE.Mesh(forearmRightGeometry, this.material);

        var handRightGeometry = new THREE.SphereGeometry(this.handRadius , 64, 3);
        if (!this.hasOwnProperty("handRight"))
            this.handRight = new THREE.Mesh(handRightGeometry, this.material);


        // ARM LEFT ____________________________________________________________________________________________________
        var armLeftGeometry = new THREE.CylinderGeometry(0.5 * this.armRadius , this.armRadius , this.armLength, 64);
        if (!this.hasOwnProperty("armLeft"))
            this.armLeft = new THREE.Mesh(armLeftGeometry, this.material);

        var forearmLeftGeometry = new THREE.CylinderGeometry(0.5 * this.armRadius , this.armRadius , this.forearmLength, 64);
        if (!this.hasOwnProperty("forearmLeft"))
            this.forearmLeft = new THREE.Mesh(forearmLeftGeometry, this.material);

        var handLeftGeometry = new THREE.SphereGeometry(this.handRadius , 64, 3);
        if (!this.hasOwnProperty("handLeft"))
            this.handLeft = new THREE.Mesh(handLeftGeometry, this.material);


        // LEG RIGHT ___________________________________________________________________________________________________

        var legRightGeometry = new THREE.CylinderGeometry(0.5 * this.legRadius , this.legRadius , this.legLength, 64);
        if (!this.hasOwnProperty("legRight"))
            this.legRight = new THREE.Mesh(legRightGeometry, this.material);

        var shinRightGeometry = new THREE.CylinderGeometry(0.5 * this.legRadius , this.legRadius , this.shinLength, 64);
        if (!this.hasOwnProperty("shinRight"))
            this.shinRight = new THREE.Mesh(shinRightGeometry, this.material);

        var footRightGeometry = new THREE.BoxGeometry(this.footWidth , 0.1, this.footDepth);
        if (!this.hasOwnProperty("footRight"))
            this.footRight = new THREE.Mesh(footRightGeometry, this.material);


        // LEFT LEG ____________________________________________________________________________________________________
        var legLeftGeometry = new THREE.CylinderGeometry(0.5 * this.legRadius , this.legRadius , this.legLength, 64);
        if (!this.hasOwnProperty("legLeft"))
            this.legLeft = new THREE.Mesh(legLeftGeometry, this.material);

        var shinLeftGeometry = new THREE.CylinderGeometry(0.5 * this.legRadius , this.legRadius , this.shinLength, 64);
        if (!this.hasOwnProperty("shinLeft"))
            this.shinLeft = new THREE.Mesh(shinLeftGeometry, this.material);

        var footLeftGeometry = new THREE.BoxGeometry(this.footWidth , 0.1, this.footDepth);
        if (!this.hasOwnProperty("footLeft"))
            this.footLeft = new THREE.Mesh(footLeftGeometry, this.material);



        // Spine matrix
        this.spineMatrix = new THREE.Matrix4().set(
            1, 0, 0, 0,
            0, 1, 0, this.spineTranslation.y + this.spineLength / 2,
            0, 0, 1, 0,
            0, 0, 0, 1);


        this.chestMatrix = new THREE.Matrix4().set(
            1, 0, 0, 0,
            0, 1, 0, this.chestTranslation.y - this.spineLength / 2 + this.chestLength / 2,
            0, 0, 1, 0,
            0, 0, 0, 1);
        var chestMatrix = new THREE.Matrix4().multiplyMatrices(this.spineMatrix, this.chestMatrix);
        this.chestM = new THREE.Matrix4().multiplyMatrices(this.spineMatrix, this.chestMatrix);

        // Neck matrix
        this.neckMatrix = new THREE.Matrix4().set(
            1, 0, 0, 0,
            0, 1, 0, this.neckTranslation.y - this.chestLength / 2 + this.neckLength / 2,
            0, 0, 1, 0,
            0, 0, 0, 1);
        var neckMatrix = new THREE.Matrix4().multiplyMatrices(chestMatrix, this.neckMatrix);


        // Added Matrix ________________________________________________________________________________________________
        var chestBase = matMul(chestMatrix, translation(0,-this.chestLength - this.neckLength / 2,0))
        this.chestBase = matMul(chestMatrix, translation(0,-this.chestLength - this.neckLength / 2,0))
        var spineBase = matMul(chestMatrix, translation(0, -this.chestLength/ 2 -this.spineLength* 2 ,0));
        this.spineBase = matMul(chestMatrix, translation(0, -this.chestLength/ 2 -this.spineLength* 2 ,0));


        // ARM RIGHT ___________________________________________________________________________________________________
        this.armRightMatrix = matMul(new THREE.Matrix4(),translation(this.armRightTranslation.x * 2 ,this.armRightTranslation.y  ,this.armRightTranslation.z ));
        this.armRightMatrix = matMul(this.armRightMatrix,rotZ(this.armRightRotation.z));
        this.armRightMatrix = matMul(this.armRightMatrix,rotY(this.armRightRotation.y ));
        this.armRightMatrix = matMul(this.armRightMatrix,rotX(this.armRightRotation.x));
        var armRightMatrix = new THREE.Matrix4().multiplyMatrices(this.chestBase, this.armRightMatrix);
        var armRightBase = matMul(armRightMatrix, translation(0.09, this.armLength * 1.9,0.01));
        this.armRightBase = matMul(armRightMatrix, translation(0.09, this.armLength * 1.9,0.01));


        this.forearmRightMatrix = matMul(new THREE.Matrix4(),translation(-this.forearmTranslation.x,-this.forearmTranslation.y ,-this.forearmTranslation.z));
        this.forearmRightMatrix = matMul(this.forearmRightMatrix,rotZ(this.forearmRightRotation.z));
        this.forearmRightMatrix = matMul(this.forearmRightMatrix,rotY(this.forearmRightRotation.y));
        this.forearmRightMatrix = matMul(this.forearmRightMatrix,rotX(this.forearmRightRotation.x));
        var forearmRightMatrix = new THREE.Matrix4().multiplyMatrices(this.armRightBase, this.forearmRightMatrix);
        this.forearmRightBase = matMul(forearmRightMatrix, translation(0, this.forearmLength *1.8,0));

        this.handRightMatrix = matMul(new THREE.Matrix4(),translation(-this.forearmTranslation.x,-this.forearmTranslation.y ,-this.forearmTranslation.z));
        var handRightMatrix = new THREE.Matrix4().multiplyMatrices(this.forearmRightBase, this.handRightMatrix);


        // ARM LEFT ____________________________________________________________________________________________________
        this.armLeftMatrix = matMul(new THREE.Matrix4(),translation(this.armLeftTranslation.x * 2,this.armLeftTranslation.y ,this.armLeftTranslation.z));
        this.armLeftMatrix = matMul(this.armLeftMatrix,rotZ(this.armLeftRotation.z));
        this.armLeftMatrix = matMul(this.armLeftMatrix,rotY(this.armLeftRotation.y));
        this.armLeftMatrix = matMul(this.armLeftMatrix,rotX(this.armLeftRotation.x));
        var armLeftMatrix = new THREE.Matrix4().multiplyMatrices(this.chestBase, this.armLeftMatrix);
        this.armLeftBase = matMul(armLeftMatrix, translation(-0.09, this.armLength * 1.9,-0.01));

        this.forearmLeftMatrix = matMul(new THREE.Matrix4(),translation(-this.forearmLeftTranslation.x,-this.forearmLeftTranslation.y ,-this.forearmLeftTranslation.z));
        this.forearmLeftMatrix = matMul(this.forearmLeftMatrix,rotZ(this.forearmLeftRotation.z));
        this.forearmLeftMatrix = matMul(this.forearmLeftMatrix,rotY(this.forearmLeftRotation.y));
        this.forearmLeftMatrix = matMul(this.forearmLeftMatrix,rotX(this.forearmLeftRotation.x));
        var forearmLeftMatrix = new THREE.Matrix4().multiplyMatrices(this.armLeftBase, this.forearmLeftMatrix);
        this.forearmLeftBase = matMul(forearmLeftMatrix, translation(0,this.forearmLength *1.8 ,0));

        this.handLeftMatrix = matMul(new THREE.Matrix4(),translation(-this.forearmLeftTranslation.x,-this.forearmLeftTranslation.y ,-this.forearmLeftTranslation.z));
        var handLeftMatrix = new THREE.Matrix4().multiplyMatrices(this.forearmLeftBase, this.handLeftMatrix);


        // LEG RIGHT ___________________________________________________________________________________________________
        this.legRightMatrix = matMul(new THREE.Matrix4(),translation(this.legRightTranslation.x *1.5,this.legRightTranslation.y ,this.legRightTranslation.z));
        this.legRightMatrix = matMul(this.legRightMatrix,rotZ(this.legRightRotation.z));
        this.legRightMatrix = matMul(this.legRightMatrix,rotY(this.legRightRotation.y));
        this.legRightMatrix = matMul(this.legRightMatrix,rotX(this.legRightRotation.x));
        var legRightMatrix = new THREE.Matrix4().multiplyMatrices(this.spineBase, this.legRightMatrix);
        this.legRightBase = matMul(legRightMatrix, translation(0, -0.1,-0.1));

        this.shinRightMatrix = matMul(new THREE.Matrix4(),translation(this.shinRightTranslation.x,this.shinRightTranslation.y ,this.shinRightTranslation.z));
        this.shinRightMatrix = matMul(this.shinRightMatrix,rotZ(-this.shinRightRotation.z));
        this.shinRightMatrix = matMul(this.shinRightMatrix,rotY(-this.shinRightRotation.y));
        this.shinRightMatrix = matMul(this.shinRightMatrix,rotX(-this.shinRightRotation.x));
        var shinRightMatrix = new THREE.Matrix4().multiplyMatrices(this.legRightBase, this.shinRightMatrix);
        this.shinRightBase = matMul(shinRightMatrix, translation(0, this.shinLength /2,0));

        this.footRightMatrix =matMul(new THREE.Matrix4(), translation(0,0,0.1));
        var footRightMatrix = new THREE.Matrix4().multiplyMatrices(this.shinRightBase, this.footRightMatrix);


        // LEG LEFT ____________________________________________________________________________________________________
        this.legLeftMatrix = matMul(new THREE.Matrix4(),translation(this.legLeftTranslation.x * 1.5,this.legLeftTranslation.y ,this.legLeftTranslation.z));
        this.legLeftMatrix = matMul(this.legLeftMatrix,rotZ(this.legLeftRotation.z));
        this.legLeftMatrix = matMul(this.legLeftMatrix,rotY(this.legLeftRotation.y));
        this.legLeftMatrix = matMul(this.legLeftMatrix,rotX(this.legLeftRotation.x));
        var legLeftMatrix = new THREE.Matrix4().multiplyMatrices(this.spineBase, this.legLeftMatrix);
        var legLeftBase = matMul(legLeftMatrix, translation(0,-0.1 ,-0.1));
        this.legLeftBase = matMul(legLeftMatrix, translation(0,-0.1 ,-0.1));

        this.shinLeftMatrix = matMul(new THREE.Matrix4(),translation(this.shinLeftTranslation.x,this.shinLeftTranslation.y ,this.shinLeftTranslation.z));
        this.shinLeftMatrix = matMul(this.shinLeftMatrix,rotZ(-this.shinLeftRotation.z));
        this.shinLeftMatrix = matMul(this.shinLeftMatrix,rotY(-this.shinLeftRotation.y));
        this.shinLeftMatrix = matMul(this.shinLeftMatrix,rotX(-this.shinLeftRotation.x));
        var shinLeftMatrix = new THREE.Matrix4().multiplyMatrices(this.legLeftBase, this.shinLeftMatrix);
        this.shinLeftBase = matMul(shinLeftMatrix, translation(0,this.shinLength /2 ,0));

        this.footLeftMatrix = matMul(new THREE.Matrix4(), translation(0,0,0.1));
        var footLeftMatrix = new THREE.Matrix4().multiplyMatrices(this.shinLeftBase, this.footLeftMatrix);

        // Head matrix
        this.headMatrix = new THREE.Matrix4().set(
            1, 0, 0, 0,
            0, 1, 0, this.headTranslation.y-this.neckLength/2+this.headLength/2,
            0, 0, 1, 0,
            0, 0, 0, 1);
        var headMatrix = new THREE.Matrix4().multiplyMatrices(neckMatrix, this.headMatrix);


        // Apply transformation
        this.spine.setMatrix(this.spineMatrix);
        if (scene.getObjectById(this.spine.id) === undefined)
            scene.add(this.spine);

        this.chest.setMatrix(chestMatrix);
        if (scene.getObjectById(this.chest.id) === undefined)
            scene.add(this.chest);

        this.neck.setMatrix(neckMatrix);
        if (scene.getObjectById(this.neck.id) === undefined)
            scene.add(this.neck);

        this.head.setMatrix(headMatrix);
        if (scene.getObjectById(this.head.id) === undefined)
            scene.add(this.head);

        // ADDED transformations _______________________________________________________________________________________
        this.armRight.setMatrix(armRightMatrix);
        if (scene.getObjectById(this.armRight.id) === undefined)
            scene.add(this.armRight);
        this.forearmRight.setMatrix(forearmRightMatrix);
        if (scene.getObjectById(this.forearmRight.id) === undefined)               //RIGHT HAND
            scene.add(this.forearmRight);
        this.handRight.setMatrix(handRightMatrix);
        if (scene.getObjectById(this.handRight.id) === undefined)
            scene.add(this.handRight);


        this.armLeft.setMatrix(armLeftMatrix);
        if (scene.getObjectById(this.armLeft.id) === undefined)
            scene.add(this.armLeft);
        this.forearmLeft.setMatrix(forearmLeftMatrix);
        if (scene.getObjectById(this.forearmLeft.id) === undefined)                //LEFT HAND
            scene.add(this.forearmLeft);
        this.handLeft.setMatrix(handLeftMatrix);
        if (scene.getObjectById(this.handLeft.id) === undefined)
            scene.add(this.handLeft);


        this.legRight.setMatrix(legRightMatrix);
        if (scene.getObjectById(this.legRight.id) === undefined)
            scene.add(this.legRight);
        this.shinRight.setMatrix(shinRightMatrix);
        if (scene.getObjectById(this.shinRight.id) === undefined)                  //RIGHT LEG
            scene.add(this.shinRight);
        this.footRight.setMatrix(footRightMatrix);
        if (scene.getObjectById(this.footRight.id) === undefined)
            scene.add(this.footRight);


        this.legLeft.setMatrix(legLeftMatrix);
        if (scene.getObjectById(this.legLeft.id) === undefined)
            scene.add(this.legLeft);
        this.shinLeft.setMatrix(shinLeftMatrix);
        if (scene.getObjectById(this.shinLeft.id) === undefined)                   //LEFT LEG
            scene.add(this.shinLeft);
        this.footLeft.setMatrix(footLeftMatrix);
        if (scene.getObjectById(this.footLeft.id) === undefined)
            scene.add(this.footLeft);
    }
    hideRobot() {
        this.spine.visible = false;
        this.chest.visible = false;
        this.neck.visible = false;
        this.head.visible = false;

        this.armLeft.visible = false;
        this.armRight.visible = false;
        this.forearmRight.visible = false;
        this.forearmLeft.visible = false;
        this.handRight.visible = false;
        this.handLeft.visible = false;

        this.legRight.visible = false;
        this.legLeft.visible = false;
        this.shinRight.visible = false;
        this.shinLeft.visible = false;
        this.footRight.visible = false;
        this.footLeft.visible = false;
    }
    hideHuman() {
        this.human.visible = false;
    }

    showRobot() {
        this.spine.visible = true;
        this.chest.visible = true;
        this.neck.visible = true;
        this.head.visible = true;

        this.armLeft.visible = true;
        this.armRight.visible = true;
        this.forearmRight.visible = true;
        this.forearmLeft.visible = true;
        this.handRight.visible = true;
        this.handLeft.visible = true;

        this.legRight.visible = true;
        this.legLeft.visible = true;
        this.shinRight.visible = true;
        this.shinLeft.visible = true;
        this.footRight.visible = true;
        this.footLeft.visible = true;
    }
    showHuman() {
        this.human.visible = true;
    }

    pose1(){
                                // MATRIX EDIT
        // R ARM
        this.armRightMatrix = matMul(new THREE.Matrix4(),translation(this.armRightTranslation.x * 2 ,this.armRightTranslation.y + this.chestLength*2  ,this.armRightTranslation.z ));
        this.armRightMatrix = matMul(this.armRightMatrix,rotZ(this.armRightRotation.z-1.5));
        this.armRightMatrix = matMul(this.armRightMatrix,rotY(this.armRightRotation.y ));
        this.armRightMatrix = matMul(this.armRightMatrix,rotX(this.armRightRotation.x));
        var armRightMatrix = new THREE.Matrix4().multiplyMatrices(this.chestBase, this.armRightMatrix);
        this.armRightBase = matMul(armRightMatrix, translation(0.09, this.armLength * 1.9,0.01));

        this.forearmRightMatrix = matMul(new THREE.Matrix4(),translation(-this.forearmTranslation.x,-this.forearmTranslation.y ,-this.forearmTranslation.z));
        this.forearmRightMatrix = matMul(this.forearmRightMatrix,rotZ(this.forearmRightRotation.z));
        this.forearmRightMatrix = matMul(this.forearmRightMatrix,rotY(this.forearmRightRotation.y));
        this.forearmRightMatrix = matMul(this.forearmRightMatrix,rotX(this.forearmRightRotation.x));
        var forearmRightMatrix = new THREE.Matrix4().multiplyMatrices(this.armRightBase, this.forearmRightMatrix);
        this.forearmRightBase = matMul(forearmRightMatrix, translation(0, this.forearmLength *1.8,0));

        this.handRightMatrix = matMul(new THREE.Matrix4(),translation(-this.forearmTranslation.x,-this.forearmTranslation.y ,-this.forearmTranslation.z));
        var handRightMatrix = new THREE.Matrix4().multiplyMatrices(this.forearmRightBase, this.handRightMatrix);

        // L ARM
        this.armLeftMatrix = matMul(new THREE.Matrix4(),translation(this.armLeftTranslation.x * 2,this.armLeftTranslation.y + this.chestLength*2,this.armLeftTranslation.z));
        this.armLeftMatrix = matMul(this.armLeftMatrix,rotZ(this.armLeftRotation.z+1.5));
        this.armLeftMatrix = matMul(this.armLeftMatrix,rotY(this.armLeftRotation.y));
        this.armLeftMatrix = matMul(this.armLeftMatrix,rotX(this.armLeftRotation.x));
        var armLeftMatrix = new THREE.Matrix4().multiplyMatrices(this.chestBase, this.armLeftMatrix);
        this.armLeftBase = matMul(armLeftMatrix, translation(-0.09, this.armLength * 1.9,-0.01));

        this.forearmLeftMatrix = matMul(new THREE.Matrix4(),translation(-this.forearmLeftTranslation.x,-this.forearmLeftTranslation.y ,-this.forearmLeftTranslation.z));
        this.forearmLeftMatrix = matMul(this.forearmLeftMatrix,rotZ(this.forearmLeftRotation.z));
        this.forearmLeftMatrix = matMul(this.forearmLeftMatrix,rotY(this.forearmLeftRotation.y));
        this.forearmLeftMatrix = matMul(this.forearmLeftMatrix,rotX(this.forearmLeftRotation.x));
        var forearmLeftMatrix = new THREE.Matrix4().multiplyMatrices(this.armLeftBase, this.forearmLeftMatrix);
        this.forearmLeftBase = matMul(forearmLeftMatrix, translation(0,this.forearmLength *1.8 ,0));

        this.handLeftMatrix = matMul(new THREE.Matrix4(),translation(-this.forearmLeftTranslation.x,-this.forearmLeftTranslation.y ,-this.forearmLeftTranslation.z));
        var handLeftMatrix = new THREE.Matrix4().multiplyMatrices(this.forearmLeftBase, this.handLeftMatrix);

        // R LEG
        this.legRightMatrix = matMul(new THREE.Matrix4(),translation(this.legRightTranslation.x *1.5,this.legRightTranslation.y ,this.legRightTranslation.z));
        this.legRightMatrix = matMul(this.legRightMatrix,rotZ(this.legRightRotation.z));
        this.legRightMatrix = matMul(this.legRightMatrix,rotY(this.legRightRotation.y));
        this.legRightMatrix = matMul(this.legRightMatrix,rotX(this.legRightRotation.x));
        var legRightMatrix = new THREE.Matrix4().multiplyMatrices(this.spineBase, this.legRightMatrix);
        this.legRightBase = matMul(legRightMatrix, translation(0, -0.1,-0.1));

        this.shinRightMatrix = matMul(new THREE.Matrix4(),translation(this.shinRightTranslation.x,this.shinRightTranslation.y- this.shinLength/2 ,this.shinRightTranslation.z-0.5));
        this.shinRightMatrix = matMul(this.shinRightMatrix,rotZ(-this.shinRightRotation.z));
        this.shinRightMatrix = matMul(this.shinRightMatrix,rotY(-this.shinRightRotation.y));
        this.shinRightMatrix = matMul(this.shinRightMatrix,rotX(-this.shinRightRotation.x+1.5));
        var shinRightMatrix = new THREE.Matrix4().multiplyMatrices(this.legRightBase, this.shinRightMatrix);
        this.shinRightBase = matMul(shinRightMatrix, translation(0, this.shinLength /2,0));

        this.footRightMatrix =matMul(new THREE.Matrix4(), translation(0,0,0.1));
        var footRightMatrix = new THREE.Matrix4().multiplyMatrices(this.shinRightBase, this.footRightMatrix);

        // L LEG
        this.legLeftMatrix = matMul(new THREE.Matrix4(),translation(this.legLeftTranslation.x * 1.5,this.legLeftTranslation.y ,this.legLeftTranslation.z));
        this.legLeftMatrix = matMul(this.legLeftMatrix,rotZ(this.legLeftRotation.z));
        this.legLeftMatrix = matMul(this.legLeftMatrix,rotY(this.legLeftRotation.y));
        this.legLeftMatrix = matMul(this.legLeftMatrix,rotX(this.legLeftRotation.x));
        var legLeftMatrix = new THREE.Matrix4().multiplyMatrices(this.spineBase, this.legLeftMatrix);
        var legLeftBase = matMul(legLeftMatrix, translation(0,-0.1 ,-0.1));
        this.legLeftBase = matMul(legLeftMatrix, translation(0,-0.1 ,-0.1));

        this.shinLeftMatrix = matMul(new THREE.Matrix4(),translation(this.shinLeftTranslation.x,this.shinLeftTranslation.y - this.shinLength/2 ,this.shinLeftTranslation.z-0.5));
        this.shinLeftMatrix = matMul(this.shinLeftMatrix,rotZ(-this.shinLeftRotation.z));
        this.shinLeftMatrix = matMul(this.shinLeftMatrix,rotY(-this.shinLeftRotation.y ));
        this.shinLeftMatrix = matMul(this.shinLeftMatrix,rotX(-this.shinLeftRotation.x + 1.5));
        var shinLeftMatrix = new THREE.Matrix4().multiplyMatrices(this.legLeftBase, this.shinLeftMatrix);
        this.shinLeftBase = matMul(shinLeftMatrix, translation(0,this.shinLength /2 ,0));

        this.footLeftMatrix = matMul(new THREE.Matrix4(), translation(0,0,0.1));
        var footLeftMatrix = new THREE.Matrix4().multiplyMatrices(this.shinLeftBase, this.footLeftMatrix);


        // Apply Transformations
        this.armRight.setMatrix(armRightMatrix);
        this.forearmRight.setMatrix(forearmRightMatrix);  //R ARM
        this.handRight.setMatrix(handRightMatrix);

        this.armLeft.setMatrix(armLeftMatrix);
        this.forearmLeft.setMatrix(forearmLeftMatrix);   //L ARM
        this.handLeft.setMatrix(handLeftMatrix);

        this.legLeft.setMatrix(legLeftMatrix);
        this.shinLeft.setMatrix(shinLeftMatrix);         //L LEG
        this.footLeft.setMatrix(footLeftMatrix);

        this.legRight.setMatrix(legRightMatrix);
        this.shinRight.setMatrix(shinRightMatrix);       //R LEG
        this.footRight.setMatrix(footRightMatrix);
    }

    pose2(){
        // R ARM
        this.armRightMatrix = matMul(new THREE.Matrix4(),translation(this.armRightTranslation.x * 2 ,this.armRightTranslation.y  ,this.armRightTranslation.z ));
        this.armRightMatrix = matMul(this.armRightMatrix,rotZ(this.armRightRotation.z+0.55));
        this.armRightMatrix = matMul(this.armRightMatrix,rotY(this.armRightRotation.y ));
        this.armRightMatrix = matMul(this.armRightMatrix,rotX(this.armRightRotation.x));
        var armRightMatrix = new THREE.Matrix4().multiplyMatrices(this.chestBase, this.armRightMatrix);
        this.armRightBase = matMul(armRightMatrix, translation(0.09, this.armLength * 1.9,0.01));

        this.forearmRightMatrix = matMul(new THREE.Matrix4(),translation(-this.forearmTranslation.x,-this.forearmTranslation.y ,-this.forearmTranslation.z));
        this.forearmRightMatrix = matMul(this.forearmRightMatrix,rotZ(this.forearmRightRotation.z));
        this.forearmRightMatrix = matMul(this.forearmRightMatrix,rotY(this.forearmRightRotation.y));
        this.forearmRightMatrix = matMul(this.forearmRightMatrix,rotX(this.forearmRightRotation.x));
        var forearmRightMatrix = new THREE.Matrix4().multiplyMatrices(this.armRightBase, this.forearmRightMatrix);
        this.forearmRightBase = matMul(forearmRightMatrix, translation(0, this.forearmLength *1.8,0));

        this.handRightMatrix = matMul(new THREE.Matrix4(),translation(-this.forearmTranslation.x,-this.forearmTranslation.y ,-this.forearmTranslation.z));
        var handRightMatrix = new THREE.Matrix4().multiplyMatrices(this.forearmRightBase, this.handRightMatrix);

        // L ARM
        this.armLeftMatrix = matMul(new THREE.Matrix4(),translation(this.armLeftTranslation.x * 2,this.armLeftTranslation.y + this.chestLength ,this.armLeftTranslation.z));
        this.armLeftMatrix = matMul(this.armLeftMatrix,rotZ(this.armLeftRotation.z+2));
        this.armLeftMatrix = matMul(this.armLeftMatrix,rotY(this.armLeftRotation.y));
        this.armLeftMatrix = matMul(this.armLeftMatrix,rotX(this.armLeftRotation.x+0.5));
        var armLeftMatrix = new THREE.Matrix4().multiplyMatrices(this.chestBase, this.armLeftMatrix);
        this.armLeftBase = matMul(armLeftMatrix, translation(-0.09, this.armLength * 1.9,-0.01));

        this.forearmLeftMatrix = matMul(new THREE.Matrix4(),translation(-this.forearmLeftTranslation.x,-this.forearmLeftTranslation.y ,-this.forearmLeftTranslation.z));
        this.forearmLeftMatrix = matMul(this.forearmLeftMatrix,rotZ(this.forearmLeftRotation.z+1.5));
        this.forearmLeftMatrix = matMul(this.forearmLeftMatrix,rotY(this.forearmLeftRotation.y));
        this.forearmLeftMatrix = matMul(this.forearmLeftMatrix,rotX(this.forearmLeftRotation.x));
        var forearmLeftMatrix = new THREE.Matrix4().multiplyMatrices(this.armLeftBase, this.forearmLeftMatrix);
        this.forearmLeftBase = matMul(forearmLeftMatrix, translation(0,this.forearmLength *1.8 ,0));

        this.handLeftMatrix = matMul(new THREE.Matrix4(),translation(-this.forearmLeftTranslation.x,-this.forearmLeftTranslation.y ,-this.forearmLeftTranslation.z));
        var handLeftMatrix = new THREE.Matrix4().multiplyMatrices(this.forearmLeftBase, this.handLeftMatrix);

        // R LEG
        this.legRightMatrix = matMul(new THREE.Matrix4(),translation(this.legRightTranslation.x *1.5,this.legRightTranslation.y +0.5,this.legRightTranslation.z+0.5));
        this.legRightMatrix = matMul(this.legRightMatrix,rotZ(this.legRightRotation.z));
        this.legRightMatrix = matMul(this.legRightMatrix,rotY(this.legRightRotation.y));
        this.legRightMatrix = matMul(this.legRightMatrix,rotX(this.legRightRotation.x-1.3));
        var legRightMatrix = new THREE.Matrix4().multiplyMatrices(this.spineBase, this.legRightMatrix);
        this.legRightBase = matMul(legRightMatrix, translation(0, -0.1,-0.1));

        this.shinRightMatrix = matMul(new THREE.Matrix4(),translation(this.shinRightTranslation.x,this.shinRightTranslation.y -0.2 ,this.shinRightTranslation.z -0.3));
        this.shinRightMatrix = matMul(this.shinRightMatrix,rotZ(-this.shinRightRotation.z));
        this.shinRightMatrix = matMul(this.shinRightMatrix,rotY(-this.shinRightRotation.y));
        this.shinRightMatrix = matMul(this.shinRightMatrix,rotX(-this.shinRightRotation.x+0.6));
        var shinRightMatrix = new THREE.Matrix4().multiplyMatrices(this.legRightBase, this.shinRightMatrix);
        this.shinRightBase = matMul(shinRightMatrix, translation(0, this.shinLength /2,0));

        this.footRightMatrix =matMul(new THREE.Matrix4(), translation(0,0,0.1));
        var footRightMatrix = new THREE.Matrix4().multiplyMatrices(this.shinRightBase, this.footRightMatrix);

        // L LEG
        this.legLeftMatrix = matMul(new THREE.Matrix4(),translation(this.legLeftTranslation.x * 1.5,this.legLeftTranslation.y + 0.5 ,this.legLeftTranslation.z +0.5));
        this.legLeftMatrix = matMul(this.legLeftMatrix,rotZ(this.legLeftRotation.z));
        this.legLeftMatrix = matMul(this.legLeftMatrix,rotY(this.legLeftRotation.y));
        this.legLeftMatrix = matMul(this.legLeftMatrix,rotX(this.legLeftRotation.x-1.3));
        var legLeftMatrix = new THREE.Matrix4().multiplyMatrices(this.spineBase, this.legLeftMatrix);
        this.legLeftBase = matMul(legLeftMatrix, translation(0,-0.1 ,-0.1));

        this.shinLeftMatrix = matMul(new THREE.Matrix4(),translation(this.shinLeftTranslation.x,this.shinLeftTranslation.y -0.2 ,this.shinLeftTranslation.z-0.3));
        this.shinLeftMatrix = matMul(this.shinLeftMatrix,rotZ(-this.shinLeftRotation.z));
        this.shinLeftMatrix = matMul(this.shinLeftMatrix,rotY(-this.shinLeftRotation.y));
        this.shinLeftMatrix = matMul(this.shinLeftMatrix,rotX(-this.shinLeftRotation.x+0.6));
        var shinLeftMatrix = new THREE.Matrix4().multiplyMatrices(this.legLeftBase, this.shinLeftMatrix);
        this.shinLeftBase = matMul(shinLeftMatrix, translation(0,this.shinLength /2 ,0));

        this.footLeftMatrix = matMul(new THREE.Matrix4(), translation(0,0,0.1));
        var footLeftMatrix = new THREE.Matrix4().multiplyMatrices(this.shinLeftBase, this.footLeftMatrix);


        // Apply transformations
        this.armRight.setMatrix(armRightMatrix);
        this.forearmRight.setMatrix(forearmRightMatrix); // R ARM
        this.handRight.setMatrix(handRightMatrix);

        this.armLeft.setMatrix(armLeftMatrix);
        this.forearmLeft.setMatrix(forearmLeftMatrix);   // L ARM
        this.handLeft.setMatrix(handLeftMatrix);


        this.legLeft.setMatrix(legLeftMatrix);
        this.shinLeft.setMatrix(shinLeftMatrix);         // L LEG
        this.footLeft.setMatrix(footLeftMatrix);

        this.legRight.setMatrix(legRightMatrix);
        this.shinRight.setMatrix(shinRightMatrix);       // R LEG
        this.footRight.setMatrix(footRightMatrix);
    }

    animate(t) {
        // Cyclic local var who depend in parameter t : useful to rotate right and left leg
        let a = cos(t);
        let b = cos(t + 7);

        //TOP BODY PART : translate spine (& everything) to the front so its look like the robot is moving foward
        this.spineMatrix = matMul(this.spineMatrix, translation(0,0,Math.abs(b/45)));
        this.spine.setMatrix(this.spineMatrix);

        var chestMatrix = new THREE.Matrix4().multiplyMatrices(this.spineMatrix, this.chestMatrix);
        this.chest.setMatrix(chestMatrix);

        var neckMatrix = new THREE.Matrix4().multiplyMatrices(chestMatrix, this.neckMatrix);
        this.neck.setMatrix(neckMatrix);

        var headMatrix = new THREE.Matrix4().multiplyMatrices(neckMatrix, this.headMatrix);
        this.head.setMatrix(headMatrix);

        // ARMS PART : Arm rotating around so the walk looks more realistic
        this.chestBase = matMul(chestMatrix, translation(0,-this.chestLength - this.neckLength / 2,0))
        this.armLeftMatrix = matMul(this.armLeftMatrix, rotX(a/100));
        var armLeftMatrix = new THREE.Matrix4().multiplyMatrices(this.chestBase, this.armLeftMatrix);
        this.armLeft.setMatrix(armLeftMatrix);

        this.armRightMatrix = matMul(this.armRightMatrix, rotX(b/100));
        var armRightMatrix = new THREE.Matrix4().multiplyMatrices(this.chestBase, this.armRightMatrix);
        this.armRight.setMatrix(armRightMatrix);
        this.armRightBase = matMul(armRightMatrix, translation(0.09, this.armLength * 1.9,0.01));

        this.armLeftBase = matMul(armLeftMatrix, translation(-0.09, this.armLength * 1.9,-0.0));
        var forearmLeftMatrix = new THREE.Matrix4().multiplyMatrices(this.armLeftBase, this.forearmLeftMatrix);
        var forearmRightMatrix = new THREE.Matrix4().multiplyMatrices(this.armRightBase, this.forearmRightMatrix);
        this.forearmLeft.setMatrix(forearmLeftMatrix);
        this.forearmRight.setMatrix(forearmRightMatrix);
        this.forearmRightBase = matMul(forearmRightMatrix, translation(0, this.forearmLength *1.8,0));
        this.forearmLeftBase = matMul(forearmLeftMatrix, translation(0,this.forearmLength *1.8 ,0));

        this.handLeftMatrix = matMul(new THREE.Matrix4(),translation(-this.forearmLeftTranslation.x,-this.forearmLeftTranslation.y ,-this.forearmLeftTranslation.z));
        var handLeftMatrix = new THREE.Matrix4().multiplyMatrices(this.forearmLeftBase, this.handLeftMatrix);
        this.handLeft.setMatrix(handLeftMatrix);

        this.handRightMatrix = matMul(new THREE.Matrix4(),translation(-this.forearmTranslation.x,-this.forearmTranslation.y ,-this.forearmTranslation.z));
        var handRightMatrix = new THREE.Matrix4().multiplyMatrices(this.forearmRightBase, this.handRightMatrix);
        this.handRight.setMatrix(handRightMatrix);

        // Leg parts : Using cos/sin function so the leg go front and back. So the robots looks like walking
        this.spineBase = matMul(chestMatrix, translation(0, -this.chestLength/ 2 -this.spineLength* 2 ,0));
        this.legRightMatrix = matMul(this.legRightMatrix,rotX(b/25));
        let legRightM = matMul(this.spineBase, this.legRightMatrix);
        this.legRight.setMatrix(legRightM);
        this.legRightBase = matMul(legRightM, translation(0, -0.1,-0.1));

        this.shinRightMatrix = matMul(this.shinRightMatrix,rotX(a/800));
        let shinRightM = matMul(this.legRightBase, this.shinRightMatrix);
        this.shinRight.setMatrix(shinRightM);
        this.shinRightBase = matMul(shinRightM, translation(0, this.shinLength /2,0));

        let footRightM = matMul(this.shinRightBase, this.footRightMatrix);
        this.footRight.setMatrix(footRightM);

        this.legLeftMatrix = matMul(this.legLeftMatrix,rotX( -a/27 ));
        let legLeftM = matMul(this.spineBase, this.legLeftMatrix);
        this.legLeft.setMatrix(legLeftM);
        this.legLeftBase = matMul(legLeftM, translation(0, -0.1,-0.1));

        this.shinLeftMatrix = matMul(this.shinLeftMatrix,rotX(b/1600));
        let shinLeftM = matMul(this.legLeftBase, this.shinLeftMatrix);
        this.shinLeft.setMatrix(shinLeftM);

        this.shinLeftBase = matMul(shinLeftM, translation(0, this.shinLength /2,0));
        let footLeftM = matMul(this.shinLeftBase, this.footLeftMatrix);
        this.footLeft.setMatrix(footLeftM);
    }

    // DEFAULT POSITION OF EVERYTHING (Except head, neck, spine & chest 'cause never changed)
    // This function is useful to reset a pose to see the other one.
    /*reset(){
                                            //MATRIX EDIT//
        //R ARM ________________________________________________________________________________________________________
        this.armRightMatrix = matMul(new THREE.Matrix4(),translation(this.armRightTranslation.x * 2 ,this.armRightTranslation.y  ,this.armRightTranslation.z ));
        this.armRightMatrix = matMul(this.armRightMatrix,rotZ(this.armRightRotation.z));
        this.armRightMatrix = matMul(this.armRightMatrix,rotY(this.armRightRotation.y ));
        this.armRightMatrix = matMul(this.armRightMatrix,rotX(this.armRightRotation.x));
        var armRightMatrix = new THREE.Matrix4().multiplyMatrices(this.chestBase, this.armRightMatrix);
        this.armRightBase = matMul(armRightMatrix, translation(0.09, this.armLength * 1.9,0.01));

        this.forearmRightMatrix = matMul(new THREE.Matrix4(),translation(-this.forearmTranslation.x,-this.forearmTranslation.y ,-this.forearmTranslation.z));
        this.forearmRightMatrix = matMul(this.forearmRightMatrix,rotZ(this.forearmRightRotation.z));
        this.forearmRightMatrix = matMul(this.forearmRightMatrix,rotY(this.forearmRightRotation.y));
        this.forearmRightMatrix = matMul(this.forearmRightMatrix,rotX(this.forearmRightRotation.x));
        var forearmRightMatrix = new THREE.Matrix4().multiplyMatrices(this.armRightBase, this.forearmRightMatrix);
        this.forearmRightBase = matMul(forearmRightMatrix, translation(0, this.forearmLength *1.8,0));

        this.handRightMatrix = matMul(new THREE.Matrix4(),translation(-this.forearmTranslation.x,-this.forearmTranslation.y ,-this.forearmTranslation.z));
        var handRightMatrix = new THREE.Matrix4().multiplyMatrices(this.forearmRightBase, this.handRightMatrix);


        // L ARM _______________________________________________________________________________________________________
        this.armLeftMatrix = matMul(new THREE.Matrix4(),translation(this.armLeftTranslation.x * 2,this.armLeftTranslation.y ,this.armLeftTranslation.z));
        this.armLeftMatrix = matMul(this.armLeftMatrix,rotZ(this.armLeftRotation.z));
        this.armLeftMatrix = matMul(this.armLeftMatrix,rotY(this.armLeftRotation.y));
        this.armLeftMatrix = matMul(this.armLeftMatrix,rotX(this.armLeftRotation.x));
        var armLeftMatrix = new THREE.Matrix4().multiplyMatrices(this.chestBase, this.armLeftMatrix);
        this.armLeftBase = matMul(armLeftMatrix, translation(-0.09, this.armLength * 1.9,-0.01));

        this.forearmLeftMatrix = matMul(new THREE.Matrix4(),translation(-this.forearmLeftTranslation.x,-this.forearmLeftTranslation.y ,-this.forearmLeftTranslation.z));
        this.forearmLeftMatrix = matMul(this.forearmLeftMatrix,rotZ(this.forearmLeftRotation.z));
        this.forearmLeftMatrix = matMul(this.forearmLeftMatrix,rotY(this.forearmLeftRotation.y));
        this.forearmLeftMatrix = matMul(this.forearmLeftMatrix,rotX(this.forearmLeftRotation.x));
        var forearmLeftMatrix = new THREE.Matrix4().multiplyMatrices(this.armLeftBase, this.forearmLeftMatrix);
        this.forearmLeftBase = matMul(forearmLeftMatrix, translation(0,this.forearmLength *1.8 ,0));

        this.handLeftMatrix = matMul(new THREE.Matrix4(),translation(-this.forearmLeftTranslation.x,-this.forearmLeftTranslation.y ,-this.forearmLeftTranslation.z));
        var handLeftMatrix = new THREE.Matrix4().multiplyMatrices(this.forearmLeftBase, this.handLeftMatrix);

        // R LEG _______________________________________________________________________________________________________
        this.legRightMatrix = matMul(new THREE.Matrix4(),translation(this.legRightTranslation.x *1.5,this.legRightTranslation.y ,this.legRightTranslation.z));
        this.legRightMatrix = matMul(this.legRightMatrix,rotZ(this.legRightRotation.z));
        this.legRightMatrix = matMul(this.legRightMatrix,rotY(this.legRightRotation.y));
        this.legRightMatrix = matMul(this.legRightMatrix,rotX(this.legRightRotation.x));
        var legRightMatrix = new THREE.Matrix4().multiplyMatrices(this.spineBase, this.legRightMatrix);
        this.legRightBase = matMul(legRightMatrix, translation(0, -0.1,-0.1));

        this.shinRightMatrix = matMul(new THREE.Matrix4(),translation(this.shinRightTranslation.x,this.shinRightTranslation.y ,this.shinRightTranslation.z));
        this.shinRightMatrix = matMul(this.shinRightMatrix,rotZ(-this.shinRightRotation.z));
        this.shinRightMatrix = matMul(this.shinRightMatrix,rotY(-this.shinRightRotation.y));
        this.shinRightMatrix = matMul(this.shinRightMatrix,rotX(-this.shinRightRotation.x));
        var shinRightMatrix = new THREE.Matrix4().multiplyMatrices(this.legRightBase, this.shinRightMatrix);
        this.shinRightBase = matMul(shinRightMatrix, translation(0, this.shinLength /2,0));

        this.footRightMatrix =matMul(new THREE.Matrix4(), translation(0,0,0.1));
        var footRightMatrix = new THREE.Matrix4().multiplyMatrices(this.shinRightBase, this.footRightMatrix);


        // L LEG _______________________________________________________________________________________________________
        this.legLeftMatrix = matMul(new THREE.Matrix4(),translation(this.legLeftTranslation.x * 1.5,this.legLeftTranslation.y ,this.legLeftTranslation.z));
        this.legLeftMatrix = matMul(this.legLeftMatrix,rotZ(this.legLeftRotation.z));
        this.legLeftMatrix = matMul(this.legLeftMatrix,rotY(this.legLeftRotation.y));
        this.legLeftMatrix = matMul(this.legLeftMatrix,rotX(this.legLeftRotation.x));
        var legLeftMatrix = new THREE.Matrix4().multiplyMatrices(this.spineBase, this.legLeftMatrix);
        this.legLeftBase = matMul(legLeftMatrix, translation(0,-0.1 ,-0.1));

        this.shinLeftMatrix = matMul(new THREE.Matrix4(),translation(this.shinLeftTranslation.x,this.shinLeftTranslation.y ,this.shinLeftTranslation.z));
        this.shinLeftMatrix = matMul(this.shinLeftMatrix,rotZ(-this.shinLeftRotation.z));
        this.shinLeftMatrix = matMul(this.shinLeftMatrix,rotY(-this.shinLeftRotation.y));
        this.shinLeftMatrix = matMul(this.shinLeftMatrix,rotX(-this.shinLeftRotation.x));
        var shinLeftMatrix = new THREE.Matrix4().multiplyMatrices(this.legLeftBase, this.shinLeftMatrix);
        this.shinLeftBase = matMul(shinLeftMatrix, translation(0,this.shinLength /2 ,0));

        this.footLeftMatrix = matMul(new THREE.Matrix4(), translation(0,0,0.1));
        var footLeftMatrix = new THREE.Matrix4().multiplyMatrices(this.shinLeftBase, this.footLeftMatrix);


        // APPLY transformation
        this.armRight.setMatrix(armRightMatrix);
        this.forearmRight.setMatrix(forearmRightMatrix);      //R ARM
        this.handRight.setMatrix(handRightMatrix);

        this.armLeft.setMatrix(armLeftMatrix);
        this.forearmLeft.setMatrix(forearmLeftMatrix);        //L ARM
        this.handLeft.setMatrix(handLeftMatrix);


        this.legLeft.setMatrix(legLeftMatrix);
        this.shinLeft.setMatrix(shinLeftMatrix);              //L LEG
        this.footLeft.setMatrix(footLeftMatrix);

        this.legRight.setMatrix(legRightMatrix);
        this.shinRight.setMatrix(shinRightMatrix);            //R LEG
        this.footRight.setMatrix(footRightMatrix);
    }*/
}

var keyboard = new THREEx.KeyboardState();
var channel = 'p';
var pi = Math.PI;

function init() {

    container = document.getElementById('container');

    camera = new THREE.PerspectiveCamera(45, window.innerWidth / window.innerHeight, 0.1, 2000);
    camera.position.set(8, 10, 8);
    camera.lookAt(0, 3, 0);

    scene = new THREE.Scene();
    scene.add(camera);

    controls = new OrbitControls(camera, container);
    controls.damping = 0.2;

    clock = new THREE.Clock();

    boneDict = {}

    boneArray = new Float32Array(12 * 16);

    humanMaterial = new THREE.ShaderMaterial({
        uniforms: {
            bones: {
                value: boneArray
            }
        }
    });

    const shaderLoader = new THREE.FileLoader();
    shaderLoader.load('glsl/human.vs.glsl',
        function (data) {
            humanMaterial.vertexShader = data;
        })
    shaderLoader.load('glsl/human.fs.glsl',
        function (data) {
            humanMaterial.fragmentShader = data;
        })

    // loading manager

    const loadingManager = new THREE.LoadingManager(function () {
        scene.add(humanMesh);
    });

    // collada
    humanGeometry = new THREE.BufferGeometry();
    const loader = new ColladaLoader(loadingManager);
    loader.load('./model/human.dae', function (collada) {
        skinIndices = collada.library.geometries['human-mesh'].build.triangles.data.attributes.skinIndex.array;
        skinWeight = collada.library.geometries['human-mesh'].build.triangles.data.attributes.skinWeight.array;
        realBones = collada.library.nodes.human.build.skeleton.bones;

        buildSkeleton();
        buildShaderBoneMatrix();
        humanGeometry.setAttribute('position', new THREE.BufferAttribute(collada.library.geometries['human-mesh'].build.triangles.data.attributes.position.array, 3));
        humanGeometry.setAttribute('skinWeight', new THREE.BufferAttribute(skinWeight, 4));
        humanGeometry.setAttribute('skinIndex', new THREE.BufferAttribute(skinIndices, 4));
        humanGeometry.setAttribute('normal', new THREE.BufferAttribute(collada.library.geometries['human-mesh'].build.triangles.data.attributes.normal.array, 3));

        humanMesh = new THREE.Mesh(humanGeometry, humanMaterial);
        robot = new Robot(humanMesh);
        robot.hideHuman();

    });

    //

    const ambientLight = new THREE.AmbientLight(0xcccccc, 0.4);
    scene.add(ambientLight);

    const directionalLight = new THREE.DirectionalLight(0xffffff, 0.8);
    directionalLight.position.set(1, 1, 0).normalize();
    scene.add(directionalLight);

    //

    renderer = new THREE.WebGLRenderer();
    renderer.setPixelRatio(window.devicePixelRatio);
    renderer.setSize(window.innerWidth, window.innerHeight);
    container.appendChild(renderer.domElement);

    //

    stats = new Stats();
    container.appendChild(stats.dom);

    //

    window.addEventListener('resize', onWindowResize);
    lights = [];
    lights[0] = new THREE.PointLight(0xffffff, 1, 0);
    lights[1] = new THREE.PointLight(0xffffff, 1, 0);
    lights[2] = new THREE.PointLight(0xffffff, 1, 0);

    lights[0].position.set(0, 200, 0);
    lights[1].position.set(100, 200, 100);
    lights[2].position.set( - 100,  - 200,  - 100);

    scene.add(lights[0]);
    scene.add(lights[1]);
    scene.add(lights[2]);

    var floorTexture = new THREE.ImageUtils.loadTexture('textures/hardwood2_diffuse.jpg');
    floorTexture.wrapS = floorTexture.wrapT = THREE.RepeatWrapping;
    floorTexture.repeat.set(4, 4);

    var floorMaterial = new THREE.MeshBasicMaterial({
        map: floorTexture,
        side: THREE.DoubleSide
    });
    var floorGeometry = new THREE.PlaneBufferGeometry(30, 30);
    var floor = new THREE.Mesh(floorGeometry, floorMaterial);
    floor.rotation.x = Math.PI / 2;
    floor.position.y -= 2.5;
    scene.add(floor);

}


function buildSkeleton() {
    boneDict["Spine"] = new THREE.Bone();
    boneDict["Chest"] = new THREE.Bone();
    boneDict["Neck"] = new THREE.Bone();
    boneDict["Head"] = new THREE.Bone();
    boneDict["Arm_L"] = new THREE.Bone();
    boneDict["Forearm_L"] = new THREE.Bone();
    boneDict["Arm_R"] = new THREE.Bone();
    boneDict["Forearm_R"] = new THREE.Bone();
    boneDict["Leg_L"] = new THREE.Bone();
    boneDict["Shin_L"] = new THREE.Bone();
    boneDict["Leg_R"] = new THREE.Bone();
    boneDict["Shin_R"] = new THREE.Bone();


    boneDict['Chest'].matrixWorld = matMul(boneDict['Spine'].matrixWorld, realBones[1].matrix);
    boneDict['Neck'].matrixWorld = matMul(boneDict['Chest'].matrixWorld, realBones[2].matrix);
    boneDict['Head'].matrixWorld = matMul(boneDict['Neck'].matrixWorld, realBones[3].matrix);
    boneDict['Arm_L'].matrixWorld = matMul(boneDict['Chest'].matrixWorld, realBones[4].matrix);
    boneDict['Forearm_L'].matrixWorld = matMul(boneDict['Arm_L'].matrixWorld, realBones[5].matrix);
    boneDict['Arm_R'].matrixWorld = matMul(boneDict['Chest'].matrixWorld, realBones[6].matrix);
    boneDict['Forearm_R'].matrixWorld = matMul(boneDict['Arm_R'].matrixWorld, realBones[7].matrix);
    boneDict['Leg_L'].matrixWorld = matMul(boneDict['Spine'].matrixWorld, realBones[8].matrix);
    boneDict['Shin_L'].matrixWorld = matMul(boneDict['Leg_L'].matrixWorld, realBones[9].matrix);
    boneDict['Leg_R'].matrixWorld = matMul(boneDict['Spine'].matrixWorld, realBones[10].matrix);
    boneDict['Shin_R'].matrixWorld = matMul(boneDict['Leg_R'].matrixWorld, realBones[11].matrix);

}

/**
 * Fills the Float32Array boneArray with the bone matrices to be passed to
 * the vertex shader
 */
function buildShaderBoneMatrix() {
    var c = 0;
    for (var key in boneDict) {
        for (var i = 0; i < 16; i++) {
            boneArray[c++] = boneDict[key].matrix.elements[i];
        }
    }
}

function onWindowResize() {

    camera.aspect = window.innerWidth / window.innerHeight;
    camera.updateProjectionMatrix();

    renderer.setSize(window.innerWidth, window.innerHeight);

}

function animate() {

    checkKeyboard();

    updateBody();
    requestAnimationFrame(animate);
    render();
    stats.update();

}

function render() {

    const delta = clock.getDelta();

    renderer.render(scene, camera);


}

/**
 * Returns a new Matrix4 as a multiplcation of m1 and m2
 *
 * @param {Matrix4} m1 The first matrix
 * @param {Matrix4} m2 The second matrix
 * @return {Matrix4} m1 x m2
 */
function matMul(m1, m2) {
    return new THREE.Matrix4().multiplyMatrices(m1, m2);
}

/**
 * Returns a new Matrix4 as a scalar multiplcation of s and m
 *
 * @param {number} s The scalar
 * @param {Matrix4} m The  matrix
 * @return {Matrix4} s * m2
 */
function scalarMul(s, m) {
    var r = m;
    return r.multiplyScalar(s)
}

/**
 * Returns an array containing the x,y and z translation component
 * of a transformation matrix
 *
 * @param {Matrix4} M The transformation matrix
 * @return {Array} x,y,z translation components
 */
function getTranslationValues(M) {
    var elems = M.elements;
    return elems.slice(12, 15);
}

/**
 * Returns a new Matrix4 as a translation matrix of [x,y,z]
 *
 * @param {number} x x component
 * @param {number} y y component
 * @param {number} z z component
 * @return {Matrix4} The translation matrix of [x,y,z]
 */
function translation(x, y, z) {
    let mat = new THREE.Matrix4();
    mat.set(1,0,0,x,
        0,1,0,y,
        0,0,1,z,
        0,0,0,1);
    return mat;

}

/**
 * Returns a new Matrix4 as a rotation matrix of theta radians around the x-axis
 *
 * @param {number} theta The angle expressed in radians
 * @return {Matrix4} The rotation matrix of theta rad around the x-axis
 */
function rotX(theta) {
    let mat = new THREE.Matrix4();
    mat.set(1,0,0,0,
        0,cos(theta),sin(theta),0,
        0,-1 * sin(theta),cos(theta),0,
        0,0,0,1);
    return mat;
}

/**
 * Returns a new Matrix4 as a rotation matrix of theta radians around the y-axis
 *
 * @param {number} theta The angle expressed in radians
 * @return {Matrix4} The rotation matrix of theta rad around the y-axis
 */
function rotY(theta) {
    let mat = new THREE.Matrix4();
    mat.set(cos(theta),0, -1 * sin(theta),0,
        0,1,0,0,
        sin(theta),0,cos(theta),0,
        0,0,0,1);
    return mat;
}

/**
 * Returns a new Matrix4 as a rotation matrix of theta radians around the z-axis
 *
 * @param {number} theta The angle expressed in radians
 * @return {Matrix4} The rotation matrix of theta rad around the z-axis
 */
function rotZ(theta) {
    let mat = new THREE.Matrix4();
    mat.set(cos(theta),sin(theta) * -1,0,0,
        sin(theta),cos(theta),0,0,
        0,0,1,0,
        0,0,0,1);
    return mat;
}

/**
 * Returns a new Matrix4 as a scaling matrix with factors of x,y,z
 *
 * @param {number} x x component
 * @param {number} y y component
 * @param {number} z z component
 * @return {Matrix4} The scaling matrix with factors of x,y,z
 */
function scale(x, y, z) {
    let mat = new THREE.Matrix4();
    mat.set(x,0,0,0,
        0,y,0,0,
        0,0,z,0,
        0,0,0,1);
    return mat;

}

function cos(angle) {
    return Math.cos(angle);
}

function sin(angle) {
    return Math.sin(angle);
}

function checkKeyboard() {
    for (var i = 0; i < 10; i++) {
        if (keyboard.pressed(i.toString())) {
            channel = i;
            break;
        }
    }
}
function updateBody() {

    switch (channel) {
        case 0:
            var t = clock.getElapsedTime();

            // Notice That I use sin and cos function with a lot of divisions. So using others parameters can give
            // unexpected results , here are some suggestion:
            let p = 10;   // default
            //let p = 7;  // running
            //let p = 20; // fast walk
            t = t * p;
            robot.animate(t)
            break;

        // add poses here:
        case 1:
            robot.pose1()
            break;

        case 2:
            robot.pose2()
            break;

        case 3:
            //robot.reset();
            break;

        case 4:



            break;

        case 5:
            break;
        case 6:
            robot.hideRobot();
            break;
        case 7:
            robot.showRobot();
            break;
        case 8:
            robot.hideHuman();
            break;
        case 9:
            robot.showHuman();
            break;
        default:
            break;
    }
}

init();
animate();