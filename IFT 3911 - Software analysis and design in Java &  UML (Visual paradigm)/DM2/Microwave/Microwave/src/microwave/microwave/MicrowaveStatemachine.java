package microwave.microwave;

import microwave.ITimer;

public class MicrowaveStatemachine implements IMicrowaveStatemachine {
	protected class SCInterfaceImpl implements SCInterface {
	
		private SCInterfaceOperationCallback operationCallback;
		
		public void setSCInterfaceOperationCallback(
				SCInterfaceOperationCallback operationCallback) {
			this.operationCallback = operationCallback;
		}
		private boolean high;
		
		public void raiseHigh() {
			high = true;
			runCycle();
		}
		
		private boolean low;
		
		public void raiseLow() {
			low = true;
			runCycle();
		}
		
		private boolean digit;
		
		private long digitValue;
		
		public void raiseDigit(long value) {
			digit = true;
			digitValue = value;
			runCycle();
		}
		
		protected long getDigitValue() {
			if (! digit ) 
				throw new IllegalStateException("Illegal event value access. Event Digit is not raised!");
			return digitValue;
		}
		
		private boolean timer;
		
		public void raiseTimer() {
			timer = true;
			runCycle();
		}
		
		private boolean start;
		
		public void raiseStart() {
			start = true;
			runCycle();
		}
		
		private boolean stop;
		
		public void raiseStop() {
			stop = true;
			runCycle();
		}
		
		private boolean open;
		
		public void raiseOpen() {
			open = true;
			runCycle();
		}
		
		private boolean close;
		
		public void raiseClose() {
			close = true;
			runCycle();
		}
		
		private long power;
		
		public long getPower() {
			return power;
		}
		
		public void setPower(long value) {
			this.power = value;
		}
		
		protected void clearEvents() {
			high = false;
			low = false;
			digit = false;
			timer = false;
			start = false;
			stop = false;
			open = false;
			close = false;
		}
	}
	
	protected SCInterfaceImpl sCInterface;
	
	private boolean initialized = false;
	
	public enum State {
		microwave_Main,
		microwave_Main_r1_Init,
		microwave_Main_r1_HighPower,
		microwave_Main_r1_LowPower,
		microwave_Main_r1_WaitForDigits,
		microwave_Main_r1_Cooking,
		microwave_Main_r1_GetDigits,
		microwave_Main_r1_GetDigits_r1_Digit1,
		microwave_Main_r1_GetDigits_r1_Digit2,
		microwave_Main_r1_GetDigits_r1_Digit3,
		microwave_Main_r1_GetDigits_r1_Digit4,
		microwave_Main_r1_CookedAlert,
		microwave_Main_r1_CookedAlert_r1_FinishedAlert,
		microwave_Main_r1_CookedAlert_r2_BeepOn,
		microwave_Main_r1_CookedAlert_r2_BeepOff,
		microwave_DoorOpened,
		microwave_DoorOpened_r1_WaitingDoor,
		microwave_DoorOpened_r1_WaitStartCooking,
		microwave_StopAll,
		$NullState$
	};
	
	private State[] historyVector = new State[1];
	private final State[] stateVector = new State[2];
	
	private int nextStateIndex;
	
	
	private ITimer timer;
	
	private final boolean[] timeEvents = new boolean[5];
	private long dig1;
	
	protected void setDig1(long value) {
		dig1 = value;
	}
	
	protected long getDig1() {
		return dig1;
	}
	
	private long dig2;
	
	protected void setDig2(long value) {
		dig2 = value;
	}
	
	protected long getDig2() {
		return dig2;
	}
	
	private long dig3;
	
	protected void setDig3(long value) {
		dig3 = value;
	}
	
	protected long getDig3() {
		return dig3;
	}
	
	private long dig4;
	
	protected void setDig4(long value) {
		dig4 = value;
	}
	
	protected long getDig4() {
		return dig4;
	}
	
	private long timeLeft;
	
	protected void setTimeLeft(long value) {
		timeLeft = value;
	}
	
	protected long getTimeLeft() {
		return timeLeft;
	}
	
	private long val1;
	
	protected void setVal1(long value) {
		val1 = value;
	}
	
	protected long getVal1() {
		return val1;
	}
	
	private long val2;
	
	protected void setVal2(long value) {
		val2 = value;
	}
	
	protected long getVal2() {
		return val2;
	}
	
	private long val3;
	
	protected void setVal3(long value) {
		val3 = value;
	}
	
	protected long getVal3() {
		return val3;
	}
	
	private long val4;
	
	protected void setVal4(long value) {
		val4 = value;
	}
	
	protected long getVal4() {
		return val4;
	}
	
	private long secs;
	
	protected void setSecs(long value) {
		secs = value;
	}
	
	protected long getSecs() {
		return secs;
	}
	
	private long minutes;
	
	protected void setMinutes(long value) {
		minutes = value;
	}
	
	protected long getMinutes() {
		return minutes;
	}
	
	private boolean cooking;
	
	protected void setCooking(boolean value) {
		cooking = value;
	}
	
	protected boolean getCooking() {
		return cooking;
	}
	
	public MicrowaveStatemachine() {
		sCInterface = new SCInterfaceImpl();
	}
	
	public void init() {
		this.initialized = true;
		if (timer == null) {
			throw new IllegalStateException("timer not set.");
		}
		if (this.sCInterface.operationCallback == null) {
			throw new IllegalStateException("Operation callback for interface sCInterface must be set.");
		}
		
		for (int i = 0; i < 2; i++) {
			stateVector[i] = State.$NullState$;
		}
		for (int i = 0; i < 1; i++) {
			historyVector[i] = State.$NullState$;
		}
		clearEvents();
		clearOutEvents();
		sCInterface.setPower(0);
		
		setDig1(0);
		
		setDig2(0);
		
		setDig3(0);
		
		setDig4(0);
		
		setTimeLeft(0);
		
		setVal1(0);
		
		setVal2(0);
		
		setVal3(0);
		
		setVal4(0);
		
		setSecs(0);
		
		setMinutes(0);
		
		setCooking(false);
	}
	
