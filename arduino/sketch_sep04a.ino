#include "Shooter.h"
#include "GPS.h"

// Begin command strings

// Pivot
const char PROGMEM pivotHomeCmd[] = "pivotHome";
const char PROGMEM updatePivotAngleCmd[] = "updatePivotAngle";

// Barrel Pop
const char PROGMEM popLeftCmd[] = "popLeft";
const char PROGMEM popRightCmd[] = "popRight";

// Barrel Push
const char PROGMEM pushLeftCmd[] = "pushLeft";
const char PROGMEM pushRightCmd[] = "pushRight";

// Barrel Eject
const char PROGMEM ejectLeftCmd[] = "ejectLeft";
const char PROGMEM ejectRightCmd[] = "ejectRight";

// Barrel Retract
const char PROGMEM retractLeftCmd[] = "retractLeft";
const char PROGMEM retractRightCmd[] = "retractRight";

// Barrel Shoot
const char PROGMEM shootLeftCmd[] = "shootLeft";
const char PROGMEM shootRightCmd[] = "shootRight";

// Motor
const char PROGMEM leftMotorSpeedCmd[] = "leftMotorSpeed";
const char PROGMEM rightMotorSpeedCmd[] = "rightMotorSpeed";

// GPS/Accelerometer
const char PROGMEM updateGPSCmd[] = "updateGPS";
const char PROGMEM updateGyroCmd[] = "updateGyro";
// End command strings

// Start command enum
enum {
  pivotHome,
  updatePivotAngle,
  popLeft,
  popRight,
  pushLeft,
  pushRight,
  ejectLeft,
  ejectRight,
  retractLeft,
  retractRight,
  shootLeft,
  shootRight,
  leftMotorSpeed,
  rightMotorSpeed,
  updateGPS,
  updateGyro
};
// End command enum

// Start command pointers
const char* const cmdTable[] PROGMEM = {
  pivotHomeCmd,
  updatePivotAngleCmd,
  popLeftCmd,
  popRightCmd,
  pushLeftCmd,
  pushRightCmd,
  ejectLeftCmd,
  ejectRightCmd,
  retractLeftCmd,
  retractRightCmd,
  shootLeftCmd,
  shootRightCmd,
  leftMotorSpeedCmd,
  rightMotorSpeedCmd,
  updateGPSCmd,
  updateGyroCmd
};
// End command pointers

// Start command helper functions
int cmdCount = sizeof(cmdTable) / sizeof(cmdTable[0]);
char buffer[128];

// Get string from program memory
char* getStringFromProgramMemory(const char* const Table[], int i) {
  strcpy_P(buffer, (char*) pgm_read_word(&(Table[i])));
  return buffer;
}

int getCommandID(char* search) {
  int startCount = 0;
  int foundIndex = -1;

  while (startCount < cmdCount) {
    if (strcmp(search, getStringFromProgramMemory(cmdTable, startCount)) == 0) {
      foundIndex = startCount;
      break;
    }
    startCount++;
  }
  return foundIndex;
}
// End command helper functions

// Start Execute Command
boolean ExecuteCommand(char* commandBuffer) {
  char* Command;
  char* Payload;

  Command = strtok(commandBuffer, ",");
  Payload = strtok(NULL, ",");

  Serial.println("Command: " + String(Command));
  Serial.println("Payload: " + String(Payload));

  int cmdID = getCommandID(Command);

  Serial.println("CommandID: " + String(cmdID));

  switch (cmdID) {
    case pivotHome:
      Serial.println("Pivot Home Requested");
      break;
    case updatePivotAngle:
      Serial.println("Pivot Angle Update Requested to angle: " + String(Payload));
      break;
    case popLeft:
      Serial.println("Pop Left Barrel Requested");
      pop_left();
      break;
    case popRight:
      Serial.println("Pop Right Barrel Requested");
      pop_right();
      break;
    case pushLeft:
      Serial.println("Push Left Barrel Requested");
      push_left();
      break;
    case pushRight:
      Serial.println("Push Right Barrel Requested");
      push_right();
      break;
    case ejectLeft:
      Serial.println("Eject Left Barrel Requested");
      eject_left();
      break;
    case ejectRight:
      Serial.println("Eject Right Barrel Requested");
      break;
    case retractLeft:
      Serial.println("Retract Left Barrel Requested");
      retract_left();
      break;
    case retractRight:
      Serial.println("Retract Right Barrel Requested");
      retract_right();
      break;
    case shootLeft:
      Serial.println("Opening left barrel for " + String(Payload) + "ms to shoot");
      shoot_left(atoi(Payload));
      break;
    case shootRight:
      Serial.println("Opening right barrel for " + String(Payload) + "ms to shoot");
      shoot_right(atoi(Payload));
      break;
    case leftMotorSpeed:
      Serial.println("Left motor speed set to " + String(Payload));
      left_motor(atof(Payload));
      break;
    case rightMotorSpeed:
      Serial.println("Right motor speed set to " + String(Payload));
      right_motor(atof(Payload));
      break;
    case updateGPS:
      Serial.println("GPS location update requested");
      //updateGPS_JSON();
      retrieveGPSValues();
      break;
    case updateGyro:
      Serial.println("New gyro values requested");
      break;
    default:
      Serial.println("Command not found, please consult the Arduino code for possible commands  ");
      break;
  }
}
// End Execute Command

// Start global variables
char inputBuffer[50];
int serialIndex = 0;
// End global variables

boolean CheckSerial() {
  boolean lineFound = false;
  // if there's any serial available, read it:
  while (Serial.available() > 0) {
    // Read a character as it comes in:
    // currently this will throw away anything after the buffer is full or the \n is detected
    char charBuffer = Serial.read(); 
      if (charBuffer == ';') {
           inputBuffer[serialIndex] = 0; // terminate the string
           lineFound = (serialIndex > 0); // only good if we sent more than an empty line
           serialIndex = 0; // reset for next line of data
         }
         else if(charBuffer == '\r') {
           // Just ignore the Carrage return, were only interested in new line
         }
         else if(serialIndex < 50 && lineFound == false) {
           /* Place the character in the string buffer: */
           inputBuffer[serialIndex++] = charBuffer; // auto increment index
         }
  }// End of While
  return lineFound;
}

void setup() {
  // put your setup code here, to run once:
  Serial.begin(1000000, SERIAL_8N2);
  while (!Serial) {}
  Serial.println("Main Init");
  //Serial.setTimeout(10);
  initShooter();
  initGPS();
}

void loop() {
  // put your main code here, to run repeatedly:
  if (CheckSerial()) {
    ExecuteCommand(inputBuffer);
  }
  delay(10);
}
