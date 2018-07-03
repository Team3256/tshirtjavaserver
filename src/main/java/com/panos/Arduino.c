/*
  This is the node on the arduino that will recieve values from the jetson and run the electronics.
  TODO:
  1. Add limits on pivot
  2. Add auto moving to position on pivot
  3. Implement full move to flat, reload, and move back to current position
*/

#include <Servo.h>
#include <Encoder.h>

//define relay pins
#define LEFT_POP_FORWARD 28
#define LEFT_POP_REVERSE 26
#define RIGHT_POP_FORWARD 24
#define RIGHT_POP_REVERSE 22

#define LEFT_EJECT_FORWARD 27
#define LEFT_EJECT_REVERSE 29
#define RIGHT_EJECT_FORWARD 23
#define RIGHT_EJECT_REVERSE 25

#define LEFT_SHOOT 30
#define RIGHT_SHOOT 31

// define motor pins
#define LEFT_FRONT_PIN 2
#define LEFT_BACK_PIN 3
#define RIGHT_FRONT_PIN 4
#define RIGHT_BACK_PIN 5
#define PIVOT_PIN 10

// define sensor pins
#define HALL_EFFECT 20
#define ENCODER_A 18
#define ENCODER_B 19

// define PWM frequencies for motor controllers
#define TALON_CENTER_PULSE_US 1500
#define TALON_MIN_PULSE_US 1000
#define TALON_MAX_PULSE_US 2000

// assign variables
#define PIVOT_ENCODER_BACK_LIMIT -75000
#define PIVOT_ENCODER_FORWARD_LIMIT 5000

#define PRESET_LOW -10000
#define PRESET_HIGH -60000
#define PRESET_TOLERANCE 3000

// Define Motors
Servo left_front;
Servo left_back;
Servo right_front;
Servo right_back;
Servo pivot;

// Define Sensors (Hall Effect is defined in Setup)
Encoder pivotEnc(ENCODER_A, ENCODER_B);

// Define variables
int pivotTarget = 0;
float pivot_power = 0.25;
float motor_power = 0;
long pivotPos = -999;
bool is_left_reloading = false;
bool is_right_reloading = false;
bool is_calibrated = false;
float ticksPerDegree = 0;

// Define states for the pivot
enum pivotState{
  manual,
  autoMoving,
  reloading,
  still
};
pivotState mstate = pivotState::manual;

// Method to control motor controllers at the desired frequencies
void run_motor(Servo motor, double power) {
  power *= 100;
  int pulse_us = TALON_CENTER_PULSE_US + (5*power);
  if (pulse_us < TALON_MIN_PULSE_US) pulse_us = TALON_MIN_PULSE_US;
  if (pulse_us > TALON_MAX_PULSE_US) pulse_us = TALON_MAX_PULSE_US;
  motor.writeMicroseconds(pulse_us);
}

/*
  Set of methods for controlling the 4 actuators for reloading and the 2 electronic solenoids for shooting
  Pop pops the barrel forward to let the canister drop down
  Push pushes the barrel in on the canister for shooting
  Eject ejects the empty canisters when the barrel is popped out
  Retract retracts the actuator after the barrel is ejected
  Shoot opens and closes the electronic solenoids for a short time to release air to shoot the TShirt
*/
void pop_left() {
  digitalWrite(LEFT_POP_FORWARD, HIGH);
  digitalWrite(LEFT_POP_REVERSE, LOW);
}

void pop_right() {
  digitalWrite(RIGHT_POP_FORWARD, HIGH);
  digitalWrite(RIGHT_POP_REVERSE, LOW);
}

void push_left() {
  digitalWrite(LEFT_POP_FORWARD, LOW);
  digitalWrite(LEFT_POP_REVERSE, HIGH);
}

void push_right() {
  digitalWrite(RIGHT_POP_FORWARD, LOW);
  digitalWrite(RIGHT_POP_REVERSE, HIGH);
}

void eject_left() {
  digitalWrite(LEFT_EJECT_FORWARD, HIGH);
  digitalWrite(LEFT_EJECT_REVERSE, LOW);
}

void eject_right() {
  digitalWrite(RIGHT_EJECT_FORWARD, HIGH);
  digitalWrite(RIGHT_EJECT_REVERSE, LOW);
}

void retract_left() {
  digitalWrite(LEFT_EJECT_FORWARD, LOW);
  digitalWrite(LEFT_EJECT_REVERSE, HIGH);
}

void retract_right() {
  digitalWrite(RIGHT_EJECT_FORWARD, LOW);
  digitalWrite(RIGHT_EJECT_REVERSE, HIGH);
}
void shoot_right(int wait) {
  digitalWrite(RIGHT_SHOOT, LOW);
  delay(wait);
  digitalWrite(RIGHT_SHOOT, HIGH);
}
void shoot_left(int wait) {
  digitalWrite(LEFT_SHOOT, LOW);
  delay(wait);
  digitalWrite(LEFT_SHOOT, HIGH);
}

// Get hall_effect position
bool hall_effect() {
  bool val = !digitalRead(HALL_EFFECT);
  if (val) is_calibrated = true;
  return val;
}