	public void enter() {
		if (!initialized) {
			throw new IllegalStateException(
				"The state machine needs to be initialized first by calling the init() function."
			);
		}
		if (timer == null) {
			throw new IllegalStateException("timer not set.");
		}
		enterSequence_Microwave_default();
	}
	
	public void runCycle() {
		if (!initialized)
			throw new IllegalStateException(
					"The state machine needs to be initialized first by calling the init() function.");
		clearOutEvents();
		for (nextStateIndex = 0; nextStateIndex < stateVector.length; nextStateIndex++) {
			switch (stateVector[nextStateIndex]) {
			case microwave_Main_r1_Init:
				microwave_Main_r1_Init_react(true);
				break;
			case microwave_Main_r1_HighPower:
				microwave_Main_r1_HighPower_react(true);
				break;
			case microwave_Main_r1_LowPower:
				microwave_Main_r1_LowPower_react(true);
				break;
			case microwave_Main_r1_WaitForDigits:
				microwave_Main_r1_WaitForDigits_react(true);
				break;
			case microwave_Main_r1_Cooking:
				microwave_Main_r1_Cooking_react(true);
				break;
			case microwave_Main_r1_GetDigits_r1_Digit1:
				microwave_Main_r1_GetDigits_r1_Digit1_react(true);
				break;
			case microwave_Main_r1_GetDigits_r1_Digit2:
				microwave_Main_r1_GetDigits_r1_Digit2_react(true);
				break;
			case microwave_Main_r1_GetDigits_r1_Digit3:
				microwave_Main_r1_GetDigits_r1_Digit3_react(true);
				break;
			case microwave_Main_r1_GetDigits_r1_Digit4:
				microwave_Main_r1_GetDigits_r1_Digit4_react(true);
				break;
			case microwave_Main_r1_CookedAlert_r1_FinishedAlert:
				microwave_Main_r1_CookedAlert_r1_FinishedAlert_react(true);
				break;
			case microwave_Main_r1_CookedAlert_r2_BeepOn:
				microwave_Main_r1_CookedAlert_r2_BeepOn_react(true);
				break;
			case microwave_Main_r1_CookedAlert_r2_BeepOff:
				microwave_Main_r1_CookedAlert_r2_BeepOff_react(true);
				break;
			case microwave_DoorOpened_r1_WaitingDoor:
				microwave_DoorOpened_r1_WaitingDoor_react(true);
				break;
			case microwave_DoorOpened_r1_WaitStartCooking:
				microwave_DoorOpened_r1_WaitStartCooking_react(true);
				break;
			case microwave_StopAll:
				microwave_StopAll_react(true);
				break;
			default:
				// $NullState$
			}
		}
		clearEvents();
	}
	public void exit() {
		exitSequence_Microwave();
	}
	
	/**
	 * @see IStatemachine#isActive()
	 */
	public boolean isActive() {
		return stateVector[0] != State.$NullState$||stateVector[1] != State.$NullState$;
	}
	
	/** 
	* Always returns 'false' since this state machine can never become final.
	*
	* @see IStatemachine#isFinal()
	*/
	public boolean isFinal() {
		return false;
	}
	/**
	* This method resets the incoming events (time events included).
	*/
	protected void clearEvents() {
		sCInterface.clearEvents();
		for (int i=0; i<timeEvents.length; i++) {
			timeEvents[i] = false;
		}
	}
	
	/**
	* This method resets the outgoing events.
	*/
	protected void clearOutEvents() {
	}
	
	/**
	* Returns true if the given state is currently active otherwise false.
	*/
	public boolean isStateActive(State state) {
	
		switch (state) {
		case microwave_Main:
			return stateVector[0].ordinal() >= State.
					microwave_Main.ordinal()&& stateVector[0].ordinal() <= State.microwave_Main_r1_CookedAlert_r2_BeepOff.ordinal();
		case microwave_Main_r1_Init:
			return stateVector[0] == State.microwave_Main_r1_Init;
		case microwave_Main_r1_HighPower:
			return stateVector[0] == State.microwave_Main_r1_HighPower;
		case microwave_Main_r1_LowPower:
			return stateVector[0] == State.microwave_Main_r1_LowPower;
		case microwave_Main_r1_WaitForDigits:
			return stateVector[0] == State.microwave_Main_r1_WaitForDigits;
		case microwave_Main_r1_Cooking:
			return stateVector[0] == State.microwave_Main_r1_Cooking;
		case microwave_Main_r1_GetDigits:
			return stateVector[0].ordinal() >= State.
					microwave_Main_r1_GetDigits.ordinal()&& stateVector[0].ordinal() <= State.microwave_Main_r1_GetDigits_r1_Digit4.ordinal();
		case microwave_Main_r1_GetDigits_r1_Digit1:
			return stateVector[0] == State.microwave_Main_r1_GetDigits_r1_Digit1;
		case microwave_Main_r1_GetDigits_r1_Digit2:
			return stateVector[0] == State.microwave_Main_r1_GetDigits_r1_Digit2;
		case microwave_Main_r1_GetDigits_r1_Digit3:
			return stateVector[0] == State.microwave_Main_r1_GetDigits_r1_Digit3;
		case microwave_Main_r1_GetDigits_r1_Digit4:
			return stateVector[0] == State.microwave_Main_r1_GetDigits_r1_Digit4;
		case microwave_Main_r1_CookedAlert:
			return stateVector[0].ordinal() >= State.
					microwave_Main_r1_CookedAlert.ordinal()&& stateVector[0].ordinal() <= State.microwave_Main_r1_CookedAlert_r2_BeepOff.ordinal();
		case microwave_Main_r1_CookedAlert_r1_FinishedAlert:
			return stateVector[0] == State.microwave_Main_r1_CookedAlert_r1_FinishedAlert;
		case microwave_Main_r1_CookedAlert_r2_BeepOn:
			return stateVector[1] == State.microwave_Main_r1_CookedAlert_r2_BeepOn;
		case microwave_Main_r1_CookedAlert_r2_BeepOff:
			return stateVector[1] == State.microwave_Main_r1_CookedAlert_r2_BeepOff;
		case microwave_DoorOpened:
			return stateVector[0].ordinal() >= State.
					microwave_DoorOpened.ordinal()&& stateVector[0].ordinal() <= State.microwave_DoorOpened_r1_WaitStartCooking.ordinal();
		case microwave_DoorOpened_r1_WaitingDoor:
			return stateVector[0] == State.microwave_DoorOpened_r1_WaitingDoor;
		case microwave_DoorOpened_r1_WaitStartCooking:
			return stateVector[0] == State.microwave_DoorOpened_r1_WaitStartCooking;
		case microwave_StopAll:
			return stateVector[0] == State.microwave_StopAll;
		default:
			return false;
		}
	}
	
