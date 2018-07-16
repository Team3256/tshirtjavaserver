package com.panos.interfaces;

import com.panos.RobotSerial;

public interface Subsystem {
    void emergencyStop();

    void setSerial(RobotSerial serial);
}
