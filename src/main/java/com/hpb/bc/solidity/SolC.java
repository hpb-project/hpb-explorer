/*
 * Copyright 2020 HPB Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hpb.bc.solidity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;

/**
 * Wrapper class to the native solc execution on different platforms.
 * <p>
 * Inspired by https://github.com/ethereum/ethereumj/tree/develop/ethereumj-core/src/main/java/org/ethereum/solidity
 */
public class SolC {

    private File solc = null;

    private String canonicalPath;
    private String canonicalWorkingDirectory;
    private File workingDirectory;

    SolC() {
        try {
            initBundled();

            canonicalPath = solc.getCanonicalPath();
            canonicalWorkingDirectory = solc.getParentFile().getCanonicalPath();
            workingDirectory = solc.getParentFile();

        } catch (IOException e) {
            throw new RuntimeException("Can't init solc compiler: ", e);
        }
    }

    private void initBundled() throws IOException {
        File tmpDir = new File(System.getProperty("java.io.tmpdir"), "solc");
        tmpDir.setReadable(true);
        tmpDir.setWritable(true);
        tmpDir.setExecutable(true);
        tmpDir.mkdirs();

        String solcPath = "/native/" + getOS() + "/solc/";
        InputStream is = getClass().getResourceAsStream(solcPath + "file.list");
        Scanner scanner = new Scanner(is);
        while (scanner.hasNext()) {
            String s = scanner.next();
            File targetFile = new File(tmpDir, s);
            InputStream fis = getClass().getResourceAsStream(solcPath + s);
            Files.copy(fis, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            if (solc == null) {
                // first file in the list denotes executable
                solc = targetFile;
                solc.setExecutable(true);
            }
            targetFile.deleteOnExit();
        }
        tmpDir.deleteOnExit();
        scanner.close();
    }


    private String getOS() {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("win")) {
            return "win";
        } else if (osName.contains("linux")) {
            return "linux";
        } else if (osName.contains("mac")) {
            return "mac";
        } else {
            throw new RuntimeException("Can't find solc compiler: unrecognized OS: " + osName);
        }
    }

    public String getCanonicalPath() {
        return canonicalPath;
    }

    public String getCanonicalWorkingDirectory() {
        return canonicalWorkingDirectory;
    }

    public File getWorkingDirectory() {
        return workingDirectory;
    }


}
