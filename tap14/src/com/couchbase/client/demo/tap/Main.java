
/**
 * Copyright (C) 2016 Couchbase, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALING
 * IN THE SOFTWARE.
 */

package com.couchbase.client.demo.tap;

import org.apache.commons.cli.*;

import javax.naming.ConfigurationException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Main {

    private static String clusterNode, bucketName, bucketPass;

    public static void main(String args[]) throws IOException, ConfigurationException {

        org.apache.commons.cli.Options options = new org.apache.commons.cli.Options();

        Option help = new Option( "help", "print this message" );
        Option nodeOpt = Option.builder("c")
                .required(true)
                .longOpt("clusterNode")
                .hasArg()
                .build();

        Option bucketOpt = Option.builder("b")
                .required(true)
                .longOpt("bucket")
                .hasArg()
                .build();

        Option bucketPassOpt = Option.builder("p")
                .required(false)
                .longOpt("bucketPass")
                .hasArg()
                .build();

        options.addOption(help).addOption(nodeOpt).addOption(bucketOpt).addOption(bucketPassOpt);


        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine line = parser.parse( options, args );

            clusterNode = line.getOptionValue("c");
            bucketName = line.getOptionValue("b");
            bucketPass = line.getOptionValue("p");
            if (bucketPass == null) {
                bucketPass = "";
            }

            runTap();

        }
        catch(ParseException ex) {
            System.err.println( "Arguments required. " + ex.getMessage() );
            System.exit(-1);
        }




    }

    private static void runTap() throws IOException, ConfigurationException {

        AuthTapBucketTest tapper = new AuthTapBucketTest();
        long startTime = System.nanoTime();


        int i = tapper.runTapOnNode(clusterNode, bucketName, bucketPass);

        long endTime = System.nanoTime();

        long duration = (endTime - startTime);

        if (i == 0 ) {
            System.err.println("Error: No items retrieved.  Check TAP messages with packet inspector.");
            System.exit(-1);
        } else {
            System.err.println("TAP complete.  Items retrieved: " + i);
            System.err.println(i / TimeUnit.NANOSECONDS.toSeconds(duration) + " ops/s");
            System.exit(0);
        }


    }

}
