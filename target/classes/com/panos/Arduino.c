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

int pivotTarget = 0;
float pivot_power = 0.25;
float motor_power = 0;
long pivotPos = -999;
float ticksPerDegree = -2050;

void run_motor(Servo motor, double power) {
  power *= 100;
  int pulse_us = TALON_CENTER_PULSE_US + (5*power);
  if (pulse_us < TALON_MIN_PULSE_US) pulse_us = TALON_MIN_PULSE_US;
  if (pulse_us > TALON_MAX_PULSE_US) pulse_us = TALON_MAX_PULSE_US;
  motor.writeMicroseconds(pulse_us);
}

float update_pivot(float angle, double power) {
  power = abs(power);
  float currPos = pivotEnc.read();
  float targetTicks = ticksPerDegree * angle;
  bool direction = true; //true is going up...false is going down
  if(ticksToDegrees(currPos) > angle) {
    direction = false;
  }
  while(direction ? currPos > targetTicks : currPos < targetTicks) {
    run_motor(pivot, -power);
    currPos = pivotEnc.read();
  }
  run_motor(pivot, 0);
}

void pivot_home() {
  int pivotState = digitalRead(20);

  while(pivotState != 0) {
    run_motor(pivot, 0.2);
    pivotState = digitalRead(20);
  }

  run_motor(pivot, 0);

  pivotEnc.write(0);
}

float degreesToTicks(float degrees) {
  return degrees*ticksPerDegree;
}

float ticksToDegrees(float ticks) {
  return ticks/ticksPerDegree;
}

String command = "";
String data = "";

void run_command() {
  Serial.println(command);
  Serial.println(data);
  if (command == "m") {

  }

  if (command == "pivot") {
    run_motor(pivot, data.toFloat());
  }

  if (command == "pivothome") {
    pivot_home();
  }
}

typedef enum CommandState {
  COMMAND_IDLE,
  COMMAND,
  DATA,
  EXECUTE
};

CommandState commandState = COMMAND_IDLE;

void setup() {
  // put your setup code here, to run once:
  Serial.begin(500000, SERIAL_8N2);
  Serial.println("SERIAL INITIALIZED");
  Serial.setTimeout(20);
  Serial.println("STARTING MAIN LOOP");
  commandState = COMMAND_IDLE;
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
  pivot_home();
}

void loop() {
  while (true) {
    pivotPos = pivotEnc.read();
    if (Serial.available() > 1) {
      char incomingByte = Serial.read();

      switch(incomingByte) {
        case '>':
          commandState = COMMAND;
          continue;
          break;
        case ',':
          commandState = DATA;
          continue;
          break;
        case ';':
          commandState = EXECUTE;
          break;
      }

      switch(commandState) {
        case COMMAND:
          command = command + incomingByte;
          break;
        case DATA:
          data = data + incomingByte;
          break;
        case EXECUTE:
          run_command();
          command = "";
          data = "";
          commandState = COMMAND_IDLE;
          break;
      }

      Serial.flush();
    }
  }
}