	/**
	* Set the {@link ITimer} for the state machine. It must be set
	* externally on a timed state machine before a run cycle can be correctly
	* executed.
	* 
	* @param timer
	*/
	public void setTimer(ITimer timer) {
		this.timer = timer;
	}
	
	/**
	* Returns the currently used timer.
	* 
	* @return {@link ITimer}
	*/
	public ITimer getTimer() {
		return timer;
	}
	
	public void timeElapsed(int eventID) {
		timeEvents[eventID] = true;
		runCycle();
	}
	
	public SCInterface getSCInterface() {
		return sCInterface;
	}
	
	public void raiseHigh() {
		sCInterface.raiseHigh();
	}
	
	public void raiseLow() {
		sCInterface.raiseLow();
	}
	
	public void raiseDigit(long value) {
		sCInterface.raiseDigit(value);
	}
	
	public void raiseTimer() {
		sCInterface.raiseTimer();
	}
	
	public void raiseStart() {
		sCInterface.raiseStart();
	}
	
	public void raiseStop() {
		sCInterface.raiseStop();
	}
	
	public void raiseOpen() {
		sCInterface.raiseOpen();
	}
	
	public void raiseClose() {
		sCInterface.raiseClose();
	}
	
	public long getPower() {
		return sCInterface.getPower();
	}
	
	public void setPower(long value) {
		sCInterface.setPower(value);
	}
	
	/* Entry action for state 'Init'. */
	private void entryAction_Microwave_Main_r1_Init() {
		sCInterface.operationCallback.clearDisplay();
		
		sCInterface.operationCallback.closeDoor();
	}
	
	/* Entry action for state 'HighPower'. */
	private void entryAction_Microwave_Main_r1_HighPower() {
		sCInterface.setPower(1);
		
		sCInterface.operationCallback.display("high");
	}
	
	/* Entry action for state 'LowPower'. */
	private void entryAction_Microwave_Main_r1_LowPower() {
		sCInterface.setPower(2);
		
		sCInterface.operationCallback.display("low");
	}
	
	/* Entry action for state 'WaitForDigits'. */
	private void entryAction_Microwave_Main_r1_WaitForDigits() {
		sCInterface.operationCallback.clearDisplay();
	}
	
	/* Entry action for state 'Cooking'. */
	private void entryAction_Microwave_Main_r1_Cooking() {
		timer.setTimer(this, 0, (1 * 1000), true);
		
		sCInterface.operationCallback.cook();
		
		setSecs((timeLeft % 60));
		
		setMinutes((timeLeft / 60));
		
		sCInterface.operationCallback.displayTime(((getMinutes() * 100) + getSecs()));
		
		setTimeLeft(getTimeLeft() - 1);
	}
	
	/* Entry action for state 'Digit1'. */
	private void entryAction_Microwave_Main_r1_GetDigits_r1_Digit1() {
		setDig1(sCInterface.getDigitValue());
		
		sCInterface.operationCallback.displayTime(getDig1());
		
		setVal1(1);
	}
	
	/* Entry action for state 'Digit2'. */
	private void entryAction_Microwave_Main_r1_GetDigits_r1_Digit2() {
		setDig2(sCInterface.getDigitValue());
		
		sCInterface.operationCallback.displayTime(((getDig1() * 10) + getDig2()));
		
		setVal1(10);
		
		setVal2(1);
	}
	
	/* Entry action for state 'Digit3'. */
	private void entryAction_Microwave_Main_r1_GetDigits_r1_Digit3() {
		setDig3(sCInterface.getDigitValue());
		
		sCInterface.operationCallback.displayTime((((getDig1() * 100) + (getDig2() * 10)) + getDig3()));
		
		setVal1(60);
		
		setVal2(10);
		
		setVal3(1);
	}
	
	/* Entry action for state 'Digit4'. */
	private void entryAction_Microwave_Main_r1_GetDigits_r1_Digit4() {
		setDig4(sCInterface.getDigitValue());
		
		sCInterface.operationCallback.displayTime(((((getDig1() * 1000) + (getDig2() * 100)) + (getDig3() * 10)) + getDig4()));
		
		setVal1(600);
		
		setVal2(60);
		
		setVal3(10);
		
		setVal4(1);
	}
	
	/* Entry action for state 'CookedAlert'. */
	private void entryAction_Microwave_Main_r1_CookedAlert() {
		timer.setTimer(this, 1, (5 * 1000), false);
	}
	
	/* Entry action for state 'FinishedAlert'. */
	private void entryAction_Microwave_Main_r1_CookedAlert_r1_FinishedAlert() {
		sCInterface.operationCallback.stopCook();
		
		setCooking(false);
		
		sCInterface.operationCallback.display("Finished");
	}
	
	/* Entry action for state 'BeepOn'. */
	private void entryAction_Microwave_Main_r1_CookedAlert_r2_BeepOn() {
		timer.setTimer(this, 2, (1 * 1000), false);
		
		sCInterface.operationCallback.beepOn();
	}
	
	/* Entry action for state 'BeepOff'. */
	private void entryAction_Microwave_Main_r1_CookedAlert_r2_BeepOff() {
		timer.setTimer(this, 3, (1 * 1000), false);
		
		sCInterface.operationCallback.beepOff();
	}
	
	/* Entry action for state 'WaitingDoor'. */
	private void entryAction_Microwave_DoorOpened_r1_WaitingDoor() {
		sCInterface.operationCallback.stopCook();
		
		sCInterface.operationCallback.openDoor();
		
		sCInterface.operationCallback.display("Waiting");
	}
	
