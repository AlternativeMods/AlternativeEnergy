/*
 * Copyright 2013 jk-5 and Lordmau5
 *
 * jk-5 and Lordmau5 License this file to you under the LGPL v3 License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 * http://www.gnu.org/licenses/lgpl.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License
 */

package jkmau5.alternativeenergy.util.config;

import java.io.*;

/**
 *  My own config library, because is is 1000x better than forge's one
 *
 *  This class represents the config file. It is loaded and saved automaticly.
 *
 *  @author jk-5
 */
@SuppressWarnings("unused")
public class ConfigFile extends ConfigTagParent {

    public ConfigFile(File file) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        this.file = file;
        newlinemode = 2;
        loadConfig();
    }

    private void loadConfig() {
        loading = true;
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(file));

            while (true) {
                reader.mark(2000);
                String line = reader.readLine();
                if (line != null && line.startsWith("#")) {
                    if (comment == null || comment.equals("")) {
                        comment = line.substring(1);
                    } else {
                        comment = comment + "\n" + line.substring(1);
                    }
                } else {
                    reader.reset();
                    break;
                }
            }
            loadChildren(reader);
            reader.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        loading = false;
    }

    @Override
    public ConfigFile setComment(String header) {
        super.setComment(header);
        return this;
    }

    @Override
    public ConfigFile setSortMode(int mode) {
        super.setSortMode(mode);
        return this;
    }

    @Override
    public String getNameQualifier() {
        return "";
    }

    public static String readLine(BufferedReader reader) throws IOException {
        String line = reader.readLine();
        if (line != null) {
            return line.replace("\t", "");
        }
        return line;
    }

    public static String formatLine(String line) {
        line = line.replace("\t", "");
        if (line.startsWith("#")) {
            return line;
        } else if (line.contains("=")) {
            line = line.substring(0, line.indexOf("=")).replace(" ", "") + line.substring(line.indexOf("="));
            return line;
        } else {
            line = line.replace(" ", "");
            return line;
        }
    }

    public static void writeLine(PrintWriter writer, String line, int tabs) {
        for (int i = 0; i < tabs; i++) {
            writer.print('\t');
        }

        writer.println(line);
    }

    public void saveConfig() {
        if (loading) {
            return;
        }

        PrintWriter writer;
        try {
            writer = new PrintWriter(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        writeComment(writer, 0);
        ConfigFile.writeLine(writer, "", 0);
        saveTagTree(writer, 0, "");
        writer.flush();
        writer.close();
    }

    public boolean isLoading() {
        return loading;
    }

    public File file;
    private boolean loading;

    public static final byte[] lineend = new byte[] {0xD, 0xA};
}
