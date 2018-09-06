package cz.zcu.kiv.eegdatabase.webservices.rest;

import com.sun.xml.bind.marshaller.CharacterEscapeHandler;

import java.io.IOException;
import java.io.Writer;

/***********************************************************************************************************************
 *
 * This file is part of the eegdatabase project

 * ==========================================
 *
 * Copyright (C) 2018 by Petr Jezek
 *
 ***********************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 ***********************************************************************************************************************
 *
 * CdataCharacterEscapeHandler, 2018/09/06 16:50 petr-jezek
 *
 **********************************************************************************************************************/
public class CdataCharacterEscapeHandler implements CharacterEscapeHandler {

    public CdataCharacterEscapeHandler() {
        super();
    }

    /**
     * @param ch The array of characters.
     * @param start The starting position.
     * @param length The number of characters to use.
     * @param isAttVal true if this is an attribute value literal.
     */
    public void escape(char[] ch, int start, int length, boolean isAttVal, Writer writer) throws IOException {

        if(AdapterCDATA.isCdata(new String(ch))) {
            writer.write( ch, start, length );
        } else {
            useStandardEscape(ch, start, length, isAttVal, writer);
        }
    }

    private void useStandardEscape(char[] ch, int start, int length, boolean isAttVal, Writer writer) throws IOException {
        CharacterEscapeHandler escapeHandler = StandardEscapeHandler.getInstance();
        escapeHandler.escape(ch, start, length, isAttVal, writer);
    }

    /**
     * A standard XML character escape handler
     * @author coderleaf
     *
     */
    private static final class StandardEscapeHandler implements CharacterEscapeHandler {

        private static StandardEscapeHandler instance;

        public static final StandardEscapeHandler getInstance() {

            if(instance == null) {
                instance = new StandardEscapeHandler();
            }

            return instance;
        }

        private StandardEscapeHandler() {
            super();
        }

        public void escape(char[] ch, int start, int length, boolean isAttVal, Writer out) throws IOException {

            int limit = start + length;
            for (int i = start; i < limit; i++) {
                char c = ch[i];

                if (c == '&' || c == '>' || (c == '\"' && isAttVal)
                        || (c == '\'' && isAttVal)) {

                    if (i != start) {
                        out.write(ch, start, i - start);
                    }

                    start = i + 1;

                    switch (ch[i]) {
                        case '&':
                            out.write("&amp;");
                            break;

                        case '>':
                            out.write("&gt;");
                            break;

                        case '\"':
                            out.write("&quot;");
                            break;

                        case '\'':
                            out.write("&apos;");
                            break;
                    }
                }
            }

            if (start != limit) {
                out.write(ch, start, limit - start);
            }
        }
    }
}