	/* Entry action for state 'WaitStartCooking'. */
	private void entryAction_Microwave_DoorOpened_r1_WaitStartCooking() {
		sCInterface.operationCallback.displayTime(((getMinutes() * 100) + getSecs()));
	}
	
	/* Entry action for state 'StopAll'. */
	private void entryAction_Microwave_StopAll() {
		timer.setTimer(this, 4, 1, false);
		
		sCInterface.operationCallback.stopCook();
		
		sCInterface.operationCallback.clearDisplay();
		
		sCInterface.operationCallback.closeDoor();
	}
	
	/* Exit action for state 'Cooking'. */
	private void exitAction_Microwave_Main_r1_Cooking() {
		timer.unsetTimer(this, 0);
	}
	
	/* Exit action for state 'GetDigits'. */
	private void exitAction_Microwave_Main_r1_GetDigits() {
		setTimeLeft(((((dig1 * val1) + (dig2 * val2)) + (dig3 * val3)) + (dig4 * val4)));
		
		setCooking(true);
	}
	
	/* Exit action for state 'CookedAlert'. */
	private void exitAction_Microwave_Main_r1_CookedAlert() {
		timer.unsetTimer(this, 1);
	}
	
	/* Exit action for state 'BeepOn'. */
	private void exitAction_Microwave_Main_r1_CookedAlert_r2_BeepOn() {
		timer.unsetTimer(this, 2);
	}
	
	/* Exit action for state 'BeepOff'. */
	private void exitAction_Microwave_Main_r1_CookedAlert_r2_BeepOff() {
		timer.unsetTimer(this, 3);
	}
	
	/* Exit action for state 'WaitingDoor'. */
	private void exitAction_Microwave_DoorOpened_r1_WaitingDoor() {
		sCInterface.operationCallback.closeDoor();
	}
	
	/* Exit action for state 'StopAll'. */
	private void exitAction_Microwave_StopAll() {
		timer.unsetTimer(this, 4);
	}
	
	/* 'default' enter sequence for state Main */
	private void enterSequence_Microwave_Main_default() {
		enterSequence_Microwave_Main_r1_default();
	}
	
	/* 'default' enter sequence for state Init */
	private void enterSequence_Microwave_Main_r1_Init_default() {
		entryAction_Microwave_Main_r1_Init();
		nextStateIndex = 0;
		stateVector[0] = State.microwave_Main_r1_Init;
		
		historyVector[0] = stateVector[0];
	}
	
	/* 'default' enter sequence for state HighPower */
	private void enterSequence_Microwave_Main_r1_HighPower_default() {
		entryAction_Microwave_Main_r1_HighPower();
		nextStateIndex = 0;
		stateVector[0] = State.microwave_Main_r1_HighPower;
		
		historyVector[0] = stateVector[0];
	}
	
	/* 'default' enter sequence for state LowPower */
	private void enterSequence_Microwave_Main_r1_LowPower_default() {
		entryAction_Microwave_Main_r1_LowPower();
		nextStateIndex = 0;
		stateVector[0] = State.microwave_Main_r1_LowPower;
		
		historyVector[0] = stateVector[0];
	}
	
	/* 'default' enter sequence for state WaitForDigits */
	private void enterSequence_Microwave_Main_r1_WaitForDigits_default() {
		entryAction_Microwave_Main_r1_WaitForDigits();
		nextStateIndex = 0;
		stateVector[0] = State.microwave_Main_r1_WaitForDigits;
		
		historyVector[0] = stateVector[0];
	}
	
	/* 'default' enter sequence for state Cooking */
	private void enterSequence_Microwave_Main_r1_Cooking_default() {
		entryAction_Microwave_Main_r1_Cooking();
		nextStateIndex = 0;
		stateVector[0] = State.microwave_Main_r1_Cooking;
		
		historyVector[0] = stateVector[0];
	}
	
	/* 'default' enter sequence for state GetDigits */
	private void enterSequence_Microwave_Main_r1_GetDigits_default() {
		enterSequence_Microwave_Main_r1_GetDigits_r1_default();
		historyVector[0] = stateVector[0];
	}
	
	/* 'default' enter sequence for state Digit1 */
	private void enterSequence_Microwave_Main_r1_GetDigits_r1_Digit1_default() {
		entryAction_Microwave_Main_r1_GetDigits_r1_Digit1();
		nextStateIndex = 0;
		stateVector[0] = State.microwave_Main_r1_GetDigits_r1_Digit1;
	}
	
	/* 'default' enter sequence for state Digit2 */
	private void enterSequence_Microwave_Main_r1_GetDigits_r1_Digit2_default() {
		entryAction_Microwave_Main_r1_GetDigits_r1_Digit2();
		nextStateIndex = 0;
		stateVector[0] = State.microwave_Main_r1_GetDigits_r1_Digit2;
	}
	
	/* 'default' enter sequence for state Digit3 */
	private void enterSequence_Microwave_Main_r1_GetDigits_r1_Digit3_default() {
		entryAction_Microwave_Main_r1_GetDigits_r1_Digit3();
		nextStateIndex = 0;
		stateVector[0] = State.microwave_Main_r1_GetDigits_r1_Digit3;
	}
	
	/* 'default' enter sequence for state Digit4 */
	private void enterSequence_Microwave_Main_r1_GetDigits_r1_Digit4_default() {
		entryAction_Microwave_Main_r1_GetDigits_r1_Digit4();
		nextStateIndex = 0;
		stateVector[0] = State.microwave_Main_r1_GetDigits_r1_Digit4;
	}
	
	/* 'default' enter sequence for state CookedAlert */
	private void enterSequence_Microwave_Main_r1_CookedAlert_default() {
		entryAction_Microwave_Main_r1_CookedAlert();
		enterSequence_Microwave_Main_r1_CookedAlert_r1_default();
		enterSequence_Microwave_Main_r1_CookedAlert_r2_default();
		historyVector[0] = stateVector[0];
	}
	