// Used to move pivot to a preset position (not being used)
float update_preset(double target, double current) {
  double error = target - current;
  double power = 0.0;
  if (fabs(error) < PRESET_TOLERANCE) {
    power = 0.0;
  }
  else if (error < 0) {
    power = -1 * pivot_power;
  }
  else {
    power = pivot_power;
  }
  //debug_msg.data = power;
  //debug.publish(&debug_msg);
  return power;
}

float update_pivot(float angle, double power) {
  float currPos = pivotEnc.read();
  float targetTicks = ticksPerDegree * angle;
  bool direction = true; //true is going up...false is going down
  if(ticksToDegrees(currPos) > angle) {
    direction = false;
  }
  if(direction) {
    while(currPos < targetTicks) {
      run_motor(pivot, power);
    }
  }
  else{
    while(currPos > targetTicks) {
      run_motor(pivot, -power);
    }
  }
  run_motor(pivot, 0);
}

float degreesToTicks(float degrees) {
  return degrees*ticksPerDegree;
}

float ticksToDegrees(float ticks) {
  return ticks/ticksPerDegree;
}


// //Called everytime the subscriber reads from the controls topic
// void controls_callback(const std_msgs::Float32MultiArray& data) {
//  //Controller values
//  float left_drive = data.data[0];
//  float right_drive = data.data[1];
//  bool left_shoot = data.data[2] != 0;
//  bool right_shoot = data.data[3] != 0;
//  bool left_reload = data.data[4] != 0;
//  bool right_reload = data.data[5] != 0;
//  bool pivot_forward = data.data[6] != 0;
//  bool pivot_backward = data.data[7] != 0;
//  bool left_preset = data.data[8] != 0;
//  bool right_preset = data.data[9] != 0;

//  //run drive with joysticks (Arcade drive is handled on Jetson side)
//  run_motor(left_front, left_drive);
//  run_motor(left_back, left_drive);
//  run_motor(right_front, -right_drive);
//  run_motor(right_back, -right_drive);

//  //shoot on triggers
//  if (left_shoot) {
//    shoot_left(30);
//  }
//  if (right_shoot) {
//    shoot_right(30);
//  }

//  //update pivot state
//  if (pivot_forward || pivot_backward) {
//    mstate = pivotState::manual;
//  }else if (left_preset || right_preset) {
//    mstate = pivotState::autoMoving;
//    //set a variable for what position to move to
//  }else if(left_reload || right_reload) {
//    mstate = pivotState::reloading;
//  }else{
//    mstate = pivotState::still;
//  }

//  //switch case for pivot state (reloading and automoving state aren't doing anything)
//  //Hard limits haven't been set
//  //Reloading should be split into two states: Reloading and moving to reload
//  //After reloading, call autoMoving to move back to past position
//  switch(mstate) {
//    //we hit front limit
//    if (is_calibrated && pivotPos > PIVOT_ENCODER_FORWARD_LIMIT) {
//      pivot_power = min(pivot_power, 0);
//    }
//    //we hit back limit
//    else if (is_calibrated && pivotPos < PIVOT_ENCODER_BACK_LIMIT) {
//      pivot_power = max(pivot_power, 0);
//    }
//    case manual:
//      //debug_msg.data = "manual";
//      motor_power = pivot_power;
//      motor_power = pivot_backward ? motor_power*-1 : motor_power;
//      mstate = pivotState::still;
//      break;
//    case autoMoving:
//      //debug_msg.data = "autoMoving";
//      motor_power = update_preset(pivotTarget, pivotPos);
//      if (!is_calibrated) {
//        motor_power = 0;
//      }
//      mstate = pivotState::still;
//      break;
//    case reloading:
//      //debug_msg.data = "reloading";
//      motor_power = update_preset(0, pivotPos);
//      if (!is_calibrated) {
//        motor_power = 0;
//      }
//      mstate = pivotState::still;
//      break;
//    case still:
//      //debug_msg.data = "still";
//      motor_power = 0;
//      break;
//  }

//  //we hit encoder front limit
//  if (is_calibrated && pivotPos > PIVOT_ENCODER_FORWARD_LIMIT) {
//    motor_power = min(motor_power, 0);
//  }
//  //we hit encoder back limit
//  else if (is_calibrated && pivotPos < PIVOT_ENCODER_BACK_LIMIT) {
//    motor_power = max(motor_power, 0);
//  }
//  //we hit hall effect front limit
//  if (hall_effect()) {
//    motor_power = min(motor_power, 0);
//  }

//  //run motor based off switch case
//  run_motor(pivot, motor_power);
//  debug.publish(&debug_msg);

//  //Old automoving method with too many safety features because the pivot is dangerous
//  //Need to merged into switch case
//  /*
//  if (is_calibrated) {
//    if (left_preset) {
//      update_preset(-5000, pivotPos);
//    }
//    else{
//      pivot_power = pivot_backward ? pivot_power*-1 : pivot_power;
//      if (pivot_forward || pivot_backward) {
//        run_motor(pivot, pivot_power);
//      }
//      else {
//        run_motor(pivot, 0);
//      }
//    }
//  }
//  */

