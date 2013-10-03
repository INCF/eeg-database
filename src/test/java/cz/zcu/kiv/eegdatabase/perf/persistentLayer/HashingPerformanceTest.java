/*******************************************************************************
 * This file is part of the EEG-database project
 * 
 *   ==========================================
 *  
 *   Copyright (C) 2013 by University of West Bohemia (http://www.zcu.cz/en/)
 *  
 *  ***********************************************************************************************************************
 *  
 *   Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 *   the License. You may obtain a copy of the License at
 *  
 *       http://www.apache.org/licenses/LICENSE-2.0
 *  
 *   Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 *   an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *   specific language governing permissions and limitations under the License.
 *  
 *  ***********************************************************************************************************************
 *  
 *   HashingPerformanceTest.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.perf.persistentLayer;

import org.databene.contiperf.PerfTest;
import org.databene.contiperf.Required;
import org.databene.contiperf.junit.ContiPerfRule;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.Assert.assertTrue;

/**
 * Created by IntelliJ IDEA.
 * User: Jiri Novotny
 * Date: 26.4.12
 * Time: 8:48
 * To change this template use File | Settings | File Templates.
 */
public class HashingPerformanceTest {

    private static final int SHA_STRENGTH = 256;
    private static final String SHA_SALT = "dt834W";
    private static final String TEST_PASSWORD = "Zd376df4";

    static final BCryptPasswordEncoder bcryptEncoder = new BCryptPasswordEncoder();
    static final String bcryptEncoded = bcryptEncoder.encode(TEST_PASSWORD);
    static final ShaPasswordEncoder shaEncoder = new ShaPasswordEncoder(SHA_STRENGTH);
    static final String shaEncoded = shaEncoder.encodePassword(TEST_PASSWORD, SHA_SALT);

    @Rule
    public ContiPerfRule i = new ContiPerfRule();

    /**
     * BCrypt is designed to be very slow, so a brute force attack on passwords
     * would take too long to be practical
     */
    @Test
    @PerfTest(invocations = 30)
    @Required(average = 200)
    public void testBcryptEncode() throws Exception {
        bcryptEncoder.encode(TEST_PASSWORD);
    }

    /**
     * BCrypt is designed to be very slow, so a brute force attack on passwords
     * would take too long to be practical
     */
    @Test
    @PerfTest(invocations = 30)
    @Required(average = 200)
    public void testBcryptMatch() throws Exception {
        assertTrue(bcryptEncoder.matches(TEST_PASSWORD, bcryptEncoded));
    }

    /**
     * SHA is fast to enable hashing of large data sets, not just passwords
     * 10000 iterations are done in 1 invocation to enable measurement of small time spans
     */
    @Test
    @PerfTest(invocations = 30)
    @Required(average = 200)
    public void testShaEncode() throws Exception {
        for(int i = 0; i < 10000; i++){
            shaEncoder.encodePassword(TEST_PASSWORD, SHA_SALT);
        }
    }

    /**
     * SHA is fast to enable hashing of large data sets, not just passwords
     * * 10000 iterations are done in 1 invocation to enable measurement of small time spans
     */
    @Test
    @PerfTest(invocations = 30)
    @Required(average = 200)
    public void testShaMatches() throws Exception {
        for(int i = 0; i < 10000; i++){
            assertTrue(shaEncoder.isPasswordValid(shaEncoded, TEST_PASSWORD, SHA_SALT));
        }
    }

}