	/* 'default' enter sequence for state FinishedAlert */
	private void enterSequence_Microwave_Main_r1_CookedAlert_r1_FinishedAlert_default() {
		entryAction_Microwave_Main_r1_CookedAlert_r1_FinishedAlert();
		nextStateIndex = 0;
		stateVector[0] = State.microwave_Main_r1_CookedAlert_r1_FinishedAlert;
	}
	
	/* 'default' enter sequence for state BeepOn */
	private void enterSequence_Microwave_Main_r1_CookedAlert_r2_BeepOn_default() {
		entryAction_Microwave_Main_r1_CookedAlert_r2_BeepOn();
		nextStateIndex = 1;
		stateVector[1] = State.microwave_Main_r1_CookedAlert_r2_BeepOn;
	}
	
	/* 'default' enter sequence for state BeepOff */
	private void enterSequence_Microwave_Main_r1_CookedAlert_r2_BeepOff_default() {
		entryAction_Microwave_Main_r1_CookedAlert_r2_BeepOff();
		nextStateIndex = 1;
		stateVector[1] = State.microwave_Main_r1_CookedAlert_r2_BeepOff;
	}
	
	/* 'default' enter sequence for state DoorOpened */
	private void enterSequence_Microwave_DoorOpened_default() {
		enterSequence_Microwave_DoorOpened_r1_default();
	}
	
	/* 'default' enter sequence for state WaitingDoor */
	private void enterSequence_Microwave_DoorOpened_r1_WaitingDoor_default() {
		entryAction_Microwave_DoorOpened_r1_WaitingDoor();
		nextStateIndex = 0;
		stateVector[0] = State.microwave_DoorOpened_r1_WaitingDoor;
	}
	
	/* 'default' enter sequence for state WaitStartCooking */
	private void enterSequence_Microwave_DoorOpened_r1_WaitStartCooking_default() {
		entryAction_Microwave_DoorOpened_r1_WaitStartCooking();
		nextStateIndex = 0;
		stateVector[0] = State.microwave_DoorOpened_r1_WaitStartCooking;
	}
	
	/* 'default' enter sequence for state StopAll */
	private void enterSequence_Microwave_StopAll_default() {
		entryAction_Microwave_StopAll();
		nextStateIndex = 0;
		stateVector[0] = State.microwave_StopAll;
	}
	
	/* 'default' enter sequence for region Microwave */
	private void enterSequence_Microwave_default() {
		react_microwave_Microwave__entry_Default();
	}
	
	/* 'default' enter sequence for region r1 */
	private void enterSequence_Microwave_Main_r1_default() {
		react_microwave_Microwave_Main_r1__entry_Default();
	}
	
	/* shallow enterSequence with history in child r1 */
	private void shallowEnterSequence_Microwave_Main_r1() {
		switch (historyVector[0]) {
		case microwave_Main_r1_Init:
			enterSequence_Microwave_Main_r1_Init_default();
			break;
		case microwave_Main_r1_HighPower:
			enterSequence_Microwave_Main_r1_HighPower_default();
			break;
		case microwave_Main_r1_LowPower:
			enterSequence_Microwave_Main_r1_LowPower_default();
			break;
		case microwave_Main_r1_WaitForDigits:
			enterSequence_Microwave_Main_r1_WaitForDigits_default();
			break;
		case microwave_Main_r1_Cooking:
			enterSequence_Microwave_Main_r1_Cooking_default();
			break;
		case microwave_Main_r1_GetDigits_r1_Digit1:
			enterSequence_Microwave_Main_r1_GetDigits_default();
			break;
		case microwave_Main_r1_GetDigits_r1_Digit2:
			enterSequence_Microwave_Main_r1_GetDigits_default();
			break;
		case microwave_Main_r1_GetDigits_r1_Digit3:
			enterSequence_Microwave_Main_r1_GetDigits_default();
			break;
		case microwave_Main_r1_GetDigits_r1_Digit4:
			enterSequence_Microwave_Main_r1_GetDigits_default();
			break;
		case microwave_Main_r1_CookedAlert_r1_FinishedAlert:
			enterSequence_Microwave_Main_r1_CookedAlert_default();
			break;
		default:
			break;
		}
	}
	
	/* 'default' enter sequence for region r1 */
	private void enterSequence_Microwave_Main_r1_GetDigits_r1_default() {
		react_microwave_Microwave_Main_r1_GetDigits_r1__entry_Default();
	}
	
	/* 'default' enter sequence for region r1 */
	private void enterSequence_Microwave_Main_r1_CookedAlert_r1_default() {
		react_microwave_Microwave_Main_r1_CookedAlert_r1__entry_Default();
	}
	
	/* 'default' enter sequence for region r2 */
	private void enterSequence_Microwave_Main_r1_CookedAlert_r2_default() {
		react_microwave_Microwave_Main_r1_CookedAlert_r2__entry_Default();
	}
	
	/* 'default' enter sequence for region r1 */
	private void enterSequence_Microwave_DoorOpened_r1_default() {
		react_microwave_Microwave_DoorOpened_r1__entry_Default();
	}
	
	/* Default exit sequence for state Main */
	private void exitSequence_Microwave_Main() {
		exitSequence_Microwave_Main_r1();
	}
	
	/* Default exit sequence for state Init */
	private void exitSequence_Microwave_Main_r1_Init() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state HighPower */
	private void exitSequence_Microwave_Main_r1_HighPower() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state LowPower */
	private void exitSequence_Microwave_Main_r1_LowPower() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state WaitForDigits */
	private void exitSequence_Microwave_Main_r1_WaitForDigits() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state Cooking */
	private void exitSequence_Microwave_Main_r1_Cooking() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
		
