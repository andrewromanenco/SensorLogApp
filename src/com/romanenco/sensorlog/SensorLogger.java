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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

import android.os.Environment;
import android.util.Log;

/**
 * Assumes being used in single threaded environment.
 *
 * @author Andrew Romanenco
 *
 */
public class SensorLogger {

    private static final String TAG = "SensorLogger";
    private static final String sensorLogFileName = "sensor_data.txt";

    private final List<String> logData = new LinkedList<>();
    private final LogEntryFactory logEntryFactory = new AccelerometerEntryFactory();


    public void logSensorEvent(float[] values) {
        addToLog(logEntryFactory.createLogEntry(values));
    }

    public void addToLog(String entry) {
        Log.d(TAG, entry);
        logData.add(entry);
        flush();
    }

    public void flush() {
        File sdCard = Environment.getExternalStorageDirectory();
        File markersFile = new File(sdCard, sensorLogFileName);
        try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(markersFile, true)))) {
            for (String record: logData) {
                out.print(record);
            }
        }catch (IOException e) {
            Log.e(TAG, "Sensor data write error", e);
        }
        logData.clear();
    }
}