//  //Automatic Reloading Sequence (run on button pressed not held because that will cause issues interrupting the sequence)
//  //Should be implemented into the pivot states
//  if (left_reload && !is_left_reloading) {
//    is_left_reloading = true;
//    pop_left();
//    delay(500);
//    eject_left();
//    delay(500);
//    retract_left();
//    delay(500);
//    push_left();
//    is_left_reloading = false;
//  }
//  if (right_reload && !is_right_reloading) {
//    is_right_reloading = true;
//    pop_right();
//    delay(500);
//    eject_right();
//    delay(500);
//    retract_right();
//    delay(500);
//    push_right();
//    is_right_reloading = false;
//  }
// }

void setup() {
  //Attach motors
  Serial.begin(115200);
  Serial.setTimeout(30);
  left_front.attach(LEFT_FRONT_PIN);
  left_back.attach(LEFT_BACK_PIN);
  right_front.attach(RIGHT_FRONT_PIN);
  right_back.attach(RIGHT_BACK_PIN);
  pivot.attach(PIVOT_PIN);

  pinMode(HALL_EFFECT, INPUT);

  //Define 6 solenoids (the 4 solenoids on the manifold are dual-channeled)
  pinMode(LEFT_POP_FORWARD, OUTPUT);
  pinMode(LEFT_POP_REVERSE, OUTPUT);
  pinMode(RIGHT_POP_FORWARD, OUTPUT);
  pinMode(RIGHT_POP_REVERSE, OUTPUT);

  pinMode(LEFT_EJECT_FORWARD, OUTPUT);
  pinMode(LEFT_EJECT_REVERSE, OUTPUT);
  pinMode(RIGHT_EJECT_FORWARD, OUTPUT);
  pinMode(RIGHT_EJECT_REVERSE, OUTPUT);

  pinMode(LEFT_SHOOT, OUTPUT);
  pinMode(RIGHT_SHOOT, OUTPUT);

  //close electronic solenoids when initializing
  digitalWrite(LEFT_SHOOT, HIGH);
  digitalWrite(RIGHT_SHOOT, HIGH);

  int pivotState = digitalRead(20);

  while(pivotState != 0) {
    run_motor(pivot, 0.2);
    pivotState = digitalRead(20);
  }

  run_motor(pivot, 0);

  pivotEnc.write(0);

}

enum commandState {
  idle,
  readingCommand,
  readingPayload,
  executeCommand
};

commandState state = commandState::idle;

String command = "";
String payload = "";
String payload2 = "";

void runCommand() {
  if (command == "m") {
      Serial.println("RUN MOTOR");
      Serial.println(payload.toFloat());
      run_motor(left_front, payload.toFloat());
      run_motor(left_back, payload.toFloat());
      run_motor(right_front, payload2.toFloat());
      run_motor(right_back, payload2.toFloat());
  }

  if (command == "b4" && payload == "true") {
     pop_left();
     delay(500);
     eject_left();
     delay(500);
     retract_left();
     delay(500);
     push_left();
  }

  if (command == "b5" && payload == "true") {
     //pop_left();
     pop_right();
     delay(500);
     //eject_left();
     eject_right();
     delay(500);
     //retract_left();
     retract_right();
     delay(500);
     push_right();
  }

  if (command == "b6" && payload == "true") {
    Serial.println(pivotEnc.read());
    run_motor(pivot, -0.2);
  }

  if (command == "b6" && payload == "false") {
    Serial.println(pivotEnc.read());
    run_motor(pivot, 0);
  }

  if (command == "b7" && payload == "true") {
    Serial.println(pivotEnc.read());
    run_motor(pivot, 0.2);
  }

  if (command == "b7" && payload == "false") {
    Serial.println(pivotEnc.read());
    run_motor(pivot, 0);
  }

  if (command == "b8" && payload == "true") {
    shoot_left(30);
  }

  if (command == "b9" && payload == "true") {
    shoot_right(30);
  }
}

void loop() {
  //Update encoder
  pivotPos = pivotEnc.read();
  Serial.println(pivotPos);

  if (Serial.available()) {
    Serial.println("Reading");

    String letter = Serial.readString();

    int parenPos = letter.indexOf("(");
    int paren2Pos = letter.indexOf(")");

    int comma = letter.indexOf(",");

    command = letter.substring(0, letter.indexOf("("));

    if (comma != -1) {
      payload = letter.substring(parenPos + 1, comma);
      payload2 = letter.substring(comma + 2, paren2Pos);
    } else {
      payload = letter.substring(parenPos + 1, paren2Pos);
    }

    Serial.println(command);
    Serial.println(payload);
    Serial.println(payload2);

    runCommand();

    command = "";
    payload = "";
    payload2 = "";
  }


  //update sensor topic
  // if (hall_effect()) {
  //  pivotEnc.write(0);
  //  sensorVals.data[0] = 1.0;
  // } else {
  //  sensorVals.data[0] = 0.0;
  // }
}