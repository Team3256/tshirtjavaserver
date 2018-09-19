#include "Arduino.h"

#include <SoftwareSerial.h>
#include <TinyGPS.h>
#include <ArduinoJson.h>

#define GPS_RX 4
#define GPS_TX 2

TinyGPS gps;
SoftwareSerial ss(4, 2);

void initGPS() {
  ss.begin(9600);
  Serial.println("GPS Init");
}

void retrieveGPSValues() {
  bool newData = false;
  unsigned long chars;
  unsigned short sentences, failed;
  
  for (unsigned long start = millis(); millis() - start < 5000;)
  {
    while (ss.available())
    {
      char c = ss.read();
      //Serial.write(c); // uncomment this line if you want to see the GPS data flowing
      if (gps.encode(c)) // Did a new valid sentence come in?
        newData = true;
    }
  }

  if (true)
  {
    float flat, flon;
    unsigned long age;
    gps.f_get_position(&flat, &flon, &age);

    DynamicJsonBuffer jsonBuffer;
    JsonObject& root = jsonBuffer.createObject();
    root["sensor"] = "gps";
    
    JsonObject& gpsValues = root.createNestedObject("values");
    gpsValues["lat"] = flat == TinyGPS::GPS_INVALID_F_ANGLE ? 0.0 : flat;
    gpsValues["lng"] = flon == TinyGPS::GPS_INVALID_F_ANGLE ? 0.0 : flon;
    
    root.prettyPrintTo(Serial);
  }

  gps.stats(&chars, &sentences, &failed);
  Serial.print(" CHARS=");
  Serial.print(chars);
  Serial.print(" SENTENCES=");
  Serial.print(sentences);
  Serial.print(" CSUM ERR=");
  Serial.println(failed);
  if (chars == 0)
    Serial.println("** No characters received from GPS: check wiring **");
}

