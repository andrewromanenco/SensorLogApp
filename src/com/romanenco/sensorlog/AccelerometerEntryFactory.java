/*
 * Copyright 2014 Andrew Romanenco (andrew@romanenco.com)
 *
 * This file is part of SensorLog app.
 *
 * SensorLog is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * SensorLog is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with SensorLog.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.romanenco.sensorlog;

/**
 * Accelerometer data to CSV string
 *
 * @author Andrew Romanenco
 *
 */
public class AccelerometerEntryFactory implements LogEntryFactory {

    @Override
    public String createLogEntry(float[] values) {
        float x = values[0];
        float y = values[1];
        float z = values[2];
        return System.currentTimeMillis()
                + "," + Float.toString(x)
                + "," + Float.toString(y)
                + "," + Float.toString(z)
                + "\n";
    }

}