		exitAction_Microwave_Main_r1_Cooking();
	}
	
	/* Default exit sequence for state GetDigits */
	private void exitSequence_Microwave_Main_r1_GetDigits() {
		exitSequence_Microwave_Main_r1_GetDigits_r1();
		exitAction_Microwave_Main_r1_GetDigits();
	}
	
	/* Default exit sequence for state Digit1 */
	private void exitSequence_Microwave_Main_r1_GetDigits_r1_Digit1() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state Digit2 */
	private void exitSequence_Microwave_Main_r1_GetDigits_r1_Digit2() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state Digit3 */
	private void exitSequence_Microwave_Main_r1_GetDigits_r1_Digit3() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state Digit4 */
	private void exitSequence_Microwave_Main_r1_GetDigits_r1_Digit4() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state FinishedAlert */
	private void exitSequence_Microwave_Main_r1_CookedAlert_r1_FinishedAlert() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state BeepOn */
	private void exitSequence_Microwave_Main_r1_CookedAlert_r2_BeepOn() {
		nextStateIndex = 1;
		stateVector[1] = State.$NullState$;
		
		exitAction_Microwave_Main_r1_CookedAlert_r2_BeepOn();
	}
	
	/* Default exit sequence for state BeepOff */
	private void exitSequence_Microwave_Main_r1_CookedAlert_r2_BeepOff() {
		nextStateIndex = 1;
		stateVector[1] = State.$NullState$;
		
		exitAction_Microwave_Main_r1_CookedAlert_r2_BeepOff();
	}
	
	/* Default exit sequence for state DoorOpened */
	private void exitSequence_Microwave_DoorOpened() {
		exitSequence_Microwave_DoorOpened_r1();
	}
	
	/* Default exit sequence for state WaitingDoor */
	private void exitSequence_Microwave_DoorOpened_r1_WaitingDoor() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
		
		exitAction_Microwave_DoorOpened_r1_WaitingDoor();
	}
	
	/* Default exit sequence for state WaitStartCooking */
	private void exitSequence_Microwave_DoorOpened_r1_WaitStartCooking() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state StopAll */
	private void exitSequence_Microwave_StopAll() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
		
		exitAction_Microwave_StopAll();
	}
	
	/* Default exit sequence for region Microwave */
	private void exitSequence_Microwave() {
		switch (stateVector[0]) {
		case microwave_Main_r1_Init:
			exitSequence_Microwave_Main_r1_Init();
			break;
		case microwave_Main_r1_HighPower:
			exitSequence_Microwave_Main_r1_HighPower();
			break;
		case microwave_Main_r1_LowPower:
			exitSequence_Microwave_Main_r1_LowPower();
			break;
		case microwave_Main_r1_WaitForDigits:
			exitSequence_Microwave_Main_r1_WaitForDigits();
			break;
		case microwave_Main_r1_Cooking:
			exitSequence_Microwave_Main_r1_Cooking();
			break;
		case microwave_Main_r1_GetDigits_r1_Digit1:
			exitSequence_Microwave_Main_r1_GetDigits_r1_Digit1();
			exitAction_Microwave_Main_r1_GetDigits();
			break;
		case microwave_Main_r1_GetDigits_r1_Digit2:
			exitSequence_Microwave_Main_r1_GetDigits_r1_Digit2();
			exitAction_Microwave_Main_r1_GetDigits();
			break;
		case microwave_Main_r1_GetDigits_r1_Digit3:
			exitSequence_Microwave_Main_r1_GetDigits_r1_Digit3();
			exitAction_Microwave_Main_r1_GetDigits();
			break;
		case microwave_Main_r1_GetDigits_r1_Digit4:
			exitSequence_Microwave_Main_r1_GetDigits_r1_Digit4();
			exitAction_Microwave_Main_r1_GetDigits();
			break;
		case microwave_Main_r1_CookedAlert_r1_FinishedAlert:
			exitSequence_Microwave_Main_r1_CookedAlert_r1_FinishedAlert();
			break;
		case microwave_DoorOpened_r1_WaitingDoor:
			exitSequence_Microwave_DoorOpened_r1_WaitingDoor();
			break;
		case microwave_DoorOpened_r1_WaitStartCooking:
			exitSequence_Microwave_DoorOpened_r1_WaitStartCooking();
			break;
		case microwave_StopAll:
			exitSequence_Microwave_StopAll();
			break;
		default:
			break;
		}
		
		switch (stateVector[1]) {
		case microwave_Main_r1_CookedAlert_r2_BeepOn:
			exitSequence_Microwave_Main_r1_CookedAlert_r2_BeepOn();
			exitAction_Microwave_Main_r1_CookedAlert();
			break;
		case microwave_Main_r1_CookedAlert_r2_BeepOff:
			exitSequence_Microwave_Main_r1_CookedAlert_r2_BeepOff();
			exitAction_Microwave_Main_r1_CookedAlert();
			break;
		default:
			break;
		}
	}
	
	/* Default exit sequence for region r1 */
	private void exitSequence_Microwave_Main_r1() {
		switch (stateVector[0]) {
		case microwave_Main_r1_Init:
			exitSequence_Microwave_Main_r1_Init();
			break;
		case microwave_Main_r1_HighPower:
			exitSequence_Microwave_Main_r1_HighPower();
			break;
		case microwave_Main_r1_LowPower:
			exitSequence_Microwave_Main_r1_LowPower();
			break;
		case microwave_Main_r1_WaitForDigits:
			exitSequence_Microwave_Main_r1_WaitForDigits();
			break;
		case microwave_Main_r1_Cooking:
			exitSequence_Microwave_Main_r1_Cooking();
			break;
		case microwave_Main_r1_GetDigits_r1_Digit1:
			exitSequence_Microwave_Main_r1_GetDigits_r1_Digit1();
			exitAction_Microwave_Main_r1_GetDigits();
			break;
		case microwave_Main_r1_GetDigits_r1_Digit2:
			exitSequence_Microwave_Main_r1_GetDigits_r1_Digit2();
			exitAction_Microwave_Main_r1_GetDigits();
			break;
		case microwave_Main_r1_GetDigits_r1_Digit3:
			exitSequence_Microwave_Main_r1_GetDigits_r1_Digit3();
			exitAction_Microwave_Main_r1_GetDigits();
			break;
		case microwave_Main_r1_GetDigits_r1_Digit4:
			exitSequence_Microwave_Main_r1_GetDigits_r1_Digit4();
			exitAction_Microwave_Main_r1_GetDigits();
			break;
		case microwave_Main_r1_CookedAlert_r1_FinishedAlert:
			exitSequence_Microwave_Main_r1_CookedAlert_r1_FinishedAlert();
			break;
		default:
			break;
		}
		
		switch (stateVector[1]) {
		case microwave_Main_r1_CookedAlert_r2_BeepOn:
			exitSequence_Microwave_Main_r1_CookedAlert_r2_BeepOn();
			exitAction_Microwave_Main_r1_CookedAlert();
			break;
		case microwave_Main_r1_CookedAlert_r2_BeepOff:
			exitSequence_Microwave_Main_r1_CookedAlert_r2_BeepOff();
			exitAction_Microwave_Main_r1_CookedAlert();
			break;
		default:
			break;
		}
	}
	
	/* Default exit sequence for region r1 */
	private void exitSequence_Microwave_Main_r1_GetDigits_r1() {
		switch (stateVector[0]) {
		case microwave_Main_r1_GetDigits_r1_Digit1:
			exitSequence_Microwave_Main_r1_GetDigits_r1_Digit1();
			break;
		case microwave_Main_r1_GetDigits_r1_Digit2:
			exitSequence_Microwave_Main_r1_GetDigits_r1_Digit2();
			break;
		case microwave_Main_r1_GetDigits_r1_Digit3:
			exitSequence_Microwave_Main_r1_GetDigits_r1_Digit3();
			break;
		case microwave_Main_r1_GetDigits_r1_Digit4:
			exitSequence_Microwave_Main_r1_GetDigits_r1_Digit4();
			break;
		default:
			break;
		}
	}
	
	/* Default exit sequence for region r1 */
	private void exitSequence_Microwave_Main_r1_CookedAlert_r1() {
		switch (stateVector[0]) {
		case microwave_Main_r1_CookedAlert_r1_FinishedAlert:
			exitSequence_Microwave_Main_r1_CookedAlert_r1_FinishedAlert();
			break;
		default:
			break;
		}
	}
	
	/* Default exit sequence for region r2 */
	private void exitSequence_Microwave_Main_r1_CookedAlert_r2() {
		switch (stateVector[1]) {
		case microwave_Main_r1_CookedAlert_r2_BeepOn:
			exitSequence_Microwave_Main_r1_CookedAlert_r2_BeepOn();
			break;
		case microwave_Main_r1_CookedAlert_r2_BeepOff:
			exitSequence_Microwave_Main_r1_CookedAlert_r2_BeepOff();
			break;
		default:
			break;
		}
	}
	
	/* Default exit sequence for region r1 */
	private void exitSequence_Microwave_DoorOpened_r1() {
		switch (stateVector[0]) {
		case microwave_DoorOpened_r1_WaitingDoor:
			exitSequence_Microwave_DoorOpened_r1_WaitingDoor();
			break;
		case microwave_DoorOpened_r1_WaitStartCooking:
			exitSequence_Microwave_DoorOpened_r1_WaitStartCooking();
			break;
		default:
			break;
		}
	}
	
	/* Default react sequence for initial entry  */
	private void react_microwave_Microwave_Main_r1_GetDigits_r1__entry_Default() {
		enterSequence_Microwave_Main_r1_GetDigits_r1_Digit1_default();
	}
	
	/* Default react sequence for shallow history entry continue */
	private void react_microwave_Microwave_Main_r1_continue() {
		/* Enter the region with shallow history */
		if (historyVector[0] != State.$NullState$) {
			shallowEnterSequence_Microwave_Main_r1();
		} else {
			enterSequence_Microwave_Main_r1_Init_default();
		}
	}
	
	/* Default react sequence for initial entry  */
	private void react_microwave_Microwave_Main_r1__entry_Default() {
		enterSequence_Microwave_Main_r1_Init_default();
	}
	
	/* Default react sequence for initial entry  */
	private void react_microwave_Microwave_Main_r1_CookedAlert_r1__entry_Default() {
		enterSequence_Microwave_Main_r1_CookedAlert_r1_FinishedAlert_default();
	}
	
	/* Default react sequence for initial entry  */
	private void react_microwave_Microwave_Main_r1_CookedAlert_r2__entry_Default() {
		enterSequence_Microwave_Main_r1_CookedAlert_r2_BeepOff_default();
	}
	
	/* Default react sequence for initial entry  */
	private void react_microwave_Microwave__entry_Default() {
		enterSequence_Microwave_Main_default();
	}
	
	/* Default react sequence for initial entry  */
	private void react_microwave_Microwave_DoorOpened_r1__entry_Default() {
		enterSequence_Microwave_DoorOpened_r1_WaitingDoor_default();
	}
	
	private boolean react() {
		return false;
	}
	
	private boolean microwave_Main_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (react()==false) {
				if (sCInterface.open) {
					exitSequence_Microwave_Main();
					enterSequence_Microwave_DoorOpened_default();
				} else {
					if (sCInterface.stop) {
						exitSequence_Microwave_Main();
						enterSequence_Microwave_StopAll_default();
					} else {
						did_transition = false;
					}
				}
			}
		}
		return did_transition;
	}
	
	private boolean microwave_Main_r1_Init_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (microwave_Main_react(try_transition)==false) {
				if (sCInterface.high) {
					exitSequence_Microwave_Main_r1_Init();
					enterSequence_Microwave_Main_r1_HighPower_default();
				} else {
					if (sCInterface.low) {
						exitSequence_Microwave_Main_r1_Init();
						enterSequence_Microwave_Main_r1_LowPower_default();
					} else {
						did_transition = false;
					}
				}
			}
		}
		return did_transition;
	}
	
	private boolean microwave_Main_r1_HighPower_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (microwave_Main_react(try_transition)==false) {
				if (sCInterface.low) {
					exitSequence_Microwave_Main_r1_HighPower();
					enterSequence_Microwave_Main_r1_LowPower_default();
				} else {
					if (sCInterface.timer) {
						exitSequence_Microwave_Main_r1_HighPower();
						enterSequence_Microwave_Main_r1_WaitForDigits_default();
					} else {
						did_transition = false;
					}
				}
			}
		}
		return did_transition;
	}
	
	private boolean microwave_Main_r1_LowPower_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (microwave_Main_react(try_transition)==false) {
				if (sCInterface.high) {
					exitSequence_Microwave_Main_r1_LowPower();
					enterSequence_Microwave_Main_r1_HighPower_default();
				} else {
					if (sCInterface.timer) {
						exitSequence_Microwave_Main_r1_LowPower();
						enterSequence_Microwave_Main_r1_WaitForDigits_default();
					} else {
						did_transition = false;
					}
				}
			}
		}
		return did_transition;
	}
	
	private boolean microwave_Main_r1_WaitForDigits_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (microwave_Main_react(try_transition)==false) {
				if (sCInterface.digit) {
					exitSequence_Microwave_Main_r1_WaitForDigits();
					enterSequence_Microwave_Main_r1_GetDigits_default();
				} else {
					did_transition = false;
				}
			}
		}
		return did_transition;
	}
	
	private boolean microwave_Main_r1_Cooking_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (microwave_Main_react(try_transition)==false) {
				if (getTimeLeft()==0) {
					exitSequence_Microwave_Main_r1_Cooking();
					enterSequence_Microwave_Main_r1_CookedAlert_default();
				} else {
					if (timeEvents[0]) {
						exitSequence_Microwave_Main_r1_Cooking();
						enterSequence_Microwave_Main_r1_Cooking_default();
					} else {
						did_transition = false;
					}
				}
			}
		}
		return did_transition;
	}
	
	private boolean microwave_Main_r1_GetDigits_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (microwave_Main_react(try_transition)==false) {
				if (sCInterface.start) {
					exitSequence_Microwave_Main_r1_GetDigits();
					enterSequence_Microwave_Main_r1_Cooking_default();
				} else {
					did_transition = false;
				}
			}
		}
		return did_transition;
	}
	
	private boolean microwave_Main_r1_GetDigits_r1_Digit1_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (microwave_Main_r1_GetDigits_react(try_transition)==false) {
				if (sCInterface.digit) {
					exitSequence_Microwave_Main_r1_GetDigits_r1_Digit1();
					enterSequence_Microwave_Main_r1_GetDigits_r1_Digit2_default();
				} else {
					did_transition = false;
				}
			}
		}
		return did_transition;
	}
	
	private boolean microwave_Main_r1_GetDigits_r1_Digit2_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (microwave_Main_r1_GetDigits_react(try_transition)==false) {
				if (sCInterface.digit) {
					exitSequence_Microwave_Main_r1_GetDigits_r1_Digit2();
					enterSequence_Microwave_Main_r1_GetDigits_r1_Digit3_default();
				} else {
					did_transition = false;
				}
			}
		}
		return did_transition;
	}
	
	private boolean microwave_Main_r1_GetDigits_r1_Digit3_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (microwave_Main_r1_GetDigits_react(try_transition)==false) {
				if (sCInterface.digit) {
					exitSequence_Microwave_Main_r1_GetDigits_r1_Digit3();
					enterSequence_Microwave_Main_r1_GetDigits_r1_Digit4_default();
				} else {
					did_transition = false;
				}
			}
		}
		return did_transition;
	}
	
	private boolean microwave_Main_r1_GetDigits_r1_Digit4_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (microwave_Main_r1_GetDigits_react(try_transition)==false) {
				did_transition = false;
			}
		}
		return did_transition;
	}
	
	private boolean microwave_Main_r1_CookedAlert_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (microwave_Main_react(try_transition)==false) {
				if (timeEvents[1]) {
					exitSequence_Microwave_Main();
					enterSequence_Microwave_Main_default();
				} else {
					did_transition = false;
				}
			}
		}
		return did_transition;
	}
	
	private boolean microwave_Main_r1_CookedAlert_r1_FinishedAlert_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (microwave_Main_r1_CookedAlert_react(try_transition)==false) {
				did_transition = false;
			}
		}
		return did_transition;
	}
	
	private boolean microwave_Main_r1_CookedAlert_r2_BeepOn_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (timeEvents[2]) {
				exitSequence_Microwave_Main_r1_CookedAlert_r2_BeepOn();
				enterSequence_Microwave_Main_r1_CookedAlert_r2_BeepOff_default();
			} else {
				did_transition = false;
			}
		}
		return did_transition;
	}
	
	private boolean microwave_Main_r1_CookedAlert_r2_BeepOff_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (timeEvents[3]) {
				exitSequence_Microwave_Main_r1_CookedAlert_r2_BeepOff();
				enterSequence_Microwave_Main_r1_CookedAlert_r2_BeepOn_default();
			} else {
				did_transition = false;
			}
		}
		return did_transition;
	}
	
	private boolean microwave_DoorOpened_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (react()==false) {
				if (sCInterface.stop) {
					exitSequence_Microwave_DoorOpened();
					enterSequence_Microwave_Main_default();
				} else {
					if (((sCInterface.close) && (getCooking()==false))) {
						exitSequence_Microwave_DoorOpened();
						react_microwave_Microwave_Main_r1_continue();
					} else {
						did_transition = false;
					}
				}
			}
		}
		return did_transition;
	}
	
	private boolean microwave_DoorOpened_r1_WaitingDoor_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (microwave_DoorOpened_react(try_transition)==false) {
				if (sCInterface.close) {
					exitSequence_Microwave_DoorOpened_r1_WaitingDoor();
					enterSequence_Microwave_DoorOpened_r1_WaitStartCooking_default();
				} else {
					did_transition = false;
				}
			}
		}
		return did_transition;
	}
	
	private boolean microwave_DoorOpened_r1_WaitStartCooking_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (microwave_DoorOpened_react(try_transition)==false) {
				if (sCInterface.start) {
					exitSequence_Microwave_DoorOpened();
					react_microwave_Microwave_Main_r1_continue();
				} else {
					did_transition = false;
				}
			}
		}
		return did_transition;
	}
	
	private boolean microwave_StopAll_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (react()==false) {
				if (timeEvents[4]) {
					exitSequence_Microwave_StopAll();
					enterSequence_Microwave_Main_default();
				} else {
					did_transition = false;
				}
			}
		}
		return did_transition;
	}
	
}
